package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.enumeration.SISUserRole;
import accountmanager.supporttool.enumeration.UserRole;
import accountmanager.supporttool.model.amstate.AccountState;
import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.model.amstate.StudentWithNoUserRecordInfo;
import accountmanager.supporttool.model.amstate.UserSSOSPCallInfo;
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
    public List<AccountState> getSISData(String officialEmail) {
        List<AccountState> accountStates = accountStateRepository.getAllSISData(officialEmail);
        return accountStates;
    }

    @SwitchDataSource(value = "SIS")
    public List<AccountState> getSSOData(String officialEmail) {
        List<AccountState> accountStates = accountStateRepository.getAllSSOData(officialEmail);
        return accountStates;
    }

    public AccountStateDTO buildAccountStateDTO(List<AccountState> sisAccountStates, List<AccountState> ssoAccountStates){
        AccountStateDTO accountStateDTO = new AccountStateDTO();
        boolean checkExternalIDMatch = false;

        if(sisAccountStates != null && !sisAccountStates.isEmpty()){
            accountStateDTO.setValue(sisAccountStates.get(0).getValue());
            accountStateDTO.setAccountID(sisAccountStates.get(0).getAccountID());
            accountStateDTO.setExternalId(sisAccountStates.get(0).getExternalId());
            accountStateDTO.setLastLoginDate(sisAccountStates.get(0).getLastLoginDate());
            accountStateDTO.setStaffNumber(sisAccountStates.get(0).getStaffNumber());
            accountStateDTO.setStudentNumber(sisAccountStates.get(0).getStudentNumber());
            int gender = sisAccountStates.get(0).getGender();
            accountStateDTO.setGender(gender);
            accountStateDTO.setHasUserRecord(sisAccountStates.get(0).getUserName() != null && !sisAccountStates.get(0).getUserName().isEmpty()); //TODO:CHANGE
            accountStateDTO.setHasOfficialEmailsMatch(sisAccountStates.get(0).getValue() != null && !sisAccountStates.get(0).getValue().isEmpty());
            if(sisAccountStates.get(0).getAccountManagerState_Id() != 0){
                accountStateDTO.setHasStateRecord(true);
            }

            //user role
            int role = sisAccountStates.get(0).getStaff_id() == null ? 1: 2;
            accountStateDTO.setRole(SISUserRole.valueOfRole(role));

            if(role == 1){ //Student
                //does gender and official email match
                List<String> officialEmails = new LinkedList<>();
                officialEmails.add(sisAccountStates.get(0).getValue());
                officialEmails.add(sisAccountStates.get(0).getAccountID());
                accountStateDTO.setOfficialEmailMatchGender(OfficialEmailUtil.doOfficialEmailsMatchWithGender(gender, officialEmails));
            }else{ //no need to check gender email format
                accountStateDTO.setOfficialEmailMatchGender(true);
            }

            checkExternalIDMatch = true;
        }
        if(ssoAccountStates != null && !ssoAccountStates.isEmpty()){
            accountStateDTO.setSISUserEmail(ssoAccountStates.get(0).getSISUserEmail());
            accountStateDTO.setSISUnifiedUID(ssoAccountStates.get(0).getSISUnifiedUID());
            accountStateDTO.setHasSSORecord(ssoAccountStates.get(0).getSISUserEmail() != null);

            if(checkExternalIDMatch){
                accountStateDTO.setHasExternalIdsMatch(sisAccountStates.get(0).getExternalId().equalsIgnoreCase(ssoAccountStates.get(0).getSISUnifiedUID()));
            }
        }
        return accountStateDTO;
    }

    public AccountStateDTO fixAccountState(AccountStateDTO accountStateDTO){
        List<String> troubleShootingRCA = new LinkedList<>();
        int isUserSSORecordFixed = 1; //false
        if(!accountStateDTO.isHasExternalIdsMatch()){//sso sp script
            troubleShootingRCA.add("the user externalId in SIS DB doesn't match the corresponding value in SSO DB. this will cause a login failure in AL-Manhal\n");
            isUserSSORecordFixed = fixUserSSORecord(accountStateDTO.getValue());
        }
        if(isUserSSORecordFixed == 1 && !accountStateDTO.isHasSSORecord()){//sso sp script : call in case it was not called in the upper checking
            troubleShootingRCA.add("the user account doesn't have an SSO record in the intermediate SSO DB. ths will cause login failure in all ecosystem apps\n");
            fixUserSSORecord(accountStateDTO.getValue());
            accountStateDTO.setHasSSORecord(true);
        }
        if(!accountStateDTO.isHasUserRecord()){ //fix user script
            troubleShootingRCA.add("- the user account is not linked to a SYS_User record in SIS DB. this will cause a login failure in AL-Manhal and Student Portal Apps\n");
            fixUserRecord(accountStateDTO.getAccountID());
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
            this.accountStateRepository.fixAccountManagerStudentState(accountStateDTO);
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

    public void fixUserRecord(String officialEmail){
        this.accountStateRepository.fixStudentWithNoUserRecord(officialEmail);
    }

    public void fixUserRecord(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo){
        this.accountStateRepository.fixStudentWithNoUserRecord(studentWithNoUserRecordInfo);
    }

}
