import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Weather {

    private String city;
    public String temperature;
    public String humidity;
    public String cloudiness;
    public String windSpeed;
    private String output;
    private String mainWeather;
    public String translateMainWeather;
    public String sunriseTime;
    public String sunsetTime;
    public String changeLog;
    boolean isCityExist = true;

    public Weather(String city){
        this.city = city;
    }

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
            System.out.println(obj.getJSONArray("weather").getJSONObject(0).getString("main"));
            mainWeather = String.valueOf(obj.getJSONArray("weather").getJSONObject(0).getString("main"));

            //������� �������� ������ � ����������� �� �������
            switch (mainWeather){
                case "Rain":
                    translateMainWeather = "�����";
                    break;
                case "Thunderstorm":
                    translateMainWeather = "�����";
                    break;
                case "Drizzle":
                    translateMainWeather = "������";
                    break;
                case "Snow":
                    translateMainWeather = "����";
                    break;
                case "Clear":
                    translateMainWeather = "����";
                    break;
                case "Clouds":
                    translateMainWeather = "�������";
                    break;
                default:
                    translateMainWeather = "";
            }

            System.out.println(translateMainWeather);

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
            JSONObject obj = new JSONObject(output);
            sunriseTime = String.valueOf(obj.getJSONObject("main").getDouble("temp"));
            isCityExist = true;
        } else {
            isCityExist = false;
        }
        return sunriseTime;
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
        String output = getUrlContent("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=57a6d6faf9702432eed0384f4a9283ea&units=metric");
        System.out.println(output);
        this.output = output;
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

    public String anotherCity(){
        String tempCity = this.city;
        getWeather();
        String response;
        if(!output.isEmpty()){
            System.out.println(output);
            response = "������ � ������ " + tempCity + ": " + MainWeather() +"\n�����������: " + getTemperature() + " ��\n���������: " + getHumidity()  +  " %\n����������: " + getCloudiness() + " %\n�������� �����: " + getWindSpeed() + " �/�";
        } else {
            System.out.println(output);
            response = "�� ������ ����� � ����� ���������";
        }
        return response;
    }

    public String readFromChangelogFile() throws IOException {
        String path = "./src/main/resources/changeLog.txt";

        DataInputStream dis = new DataInputStream(new FileInputStream(path));

        byte[] buffer = new byte[512];
        while (dis.available() != 0){
            int count = dis.read(buffer);

            if(count > 0){
                System.out.println(new String(buffer));
                changeLog = new String(buffer);
            }
        }

        return changeLog;
    }
}
