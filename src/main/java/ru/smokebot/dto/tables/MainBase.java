package ru.smokebot.dto.tables;

import javax.persistence.*;

@Entity(name="mainbase")
public class MainBase
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "main_id_gen"
    )
    @Column(
            name = "main_id",
            unique = true,
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Long mainId;

    @Column(
            name = "telegram_id",
            unique = true,
            nullable = false
    )
    private Long telegramId;

    public MainBase(Long telegramId){this.telegramId = telegramId;}
    public MainBase(){}


    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
}
