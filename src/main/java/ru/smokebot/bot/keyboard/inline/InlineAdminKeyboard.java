package ru.smokebot.bot.keyboard.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.smokebot.dto.Package;

import java.util.ArrayList;
import java.util.List;

import static ru.smokebot.bot.keyboard.inline.DataType.*;

public class InlineAdminKeyboard
{

    /**
     *
     * @param index
     * Параметр - целочисленное число - номер заказа из списка заказов
     * @return
     * Возвращает inline - клавиатуру, включающую
     * в себя кнопку "вернуться назад" из просмотра
     * дополнительных параметров пользовательских заказов.
     */
    public InlineKeyboardMarkup getBackMarkup(int index)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton>  buttonsList = new ArrayList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton();

        btn.setText("Вернуться назад");
        btn.setCallbackData(ADMIN_ORDER + ":" + index);

        buttonsList.add(btn);

        buttons.add(buttonsList);
        markup.setKeyboard(buttons);
        return markup;
    }

    /**
     *
     * @param ind
     * Параметр - целочисленное число - номер заказа из списка заказов
     * @return
     * Возвращает inline - клавиатуру, которая включает
     * набор кнопок, обладающих расширенным функционалом
     * для управления пользователскими заказами.
     */
    public InlineKeyboardMarkup getAdvanced(int ind)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton>  buttonsList = new ArrayList<>();
        List<InlineKeyboardButton>  buttonList2 = new ArrayList<>();
        List<InlineKeyboardButton>  buttonList3 = new ArrayList<>();

        InlineKeyboardButton change = new InlineKeyboardButton();
        InlineKeyboardButton register = new InlineKeyboardButton();
        InlineKeyboardButton cancel = new InlineKeyboardButton();
        InlineKeyboardButton back = new InlineKeyboardButton();

        change.setText("Дополнительно");
        change.setCallbackData(ADMIN_MORE + ":" + ind);

        register.setText("Подтвердить заказ");
        register.setCallbackData(ADMIN_ACCESS_ORDER + ":" + ind);

        cancel.setText("Отменить");
        cancel.setCallbackData(ADMIN_CANCEL_ORDER + ":" + ind);

        back.setText("Назад");
        back.setCallbackData(ADMIN_CANCEL_OPERATION.name());


        buttonsList.add(change);
        buttonList2.add(register);
        buttonList2.add(cancel);
        buttonList3.add(back);

        buttons.add(buttonsList);
        buttons.add(buttonList2);
        buttons.add(buttonList3);
        markup.setKeyboard(buttons);
        return markup;
    }


    /**
     *
     * @param orders
     * Параметр - список заказов
     * @return
     * Функция возвращает inline - клавиатуру для дальнейшего
     * размещения в кнопках названий заказов.
     */
    public InlineKeyboardMarkup getOrders(ArrayList<Package> orders)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int startIndex = 0;
        for (Package order : orders)
        {
            List<InlineKeyboardButton>  buttonsList = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(startIndex + 1 + ". " + order.toString());
            btn.setCallbackData(ADMIN_ORDER + ":" + startIndex++);
            buttonsList.add(btn);
            buttons.add(buttonsList);
        }
        markup.setKeyboard(buttons);
        return markup;
    }

}
