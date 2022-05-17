import org.json.JSONObject;

public class Time {
    public String secToGmt(String output) {
        String timeInGmt;

        Weather weather = new Weather();
        timeInGmt = weather.timeInGmt;

        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            int timeInSec = (int) obj.getDouble("timezone");
            timeInGmt = switch (timeInSec) {
                case -43200 -> "GMT-12";
                case -39600 -> "GMT-11";
                case -36000 -> "GMT-10";
                case -34200 -> "GMT-09:30";
                case -32400 -> "GMT-09:00";
                case -28800 -> "GMT-8";
                case -25200 -> "GMT-7";
                case -21600 -> "GMT-6";
                case -18000 -> "GMT-5";
                case -16200 -> "GMT-4:30";
                case -14400 -> "GMT-4";
                case -12600 -> "GMT-3:30";
                case -10800 -> "GMT-3";
                case -7200 -> "GMT-2";
                case -3600 -> "GMT-1";
                case 3600 -> "GMT+1";
                case 7200 -> "GMT+2";
                case 10800 -> "GMT+3";
                case 12600 -> "GMT+3:30";
                case 14400 -> "GMT+4";
                case 16200 -> "GMT+4:30";
                case 18000 -> "GMT+5";
                case 21600 -> "GMT+6";
                case 25200 -> "GMT+7";
                case 28800 -> "GMT+8";
                case 32400 -> "GMT+9";
                case 34200 -> "GMT+9:30";
                case 36000 -> "GMT+10";
                case 39600 -> "GMT+11";
                case 43200 -> "GMT+12";
                default -> "GMT";
            };
        }
        return timeInGmt;
    }
}