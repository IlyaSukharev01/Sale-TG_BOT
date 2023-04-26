package ru.smokebot.dto;


import ru.smokebot.dto.tables.SaleProducts;

import java.util.ArrayList;

import static ru.smokebot.bot.ConfigureBot.getZonedTime;

public class Package
{
    private final ArrayList<SaleProducts> userBasket;
    private final String userName;
    private final Long tgId;
    private final Long commonDiscountPrice;
    private final Long discountSize;
    private final String time;

    public Package(ArrayList<SaleProducts> userBasket, Long tgId, String userName, Long commonDiscountPrice, Long discountSize)
    {
        this.userBasket = userBasket;
        this.tgId = tgId;
        this.userName = userName;
        this.commonDiscountPrice = commonDiscountPrice;
        this.discountSize = discountSize;
        this.time = getZonedTime(true, true);
    }

    public String getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public String toString()
    {
        return String.format
        (
                "User: %s, Время заказа: %s",
                userName,
                time
        );
    }

    public Long getTgId() {
        return tgId;
    }

    public ArrayList<SaleProducts> getUserBasket() {
        return userBasket;
    }

    public String formatPrint()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 1;
        for (SaleProducts prod : getUserBasket())
            buffer.append(counter++).append(". ").append(prod.toString()).append("\n");
        return buffer.toString();
    }

    public Long getCommonDiscountPrice() {
        return commonDiscountPrice;
    }

    public Long getDiscountSize() {
        return discountSize;
    }
}
