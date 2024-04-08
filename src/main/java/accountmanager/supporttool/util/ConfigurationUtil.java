package accountmanager.supporttool.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ConfigurationUtil {


    private Environment environment;

    @Autowired
    public ConfigurationUtil(Environment environment){
        this.environment = environment;
    }

   public String getConfiguredAcademicYear(){
       String academicYear = environment.getProperty("app.conf.academicYear");
       if(academicYear == null || academicYear.isEmpty()){
           return "";
       }
       return academicYear;
   }

    public String getConfiguredEducationTypesString(){
        String educationTypes = environment.getProperty("app.conf.educationTypes");
        if(educationTypes == null || educationTypes.isEmpty()){
            return "";
        }
        return educationTypes;
    }

    public List<String> getConfiguredEducationTypes(){
        String educationTypes = environment.getProperty("app.conf.educationTypes");
        if(educationTypes == null || educationTypes.isEmpty()){
            return new LinkedList<>();
        }
        return List.of(educationTypes.split(","));
    }

    //TODO : needs refactoring (better processing)
    public String getConfiguredEducationTypesConditionForQuery(){
        StringBuilder query1 = new StringBuilder("and epd.EducationType in (");
        List<String> educationTypeList = getConfiguredEducationTypes();
        for (String educationType : educationTypeList){
            query1.append("?,");
        }
        query1.deleteCharAt(query1.lastIndexOf(","));
        query1.append(")\n");
        return query1.toString();
    }

}
