package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.enumeration.SISUserRole;
import accountmanager.supporttool.model.amstate.*;
import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.repository.AccountStateRepository;
import accountmanager.supporttool.util.CSVFileUtil;
import accountmanager.supporttool.util.OfficialEmailUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class AccountStateService {

    private final AccountStateRepository accountStateRepository;

    @Autowired
    public AccountStateService(AccountStateRepository accountStateRepository) {
        this.accountStateRepository = accountStateRepository;
    }

    @SwitchDataSource(value = "ZLMESE")
    public List<AccountState> getAccountSISData(String officialEmail) {
        List<AccountState> accountStates = accountStateRepository.getAllSISData(officialEmail);
        return accountStates;
    }

    @SwitchDataSource(value = "SIS")
    public List<AccountState> getAccountSSOData(String officialEmail) {
        List<AccountState> accountStates = accountStateRepository.getAllSSOData(officialEmail);
        return accountStates;
    }

    public AccountStateDTO buildAccountStateDTO(List<AccountState> sisAccountStates, List<AccountState> ssoAccountStates){
        AccountStateDTO accountStateDTO = new AccountStateDTO();
        boolean checkExternalIDMatch = false;

        if(sisAccountStates != null && !sisAccountStates.isEmpty()){
            AccountState accountState = sisAccountStates.get(0); //get the needed user account state object (first one)
            accountStateDTO.setOfficialEmailInCommPhone(accountState.getValue());
            accountStateDTO.setOfficialEmailInAMState(accountState.getAccountID());
            accountStateDTO.setExternalId(accountState.getExternalId());
            accountStateDTO.setLastLoginDate(accountState.getLastLoginDate());
            accountStateDTO.setStaffNumber(accountState.getStaffNumber());
            accountStateDTO.setStudentNumber(accountState.getStudentNumber());
            int gender = accountState.getGender();
            accountStateDTO.setGender(gender);
            accountStateDTO.setHasUserRecord(accountState.getUserName() != null && !accountState.getUserName().isEmpty()); //TODO:CHANGE
            accountStateDTO.setHasOfficialEmailsMatch(accountState.getValue() != null && !accountState.getValue().isEmpty());
            if(accountState.getAccountManagerState_Id() != 0){
                accountStateDTO.setHasStateRecord(true);
            }

            //user role
            int role = accountState.getStaff_id() == null ? 1: 2;
            accountStateDTO.setRole(SISUserRole.valueOfRole(role));

            if(role == 1){ //Student
                //does gender and official email match
                List<String> officialEmails = new LinkedList<>();
                officialEmails.add(accountState.getValue());
                officialEmails.add(accountState.getAccountID());
                accountStateDTO.setOfficialEmailMatchGender(OfficialEmailUtil.doOfficialEmailsMatchWithGender(gender, officialEmails));
            }else{ //no need to check gender email format
                accountStateDTO.setOfficialEmailMatchGender(true);
            }

            checkExternalIDMatch = true;
        }
        if(ssoAccountStates != null && !ssoAccountStates.isEmpty()){
            AccountState accountState = ssoAccountStates.get(0);
            accountStateDTO.setOfficialEmailInSSO(accountState.getSISUserEmail());
            accountStateDTO.setSISUnifiedUID(accountState.getSISUnifiedUID());
            accountStateDTO.setHasSSORecord(accountState.getSISUserEmail() != null);

            if(checkExternalIDMatch){
                accountStateDTO.setHasExternalIdsMatch(sisAccountStates.get(0).getExternalId().equalsIgnoreCase(accountState.getSISUnifiedUID()));
            }
        }
        return accountStateDTO;
    }

    public AccountStateDTO fixAccountState(AccountStateDTO accountStateDTO){
        List<String> troubleShootingRCA = new LinkedList<>();
        int isUserSSORecordFixed = 1; //false
        if(!accountStateDTO.isHasExternalIdsMatch()){//sso sp script
            troubleShootingRCA.add("the user externalId in SIS DB doesn't match the corresponding value in SSO DB. this will cause a login failure in AL-Manhal\n");
            isUserSSORecordFixed = fixUserSSORecord(accountStateDTO.getOfficialEmailInCommPhone());
        }
        if(isUserSSORecordFixed == 1 && !accountStateDTO.isHasSSORecord()){//sso sp script : call in case it was not called in the upper checking
            troubleShootingRCA.add("the user account doesn't have an SSO record in the intermediate SSO DB. ths will cause login failure in all ecosystem apps\n");
            fixUserSSORecord(accountStateDTO.getOfficialEmailInCommPhone());
            accountStateDTO.setHasSSORecord(true);
        }
        if(!accountStateDTO.isHasUserRecord()){ //fix user script
            troubleShootingRCA.add("- the user account is not linked to a SYS_User record in SIS DB. this will cause a login failure in AL-Manhal and Student Portal Apps\n");
            fixUserRecord(accountStateDTO.getOfficialEmailInAMState());
            accountStateDTO.setHasUserRecord(true);
        }
        //------------------------- async fix handling -------------------------
        if(!accountStateDTO.isHasOfficialEmailsMatch()){//trigger AM script
            troubleShootingRCA.add("the user official emails doesn't match between SIS and SSO DBs. this is with a minimal effect on the user login\n");
        }
        if(!accountStateDTO.isOfficialEmailMatchGender()){//trigger AM script
            troubleShootingRCA.add("the user official email doesn't match the user gender. this is with a minimal effect on the user login\n");
        }
        if(!accountStateDTO.isHasOfficialEmailsMatch()
            || !accountStateDTO.isOfficialEmailMatchGender()){//trigger AM script
            accountStateDTO.setAsyncfix(true);
            triggerAMStoredProcedure(accountStateDTO);
        }
        accountStateDTO.setTroubleshootingRCA(troubleShootingRCA);
        return accountStateDTO;
    }

    public int fixUserSSORecord(String officialEmail) {
        List<UserSSOSPCallInfo> userSSOSPCallInfoList = this.accountStateRepository.prepareSSOSPInfoFromSIS(officialEmail);
        String command = this.accountStateRepository.prepareSSOSPCallCommand(userSSOSPCallInfoList);
        return this.accountStateRepository.executeSSOSPCallCommand(command);
    }

    public Set<String> parseAccountsCSVFile(MultipartFile file){
        Set<String> emailsList = new HashSet<>();
        if (file != null && !file.isEmpty() && CSVFileUtil.hasCSVFormat(file)) {
            try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CSVParser parser = CSVFormat.DEFAULT.parse(reader);
                for (CSVRecord record : parser) {
                    String emailValueColumn = record.get(0);
                    if(OfficialEmailUtil.isValidOfficialEmail(emailValueColumn)){
                        emailsList.add(emailValueColumn);
                    }
                }
                return emailsList;
            } catch (Exception e) {
                e.printStackTrace();
                return emailsList;
            }
        }
        return emailsList;
    }

    @SwitchDataSource(value = "ZLMESE")
    public void triggerAMStoredProcedure(AccountStateDTO accountStateDTO){
        //delete AM state if exists
        if(accountStateDTO.isHasStateRecord()){
            this.accountStateRepository.deleteUserAccountState(accountStateDTO.getOfficialEmailInCommPhone());
        }
        //call the SP
        List<AMTriggerSPCallInfo> amTriggerSPCallInfoList = this.accountStateRepository.prepareAMTriggerSPCall(accountStateDTO.getStudentNumber());
        int enrollmentId = amTriggerSPCallInfoList.get(0).getEnrollment_Id();
        if(enrollmentId != 0){
            this.accountStateRepository.executeAMTriggerProcedure(enrollmentId);
        }
    }

    @SwitchDataSource(value = "ZLMESE")
    public void triggerAMStoredProcedure(AccountStateDTO accountStateDTO, int enrollmentId){
        //delete AM state if exists
        if(accountStateDTO.isHasStateRecord()){
            this.accountStateRepository.deleteUserAccountState(accountStateDTO.getOfficialEmailInCommPhone());
        }
        this.accountStateRepository.executeAMTriggerProcedure(enrollmentId);
    }

    public void fixUserRecord(String officialEmail){
        this.accountStateRepository.fixStudentWithNoUserRecord(officialEmail);
    }

    public void fixUserRecord(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo){
        this.accountStateRepository.fixStudentWithNoUserRecord(studentWithNoUserRecordInfo);
    }

    public void fixUserProfileRecord(SISUserRecord sisUserRecord){
        this.accountStateRepository.fixUserProfileRecord(sisUserRecord);
    }

    public void fixDisabledUser(SISUserRecord sisUserRecord){
        this.accountStateRepository.enableDisabledUser(sisUserRecord);
    }

    public AccountStateDTO getAccountStateDTOFromEnrollmentInfo(AMTriggerSPCallInfo amTriggerSPCallInfo){
        AccountStateDTO accountStateDTO = new AccountStateDTO();
        if(amTriggerSPCallInfo.getAccountManagerState_Id() != null){
            accountStateDTO.setHasStateRecord(true);
        }
        return accountStateDTO;
    }

}
