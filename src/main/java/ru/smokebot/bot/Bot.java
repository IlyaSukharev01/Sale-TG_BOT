package ru.smokebot.bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.smokebot.bot.keyboard.inline.InlineAdminKeyboard;
import ru.smokebot.bot.keyboard.reply.AdminReplyKeyboard;
import ru.smokebot.dto.tables.Admins;
import ru.smokebot.dto.Package;
import ru.smokebot.dto.tables.OrderBase;
import ru.smokebot.model.AdminModel;
import ru.smokebot.model.OrderModel;
import ru.smokebot.repository.Basket;
import ru.smokebot.bot.keyboard.inline.InlineKeyboard;
import ru.smokebot.bot.keyboard.inline.InlineKeyboardStatuses;
import ru.smokebot.bot.keyboard.reply.ReplyKeyboard;
import ru.smokebot.bot.keyboard.reply.ReplyKeyboardStatuses;
import ru.smokebot.dto.tables.SaleProducts;
import ru.smokebot.model.BaseModel;
import ru.smokebot.model.ShopModel;
import ru.smokebot.repository.DiscountChoice;
import ru.smokebot.repository.OrderList;

import java.util.ArrayList;

import static ru.smokebot.bot.ConfigureBot.*;
import static ru.smokebot.bot.keyboard.inline.DataType.*;
import static ru.smokebot.bot.keyboard.inline.InlineKeyboardStatuses.ACTIVE;
import static ru.smokebot.bot.keyboard.inline.InlineKeyboardStatuses.NONE;
import static ru.smokebot.bot.keyboard.reply.ReplyKeyboardStatuses.*;


@Service
public class Bot extends TelegramLongPollingBot
{
    @Autowired private BaseModel model;
    @Autowired private ShopModel shopModel;
    @Autowired private AdminModel adminModel;
    @Autowired private OrderModel orderModel;
    private final Basket basket = new Basket();
    private final DiscountChoice choices = new DiscountChoice();
    private final OrderList orders = new OrderList();

    private final static InlineKeyboard inKeyboard = new InlineKeyboard();
    private final static ReplyKeyboard repKeyboard = new ReplyKeyboard();
    private final static AdminReplyKeyboard repAdmKeyboard = new AdminReplyKeyboard();
    private final static InlineAdminKeyboard inAdmKeyboard = new InlineAdminKeyboard();

