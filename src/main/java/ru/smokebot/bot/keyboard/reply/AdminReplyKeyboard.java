package ru.smokebot.bot.keyboard.reply;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.smokebot.dto.tables.Admins;

import java.util.ArrayList;
import java.util.List;

public class AdminReplyKeyboard {

    public ReplyKeyboardMarkup setKeyboard(Admins admin) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row;

        if (admin.getCanTransactOrders().equals("true"))
        {
            row = new KeyboardRow();
            row.add("transactOrders");
            list.add(row);
        }
        if (admin.getCanSeeOrders().equals("true")) {
            row = new KeyboardRow();
            row.add("orders");
            list.add(row);
        }
        if (admin.getCanCheckProductsRemains().equals("true")) {
            row = new KeyboardRow();
            row.add("remains");
            list.add(row);
        }
        if (admin.getCanAllMessage().equals("true"))
        {
            row = new KeyboardRow();
            row.add("messageAll");
            list.add(row);
        }
        if (admin.getCanChangeReferal().equals("true"))
        {
            row = new KeyboardRow();
            row.add("changeReferal");
            list.add(row);
        }
        if (admin.getCanChangeReferalPercent().equals("true"))
        {
            row = new KeyboardRow();
            row.add("changeRefPercent");
            list.add(row);
        }
        if (admin.getCanAddAdmin().equals("true"))
        {
            row = new KeyboardRow();
            row.add("addAdmin");
            list.add(row);
        }
        if (admin.getCanAddToSaleProducts().equals("true"))
        {
            row = new KeyboardRow();
            row.add("addProducts");
            list.add(row);
        }
        if (admin.getCanClearBasket().equals("true"))
        {
            row = new KeyboardRow();
            row.add("clearBasket");
            list.add(row);
        }
        if (admin.getCanClearDiscountChoice().equals("true"))
        {
            row = new KeyboardRow();
            row.add("clearChoice");
            list.add(row);
        }
        if (admin.getCanReupdateBase().equals("true"))
        {
            row = new KeyboardRow();
            row.add("updateBase");
            list.add(row);
        }
        row = new KeyboardRow();
        row.add("exit");
        list.add(row);
        markup.setKeyboard(list);
        return markup;
    }
}
