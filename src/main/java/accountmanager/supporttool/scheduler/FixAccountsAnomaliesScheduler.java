package accountmanager.supporttool.scheduler;

import accountmanager.supporttool.controller.AuthenticationController;
import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.model.amstate.AMTriggerSPCallInfo;
import accountmanager.supporttool.model.amstate.SISUserRecord;
import accountmanager.supporttool.model.amstate.StudentWithNoUserRecordInfo;
import accountmanager.supporttool.service.AccountStateHealthCheckService;
import accountmanager.supporttool.service.AccountStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.util.Objects;


public class FixAccountsAnomaliesScheduler {

    private final Environment environment;
    private final AccountStateHealthCheckService accountStateHealthCheckService;
    private final AccountStateService accountStateService;
    private final Logger logger = LoggerFactory.getLogger(FixAccountsAnomaliesScheduler.class);

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
        try{
            if(Objects.requireNonNull(environment.getProperty("app.conf.enableScheduling")).equalsIgnoreCase("true")) {
                List<StudentWithNoUserRecordInfo> studentWithNoUserRecordInfoList = this.accountStateHealthCheckService.getListOfAccountsWithoutUserRecord();
                List<SISUserRecord> studentsWithDisabledUserRecordList = this.accountStateHealthCheckService.getListOfAccountsWithDisabledUserRecord();
                List<SISUserRecord> studentWithoutProfileRecordList = this.accountStateHealthCheckService.getListOfAccountsWithoutProfileRecord();
                List<AMTriggerSPCallInfo> studentEnrollmentIdsList = this.accountStateHealthCheckService.getListOfAccountsWithoutStateRecord();

                //TODO : TRANSFORM TO ASYNC OPERATIONS
                for (StudentWithNoUserRecordInfo studentWithNoUserRecordInfo : studentWithNoUserRecordInfoList) {
                    this.accountStateService.fixUserRecord(studentWithNoUserRecordInfo);
                }

                for (SISUserRecord sisUserRecord : studentsWithDisabledUserRecordList) {
                    this.accountStateService.fixDisabledUser(sisUserRecord);
                }

                for (SISUserRecord sisUserRecord : studentWithoutProfileRecordList) {
                    this.accountStateService.fixUserProfileRecord(sisUserRecord);
                }

                for (AMTriggerSPCallInfo amTriggerSPCallInfo : studentEnrollmentIdsList) {
                    AccountStateDTO accountStateDTO = this.accountStateService.getAccountStateDTOFromEnrollmentInfo(amTriggerSPCallInfo);
                    this.accountStateService.triggerAMStoredProcedure(accountStateDTO, amTriggerSPCallInfo.getEnrollment_Id());
                }
            }
        }catch(Exception exception){
            System.out.println(exception.getStackTrace());
        }
    }
}
