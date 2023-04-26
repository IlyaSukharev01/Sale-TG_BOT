package ru.smokebot.dto.tables;

import javax.persistence.*;


@Entity(name = "order_base")
public class OrderBase
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "not_id_generator"
    )
    @Column(
            name = "note_id",
            unique = true,
            nullable = false
    )
    private Long noteId;

    @Column(
            name = "telegram_id",
            nullable = false
    )
    private Long telegramId;
    @Column(
            name = "date_and_time",
            nullable = false
    )
    private String dateAndTime;
    @Column(
            name = "basket",
            nullable = false
    )
    private String basket;
    @Column(
            name = "price",
            nullable = false
    )
    private Long price;

    @Column(
            name = "status"
    )
    private String status;

    public OrderBase (Long telegramId, String dateAndTime, String bakset, Long price )
    {
        this.telegramId = telegramId;
        this.dateAndTime = dateAndTime;
        this.basket = bakset;
        this.price = price;
    }
    public OrderBase(){}

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
