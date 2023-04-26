package ru.smokebot.dto.tables;

import javax.persistence.*;

@Entity(name = "referalslist")
public class ReferalsList
{
    @Column(
            name = "main_id",
            nullable = false
    )
    private Long mainId;
    @Id
    @Column(
            name = "child_id",
            nullable = false,
            unique = true
    )
    private Long childId;

    @Column(
            name = "count_bought",
            nullable = false
    )
    private Long countBought;
    @Column(
            name = "discount_size",
            nullable = false
    )
    private Long discountSize;

    public ReferalsList(Long main_id, Long childId, Long countBought, Long discountSize)
    {
        this.childId = childId;
        this.mainId = main_id;
        this.countBought = countBought;
        this.discountSize = discountSize;
    }
    public ReferalsList(){}

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }


    public Long getCountBought() {
        return countBought;
    }

    public void setCountBought(Long countBought) {
        this.countBought = countBought;
    }

    public Long getDiscountSize() {
        return discountSize;
    }

    public void setDiscountSize(Long discountSize) {
        this.discountSize = discountSize;
    }
}
