package ru.smokebot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smokebot.dto.tables.MainBase;

@Repository
public interface MainBaseRepository extends JpaRepository<MainBase, Long> { }
