package ru.smokebot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smokebot.dto.tables.OrderBase;

@Repository
public interface OrderBaseRepository extends JpaRepository<OrderBase, Long> {
}
