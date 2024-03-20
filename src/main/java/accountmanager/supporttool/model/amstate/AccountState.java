package accountmanager.supporttool.model.amstate;

public class AccountState {

    private String value;
    private String AccountID;
    private String userName;
    private String SISUserEmail;
    private String externalId;
    private String SISUnifiedUID;
    private String LastLoginDate;
    private int gender;
    private Integer staff_id;
    private Integer student_id;
    private Integer AccountManagerState_Id;
    private String staffNumber;
    private String studentNumber;

    public AccountState(){}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSISUserEmail() {
        return SISUserEmail;
    }

    public void setSISUserEmail(String SISUserEmail) {
        this.SISUserEmail = SISUserEmail;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getSISUnifiedUID() {
        return SISUnifiedUID;
    }

    public void setSISUnifiedUID(String SISUnifiedUID) {
        this.SISUnifiedUID = SISUnifiedUID;
    }

    public String getLastLoginDate() {
        return LastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        LastLoginDate = lastLoginDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getAccountManagerState_Id() {
        return AccountManagerState_Id;
    }

    public void setAccountManagerState_Id(Integer accountManagerState_Id) {
        AccountManagerState_Id = accountManagerState_Id;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
