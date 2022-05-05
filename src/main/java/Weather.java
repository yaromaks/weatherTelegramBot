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

    /* Ответ от сервера об основной информации о погоде приходит на английском языке.
    А при попытке добавить в запрос аргумент lang=ru, возникает проблема.
    intellijIdea не распознаёт кодировку и выводит белиберду.
    В связи с этим было принято решение оставлять ответ на английском языке,
    но с помощью конструкции switch-case переводить его на русский язык
     */

    // Получение основной информации о погоде (солнечно, дождь, пасмурно и тд) НА АНГЛИЙСКОМ из JSON ответа
    public String MainWeather(){
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            System.out.println(obj.getJSONArray("weather").getJSONObject(0).getString("main"));
            mainWeather = String.valueOf(obj.getJSONArray("weather").getJSONObject(0).getString("main"));

            //Перевод значения погоды с английского на русский
            switch (mainWeather){
                case "Rain":
                    translateMainWeather = "Дождь";
                    break;
                case "Thunderstorm":
                    translateMainWeather = "Гроза";
                    break;
                case "Drizzle":
                    translateMainWeather = "Морось";
                    break;
                case "Snow":
                    translateMainWeather = "Снег";
                    break;
                case "Clear":
                    translateMainWeather = "Ясно";
                    break;
                case "Clouds":
                    translateMainWeather = "Облачно";
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

    // Получение данных о температуре из JSON ответа
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

    // Получение данных о восходе солнца из JSON ответа
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




    // Получение данных о влажности из JSON ответа
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

    // Получение данных об облачности из JSON ответа
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


    // Получение данных о скорости ветра из JSON ответа
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


    // Запрос на сервер openWeatherMap для получений полного отчёта о погоде в JSON формате
    public void getWeather() {
        city = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String output = getUrlContent("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=57a6d6faf9702432eed0384f4a9283ea&units=metric");
        System.out.println(output);
        this.output = output;
    }

    // Метод для использования веб-запросов
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
            System.out.println("Такой город не найден");

        }
        return content.toString();
    }

    public String anotherCity(){
        String tempCity = this.city;
        getWeather();
        String response;
        if(!output.isEmpty()){
            System.out.println(output);
            response = "Сейчас в городе " + tempCity + ": " + MainWeather() +"\nТемпература: " + getTemperature() + " °С\nВлажность: " + getHumidity()  +  " %\nОблачность: " + getCloudiness() + " %\nСкорость ветра: " + getWindSpeed() + " м/с";
        } else {
            System.out.println(output);
            response = "Не найден город с таким названием";
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
