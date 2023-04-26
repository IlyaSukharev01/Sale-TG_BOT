package ru.smokebot.bot.keyboard.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.smokebot.model.ShopModel;
import ru.smokebot.dto.tables.SaleProducts;

import java.util.ArrayList;
import java.util.List;

import static ru.smokebot.bot.keyboard.inline.DataType.*;
import static ru.smokebot.bot.keyboard.inline.DataType.NAME;

public class InlineKeyboard
{


    /**
     *
     * @param shopModel
     * Параметр - объект класса shopModel
     * @param status
     * Параметр - объект класса DataType
     * @param name
     * Параметр - строка (наименование товара)
     * @param taste
     * Параметр - строка (наименование вкуса)
     * @return
     * Возвращает inline клавиатуры по указанию
     * пользователя.
     */
    public InlineKeyboardMarkup changeInlineKeyboard(ShopModel shopModel, DataType status, String name, String taste, String countSmokes)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        if      (status.equals(NAME))               markup.setKeyboard(getButtonNames(shopModel));
        else if (status.equals(TASTE))              markup.setKeyboard(getButtonTaste(shopModel, name));
        else if (status.equals(SMOKES))             markup.setKeyboard(getButtonSmokes(shopModel, name, taste));
        else if (status.equals(DISCOUNT))           markup.setKeyboard(getDiscountPanelButtons(name, taste, countSmokes));
        else if (status.equals(ADVANCED))           markup.setKeyboard(getAdvancedTypes());
        return markup;
    }


    /**
     *
     * @param products
     * Параметр - объкт класса SaleProducts (продукт)
     * @return
     * Возвращает клавиатуру со всеми пользовательскими
     * товарами, которые лежат в корзине. (для удаления)
     */
    public InlineKeyboardMarkup getDestroingMarkup(ArrayList<SaleProducts> products)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (SaleProducts prod : products)
        {
            List<InlineKeyboardButton> buttonsList = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();

            btn.setText(prod.toString());
            btn.setCallbackData(DESTROING + ":" + prod.getName() + ":" + prod.getTaste() + ":" + prod.getCountSmokes());

            buttonsList.add(btn);
            buttons.add(buttonsList);
        }
        markup.setKeyboard(buttons);
        return markup;
    }

    public List<List<InlineKeyboardButton>> getDiscountPanelButtons(String name, String taste, String countSmokes)
    {
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();

        InlineKeyboardButton trueBtn = new InlineKeyboardButton();
        InlineKeyboardButton falseBtn = new InlineKeyboardButton();

        trueBtn.setText("Да");
        trueBtn.setCallbackData(DISCOUNT + ":" + name + ":" + taste + ":" + countSmokes + ":t");
        falseBtn.setText("Нет");
        falseBtn.setCallbackData(DISCOUNT + ":" + name + ":" + taste + ":" + countSmokes + ":f");

        buttonList.add(trueBtn);
        buttonList.add(falseBtn);
        list.add(buttonList);
        return list;
    }


    /**
     *
     * @return
     * Возвращает список кнопок,
     * которые внедряют дополнительный функционал
     * в работу с товарами. (удалить, добавить еще ...)
     */
    public List<List<InlineKeyboardButton>> getAdvancedTypes()
    {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton>  buttonList = new ArrayList<>();
        List<InlineKeyboardButton>  buttonList2 = new ArrayList<>();
        List<InlineKeyboardButton>  buttonList3 = new ArrayList<>();

        InlineKeyboardButton enter = new InlineKeyboardButton();
        InlineKeyboardButton add = new InlineKeyboardButton();
        InlineKeyboardButton remove = new InlineKeyboardButton();
        InlineKeyboardButton exit = new InlineKeyboardButton();

        enter.setText("Подтвердить");
        enter.setCallbackData("ENTER");

        add.setText("Добавить ещё");
        add.setCallbackData("ADD");

        remove.setText("Удалить");
        remove.setCallbackData("DESTROY");

        exit.setText("Главное меню");
        exit.setCallbackData("EXIT");

        buttonList.add(enter);
        buttonList2.add(add);
        buttonList2.add(remove);
        buttonList3.add(exit);

        buttons.add(buttonList);
        buttons.add(buttonList2);
        buttons.add(buttonList3);

        return buttons;
    }

    /**
     *
     * @param taste
     * Параметр - переменная лог. типа (наличие вкуса)
     * @param smokes
     * Параметр - переменная лог. типа (наличие затяжек)
     * @param productName
     * Параметр - выбранное покупателем название товара (когда делает заказ)
     * @return
     * Функция предназначена для того, чтобы возвращать кнопку назад в меню покупок.
     */
    private List<InlineKeyboardButton> getBackButton(boolean taste, boolean smokes, String productName)
    {
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText("◁  Назад");
        if      (taste)     btn.setCallbackData(BACK_NAME.name());
        else if (smokes)    btn.setCallbackData(BACK_TASTE + ":" + productName);
        buttonList.add(btn);
        return buttonList;
    }

    /**
     *
     * @param productName
     * Параметр - наименование товара
     * @return
     * Возвращает список кнопок( вкусов),
     * которые принадлежат параметру(товару) из
     * базы saleproducts для INLINE-KEYBOARD;
     */
    public List<List<InlineKeyboardButton>> getButtonTaste(ShopModel shopModel, String productName)
    {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String taste : shopModel.getProductTastes(productName))
        {
            List<InlineKeyboardButton>  buttonList = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();

            btn.setText(taste);
            btn.setCallbackData(TASTE + ":" + productName + ":" + taste);

            buttonList.add(btn);
            buttons.add(buttonList);
        }
        buttons.add(getBackButton(true, false, null));
        return buttons;
    }

    /**
     *
     * @param productName
     * Параметр - название товара
     * @return
     * Возвращает список кнопок (затяжек),принадлежащих
     * параметру ( товару )из базы saleproducts для
     * клавиатуры INLINE-KEYBOARD
     */
    public List<List<InlineKeyboardButton>> getButtonSmokes(ShopModel shopModel, String productName, String taste)
    {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Long smokes : shopModel.getProductSmokes(productName, taste))
        {
            List<InlineKeyboardButton> buttonsList = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();

            btn.setText(String.valueOf(smokes));
            btn.setCallbackData(SMOKES + ":" + productName + ":" + taste + ":" + smokes);

            buttonsList.add(btn);
            buttons.add(buttonsList);
        }
        buttons.add(getBackButton(false, true, productName));
        return buttons;
    }


    /**
     *
     * @return
     * Возвращает список кнопок (названий товаров)
     * из базы saleproducts для клавиатуры INLINE-KEYBOARD
     *
     */
    public List<List<InlineKeyboardButton>> getButtonNames(ShopModel shopModel)
    {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String name : shopModel.getProductsNames())
        {
            List<InlineKeyboardButton> buttonsList = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();

            btn.setText(name);
            btn.setCallbackData(NAME + ":" + name);

            buttonsList.add(btn);
            buttons.add(buttonsList);
        }
        return buttons;
    }
}
