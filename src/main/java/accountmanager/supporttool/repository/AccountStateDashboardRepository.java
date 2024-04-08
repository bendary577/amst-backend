package accountmanager.supporttool.repository;

import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.model.amstate.StudentUserUID;
import accountmanager.supporttool.util.ConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
public class AccountStateDashboardRepository {

    private JdbcTemplate jdbcTemplate;
    private ConfigurationUtil configurationUtil;

    @Autowired
    public AccountStateDashboardRepository(JdbcTemplate jdbcTemplate,
                                            ConfigurationUtil configurationUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.configurationUtil = configurationUtil;
    }

    public AccountStateDashboardDTO getSISDashboardInfo() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("getAMSTDashboardInfo")
                .withoutProcedureColumnMetaDataAccess();

        simpleJdbcCall.declareParameters(
                new SqlParameter("academicYear", Types.INTEGER),
                new SqlParameter("educationTypes", Types.NVARCHAR),
                new SqlOutParameter("studentsWithNoUserRecord", Types.INTEGER),
                new SqlOutParameter("studentsWithDisabledUsers", Types.INTEGER),
                new SqlOutParameter("studentsWithNoProfileRecord", Types.INTEGER),
                new SqlOutParameter("studentsWithNoStateRecord", Types.INTEGER)
        );

        //set input params
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("academicYear", Integer.valueOf(configurationUtil.getConfiguredAcademicYear()));
        inParams.put("educationTypes", configurationUtil.getConfiguredEducationTypesString());

        Map<String, Object> result = simpleJdbcCall.execute(inParams);

        // Retrieve output parameters
        int studentsWithNoUserRecord = (int) result.get("studentsWithNoUserRecord");
        int studentsWithDisabledUsers = (int) result.get("studentsWithDisabledUsers");
        int studentsWithNoProfileRecord = (int) result.get("studentsWithNoProfileRecord");
        int studentsWithNoStateRecord = (int) result.get("studentsWithNoStateRecord");

        AccountStateDashboardDTO accountStateDashboard = new AccountStateDashboardDTO();
        accountStateDashboard.setStudentsWithNoUserRecord(studentsWithNoUserRecord);
        accountStateDashboard.setStudentsWithDisabledUsers(studentsWithDisabledUsers);
        accountStateDashboard.setStudentsWithNoProfileRecord(studentsWithNoProfileRecord);
        accountStateDashboard.setStudentsWithNoStateRecord(studentsWithNoStateRecord);
        return accountStateDashboard;
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
                "where ses.AcademicYear_Id = ? \n" +
                "and e.EnrollmentType = 2\n" +
                "and e.ExitDate is null\n" +
                configurationUtil.getConfiguredEducationTypesConditionForQuery() +
                "and e.IsDeleted = 0";
        PreparedStatementCreator preparedStatementCreator = getConfigurationPreparedStatement(sql);
        List<StudentUserUID> studentUserUIDList = jdbcTemplate.query(preparedStatementCreator, new BeanPropertyRowMapper<>(StudentUserUID.class));
        return new HashSet<>(studentUserUIDList);
    }

    public HashSet<StudentUserUID> getStudentsUserUIDFromSSODB() {
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

    private PreparedStatementCreator getConfigurationPreparedStatement(String sql){
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(configurationUtil.getConfiguredAcademicYear()));
            int counter = 2;
            for(String educationType : configurationUtil.getConfiguredEducationTypes()){
                preparedStatement.setInt(counter, Integer.parseInt(educationType));
                counter++;
            }
            return preparedStatement;
        };
        return preparedStatementCreator;
    }

}
