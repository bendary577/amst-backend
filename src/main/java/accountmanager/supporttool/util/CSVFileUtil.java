package accountmanager.supporttool.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CSVFileUtil {
    public static String TYPE = "text/csv";
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

}
