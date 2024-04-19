package accountmanager.supporttool.repository;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.model.amstate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountStateRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountStateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO : use parameterized queries
    public List<AccountState> getAllSISData(String officialEmail) {
        String sql = " select p.externalId, p.gender, ph.Value, ams.AccountManagerState_Id, ams.AccountID, u.UserName, u.LastLoginDate, s.staff_id,s.staffNumber, st.student_id, st.studentNumber\n" +
                " from COMM_Person p\n" +
                " join COMM_Phone ph on p.PhoneList_Id = ph.PhoneList_Id and ContactType = 10\n" +
                " left join PRJ_AccountManagerState ams on ams.Person_Id = p.Person_Id\n" +
                " left join SYS_User u on u.User_Id = p.User_Id\n" +
                " left join ADM_Staff s on s.staff_id = p.Person_Id\n" +
                " left join ADM_Student st on st.student_id = p.person_id\n" +
                " where ph.Value = '"+ officialEmail + "'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AccountState.class));
    }

    public List<AccountState> getAllSSOData(String officialEmail) {
        String sql = "SELECT SISUserEmail, SISUnifiedUID FROM SISUsers where SISUserEmail = '" + officialEmail + "'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AccountState.class));
    }

    //------------------------------------------- NO User Record Case Handling ------------------------
    //TODO : this operation must be atomic - executed in a transaction ... handle exception
    @SwitchDataSource(value = "ZLMESE")
    public void fixStudentWithNoUserRecord(String officialEmail){
        List<StudentWithNoUserRecordInfo> studentWithNoUserRecordInfoList = studentWithNoUserRecordPrepareInfo(officialEmail);
        executeStudentUserProfileProcedure(studentWithNoUserRecordInfoList.get(0));
        executeResourceGroupAssignProcedure(studentWithNoUserRecordInfoList.get(0));
    }

    @SwitchDataSource(value = "ZLMESE")
    public void fixStudentWithNoUserRecord(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo){
        executeStudentUserProfileProcedure(studentWithNoUserRecordInfo);
        executeResourceGroupAssignProcedure(studentWithNoUserRecordInfo);
    }

    //TODO : MAKE ACADEMIC YEAR - education type CONFIGURABLE
    public List<StudentWithNoUserRecordInfo> studentWithNoUserRecordPrepareInfo(String officialEmail){
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
                "and u.User_Id is null\n" +
                "and ph.value = '"+ officialEmail + "'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StudentWithNoUserRecordInfo.class));
    }

    public void executeStudentUserProfileProcedure(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("student_create_user_profile")
                .withoutProcedureColumnMetaDataAccess();

        // Declare the parameters for the stored procedure
        simpleJdbcCall.declareParameters(
                new SqlParameter("username", Types.VARCHAR),
                new SqlParameter("password", Types.VARCHAR),
                new SqlParameter("PersonId", Types.INTEGER)
        );
        studentWithNoUserRecordInfo.buildUserAndPassword();

        // Set the input parameters
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("username", studentWithNoUserRecordInfo.getUsername());
        inParams.put("password", studentWithNoUserRecordInfo.getPassword());
        inParams.put("PersonId", studentWithNoUserRecordInfo.getPerson_Id());

        // Execute the stored procedure
        Map<String, Object> result = simpleJdbcCall.execute(inParams);
    }

    public void executeResourceGroupAssignProcedure(StudentWithNoUserRecordInfo studentWithNoUserRecordInfo) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("resourceGroup_Assign")
                .withoutProcedureColumnMetaDataAccess();

        // Declare the parameters for the stored procedure
        simpleJdbcCall.declareParameters(
                new SqlParameter("username", Types.VARCHAR),
                new SqlParameter("SiteCode", Types.INTEGER)
        );

        // Set the input parameters
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("username", studentWithNoUserRecordInfo.getUsername());
        inParams.put("SiteCode", studentWithNoUserRecordInfo.getCode());

        // Execute the stored procedure
        Map<String, Object> result = simpleJdbcCall.execute(inParams);
    }

    //------------------------------------------- SSO --------------------------------------------------------
    @SwitchDataSource(value = "ZLMESE")
    public List<UserSSOSPCallInfo> prepareSSOSPInfoFromSIS(String officialEmail) {
        String sql = "select \n" +
                "ph.Value Email, \n" +
                "isnull(p.IDNumber,'') IDNumber, \n" +
                "p.person_Id,\n" +
                "p.FirstName + ' ' + p.FamilyName SISUserNameAr,\n" +
                "isnull(p.EnglishFirstName + ' ' + p.EnglishFamilyName,'') SISUserNameEn,\n" +
                "p.ExternalID SISID\n" +
                "from COMM_Phone ph \n" +
                "join COMM_Person p on p.PhoneList_Id = ph.PhoneList_Id and contactType in (10)\n" +
                "join SYS_User u on p.User_Id = u.User_Id\n" +
                "where ph.Value = '"+officialEmail+"'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserSSOSPCallInfo.class));

    }

    //TODO : REFACTOR USER ROLE TO BE DYNAMICALLY DETECTED (not hardcoded)
    public String prepareSSOSPCallCommand(List<UserSSOSPCallInfo> userSSOSPCallInfoList){
        UserSSOSPCallInfo userSSOSPCallInfo = userSSOSPCallInfoList.get(0);
        String ssoStoredProcedureCall = "EXEC [SIS].[dbo].[SP_SyncAccountDetails] \n" +
                "N'" + userSSOSPCallInfo.getEmail() + "',\n" +
                "N'" + userSSOSPCallInfo.getIDNumber() + "', \n" +
                "N'SST11Pers" + userSSOSPCallInfo.getPerson_Id() + "@ese.gov.ae', \n" +
                "N'SST-1-1-Pers-" + userSSOSPCallInfo.getPerson_Id() + "@ese.gov.ae', \n" +
                "N'" + userSSOSPCallInfo.getSISID() + "',\n" +
                "N'" + userSSOSPCallInfo.getSISUserNameEn() + "', \n" +
                "N'" + userSSOSPCallInfo.getSISUserNameAr() + "', \n" +
                "N'[ \"STUDENT\" ]'";
        return ssoStoredProcedureCall;
    }

    @SwitchDataSource(value = "SIS")
    public int executeSSOSPCallCommand(String command){
        return jdbcTemplate.update(command);
    }
    //------------------------------------------ Trigger AM --------------------------------------------

    public void deleteUserAccountState(String officialEmail) {
        String sql = "delete PRJ_AccountManagerState WHERE AccountID = '"+officialEmail+"';";
        jdbcTemplate.update(sql);
    }

    public List<AMTriggerSPCallInfo> prepareAMTriggerSPCall(String studentNumber) {
        String sql = "select distinct \n" +
                "e.Enrollment_Id,\n" +
                "s.studentNumber,\n" +
                "ams.AccountManagerState_Id\n" +
                "from ADM_Enrollment e\n" +
                "join ADM_Session ses on ses.Session_Id = e.Session_Id\n" +
                "join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id\n" +
                "join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id\n" +
                "join COMM_Person p on p.Person_Id = e.Student_Id\n" +
                "join ADM_Student s on s.Student_Id = p.Person_Id\n" +
                "left join PRJ_AccountManagerState ams on ams.Person_Id = p.Person_Id\n" +
                "where ses.AcademicYear_Id = 2024 \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                "and epd.EducationType in (1,2)\n" +
                "and e.IsDeleted = 0\n" +
                "and s.StudentNumber = '"+studentNumber+"'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AMTriggerSPCallInfo.class));
    }

    public void executeAMTriggerProcedure(int enrollmentId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("triggerAccountManagerNotification")
                .withoutProcedureColumnMetaDataAccess();

        // Declare the parameters for the stored procedure
        simpleJdbcCall.declareParameters(
                new SqlParameter("triggered_enrollment_id", Types.INTEGER)
        );

        // Set the input parameters
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("triggered_enrollment_id", enrollmentId);

        // Execute the stored procedure
        Map<String, Object> result = simpleJdbcCall.execute(inParams);
    }

    public void fixUserProfileRecord(SISUserRecord sisUserRecord) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("student_userprofile")
                .withoutProcedureColumnMetaDataAccess();

        // Declare the parameters for the stored procedure
        simpleJdbcCall.declareParameters(
                new SqlParameter("user_id", Types.INTEGER)
        );

        // Set the input parameters
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("user_id", sisUserRecord.getUser_id());

        // Execute the stored procedure
        Map<String, Object> result = simpleJdbcCall.execute(inParams);
    }

    public void enableDisabledUser(SISUserRecord sisUserRecord) {
        String sql = "update SYS_User set UserIsEnabled = 1 where User_Id = ?";
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(sisUserRecord.getUser_id()));
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator);
    }
}
