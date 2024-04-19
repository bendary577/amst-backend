package accountmanager.supporttool.model.amstate;

public class AMTriggerSPCallInfo {

    private int Enrollment_Id;
    private String AccountManagerState_Id;

    public AMTriggerSPCallInfo(){}

    public int getEnrollment_Id() {
        return Enrollment_Id;
    }

    public void setEnrollment_Id(int enrollment_Id) {
        Enrollment_Id = enrollment_Id;
    }

    public String getAccountManagerState_Id() {
        return AccountManagerState_Id;
    }

    public void setAccountManagerState_Id(String accountManagerState_Id) {
        AccountManagerState_Id = accountManagerState_Id;
    }
}
