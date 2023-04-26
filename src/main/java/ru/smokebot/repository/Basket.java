package ru.smokebot.repository;

import ru.smokebot.dto.tables.SaleProducts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Basket
{
    private final HashMap<Long, ArrayList<SaleProducts>> map;

    public Basket()
    {
        map = new HashMap<>();
    }

    private boolean isAtMap(Long id)
    {
        return map.keySet().stream().anyMatch(it-> it.equals(id));
    }

    public void clearBasket(Long id)
    {
        if (!isAtMap(id)) map.put(id, new ArrayList<>());
        map.put(id, new ArrayList<>());
    }

    public boolean isEmpty(Long id)
    {
        if (!isAtMap(id)) map.put(id, new ArrayList<>());

        return map.get(id).size() == 0;
    }

    public void add(Long id, SaleProducts item)
    {
        if (!isAtMap(id)) map.put(id, new ArrayList<>());

        ArrayList<SaleProducts> list = map.get(id);
        if (!list.contains(item))
            list.add(item);
        map.put(id, list);
    }

    public void remove(Long id, SaleProducts item)
    {
        if (!isAtMap(id)) map.put(id, new ArrayList<>());

        ArrayList<SaleProducts> list = map.get(id);
        for (SaleProducts prod : list)
            if (
                    prod.getName().equals(item.getName()) &&
                    prod.getTaste().equals(item.getTaste()) &&
                    prod.getCountSmokes().equals(item.getCountSmokes())
            )
            {
                list.remove(prod);
                break;
            }
        map.put(id, list);
    }

    public ArrayList<SaleProducts> getAllProducts(Long id)
    {
        if (map.keySet().stream().anyMatch(it-> it.equals(id))) return map.get(id);
        return null;
    }


    public String toString(Long id)
    {
        if (!isAtMap(id))
        {
            map.put(id, new ArrayList<>());
            return  null;
        }

        StringBuffer buffer = new StringBuffer();
        for (SaleProducts prod : map.get(id))
            buffer.append(prod.getName())
                    .append(", ")
                    .append(prod.getTaste())
                    .append(", ")
                    .append(prod.getCountSmokes())
                    .append("| Цена: ")
                    .append(prod.getPrice())
                    .append("\n");

        return buffer.toString();
    }

    public Long getCommonPrice(Long tgId)
    {
        ArrayList<SaleProducts> products = getAllProducts(tgId);
        Long amount = 0L;
        for (SaleProducts prod : products)
            amount += prod.getPrice();
        return amount;
    }

    public void clearAllBasket()
    {
        map.clear();
    }

    public ArrayList<ArrayList<SaleProducts>> getNoEmptyBasket()
    {
        ArrayList<ArrayList<SaleProducts>> basket = new ArrayList<>();
        for (Map.Entry<Long, ArrayList<SaleProducts>> entry : map.entrySet())
        {
            if (!entry.getValue().isEmpty())
                basket.add(entry.getValue());
        }
        return basket;
    }
}
