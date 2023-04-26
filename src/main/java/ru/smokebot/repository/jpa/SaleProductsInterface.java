package ru.smokebot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smokebot.dto.tables.SaleProducts;

import java.util.List;

@Repository
public interface SaleProductsInterface extends JpaRepository<SaleProducts, Long>
{
    List<SaleProducts> findByOrderByCommonCountDesc();
}
