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
    //������ ����������
    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        //������ ������
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("����"));
        keyboardFirstRow.add(new KeyboardButton("������"));
        keyboardFirstRow.add(new KeyboardButton("�������"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());   // �������� ID ����
//        sendMessage.setReplyToMessageId(message.getMessageId()); // �������� ����� �� ��������� ������������
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }




    public void onUpdateReceived(Update update) {
        // ��������� ���� �� � ���������� ��������� � � ��������� ���� �����
        Message message = update.getMessage();
        Weather weather = new Weather(message.getText());

        if (message !=null && message.hasText()) {

            switch (message.getText()){
                case "����":
                    weather.getWeather();
                    sendMsg(message, "� ����� ������: " + weather.MainWeather() +"\n�����������: " + weather.getTemperature() + " ��\n���������: " + weather.getHumidity() + " %\n����������: " + weather.getCloudiness() + " %\n�������� �����: " + weather.getWindSpeed() + " �/�");
                    break;
                case "������":
                    weather.getWeather();
                    sendMsg(message, "� ������ ������: " + weather.MainWeather() +"\n�����������: " + weather.getTemperature() + " ��\n���������: " + weather.getHumidity() +  " %\n����������: " + weather.getCloudiness() + " %\n�������� �����: " + weather.getWindSpeed() + " �/�");
                    break;
                case "�������":
                    weather.getWeather();
                    sendMsg(message, "� �������� ������: " + weather.MainWeather() +"\n�����������: " + weather.getTemperature() + " ��\n���������: " + weather.getHumidity() + " %\n����������: " + weather.getCloudiness() + " %\n�������� �����: " + weather.getWindSpeed() + " �/�");
                    break;
                case "/start":
                    sendMsg(message, "������� ������� �����. � �������� ����-��������� ������ 1.0 \n���� � ���� �� �����, �� ������ ��� ������ � ������ ������� ������� ��� ����� �������");
                    break;
                case "/settings":
                    sendMsg(message, "���� ��� � ���� ���� ��������. \n�� ��������� ������ ������� �� � ��������� �������");
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
