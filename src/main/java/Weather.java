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
            String mainWeather = String.valueOf(obj.getJSONArray("weather").getJSONObject(0).getString("main"));

            //Перевод значения погоды с английского на русский
            switch (mainWeather) {
                case "Rain" -> translateMainWeather = "Дождь";
                case "Thunderstorm" -> translateMainWeather = "Гроза";
                case "Drizzle" -> translateMainWeather = "Морось";
                case "Snow" -> translateMainWeather = "Снег";
                case "Clear" -> translateMainWeather = "Ясно";
                case "Clouds" -> translateMainWeather = "Облачно";
                default -> translateMainWeather = "";
            }

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

    // Получение данных о заходе солнца из JSON ответа
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
        this.output = getUrlContent("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=57a6d6faf9702432eed0384f4a9283ea&units=metric");
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

    // Получение данных о погоде в городах которые введены вручную пользователем
    public String anotherCity(){
        String tempCity = this.city;
        getWeather();
        String response;
        if(!output.isEmpty()){
            tempCity = tempCity.substring(0, 1).toUpperCase() + tempCity.substring(1);
            System.out.println(fullUserName + ":\nГород: " + tempCity + "\n");
            response = "Сейчас в городе " + tempCity + ": " + MainWeather() +"\nТемпература: " + getTemperature() + " °С\nВлажность: " + getHumidity()  +  " %\nОблачность: " + getCloudiness() + " %\nСкорость ветра: " + getWindSpeed() + " м/с" + "\nВосход солнца ожидается в: " + getSunriseTime()  + "\nЗаход солнца ожидается в: " + getSunsetTime() + "\nЧасовой пояс: " + timeInGmt;
            System.out.println(response);
            System.out.println("--------------------------------------------------------------------------------------------------\n \n \n " );
        } else {
            response = "Не найден город с таким названием";
        }
        return response;
    }

    // выводим changeLog (все версии и обновления бота)
    public String changeLogOutput(){
        changeLog = """
                v1.0 - бот умеет отправлять данные о температуре, влажности и скорости ветра в 3 городах которые можно выбрать кнопками.
                v1.1 - добавлена поддержка ввода с клавиатуры любого города НА ЛЮБОМ ЯЗЫКЕ. Исправлены мелкие ошибки и баги
                v1.2 - добавлен показатель об общем состоянии погоды (Ясно, солнечно, дожди)
                v1.3 - добавлен показатель облачности
                v1.4 - добавлена команда /changelog
                v1.5 - проект выложен на GitHub
                v1.6 - добавлены данные о восходе и заходе солнца
                v1.7 - добавлена привязка к часовому поясу и его вывод
                v1.8 - исправлены мелкие баги и добавлена всплывающая подсказка при введении команд
                v1.9 - при выборе другого города, его название пишеться с большой буквы""";
        System.out.println(changeLog);

        return changeLog;
    }

    // получение часового пояса в GMT формате
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


    // конвертация unix в формат (часы, минуты)
    public String convertUnixToDate(long getUnixSunrise){
        Date date = new Date(getUnixSunrise*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone(timeInGmt));
        return sdf.format(date);
    }

}
