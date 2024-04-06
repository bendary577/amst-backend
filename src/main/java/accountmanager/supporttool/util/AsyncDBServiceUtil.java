package accountmanager.supporttool.util;

import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.model.amstate.StudentUserUID;
import accountmanager.supporttool.service.AccountStateDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AsyncDBServiceUtil {

    private final AccountStateDashboardService accountStateDashboardService;

    @Autowired
    public AsyncDBServiceUtil(AccountStateDashboardService accountStateDashboardService) {
        this.accountStateDashboardService = accountStateDashboardService;
    }

    public AccountStateDashboardDTO getDashboardDataAsync (){
        AccountStateDashboardDTO accountStateDashboardDTO = null;
        try{
            CompletableFuture<AccountStateDashboardDTO> accountStateDashboardAsync = this.accountStateDashboardService.getSISDashboardData();
            CompletableFuture<HashSet<StudentUserUID>> studentUserUIDSSOListAsync = this.accountStateDashboardService.getSSODashboardData();
            CompletableFuture.allOf(accountStateDashboardAsync, studentUserUIDSSOListAsync).join();

            accountStateDashboardDTO = accountStateDashboardAsync.get();
            HashSet<StudentUserUID> studentUserUIDSSOList = studentUserUIDSSOListAsync.get();

            //extract string values of StudentUserUID out of returned objects into another list
            HashSet<String> sisStudentUserUIDValues = (HashSet<String>) accountStateDashboardDTO.getStudentUserUIDList().stream()
                    .map(StudentUserUID::getSisuserUID)
                    .collect(Collectors.toSet());

            HashSet<String> ssoStudentUserUIDValues = (HashSet<String>) studentUserUIDSSOList.stream()
                    .map(StudentUserUID::getSisuserUID)
                    .collect(Collectors.toSet());

            Set<String> difference = new HashSet<>(sisStudentUserUIDValues);
            difference.removeAll(ssoStudentUserUIDValues);

            accountStateDashboardDTO.setNumberOfDifferenceBetweenSIS_SSO_DBS(difference.size());
            accountStateDashboardDTO.getStudentUserUIDList().clear();
            return accountStateDashboardDTO;
        }catch (InterruptedException | ExecutionException interruptedException){
            System.out.println(Arrays.toString(interruptedException.getStackTrace()));
        }
        return accountStateDashboardDTO;
    }


}
