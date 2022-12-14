package choi.jpa.domain;

import choi.jpa.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    /** 생성 메서드 Start */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }
    /** 생성 메서드 End */

    /** 비즈니스 로직 Start */
    public void cancel() {
        getItem().addStock(count);
    }
    /** 비즈니스 로직 End */

    /** 조회 로직 Start */
    // 전체 주문 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
    /** 조회 로직 End */

}
