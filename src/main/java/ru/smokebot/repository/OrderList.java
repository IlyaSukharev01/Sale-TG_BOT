package ru.smokebot.repository;

import ru.smokebot.dto.Package;

import java.util.ArrayList;

public class OrderList
{
    private final ArrayList<Package> orders;
    public OrderList()
    {
        orders = new ArrayList<>();
    }

    public void add(Package order)
    {
        orders.add(order);
    }

    public Package isOrderAlreadyAtList(Long tgId)
    {
        boolean state = orders.stream().anyMatch(it-> it.getTgId().equals(tgId));
        if (state)  return orders.stream().filter(it-> it.getTgId().equals(tgId)).findFirst().get();
        else        return null;
    }
    public void remove(Package order)
    {
        orders.remove(order);
    }

    public String getAllAtString()
    {
        StringBuffer buffer = new StringBuffer();
        for (Package order : orders)
            buffer.append(order.toString()).append("\n");
        return buffer.toString();
    }
    public ArrayList<Package> getAllAtList()
    {
        return orders;
    }

    public Package getOrder(int index)
    {
        if (orders.isEmpty())   return null;
        else                    return orders.get(index);
    }
}

