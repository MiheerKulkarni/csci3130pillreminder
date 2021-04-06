package com.validator.mymeds;

import java.util.Calendar;

public class SendNotification {

    /**
     * Function retrieves the current system time.
     * @return current system time as String.
     */
    public static String alertuser()
    {
        Calendar currentime = Calendar.getInstance();
        int time24hour = currentime.get(Calendar.HOUR_OF_DAY);
        int time24min = currentime.get(Calendar.MINUTE);
        if (time24min < 10)
        {
            String time24min0 = String.valueOf(time24min);
            String time24mn = "0"+time24min0;
            String time24hr = String.valueOf(time24hour);
            String timenow = time24hr+":"+time24mn;
            return timenow;
        }
        else {
            String time24mn = String.valueOf(time24min);
            String time24hr = String.valueOf(time24hour);
            String timenow = time24hr + ":" + time24mn;
            return timenow;
        }
    }
}