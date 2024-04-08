package accountmanager.supporttool.service;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.model.amstate.StudentUserUID;
import accountmanager.supporttool.repository.AccountStateDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashSet;
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
        AccountStateDashboardDTO accountStateDashboard = this.accountStateDashboardRepository.getSISDashboardInfo();
        HashSet<StudentUserUID> studentUserUIDSet = this.accountStateDashboardRepository.getStudentsUserUIDfromSISDB();
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
