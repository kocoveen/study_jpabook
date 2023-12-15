package hello.practice.domain.item;

import hello.practice.domain.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;       //이름
    private int price;         //가격
    private int stockQuantity; //재고수량

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
