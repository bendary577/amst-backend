package accountmanager.supporttool.dto;

import accountmanager.supporttool.enumeration.SISUserRole;
import java.util.List;

public class AccountStateDTO {
    private String officialEmailInCommPhone;
    private String officialEmailInAMState;
    private String externalId;
    private String officialEmailInSSO;
    private String SISUnifiedUID;
    private String LastLoginDate;
    private int gender;
    private String staffNumber;
    private String studentNumber;
    private SISUserRole role;
    private List<String> troubleshootingRCA;
    private boolean hasUserRecord;
    private boolean hasOfficialEmailsMatch;
    private boolean hasSSORecord;
    private boolean isOfficialEmailMatchGender;
    private boolean hasExternalIdsMatch;
    private boolean isAsyncfix;
    private boolean hasStateRecord;
    public AccountStateDTO(){}

    public boolean isHasUserRecord() {
        return hasUserRecord;
    }

    public void setHasUserRecord(boolean hasUserRecord) {
        this.hasUserRecord = hasUserRecord;
    }

    public boolean isHasOfficialEmailsMatch() {
        return hasOfficialEmailsMatch;
    }

    public void setHasOfficialEmailsMatch(boolean hasOfficialEmailsMatch) {
        this.hasOfficialEmailsMatch = hasOfficialEmailsMatch;
    }

    public boolean isHasSSORecord() {
        return hasSSORecord;
    }

    public void setHasSSORecord(boolean hasSSORecord) {
        this.hasSSORecord = hasSSORecord;
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

    public boolean isOfficialEmailMatchGender() {
        return isOfficialEmailMatchGender;
    }

    public void setOfficialEmailMatchGender(boolean officialEmailMatchGender) {
        isOfficialEmailMatchGender = officialEmailMatchGender;
    }

    public SISUserRole getRole() {
        return role;
    }

    public void setRole(SISUserRole role) {
        this.role = role;
    }

    public boolean isHasExternalIdsMatch() {
        return hasExternalIdsMatch;
    }

    public void setHasExternalIdsMatch(boolean hasExternalIdsMatch) {
        this.hasExternalIdsMatch = hasExternalIdsMatch;
    }


    public List<String> getTroubleshootingRCA() {
        return troubleshootingRCA;
    }

    public void setTroubleshootingRCA(List<String> troubleshootingRCA) {
        this.troubleshootingRCA = troubleshootingRCA;
    }

    public boolean isAsyncfix() {
        return isAsyncfix;
    }

    public void setAsyncfix(boolean asyncfix) {
        isAsyncfix = asyncfix;
    }

    public boolean isHasStateRecord() {
        return hasStateRecord;
    }

    public void setHasStateRecord(boolean hasStateRecord) {
        this.hasStateRecord = hasStateRecord;
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

    public String getOfficialEmailInCommPhone() {
        return officialEmailInCommPhone;
    }

    public void setOfficialEmailInCommPhone(String officialEmailInCommPhone) {
        this.officialEmailInCommPhone = officialEmailInCommPhone;
    }

    public String getOfficialEmailInAMState() {
        return officialEmailInAMState;
    }

    public void setOfficialEmailInAMState(String officialEmailInAMState) {
        this.officialEmailInAMState = officialEmailInAMState;
    }

    public String getOfficialEmailInSSO() {
        return officialEmailInSSO;
    }

    public void setOfficialEmailInSSO(String officialEmailInSSO) {
        this.officialEmailInSSO = officialEmailInSSO;
    }
}
