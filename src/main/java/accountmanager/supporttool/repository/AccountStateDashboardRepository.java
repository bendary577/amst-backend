package accountmanager.supporttool.repository;

import accountmanager.supporttool.model.amstate.StudentUserUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public class AccountStateDashboardRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountStateDashboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO : use parameterized queries
    public Integer getNumberOfStudentsWithoutUserRecord() {
        String sql = "select \n" +
                "count(*) studentsWithNoUserRecord\n" +
                "from ADM_Enrollment e\n" +
                "join STRUCT_Site ss on e.School_Id = ss.Site_Id\n" +
                "join ADM_Student s on s.Student_Id = e.Student_Id\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "left JOIN SYS_User u on p.User_Id = u.User_Id\n" +
                "where \n" +
                "ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\t\n" +
                "and p.IsDeleted = 0\t\n" +
                "and u.User_Id is null";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getNumberOfStudentsWithDisabledUsers() {
        String sql = "select \n" +
                "count(*) studentsWithDisabledUsers\n" +
                "from ADM_Enrollment e\n" +
                "join STRUCT_Site ss on e.School_Id = ss.Site_Id\n" +
                "join ADM_Student s on s.Student_Id = e.Student_Id\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "join SYS_User u on u.user_id = p.user_id\n" +
                "where \n" +
                "ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\t\n" +
                "and p.IsDeleted = 0\t\n" +
                "and u.UserIsEnabled = 0";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getNumberOfStudentsWithoutProfile() {
        String sql = "select \n" +
                "count(*) studentsWithNoProfileRecord\n" +
                "from ADM_Enrollment e\n" +
                "join STRUCT_Site ss on e.School_Id = ss.Site_Id\n" +
                "join ADM_Student s on s.Student_Id = e.Student_Id\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "join SYS_User u on u.user_id = p.user_id\n" +
                "left JOIN RIGHT_UserProfile up on up.user_id = u.user_id and up.profile_id = 39\n" + //student portal profile
                "where \n" +
                "ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\t\n" +
                "and p.IsDeleted = 0\t\n" +
                "and up.UserProfile_Id is null";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getNumberOfStudentsWithoutState() {
        String sql = "select \n" +
                "count(*) studentsWithNoStateRecord\n" +
                "from ADM_Enrollment e\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "left join PRJ_AccountManagerState ac on ac.Person_Id = e.Student_Id\n" +
                "where ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\n" +
                "and ac.AccountManagerState_Id IS null";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public HashSet<StudentUserUID> getStudentsUserUIDfromSISDB() {
        String sql = "select\n" +
                "distinct\n" +
                "'SST-1-1-Pers-' + convert(nvarchar(max), p.person_id) as sisuserUID\n" +
                "from ADM_Enrollment e\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "left join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id and ph.ContactType = 10\n" +
                "where ses.AcademicYear_Id = 2024\n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1,3)\n" +
                "and e.IsDeleted = 0";
        List<StudentUserUID> studentUserUIDList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StudentUserUID.class));
        return new HashSet<>(studentUserUIDList);
    }

    public HashSet<StudentUserUID> getStudentsUserUIDfromSSODB() {
        String sql = "select\n" +
                "distinct\n" +
                "sisuserUID\n" +
                "from SISUsers\n" +
                "where\n" +
                "SISUserRole like '%STUDENT%'\n" +
                "or (SISUserRole LIKE '%ADMIN%' and SISUserEmail like 'stu%')\n" +
                "or (SISUserRole LIKE '%ADMIN%' and SISUserEmail like 'S%')";
        List<StudentUserUID> studentUserUIDList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StudentUserUID.class));
        return new HashSet<>(studentUserUIDList);
    }


}
