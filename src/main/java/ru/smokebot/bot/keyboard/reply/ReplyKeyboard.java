package ru.smokebot.bot.keyboard.reply;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static ru.smokebot.bot.keyboard.reply.ReplyKeyboardStatuses.*;

public class ReplyKeyboard
{
    /**
     *
     * @return
     * Функция предназначена для того, чтобы манипулировать
     * разным reply-клавиатурами из функций: "getMainReplyKeyboard" и
     * "getOrderReplyKeyboard" в зависимости от статуса из класса
     * "KeyboardStatuses"
     * Возвращает объект класса "ReplyKeyboardMarkup"
     */
    public ReplyKeyboardMarkup changeReplyKeyboard(ReplyKeyboardStatuses status)
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        if      (status.equals(MAIN))        replyKeyboardMarkup.setKeyboard(getMainReplyKeyboard());
        else if (status.equals(ORDER))       replyKeyboardMarkup.setKeyboard(getOrderReplyKeyboard());
        else if (status.equals(OFF))         return null;

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }

    /**
     *
     * @return
     * Функция, предназначенная для создания главной
     * reply-клавиатуры.
     * Возвращает список строк, которые являются частями
     * этой клавиатуры.
     */
    private List<KeyboardRow> getMainReplyKeyboard()
    {
        List<KeyboardRow> rowsList = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        secondRow.add("Профиль");
        secondRow.add("Поддержка");
        firstRow.add("Оформить заказ");

        rowsList.add(firstRow);
        rowsList.add(secondRow);
        return rowsList;
    }

    /**
     *
     * @return
     * Функция, предназначенная для создания второстепенной
     * reply-клавиатуры (для обработки заказов).
     * Возвращает список строк, которые являются частями
     * этой клавиатуры.
     */
    private List<KeyboardRow> getOrderReplyKeyboard()
    {
        List<KeyboardRow> rowsList = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        firstRow.add("Прайс");
        firstRow.add("Заказать");
        secondRow.add("Выйти в меню");

        rowsList.add(firstRow);
        rowsList.add(secondRow);
        return rowsList;
    }

}
