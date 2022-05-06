import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
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

        if (message !=null && message.hasText()) {

            switch (message.getText()){
                case "Киев":
                    weather.getWeather();
                    sendMsg(message, "В Киеве сейчас: " + weather.MainWeather() +"\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() + " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с");
                    break;
                case "Одесса":
                    weather.getWeather();
                    sendMsg(message, "В Одессе сейчас: " + weather.MainWeather() +"\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() +  " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с");
                    break;
                case "Харьков":
                    weather.getWeather();
                    sendMsg(message, "В Харькове сейчас: " + weather.MainWeather() +"\nТемпература: " + weather.getTemperature() + " °С\nВлажность: " + weather.getHumidity() + " %\nОблачность: " + weather.getCloudiness() + " %\nСкорость ветра: " + weather.getWindSpeed() + " м/с");
                    break;
                case "/start":
                    sendMsg(message, "Доброго времени суток. Я прототип бота-синоптика версии 1.0 \nПока я умею не много, но уверен мой хозяин в скором времени добавит мне новые функции");
                    break;
                case "/settings":
                    sendMsg(message, "Пока что у меня нету настроек. \nНо создатель обещал сделать их в следующих версиях");
                    break;
                case "/changelog":
                    try {
                        sendMsg(message, weather.readFromChangelogFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMsg(message, weather.anotherCity());
                    System.out.println();
            }
        }
    }
}
