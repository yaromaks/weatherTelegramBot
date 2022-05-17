import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Weather {

    private String city;
    private String fullUserName;
    public String temperature;
    public String humidity;
    public String cloudiness;
    public String windSpeed;
    public String output;
    public String translateMainWeather;
    public String timeInGmt;
    public String sunriseTime;
    public String sunsetTime;
    public String changeLog;
    boolean isCityExist = true;

    public Weather(){}
    public Weather(String city){
        this.city = city;
    }
    public Weather(String city, String fullUserName){this.city = city; this.fullUserName = fullUserName;}


    /* ����� �� ������� �� �������� ���������� � ������ �������� �� ���������� �����.
    � ��� ������� �������� � ������ �������� lang=ru, ��������� ��������.
    intellijIdea �� ��������� ��������� � ������� ���������.
    � ����� � ���� ���� ������� ������� ��������� ����� �� ���������� �����,
    �� � ������� ����������� switch-case ���������� ��� �� ������� ����
     */

    // ��������� �������� ���������� � ������ (��������, �����, �������� � ��) �� ���������� �� JSON ������
    public String MainWeather(){
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            String mainWeather = String.valueOf(obj.getJSONArray("weather").getJSONObject(0).getString("main"));

            //������� �������� ������ � ����������� �� �������
            switch (mainWeather) {
                case "Rain" -> translateMainWeather = "�����";
                case "Thunderstorm" -> translateMainWeather = "�����";
                case "Drizzle" -> translateMainWeather = "������";
                case "Snow" -> translateMainWeather = "����";
                case "Clear" -> translateMainWeather = "����";
                case "Clouds" -> translateMainWeather = "�������";
                default -> translateMainWeather = "";
            }

            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return translateMainWeather;
    }

    // ��������� ������ � ����������� �� JSON ������
    public String getTemperature() {
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            temperature = String.valueOf(obj.getJSONObject("main").getDouble("temp"));
            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return temperature;
    }

    // ��������� ������ � ������� ������ �� JSON ������
    public String getSunriseTime() {
        if (!output.isEmpty()) {
            getGMT();
            JSONObject obj = new JSONObject(output);
            long getUnixSunrise = (long) obj.getJSONObject("sys").getDouble("sunrise");
            sunriseTime = convertUnixToDate(getUnixSunrise);

            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return sunriseTime;
    }

    // ��������� ������ � ������ ������ �� JSON ������
    public String getSunsetTime() {
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            long getUnixSunset = (long) obj.getJSONObject("sys").getDouble("sunset");
            sunsetTime = convertUnixToDate(getUnixSunset);

            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return sunsetTime;
    }




    // ��������� ������ � ��������� �� JSON ������
    public String getHumidity() {
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            humidity = String.valueOf(Math.round(obj.getJSONObject("main").getDouble("humidity")));
            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return humidity;
    }

    // ��������� ������ �� ���������� �� JSON ������
    public String getCloudiness() {
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            cloudiness = String.valueOf(Math.round(obj.getJSONObject("clouds").getDouble("all")));
            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return cloudiness;
    }


    // ��������� ������ � �������� ����� �� JSON ������
    public String getWindSpeed() {
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            windSpeed = String.valueOf(obj.getJSONObject("wind").getDouble("speed"));
            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return windSpeed;
    }


    // ������ �� ������ openWeatherMap ��� ��������� ������� ������ � ������ � JSON �������
    public void getWeather() {
        city = URLEncoder.encode(city, StandardCharsets.UTF_8);
        this.output = getUrlContent("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=57a6d6faf9702432eed0384f4a9283ea&units=metric");
    }

    // ����� ��� ������������� ���-��������
    private static String getUrlContent(String urlAddress){
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e){
            System.out.println("����� ����� �� ������");

        }
        return content.toString();
    }

    // ��������� ������ � ������ � ������� ������� ������� ������� �������������
    public String anotherCity(){
        String tempCity = this.city;
        getWeather();
        String response;
        if(!output.isEmpty()){
            tempCity = tempCity.substring(0, 1).toUpperCase() + tempCity.substring(1);
            System.out.println(fullUserName + ":\n�����: " + tempCity + "\n");
            response = "������ � ������ " + tempCity + ": " + MainWeather() +"\n�����������: " + getTemperature() + " ��\n���������: " + getHumidity()  +  " %\n����������: " + getCloudiness() + " %\n�������� �����: " + getWindSpeed() + " �/�" + "\n������ ������ ��������� �: " + getSunriseTime()  + "\n����� ������ ��������� �: " + getSunsetTime() + "\n������� ����: " + timeInGmt;
            System.out.println(response);
            System.out.println("--------------------------------------------------------------------------------------------------\n \n \n " );
        } else {
            response = "�� ������ ����� � ����� ���������";
        }
        return response;
    }

    // ������� changeLog (��� ������ � ���������� ����)
    public String changeLogOutput(){
        changeLog = """
                v1.0 - ��� ����� ���������� ������ � �����������, ��������� � �������� ����� � 3 ������� ������� ����� ������� ��������.
                v1.1 - ��������� ��������� ����� � ���������� ������ ������ �� ����� �����. ���������� ������ ������ � ����
                v1.2 - �������� ���������� �� ����� ��������� ������ (����, ��������, �����)
                v1.3 - �������� ���������� ����������
                v1.4 - ��������� ������� /changelog
                v1.5 - ������ ������� �� GitHub
                v1.6 - ��������� ������ � ������� � ������ ������
                v1.7 - ��������� �������� � �������� ����� � ��� �����
                v1.8 - ���������� ������ ���� � ��������� ����������� ��������� ��� �������� ������
                v1.9 - ��� ������ ������� ������, ��� �������� �������� � ������� �����""";
        System.out.println(changeLog);

        return changeLog;
    }

    // ��������� �������� ����� � GMT �������
    public String getGMT(){
        if (!output.isEmpty()) {
            Time time = new Time();
            timeInGmt = time.secToGmt(output);

            isCityExist = true;
        } else {
            isCityExist = false;
        }

        return timeInGmt;
    }


    // ����������� unix � ������ (����, ������)
    public String convertUnixToDate(long getUnixSunrise){
        Date date = new Date(getUnixSunrise*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone(timeInGmt));
        return sdf.format(date);
    }

}
