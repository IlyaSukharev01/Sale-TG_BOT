package ru.smokebot.repository;

import java.util.HashMap;

public class DiscountChoice
{
    private final HashMap<Long, Boolean> map;

    public DiscountChoice(){map = new HashMap<>();}

    public boolean isAtMap(Long tgId)
    {
        return map.keySet().stream().anyMatch(it-> it.equals(tgId));
    }

    public void add(Long tgId, Boolean flag)
    {
        map.put(tgId, flag);
    }

    public boolean getValue(Long tgId)
    {
        if (isAtMap(tgId))  return map.get(tgId);
        else                return false;
    }

    public void clearDiscountMap()
    {
        map.clear();
    }
    public int getSize()
    {
        return map.size();
    }
}