    public void onUpdateReceived(Update update)
    {
        ReplyKeyboardMarkup replyKeyboardMarkup;
        ReplyKeyboardMarkup adminKeyboardMarkup = null;

        if (update.hasMessage() && update.getMessage().getText() != null)
        {
            String text;
            String messageFromUser = update.getMessage().getText();
            if (!basket.isEmpty(update.getMessage().getChatId()))
            {
                Package pack = new Package(basket.getAllProducts(update.getMessage().getChatId()), null, null, null, null);
                shopModel.changeProductsCount(pack, true);
                basket.clearBasket(update.getMessage().getChatId());
            }
            ReplyKeyboardStatuses replyKeyboardStatus = MAIN;
            InlineKeyboardStatuses inlineKeyboardStatus = NONE;
            InlineKeyboardStatuses inAdminKeyboardStatus = NONE;
            InlineKeyboardMarkup inlineMarkup = null;

            if (messageFromUser.equals("/start"))
            {
                model.addToMainBase(update.getMessage().getChatId());
                text = welcome;
            }
            else if (messageFromUser.contains("/start"))
            {
                model.registerNewRef(update.getMessage());
                text = welcome;
            }
            else if (messageFromUser.equals("/price") || messageFromUser.equals("Прайс"))
            {
                text = price + priceList;
                replyKeyboardStatus = ORDER;
            }
            else if (messageFromUser.equals("/reg") || messageFromUser.equals("Оформить заказ"))
            {
                text = registerVape;
                replyKeyboardStatus = ORDER;
            }
            else if (messageFromUser.equals("/profile") || messageFromUser.equals("Профиль"))
            {
                text = getInfoAboutProfile(update.getMessage());
            }
            else if (messageFromUser.equals("/help") ||  messageFromUser.equals("Поддержка"))
            {
                text = helpText;
            }
            else if (messageFromUser.equals("/adminpanel"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    text = adminPanel;
                    replyKeyboardStatus = OFF;
                    adminKeyboardMarkup = repAdmKeyboard.setKeyboard(admin);
                }
                else  text = warning;
            }
            else if (messageFromUser.equals("orders"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanSeeOrders().equals("true"))    text = noRights;
                    else
                    {
                        String ordersAll = orders.getAllAtString();
                        if (ordersAll.equals(""))   text = noOrders;
                        else                        text = showOrders + ordersAll;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("remains"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanCheckProductsRemains().equals("true")) text = noRights;
                    else
                    {
                        text = shopModel.getCommonRemains();
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.contains("messageAll>"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanAllMessage().equals("true")) text = noRights;
                    else
                    {
                        text = warning;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("transactOrders"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanTransactOrders().equals("true"))  text = noRights;
                    else
                    {
                        ArrayList<Package> ordersAll = orders.getAllAtList();
                        if (ordersAll.size() == 0) text = noOrders;
                        else
                        {
                            text = showOrders;
                            inAdminKeyboardStatus = ACTIVE;
                            inlineMarkup = inAdmKeyboard.getOrders(ordersAll);
                        }
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("clearBasket"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanClearBasket().equals("true"))  text = noRights;
                    else
                    {
                        ArrayList<ArrayList<SaleProducts>> products = basket.getNoEmptyBasket();
                        for (ArrayList<SaleProducts> prods : products)
                        {
                            Package pkg = new Package(prods, null, null, null, null);
                            shopModel.changeProductsCount(pkg, true);
                        }
                        basket.clearAllBasket();
                        text = basketClear;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("clearChoice"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanClearDiscountChoice().equals("true"))  text = noRights;
                    else
                    {
                        text = choiceDiscountClear;
                        choices.clearDiscountMap();
                    }
                }
                else text = warning;
            }
            else if (messageFromUser.equals("updateBase"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanReupdateBase().equals("true")) text = noRights;
                    else
                    {
                        shopModel.reUpdateTable();
                        text = reUpdateSaleProductsSuccess;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("changeReferal"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanChangeReferal().equals("true"))    text = noRights;
                    else
                    {
                        text = "Напиши в чат: " + "\nchangeReferal>id(кому):id(на кого)";
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.contains("changeReferal"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanChangeReferal().equals("true"))    text = noRights;
                    else
                    {
                        if (messageFromUser.contains(":") && messageFromUser.contains(">"))
                        {
                            String[] rows = messageFromUser.split(">")[1].split(":");
                            if (model.changeRef(Long.valueOf(rows[0]), Long.valueOf(rows[1])))      text = referalChangingSuccess;
                            else                                                                    text = referalChangingFailed;
                        }
                        else text = referalChangingFailed;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("changeRefPercent"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanChangeReferalPercent().equals("true")) text = noRights;
                    else
                    {
                        text = "Напиши в чат:changeRefPercent>id(кому):amount(сколько денег оставить)";
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.contains("changeRefPercent"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanChangeReferalPercent().equals("true")) text = noRights;
                    else
                    {
                        if (messageFromUser.contains(":") && messageFromUser.contains(">"))
                        {
                            String[] rows = messageFromUser.split(">")[1].split(":");
                            if (model.changePercent(Long.valueOf(rows[0]), Long.valueOf(rows[1]))) text = refPercentChangingSuccess;
                            else                                                                   text = refPercentChangingFailed;
                        }
                        else text = refPercentChangingFailed;

                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("addAdmin"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanAddAdmin().equals("true")) text = noRights;
                    else
                    {
                        text = "Напишите в чат:" +
                                "\n Айди пользователя" +
                                "\n1.писать всем" +
                                "\n2.смотреть заказы " +
                                "\n3.обрабатывать заказы" +
                                "\n4.смотреть остатки" +
                                "\n5.менять рефералов" +
                                "\n6.менять скидку рефералам" +
                                "\n7.добавлять продукты в базу" +
                                "\n8.чистить список выборов на использование скидок" +
                                "\nПример: " +
                                "addAdmin>1:true:true:true:true:true:true:true:true";
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.contains("addAdmin"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (!admin.getCanAddAdmin().equals("true")) text = noRights;
                    else
                    {
                        if (messageFromUser.contains(">") && messageFromUser.contains(":"))
                        {
                            String[] rows = messageFromUser.split(">")[1].split(":");
                            if (model.idIsExists(Long.valueOf(rows[0])))
                            {
                                Long tgId = model.findTgById(Long.valueOf(rows[0]));
                                Admins newAdmin = new Admins(
                                        tgId,
                                        rows[1],
                                        rows[2],
                                        rows[3],
                                        rows[4],
                                        "false",
                                        "false",
                                        rows[5],
                                        rows[6],
                                        "false",
                                        rows[7],
                                        rows[8]
                                );
                                adminModel.addAdmin(newAdmin);
                                text = adminWasAddedSuccessfully;
                            }
                            else text = adminWasntAdded;

                        }
                        else text = adminWasntAdded;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("addProducts"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (admin.getCanAddToSaleProducts().equals("false"))    text = noRights;
                    else
                    {
                        text = "Для добавление продукта в базу данных напишите в чат:\n" +
                                "addProduct>название:вкус:кол-во затяжек:общее кол-во:цена";
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.contains("addProducts"))
            {
                Admins admin = adminModel.getAdmin(update.getMessage().getChatId());
                if (admin != null)
                {
                    if (admin.getCanAddToSaleProducts().equals("false"))    text = noRights;
                    else
                    {
                        if (messageFromUser.contains(">") && messageFromUser.contains(":"))
                        {
                            String[] rows = messageFromUser.split(">")[1].split(":");
                            SaleProducts prod = new SaleProducts(
                                    rows[0], rows[1], Long.valueOf(rows[2]), Long.valueOf(rows[3]), Long.valueOf(rows[4])
                            );
                            shopModel.addProduct(prod);
                            text = productWasAddedSuccessfully;
                        }
                        else   text = productWasNotAdded;
                    }
                    replyKeyboardStatus = OFF;
                }
                else text = warning;
            }
            else if (messageFromUser.equals("exit"))
            {
                text = warning;
            }
            else if (messageFromUser.equals("Заказать"))
            {
                if (isTheAvailableTime())
                {
                    model.addToMainBase(update.getMessage().getChatId());
                    boolean isProfileAtReferals = model.isAtReferalBase(update.getMessage().getChatId());
                    if (!isProfileAtReferals) model.registerNewRef(update.getMessage().getChatId());
                    text = registerVapeName;
                    replyKeyboardStatus = ORDER;
                    inlineKeyboardStatus = ACTIVE;
                }
                else text = notWorkingNow;
            }
            else if (messageFromUser.equals("Выйти в меню"))
            {
                text = backToMenu;
            }
            else text = warning;

            replyKeyboardMarkup = repKeyboard.changeReplyKeyboard(replyKeyboardStatus);

            SendMessage sendMessage;

            if (replyKeyboardMarkup == null && adminKeyboardMarkup != null)
                sendMessage = getMessage(text, update.getMessage(), adminKeyboardMarkup);
            else
                sendMessage = getMessage(text, update.getMessage(), replyKeyboardMarkup);

            if (inlineKeyboardStatus.equals(ACTIVE))
            {
                inlineMarkup = inKeyboard.changeInlineKeyboard(shopModel, NAME, null, null, null);
                sendMessage.setReplyMarkup(inlineMarkup);
            }

            if (inAdminKeyboardStatus.equals(ACTIVE))
                sendMessage.setReplyMarkup(inlineMarkup);

            send(sendMessage, null);
        }
        else if (update.hasCallbackQuery())
        {
            String text;
            InlineKeyboardMarkup markup = null;
            Message message = update.getCallbackQuery().getMessage();
            SendMessage sendMessage = null;

            if (update.getCallbackQuery().getData().contains(":"))
            {
                String[] rows = update.getCallbackQuery().getData().split(":");

                switch (rows[0]) {
                    case "NAME":
                    case "BACK_TASTE":
                        if (isTheAvailableTime())
                        {
                            markup = inKeyboard.changeInlineKeyboard(shopModel, TASTE, rows[1], null, null);
                            text = registerVapeTaste;
                        }
                        else text = notWorkingNow;
                        break;
                    case "TASTE":
                        if (isTheAvailableTime())
                        {
                            markup = inKeyboard.changeInlineKeyboard(shopModel, SMOKES, rows[1], rows[2], null);
                            text = registerVapeSmokes;
                        }
                        else text = notWorkingNow;
                        break;
                    case "SMOKES":
                        if (isTheAvailableTime())
                        {
                            markup = inKeyboard.changeInlineKeyboard(shopModel, DISCOUNT, rows[1], rows[2], rows[3]);
                            text = registerDiscountPanel;
                        }
                        else text = notWorkingNow;
                        break;
                    case "DISCOUNT":
                        if (isTheAvailableTime())
                        {
                            SaleProducts prod = shopModel.getProduct(rows[1], rows[2], Long.valueOf(rows[3]));

                            choices.add(message.getChatId(), rows[4].equals("t"));
                            basket.add(message.getChatId(), prod);
                            shopModel.changeProductCount(prod, false);

                            markup = inKeyboard.changeInlineKeyboard(shopModel, ADVANCED, null, null, null);

                            Long commonAmount = basket.getCommonPrice(message.getChatId());
                            Long discAmount = model.getDiscountPrice(message.getChatId(), commonAmount, choices.getValue(message.getChatId()));

                            text = "Ваша корзина:\n"
                                    + basket.toString(message.getChatId())
                                    + "\n" + underline
                                    + "\n" + ConfigureBot.commonAmount + discAmount;
                        }
                        else text = notWorkingNow;
                        break;
                    case "DESTROING":
                        if (isTheAvailableTime())
                        {
                            SaleProducts product = shopModel.getProduct(rows[1], rows[2], Long.valueOf(rows[3]));
                            basket.remove(message.getChatId(), product);
                            shopModel.changeProductCount(product, true);

                            text = "Ваша корзина:\n" + basket.toString(message.getChatId());
                            markup = inKeyboard.changeInlineKeyboard(shopModel, ADVANCED, null, null, null);
                        }
                        else text = notWorkingNow;
                        break;
                    case "ADMIN_ORDER":
                        text = ordersChange;
                        markup = inAdmKeyboard.getAdvanced(Integer.parseInt(rows[1]));
                        break;
                    case "ADMIN_MORE":
                        Package orderInfo = orders.getOrder(Integer.parseInt(rows[1]));
                        if (orderInfo == null)
                        {
                            text = noOrders;
                            break;
                        }
                        text = "Айди пользователя: " +
                                tAccountLink + orderInfo.getUserName() +
                                "\nТовары: \n"
                                + orderInfo.formatPrint() +
                                "\n" + underline +
                                "\n" + ConfigureBot.commonAmount + orderInfo.getCommonDiscountPrice();
                        markup = inAdmKeyboard.getBackMarkup(Integer.parseInt(rows[1]));
                        break;
                    case "ADMIN_ACCESS_ORDER":
                        text = orderWasAccepted;
                        Package accessOrder = orders.getOrder(Integer.parseInt(rows[1]));
                        if (accessOrder == null)
                        {
                            text = noOrders;
                            break;
                        }

                        model.increaseDiscountAndCount(accessOrder.getTgId(), accessOrder.getCommonDiscountPrice());
                        model.changeDiscountSize(accessOrder.getTgId(), accessOrder.getDiscountSize());
                        OrderBase note = new OrderBase(
                                accessOrder.getTgId(),
                                accessOrder.getTime(),
                                accessOrder.formatPrint(),
                                accessOrder.getCommonDiscountPrice()
                        );
                        orderModel.add(note);
                        orders.remove(accessOrder);


                        sendMessage = new SendMessage();
                        sendMessage.setChatId(accessOrder.getTgId());
                        sendMessage.setText(orderTransactedSuccess);

                        markup = inAdmKeyboard.getOrders(orders.getAllAtList());
                        break;
                    case "ADMIN_CANCEL_ORDER":
                        text = orderWasCanceled;
                        Package cancelingOrder = orders.getOrder(Integer.parseInt(rows[1]));
                        if (cancelingOrder == null)
                        {
                            text = noOrders;
                            break;
                        }
                        orders.remove(cancelingOrder);
                        shopModel.changeProductsCount(cancelingOrder, true);

                        sendMessage = new SendMessage();
                        sendMessage.setChatId(cancelingOrder.getTgId());
                        sendMessage.setText(orderTransactedFailed);

                        markup = inAdmKeyboard.getOrders(orders.getAllAtList());
                        break;
                    default:
                        text = basketClear;
                        break;
                }
            }
            else
            {
                String data = update.getCallbackQuery().getData();
                switch (data) {
                    case "ADD":
                        if (isTheAvailableTime())
                        {
                            markup = inKeyboard.changeInlineKeyboard(shopModel, NAME, null, null, null);
                            text = registerVapeName;
                        }
                        else text = notWorkingNow;
                        break;
                    case "DESTROY":
                        if (isTheAvailableTime())
                        {
                            if (message.getText().equals(registerBasketAlreadyEmpty))   text = registerFailed;

                            else
                            {
                                if (basket.isEmpty(message.getChatId()))
                                {
                                    text = registerBasketAlreadyEmpty;
                                    markup = inKeyboard.changeInlineKeyboard(shopModel, ADVANCED, null, null, null);
                                }
                                else
                                {
                                    text = registerRemoveElement;
                                    markup = inKeyboard.getDestroingMarkup(basket.getAllProducts(message.getChatId()));
                                }
                            }
                        }
                        else text = notWorkingNow;

                        break;
                    case "ENTER":
                        if (isTheAvailableTime())
                        {
                            if (basket.isEmpty(message.getChatId()))    text = registerFailed;
                            else
                            {
                                text = registerSuccess;

                                Long discountPrice = model.getDiscountPrice(
                                        message.getChatId(),
                                        basket.getCommonPrice(message.getChatId()),
                                        choices.getValue(message.getChatId())
                                );

                                Package order = new Package(
                                        basket.getAllProducts(message.getChatId()),
                                        message.getChatId(),
                                        message.getChat().getUserName(),
                                        discountPrice,
                                        basket.getCommonPrice(message.getChatId()) - discountPrice
                                );
                                Package pkg = orders.isOrderAlreadyAtList(message.getChatId());
                                if (pkg != null)
                                {
                                    shopModel.changeProductsCount(pkg, true);
                                    orders.remove(pkg);
                                }
                                orders.add(order);
                                basket.clearBasket(message.getChatId());

                                sendMessage = new SendMessage();
                                sendMessage.setChatId(5534904995L);
                                sendMessage.setText("Новый заказ!");
                            }
                        }
                        else text = notWorkingNow;
                        break;
                    case "BACK_NAME":
                        text = registerVapeName;
                        markup = inKeyboard.changeInlineKeyboard(shopModel, NAME, null, null, null);
                        break;
                    case "EXIT":
                        text = backToMenu + "\n" + basketClear;
                        Package pack = new Package(basket.getAllProducts(message.getChatId()), null, null, null, null);
                        basket.clearBasket(message.getChatId());
                        shopModel.changeProductsCount(pack, true);

                        break;
                    case "ADMIN_CANCEL_OPERATION":
                        text = goBack;
                        markup = inAdmKeyboard.getOrders(orders.getAllAtList());
                        break;
                    default:
                        text = basketClear;
                        break;
                }
            }

            EditMessageText msg = new EditMessageText();

            msg.setMessageId(message.getMessageId());
            msg.setChatId(message.getChatId());
            msg.setReplyMarkup(markup);
            msg.setText(text);

            send(sendMessage, msg);

        }
        else
        {
            SendMessage msg = new SendMessage();
            msg.setChatId(update.getMessage().getChatId());
            msg.setText(receivedUnknownFiles + operatorLink);
            send(msg, null);
        }

    }


    /**
     *
     * @return
     * Функция возвращает true / false в зависимости
     * от того, работает ли сейчас магазин или нет
     * (Границы: Старт работы - 12часов Конец работы - 24часа)
     */
    private boolean isTheAvailableTime()
    {
        int time = Integer.parseInt(ConfigureBot.getZonedTime(false, true).split("-")[0]);
        return time >= 12 && time <= 23;
    }

    /**
     * @param message
     * Отправляемое сообщение пользователем класса "Message"
     * @return
     * Возвращает строку, которая в себе содержит
     * элементы профиля (имя профиля, размер скидки,
     * айди отца-рефера, реферальную ссылку для отпраки)
     *
     * Добавит пользователя в mainbase, если он там отсутствует!
     */
    private String getInfoAboutProfile(Message message)
    {
        model.addToMainBase(message.getChatId());
        boolean isProfileAtReferals = model.isAtReferalBase(message.getChatId());
        if (!isProfileAtReferals) model.registerNewRef(message.getChatId());

        String userNickname = message.getChat().getFirstName();
        Long discountSize = model.getDiscount(message.getChatId());
        Long fathersId = model.getFatherId(message.getChatId());
        Long userId = model.findIdByTg(message.getChatId());
        String refLink = model.getRefLink(message);

        return String.format
        (
                "Смотри, что я откопал о тебе, %s:." +
                "\nРазмер твоей текущей скидки: %d руб." +
                "\nАйди пригласившего реферала: %d." +
                "\nТвой айди: %d." +
                "\nРеферальная ссылка для отправки друзьям: \n%s",
                userNickname,
                discountSize,
                fathersId,
                userId,
                refLink
        );
    }

    /**
     *
     * @param text
     * Предполагаемый текст для вставки в объект класса "SendMessage"
     * @param msg
     * Предполагаемый объект класса Message
     * @param replyKeyboardMarkup
     * Предполагаемый объект класса ReplyKeyboardMarkup
     * @return
     * Функция создаёт объект класса SendMessage
     * и начиняет его клавиатурой  != null.
     * Также метод обрабатывает сообщение при старте бота, а именно
     * позволяет добавить никнейм пользователя, полученный из Message
     */
    private SendMessage getMessage(String text, Message msg, ReplyKeyboardMarkup replyKeyboardMarkup)
    {
        SendMessage message = new SendMessage();
        message.setChatId(msg.getChatId());

        if (text.equals(welcome))      message.setText(msg.getChat().getFirstName() + text);
        else                                        message.setText(text);

        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }

    /**
     *
     * @param msg1
     * Параметр - объект класса SendMessage(Отправляемое сообщение)
     * @param msg2
     * Параметр - объект класса EditMessageText (
     * Редактируемое сообщение)
     * Принимает в качестве аргумента отправляемое
     * или редактируемое сообщение и производит
     * его выполнениев реальном времени.
     *
     */
    private void send(SendMessage msg1, EditMessageText msg2)
    {
        try
        {
            if (msg1 != null)
                execute(msg1);
            if (msg2 != null)
                execute(msg2);

        }catch(TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return botName;
    }

    public String getBotToken()
    {
        return botToken;
    }
}
