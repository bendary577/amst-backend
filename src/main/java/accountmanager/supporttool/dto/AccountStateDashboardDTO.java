package accountmanager.supporttool.dto;

public class AccountStateDashboardDTO {

    private int studentsWithNoUserRecord;
    private int studentsWithDisabledUsers;
    private int studentsWithNoProfileRecord;
    private int studentsWithNoStateRecord;
    public AccountStateDashboardDTO(){}

    public int getStudentsWithNoUserRecord() {
        return studentsWithNoUserRecord;
    }

    public void setStudentsWithNoUserRecord(int studentsWithNoUserRecord) {
        this.studentsWithNoUserRecord = studentsWithNoUserRecord;
    }

    public int getStudentsWithDisabledUsers() {
        return studentsWithDisabledUsers;
    }

    public void setStudentsWithDisabledUsers(int studentsWithDisabledUsers) {
        this.studentsWithDisabledUsers = studentsWithDisabledUsers;
    }

    public int getStudentsWithNoProfileRecord() {
        return studentsWithNoProfileRecord;
    }

    public void setStudentsWithNoProfileRecord(int studentsWithNoProfileRecord) {
        this.studentsWithNoProfileRecord = studentsWithNoProfileRecord;
    }

    public int getStudentsWithNoStateRecord() {
        return studentsWithNoStateRecord;
    }

    public void setStudentsWithNoStateRecord(int studentsWithNoStateRecord) {
        this.studentsWithNoStateRecord = studentsWithNoStateRecord;
    }
}
