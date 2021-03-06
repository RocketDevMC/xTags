package xyz.invisraidinq.tags.utils;

public class FormatUtils {

    public static String formatDecimals(Integer decimalSpaces, int number) {
        return String.format("%1$,." + decimalSpaces + "f", 0.0 + number);
    }

    public static String formatToTime(long millis) {
        int seconds = (int) (millis / 1000);
        if (seconds < 60) {
            return seconds + "s";
        }
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60) {
            if (secondsLeft > 0) {
                return minutes + "m " + secondsLeft + "s";
            }
            return minutes + "m";
        }
        if (minutes < 1440) {
            String time = "";
            int hours = minutes / 60;
            time = hours + "h";
            int inMins = 60 * hours;
            int leftOver = minutes - inMins;
            if (leftOver >= 1) {
                time = time + " " + leftOver + "m";
            }
            if (secondsLeft > 0) {
                time = time + " " + secondsLeft + "s";
            }
            return time;
        }
        String time = "";
        int days = minutes / 1440;
        time = days + "d";
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + "h";
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;
                if (leftOver >= 1) {
                    time = time + " " + minsLeft + "m";
                }
            }
        }
        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + "s";
        }
        return time;
    }
}
