package accountmanager.supporttool.repository;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.model.amstate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountStateHealthCheckRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountStateHealthCheckRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StudentWithNoUserRecordInfo> getListOfAccountsWithoutUserRecord(){
        String sql = "select \n" +
                "distinct\n" +
                "p.Person_Id,\n" +
                "P.gender,\n" +
                "s.studentNumber,\n" +
                "ss.Code\n" +
                "from ADM_Enrollment e\n" +
                "join STRUCT_Site ss on e.School_Id = ss.Site_Id\n" +
                "join ADM_Student s on s.Student_Id = e.Student_Id\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "join COMM_Phone ph on ph.phoneList_id = p.phoneList_id and contacttype = 10\n" +
                "left JOIN SYS_User u on p.User_Id = u.User_Id\n" +
                "where \n" +
                "ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\t\n" +
                "and p.IsDeleted = 0\t\n" +
                "and u.User_Id is null";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StudentWithNoUserRecordInfo.class));
    }

    public List<SISUserRecord> getListOfAccountsWithDisabledUserRecord(){
        String sql = "select \n" +
                "distinct\n" +
                "u.user_id\n" +
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
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SISUserRecord.class));
    }


    public List<SISUserRecord> getListOfAccountsWithoutProfileRecord() {
        String sql = "select \n" +
                "distinct\n" +
                "u.user_id\n" +
                "from ADM_Enrollment e\n" +
                "join STRUCT_Site ss on e.School_Id = ss.Site_Id\n" +
                "join ADM_Student s on s.Student_Id = e.Student_Id\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "join SYS_User u on u.user_id = p.user_id\n" +
                "left JOIN RIGHT_UserProfile up on up.user_id = u.user_id and up.Profile_Id = 39  \n" +
                "where \n" +
                "ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1, 3)\n" +
                "and e.IsDeleted = 0\t\n" +
                "and p.IsDeleted = 0\t\n" +
                "and (up.UserProfile_Id is null)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SISUserRecord.class));
    }

    public List<AMTriggerSPCallInfo> getListOfAccountsWithoutStateRecord() {
        String sql = "select \n" +
                "distinct\n" +
                "e.Enrollment_Id\n" +
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
                "and e.IsDeleted = 0\t\t    \n" +
                "and ac.AccountManagerState_Id IS null";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AMTriggerSPCallInfo.class));
    }
}
