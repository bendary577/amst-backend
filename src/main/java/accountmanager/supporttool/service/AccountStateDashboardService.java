package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.model.amstate.StudentUserUID;
import accountmanager.supporttool.repository.AccountStateDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountStateDashboardService {

    private AccountStateDashboardRepository accountStateDashboardRepository;

    @Autowired
    public AccountStateDashboardService(AccountStateDashboardRepository accountStateDashboardRepository) {
        this.accountStateDashboardRepository = accountStateDashboardRepository;
    }

    @Async
    @SwitchDataSource(value = "ZLMESE")
    public CompletableFuture<AccountStateDashboardDTO> getSISDashboardData() {
        int getNumberOfStudentsWithoutUserRecord = this.accountStateDashboardRepository.getNumberOfStudentsWithoutUserRecord();
        int getNumberOfStudentsWithDisabledUsers = this.accountStateDashboardRepository.getNumberOfStudentsWithDisabledUsers();
        int getNumberOfStudentsWithoutProfile = this.accountStateDashboardRepository.getNumberOfStudentsWithoutProfile();
        int getNumberOfStudentsWithoutState = this.accountStateDashboardRepository.getNumberOfStudentsWithoutState();
        HashSet<StudentUserUID> studentUserUIDSet = this.accountStateDashboardRepository.getStudentsUserUIDfromSISDB();
        AccountStateDashboardDTO accountStateDashboard = new AccountStateDashboardDTO();
        accountStateDashboard.setStudentsWithNoUserRecord(getNumberOfStudentsWithoutUserRecord);
        accountStateDashboard.setStudentsWithDisabledUsers(getNumberOfStudentsWithDisabledUsers);
        accountStateDashboard.setStudentsWithNoProfileRecord(getNumberOfStudentsWithoutProfile);
        accountStateDashboard.setStudentsWithNoStateRecord(getNumberOfStudentsWithoutState);
        accountStateDashboard.setStudentUserUIDList(studentUserUIDSet);
        return CompletableFuture.completedFuture(accountStateDashboard);
   }

   @Async
    @SwitchDataSource(value = "SIS")
    public CompletableFuture<HashSet<StudentUserUID>> getSSODashboardData() {
        HashSet<StudentUserUID> studentUserUIDList = this.accountStateDashboardRepository.getStudentsUserUIDfromSSODB();
        return CompletableFuture.completedFuture(studentUserUIDList);
    }

}
