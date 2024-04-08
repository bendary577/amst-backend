package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.model.amstate.AMTriggerSPCallInfo;
import accountmanager.supporttool.model.amstate.SISUserRecord;
import accountmanager.supporttool.model.amstate.StudentWithNoUserRecordInfo;
import accountmanager.supporttool.repository.AccountStateHealthCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountStateHealthCheckService {

    private final AccountStateHealthCheckRepository accountStateHealthCheckRepository;

    @Autowired
    public AccountStateHealthCheckService(AccountStateHealthCheckRepository accountStateHealthCheckRepository) {
        this.accountStateHealthCheckRepository = accountStateHealthCheckRepository;
    }

    @SwitchDataSource(value = "ZLMESE")
    public List<StudentWithNoUserRecordInfo> getListOfAccountsWithoutUserRecord() {
        return accountStateHealthCheckRepository.getListOfAccountsWithoutUserRecord();
    }

    @SwitchDataSource(value = "ZLMESE")
    public List<SISUserRecord> getListOfAccountsWithDisabledUserRecord() {
        return accountStateHealthCheckRepository.getListOfAccountsWithDisabledUserRecord();
    }

    @SwitchDataSource(value = "ZLMESE")
    public List<SISUserRecord> getListOfAccountsWithoutProfileRecord() {
        return accountStateHealthCheckRepository.getListOfAccountsWithoutProfileRecord();
    }

    @SwitchDataSource(value = "ZLMESE")
    public List<AMTriggerSPCallInfo> getListOfAccountsWithoutStateRecord() {
        return accountStateHealthCheckRepository.getListOfAccountsWithoutStateRecord();
    }


}
