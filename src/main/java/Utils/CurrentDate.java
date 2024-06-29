package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate {
    public static String GenerateCurrentDateAndTime() {
        return new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }
}
