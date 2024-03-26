package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.repository.AccountStateDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountStateDashboardService {

    private AccountStateDashboardRepository accountStateDashboardRepository;

    @Autowired
    public AccountStateDashboardService(AccountStateDashboardRepository accountStateDashboardRepository) {
        this.accountStateDashboardRepository = accountStateDashboardRepository;
    }

    @SwitchDataSource(value = "ZLMESE")
    public AccountStateDashboardDTO getSISDashboardData() {
        int getNumberOfStudentsWithoutUserRecord = this.accountStateDashboardRepository.getNumberOfStudentsWithoutUserRecord();
        int getNumberOfStudentsWithDisabledUsers = this.accountStateDashboardRepository.getNumberOfStudentsWithDisabledUsers();
        int getNumberOfStudentsWithoutProfile = this.accountStateDashboardRepository.getNumberOfStudentsWithoutProfile();
        int getNumberOfStudentsWithoutState = this.accountStateDashboardRepository.getNumberOfStudentsWithoutState();
        AccountStateDashboardDTO accountStateDashboard = new AccountStateDashboardDTO();
        accountStateDashboard.setStudentsWithNoUserRecord(getNumberOfStudentsWithoutUserRecord);
        accountStateDashboard.setStudentsWithDisabledUsers(getNumberOfStudentsWithDisabledUsers);
        accountStateDashboard.setStudentsWithNoProfileRecord(getNumberOfStudentsWithoutProfile);
        accountStateDashboard.setStudentsWithNoStateRecord(getNumberOfStudentsWithoutState);
        return accountStateDashboard;
   }

    @SwitchDataSource(value = "SIS")
    public void getSSODashboardData(String officialEmail) {}

}
