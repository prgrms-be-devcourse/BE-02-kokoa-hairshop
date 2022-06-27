package com.prgms.kokoahairshop.util;

import java.util.ArrayList;
import java.util.List;

public class TimeUtil {

    public static int strTimeToIntMin(String time) {
        String[] splitTime = time.split(":");
        return 60 * Integer.parseInt(splitTime[0]) + Integer.parseInt(splitTime[1]);
    }

    public static List<String> getTimesFromStartAndEndTime(String startTime, String endTime) {
        int startMin = strTimeToIntMin(startTime);
        int endMin = strTimeToIntMin(endTime);

        List<String> times = new ArrayList<>();
        for (int i = startMin; i <= endMin; i += 30) {
            int hour = i / 60;
            int min = i - hour * 60;
            String time = (hour < 10 ? "0" : "") + hour + ":" + (min < 10 ? "0" : "") + min;
            times.add(time);
        }

        return times;
    }
}
