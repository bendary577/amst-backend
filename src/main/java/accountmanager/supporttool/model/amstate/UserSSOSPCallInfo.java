package accountmanager.supporttool.model.amstate;

public class UserSSOSPCallInfo {

    private int person_Id;
    private String Email;
    private String IDNumber;
    private String SISUserNameAr;
    private String SISUserNameEn;
    private String SISID;
    public UserSSOSPCallInfo(){}

    public int getPerson_Id() {
        return person_Id;
    }

    public void setPerson_Id(int person_Id) {
        this.person_Id = person_Id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getSISUserNameAr() {
        return SISUserNameAr;
    }

    public void setSISUserNameAr(String SISUserNameAr) {
        this.SISUserNameAr = SISUserNameAr;
    }

    public String getSISUserNameEn() {
        return SISUserNameEn;
    }

    public void setSISUserNameEn(String SISUserNameEn) {
        this.SISUserNameEn = SISUserNameEn;
    }

    public String getSISID() {
        return SISID;
    }

    public void setSISID(String SISID) {
        this.SISID = SISID;
    }
}
