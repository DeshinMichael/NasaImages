import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NasaTelegramBot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "MediaNasa_bot";
    private static final String URI = "https://api.nasa.gov/planetary/apod?api_key=4WStRaNMWigqnVu733aTqCSSzhzNSgytPlLHgHAK";
    private static long chat_id;

    public NasaTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        try {
            return new Scanner(new File("src/main/java/Token")).nextLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            chat_id = update.getMessage().getChatId();
            switch (update.getMessage().getText()) {
                case "/start":
                case "Help":
                case "/help":
                    sendMessage("Привет, я бот NasaEventsBot! Я высылаю события по запросу." +
                            " Напоминаю, что события на сайте NASA обновляются раз в сутки", chat_id);
                    break;
                case "Event":
                case "/event":
                    try {
                        sendMessage(Utils.getUrl(URI) + "\n" + Utils.getDesc(URI), chat_id);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    sendMessage("Я не понимаю :(", chat_id);
            }
        }
    }

    private void sendMessage(String messageText, long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Help");
        inlineKeyboardButton1.setCallbackData("HELP");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Event");
        inlineKeyboardButton2.setCallbackData("EVENT");
        rowInline1.add(inlineKeyboardButton1);
        rowInline1.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("Date");
        inlineKeyboardButton3.setCallbackData("DATE");
        rowInline2.add(inlineKeyboardButton3);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
