package accountmanager.supporttool.util;

import java.util.List;

public class OfficialEmailUtil {

    public static boolean doOfficialEmailsMatchWithGender(int gender, List<String> officialEmails){
        boolean doOfficialEmailsMatchWithGender = true;
        for(String officialEmail : officialEmails){
            if ((officialEmail != null && !officialEmail.isEmpty())
                    && (officialEmail.contains("stu"))
                    && ((officialEmail.contains("stuf") && gender == 1)
                    || (officialEmail.contains("stum") && gender == 2))) {
                doOfficialEmailsMatchWithGender = false;
                break;
            }
        }
        return doOfficialEmailsMatchWithGender;
    }

    public static boolean isValidOfficialEmail(String officialEmail){
        officialEmail = officialEmail.toLowerCase();
        String regex = "[A-Za-z0-9._%+-]+@(ese\\.gov\\.ae|kbza\\.sch\\.ae)$";
        return officialEmail.matches(regex);
    }

    public static boolean isValidInternalEmail(String officialEmail){
        officialEmail = officialEmail.toLowerCase();
        String regex = "[A-Za-z0-9._%+-]+@(secondstep\\.ae|itworxedu\\.com)$";
        return officialEmail.matches(regex);
    }
}
