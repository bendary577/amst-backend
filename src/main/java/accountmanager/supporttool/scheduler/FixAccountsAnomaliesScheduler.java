package accountmanager.supporttool.scheduler;


import org.springframework.scheduling.annotation.Scheduled;


public class FixAccountsAnomaliesScheduler {


    @Scheduled(cron = "0 0 22 * * *") // Execute every day at 10 PM
    public void scheduledTask() {
        // Perform your scheduled task here
        System.out.println("Executing scheduled task...");
    }
}
