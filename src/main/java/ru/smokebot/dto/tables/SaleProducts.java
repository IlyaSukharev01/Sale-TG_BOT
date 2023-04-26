package ru.smokebot.dto.tables;


import javax.persistence.*;

@Entity(name = "saleproducts")
public class SaleProducts
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "note_id_prod_gen"
    )
    @Column(
            name = "note_id",
            unique = true,
            nullable = false
    )
    private Long noteId;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;
    @Column(
            name = "taste",
            nullable = false
    )
    private String taste;
    @Column(
            name = "count_smokes",
            nullable = false
    )
    private Long countSmokes;
    @Column(
            name = "common_count",
            nullable = false
    )
    private Long commonCount;
    @Column(
            name = "price",
            nullable = false
    )
    private Long price;

    public SaleProducts(String name, String taste, Long countSmokes, Long commonCount, Long price)
    {
        this.name = name;
        this.taste = taste;
        this.countSmokes = countSmokes;
        this.price = price;
        this.commonCount = commonCount;
    }

    public SaleProducts(){}

    public String toString()
    {
        return String.format("%s, вкус: %s, %d затяжек, цена: %d",
                name, taste, countSmokes, price);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountSmokes() {
        return countSmokes;
    }

    public void setCountSmokes(Long countSmokes) {
        this.countSmokes = countSmokes;
    }

    public Long getCommonCount() {
        return commonCount;
    }

    public void setCommonCount(Long commonCount) {
        this.commonCount = commonCount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }
}
