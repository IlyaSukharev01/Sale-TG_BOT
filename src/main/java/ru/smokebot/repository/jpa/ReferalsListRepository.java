package ru.smokebot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smokebot.dto.tables.ReferalsList;

import java.util.List;

@Repository
public interface ReferalsListRepository extends JpaRepository<ReferalsList, Long> {

    List<ReferalsList> findByOrderByMainIdAsc();
}
