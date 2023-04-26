package ru.smokebot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smokebot.dto.tables.OrderBase;
import ru.smokebot.repository.jpa.OrderBaseRepository;

@Service
public class OrderModel
{
    @Autowired private OrderBaseRepository orderBaseRepository;

    public void add(OrderBase obj)
    {
        orderBaseRepository.save(obj);
    }
}
