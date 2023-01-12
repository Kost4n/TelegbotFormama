package formama.telegbot.Bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
import java.util.Random;



@Component
public class Bot extends TelegramLongPollingBot {

    private Message requestMessage = new Message();
    private SendMessage response = new SendMessage();

    private Random random = new Random();

//    @Value("${telegram.name}")
    @Value("${bot.name}")
    private String botName;

//    @Value("${telegram.token}")
    @Value("${bot.token}")
    private String botToken;

//    public Bot(TelegramBotsApi telegramBotsApi,
//               @Value("${telegram.name}") String botUsername,
//               @Value("${telegram.token}") String botToken) throws TelegramApiException {
//        this.botUsername = botUsername;
//        this.botToken = botToken;
//        telegramBotsApi.registerBot(this);
//    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("любовь"));
        keyboardFirstRow.add(new KeyboardButton("сочувствие"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("одобрение"));
        keyboardSecondRow.add(new KeyboardButton("похвала"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);


    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        requestMessage = update.getMessage();
        response.setChatId(requestMessage.getChatId().toString());
        setButtons(response);

        if (update.hasMessage() && requestMessage.hasText())
            //log.info("Working onUpdateREceived, text: " + requestMessage.getText());

        if (requestMessage.getText().equals("/start")) {
            try {
                defaultMsg(response, "Этот бот создан для того, чтобы поздравить тебя с днём рождения!!!\n" +
                        "Он будет отвечать на твои сообщения когда тебе надо.\n" +
                        "А именно:\n" +
                        "- любовь" + " - сообщения с признанием любви\n" +
                        "- сочувствие - если тебе надо сочувствие\n" +
                        "- одобрение - если тебе нужно будет одобрение\n" +
                        "- похвала - если нужна похвала");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        switch (requestMessage.getText()) {
            case "любовь":
                sendLove(response);
                break;
            case "сочувствие":
                compassion(response);
                break;
            case "одобрение":
                approval(response);
                break;
            case "похвала":
                praise(response);
                break;

        }
    }

    private void praise(SendMessage response) throws TelegramApiException {
        String[] text = {"Умничка", "Большая умничка",
                "Замечательно", "Молодец"};
        defaultMsg(this.response, text[random.nextInt(text.length)]);
    }

    private void approval(SendMessage response) throws TelegramApiException {
        String[] text = {"да", "всё именно так",
                "делай так, как считаешь нужным", "в любом случае, я тебя люблю"};
        defaultMsg(this.response, text[random.nextInt(text.length)]);
    }

    private void compassion(SendMessage response) throws TelegramApiException {
        String[] text = {"ну не переживай", "всё будет хорошо",
                "кто грустит - тот трансвестит, ладно шучу", "ты со всем справишься"};

        defaultMsg(this.response, text[random.nextInt(text.length)]);
    }

    private void sendLove(SendMessage response) throws TelegramApiException {
        String[] text = {"люблю тебя <3", "ты у меня самая лучшая <3", "зе бест мами)", "ты как одуванчик <3", };
        defaultMsg(this.response, text[random.nextInt(text.length)]);
    }

    private void defaultMsg(SendMessage response, String msg) throws TelegramApiException {
        response.setText(msg);
        execute(response);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
