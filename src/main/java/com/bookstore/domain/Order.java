package com.bookstore.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * Created by User on 19.12.2017.
 */
@Entity
@Table(name = "orders")//rename the table for the "Order" class since "order" is the reserved word for jpa
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Date date;
    private Date shippingDate;
    private String shippingMethod;
    private String status;
    private BigDecimal total;

//    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL )
//    private List<CartItem> cartItemList;
//
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Shipping shipping;

//    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private Billing billing;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //    public List<CartItem> getCartItemList() {
//        return cartItemList;
//    }
//
//    public void setCartItemList(List<CartItem> cartItemList) {
//        this.cartItemList = cartItemList;
//    }

//    public Billing getBilling() {
//        return billing;
//    }
//
//    public void setBilling(Billing billing) {
//        this.billing = billing;
//    }

    //see Bidirectional @OneToOne
    // http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-one
//    public void addBilling(Billing billing) {
//        billing.setOrder( this );
//        this.billing = billing;
//    }
//
//    public void removeBilling() {
//        if ( billing != null ) {
//            billing.setOrder( null );
//            this.billing = null;
//        }
//    }
//
    public void addShipping(Shipping shipping) {
        shipping.setOrder( this );
        this.shipping = shipping;
    }

    public void removeShipping() {
        if ( shipping != null ) {
            shipping.setOrder( null );
            this.shipping = null;
        }
    }

    public void addPayment(Payment payment) {
        payment.setOrder( this );
        this.payment = payment;
    }

    public void removePayment() {
        if ( payment != null ) {
            payment.setOrder( null );
            this.payment = null;
        }
    }
}
