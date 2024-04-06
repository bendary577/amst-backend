package accountmanager.supporttool.dto;

import accountmanager.supporttool.model.amstate.StudentUserUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;

public class AccountStateDashboardDTO {

    private int studentsWithNoUserRecord;
    private int studentsWithDisabledUsers;
    private int studentsWithNoProfileRecord;
    private int studentsWithNoStateRecord;

    @JsonIgnore
    private HashSet<StudentUserUID> studentUserUIDList;
    private int numberOfDifferenceBetweenSIS_SSO_DBS;
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

    public HashSet<StudentUserUID> getStudentUserUIDList() {
        return studentUserUIDList;
    }

    public void setStudentUserUIDList(HashSet<StudentUserUID> studentUserUIDList) {
        this.studentUserUIDList = studentUserUIDList;
    }

    public int getNumberOfDifferenceBetweenSIS_SSO_DBS() {
        return numberOfDifferenceBetweenSIS_SSO_DBS;
    }

    public void setNumberOfDifferenceBetweenSIS_SSO_DBS(int numberOfDifferenceBetweenSIS_SSOD_BS) {
        this.numberOfDifferenceBetweenSIS_SSO_DBS = numberOfDifferenceBetweenSIS_SSOD_BS;
    }
}
