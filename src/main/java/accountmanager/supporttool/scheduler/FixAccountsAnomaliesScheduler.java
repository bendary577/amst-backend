package accountmanager.supporttool.scheduler;


import accountmanager.supporttool.model.amstate.AMTriggerSPCallInfo;
import accountmanager.supporttool.model.amstate.SISUserRecord;
import accountmanager.supporttool.model.amstate.StudentWithNoUserRecordInfo;
import accountmanager.supporttool.service.AccountStateHealthCheckService;
import accountmanager.supporttool.service.AccountStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.util.Objects;


public class FixAccountsAnomaliesScheduler {

    private final Environment environment;
    private final AccountStateHealthCheckService accountStateHealthCheckService;
    private final AccountStateService accountStateService;

    @Autowired
    public FixAccountsAnomaliesScheduler(Environment environment,
                                         AccountStateHealthCheckService accountStateHealthCheckService,
                                         AccountStateService accountStateService){
        this.environment = environment;
        this.accountStateHealthCheckService = accountStateHealthCheckService;
        this.accountStateService = accountStateService;

    }


    @Scheduled(cron = "0 0 22 * * *") // Execute every day at 10 PM
    public void scheduledTask() {
        if(Objects.requireNonNull(environment.getProperty("app.conf.enableScheduling")).equalsIgnoreCase("true")){
            List<StudentWithNoUserRecordInfo> studentWithNoUserRecordInfoList = this.accountStateHealthCheckService.getListOfAccountsWithoutUserRecord();
            List<SISUserRecord> studentsWithDisabledUserRecordList = this.accountStateHealthCheckService.getListOfAccountsWithDisabledUserRecord();
            List<SISUserRecord> studentWithoutProfileRecordList = this.accountStateHealthCheckService.getListOfAccountsWithoutProfileRecord();
            List<AMTriggerSPCallInfo> studentEnrollmentIdsList = this.accountStateHealthCheckService.getListOfAccountsWithoutStateRecord();

            for(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo : studentWithNoUserRecordInfoList){
                this.accountStateService.fixUserRecord(studentWithNoUserRecordInfo);
            }
        }
    }
}
