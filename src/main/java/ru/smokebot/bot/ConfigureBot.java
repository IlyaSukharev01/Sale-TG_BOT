package ru.smokebot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ConfigureBot
{
    private final Bot bot;

    @Autowired
    public ConfigureBot(Bot bot) {
        this.bot = bot;
    }

    public final static String botName = "";
    public final static String botToken = "";
    public final static String refLink = "t.me/*?start=";
    public final static String operatorLink = "t.me/";
    public final static String tAccountLink = "t.me/";
    public final static String channelLink = "t.me/";
    public final static String priceList = "";
    public final static Long uniqueId = 3L;

    public final static String welcome =
            ", привет!\nЯ твой автоматизированный помощник.\nВыбери нужную команду в меню." +
            "\nИли напиши '/' (без кавычек).\n";
    public final static String price = "Лови актуальный прайс: \n";
    public final static String registerVape = "Предоставляю данные для регистрации покупки:\n";
    public final static String registerVapeName = "Выбери название дудки:\n";
    public final static String registerVapeSmokes = "Выбери количество затяжек:\n";
    public final static String registerDiscountPanel = "Использовать бонусы?";
    public final static String registerVapeTaste = "Выбери название вкуса:\n";
    public final static String registerRemoveElement = "Выбери элемент для удаления\n";
    public final static String registerBasketAlreadyEmpty = "Корзина и так пуста.";
    public final static String registerSuccess = "Твой заказ успешно сформирован.\nОжидай сообщения от оператора.\n";
    public final static String registerFailed= "Регистрация заказа прервана.\nКорзина пуста.\n";
    public final static String basketClear = "Корзина была очищена.\n";
    public final static String choiceDiscountClear = "Выборы использования скидок были очищены\n";
    public final static String backToMenu = "Выхожу в меню...\n";
    public final static String adminPanel = "Захожу в админ-панель.";
    public final static String helpText =
            "Привет ещё раз !\nДавай я немного расскажу о нас..." +
            "\n\n\nЭтот бот позволит тебе приобрести *." +
            "\nТвой заказ утвердит менеджер, предварительно связавшись с тобой." +
            "\nТы сможешь отслеживать свою скидку, если перейдёшь в" +
            " /profile." +"\nСкидку можно увеличить, если ты приведешь своих друзей,\n" +
            "Реферальную ссылку можно узнать в /profile." +
            "\nКанал с обновлениями: " + ConfigureBot.channelLink +
            "\nРаботаем ежедневно с 12:00 - 22:00." +
            "\n\n\nЕсли остались какие-то вопросы, то смело пиши!\n" +
            "\nОператор: " + operatorLink;
    public final static String warning = "Прости, но я не знаю, как отвечать на это.\nМожет, напишем оператору?\n" + operatorLink;
    public final static String receivedUnknownFiles = "Я не очень понимаю присылаемые тобой файлы, \nнапиши лучше ему, если что-то хочешь узнать: \n";
    public final static String noOrders = "Заказов нет.\n";
    public final static String showOrders = "Показываю список заказов:\n";
    public final static String noRights = "Отказано в доступе.\n";
    public final static String ordersChange = "Что хотите сделать с заказом?\n";
    public final static String orderTransactedSuccess = "Твой заказ был успешно обработан!\n";
    public final static String orderTransactedFailed = "Твой заказ не был обработан.\n";
    public final static String orderWasAccepted = "Заказ подтвержден!\n";
    public final static String goBack = "Возвращаюсь назад.\n";
    public final static String orderWasCanceled = "Заказ отменён.\n";
    public final static String reUpdateSaleProductsSuccess = "База данных saleproducts была успешно обновлена\n";
    public final static String commonAmount = "Стоимость с учётом скидки: \n";
    public final static String underline = "______________";
    public final static String referalChangingSuccess = "Рефер успешно изменён!\n";
    public final static String referalChangingFailed = "Не удалось изменить рефера!\n";
    public final static String refPercentChangingSuccess = "Сумма рефера изменена!\n";
    public final static String refPercentChangingFailed = "Сумма рефера не была изменена!\n";
    public final static String adminWasAddedSuccessfully = "Администратор был добавлен успешно.\n";
    public final static String adminWasntAdded = "Администратор не был добавлен.\n";
    public final static String productWasAddedSuccessfully = "Продукт был успешно добавлен.\n";
    public final static String productWasNotAdded = "Продукт не был добавлен.\n";
    public final static String notWorkingNow = "Прости, но мы сейчас не работаем\nРабочий график: с 12:00 и до 24:00";



    @EventListener({ContextRefreshedEvent.class})
    public void init()
    {
        try
        {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        }
        catch(TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public static String getZonedTime(boolean date, boolean time)
    {
        ZoneId place = ZoneId.of( "UTC+3");
        ZonedDateTime DateTime = ZonedDateTime.now( place );
        StringBuffer buff = new StringBuffer();
        if (date)   buff.append(DateTime.getDayOfMonth()).append(".").append(DateTime.getMonthValue()).append(",");
        if (time)   buff.append(DateTime.getHour()).append("-").append(DateTime.getMinute());
        return buff.toString();
    }
}
