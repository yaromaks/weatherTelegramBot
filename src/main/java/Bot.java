import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "5329756602:AAE4t8x8XHyv99GkzmPjU8NcjQ0f0E8VL9g";
    private static final String USERNAME = "weather Bot";

    public String getBotToken(){
        return TOKEN;
    }

    public String getBotUsername(){
        return USERNAME;
    }
    //создаём клавиатуру
    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        //создаём кнопки
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Киев"));
        keyboardFirstRow.add(new KeyboardButton("Одесса"));
        keyboardFirstRow.add(new KeyboardButton("Харьков"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());   // Получаем ID чата
//        sendMessage.setReplyToMessageId(message.getMessageId()); // отвечаем прямо на сообщение пользователя
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        // Проверяем есть ли в обновлении сообщение и в сообщении есть текст
        Message message = update.getMessage();
        Weather weather = new Weather(message.getText());
        String userFullName = message.getChat().getFirstName() + " " +  message.getChat().getLastName();
        Weather weatherWithName = new Weather(message.getText(), userFullName);

        if (message !=null && message.hasText()) {

            switch (message.getText()) {
                case "Киев" -> {
                    System.out.println(userFullName + ":\nГород: Киев\n");
                    weather.getWeather();
                    String kievInfo = "В Киеве сейчас: " + weather.MainWeather() + "\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() + " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с" + "\nВосход солнца ожидается в: " + weather.getSunriseTime() + "\nЗаход солнца ожидается в: " + weather.getSunsetTime() + "\nЧасовой пояс: " + weather.getGMT();
                    sendMsg(message, kievInfo);
                    System.out.println(kievInfo);
                    System.out.println("--------------------------------------------------------------------------------------------------\n \n \n " );
                }
                case "Одесса" -> {
                    System.out.println(userFullName + ":\nГород: Одесса\n");
                    weather.getWeather();
                    String odessaInfo = "В Одессе сейчас: " + weather.MainWeather() + "\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() + " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с" + "\nВосход солнца ожидается в: " + weather.getSunriseTime() + "\nЗаход солнца ожидается в: " + weather.getSunsetTime() + "\nЧасовой пояс: " + weather.getGMT();
                    sendMsg(message, odessaInfo);
                    System.out.println(odessaInfo);
                    System.out.println("--------------------------------------------------------------------------------------------------\n \n \n " );
                }
                case "Харьков" -> {
                    System.out.println(userFullName + ":\nГород: Харьков\n");
                    weather.getWeather();
                    String kharkovInfo = "В Харькове сейчас: " + weather.MainWeather() + "\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() + " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с" + "\nВосход солнца ожидается в: " + weather.getSunriseTime() + "\nЗаход солнца ожидается в: " + weather.getSunsetTime() + "\nЧасовой пояс: " + weather.getGMT();
                    sendMsg(message, kharkovInfo);
                    System.out.println(kharkovInfo);
                    System.out.println("--------------------------------------------------------------------------------------------------\n \n \n " );
                }
                case "/start" -> sendMsg(message, """
                        Доброго времени суток. Я прототип бота-синоптика версии 1.9\s
                        Пока я умею не много, но уверен мой хозяин в скором времени добавит мне новые функции\s
                        Что бы узнать погоду в вашем городе, просто отправьте мне его название или выберите один из предложенных вариантов\s
                        Так же что бы узнать все нововведения, вы можете написать /changelog""");

                case "/settings" -> sendMsg(message, "Пока что у меня нету настроек. \nНо создатель обещал добавить их в следующих версиях");
                case "/changelog" -> sendMsg(message, weather.changeLogOutput());
                default -> sendMsg(message, weatherWithName.anotherCity());
            }
        }
    }
}
