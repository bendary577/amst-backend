package accountmanager.supporttool.model.amstate;

public class StudentWithNoUserRecordInfo {

    private String Person_Id;
    private int gender;
    private String studentNumber;
    private String Code;
    private String username;
    private String password;

    public StudentWithNoUserRecordInfo(){}

    public String getPerson_Id() {
        return Person_Id;
    }

    public void setPerson_Id(String person_Id) {
        Person_Id = person_Id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void buildUserAndPassword(){
        if(studentNumber == null){
            return;
        }
        String genderFlag = gender == 1 ? "m" : "f";
        this.username = "stu" + genderFlag + studentNumber;
        this.password = "test123";
    }
}
