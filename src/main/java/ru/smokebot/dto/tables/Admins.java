package ru.smokebot.dto.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "admin")
public class Admins
{
    @Id
    @Column(
            name = "telegram_id",
            unique = true
    )
    private Long tgId;

    @Column(
            name = "can_message_to_all",
            columnDefinition = "VARCHAR(5)"
    )
    private String canAllMessage;
    @Column(
            name = "can_see_orders",
            columnDefinition = "VARCHAR(5)"
    )
    private String canSeeOrders;
    @Column(
            name = "can_transact_orders",
            columnDefinition = "VARCHAR(5)"
    )
    private String canTransactOrders;
    @Column(
            name = "can_check_products_remains",
            columnDefinition = "VARCHAR(5)"
    )
    private String canCheckProductsRemains;

    @Column(
            name = "can_clear_basket",
            columnDefinition = "VARCHAR(5)"
    )
    private String canClearBasket;

    @Column(
            name = "can_reupdate_base",
            columnDefinition = "VARCHAR(5)"
    )
    private String canReupdateBase;

    @Column(
            name = "can_change_referal",
            columnDefinition = "VARCHAR(5)"
    )
    private String canChangeReferal;
    @Column(
            name = "can_change_referal_percent",
            columnDefinition = "VARCHAR(5)"
    )
    private String canChangeReferalPercent;

    @Column(
            name = "can_add_admin",
            columnDefinition = "VARCHAR(5)"
    )
    private String canAddAdmin;

    @Column(
            name = "can_add_to_sale_products",
            columnDefinition = "VARCHAR(5)"
    )
    private String canAddToSaleProducts;

    @Column(
            name = "can_clear_discount_choice",
            columnDefinition = "VARCHAR(5)"
    )
    private String canClearDiscountChoice;

    public Admins (Long tgId, String canAllMessage, String canSeeOrders, String canTransactOrders, String canCheckProductsRemains,
                   String canClearBasket, String canReupdateBase, String canChangeReferal, String canChangeReferalPercent, String canAddAdmin,
                   String canAddToSaleProducts, String canClearDiscountChoice)
    {
        this.tgId = tgId;
        this.canAllMessage = canAllMessage;
        this.canTransactOrders = canTransactOrders;
        this.canSeeOrders = canSeeOrders;
        this.canCheckProductsRemains = canCheckProductsRemains;
        this.canClearBasket = canClearBasket;
        this.canReupdateBase = canReupdateBase;
        this.canChangeReferal = canChangeReferal;
        this.canChangeReferalPercent = canChangeReferalPercent;
        this.canAddAdmin = canAddAdmin;
        this.canAddToSaleProducts = canAddToSaleProducts;
        this.canClearDiscountChoice = canClearDiscountChoice;
    }
    public Admins(){}

    public Long getTgId() {
        return tgId;
    }

    public void setTgId(Long tgId) {
        this.tgId = tgId;
    }

    public String getCanAllMessage() {
        return canAllMessage;
    }

    public void setCanAllMessage(String canAllMessage) {
        this.canAllMessage = canAllMessage;
    }

    public String getCanSeeOrders() {
        return canSeeOrders;
    }

    public void setCanSeeOrders(String canSeeOrders) {
        this.canSeeOrders = canSeeOrders;
    }

    public String getCanTransactOrders() {
        return canTransactOrders;
    }

    public void setCanTransactOrders(String canTransactOrders) {
        this.canTransactOrders = canTransactOrders;
    }

    public String getCanCheckProductsRemains() {
        return canCheckProductsRemains;
    }

    public void setCanCheckProductsRemains(String canCheckProductsRemains) {
        this.canCheckProductsRemains = canCheckProductsRemains;
    }

    public String getCanClearBasket() {
        return canClearBasket;
    }

    public void setCanClearBasket(String canClearBasket) {
        this.canClearBasket = canClearBasket;
    }

    public String getCanReupdateBase() {
        return canReupdateBase;
    }

    public void setCanReupdateBase(String canReupdateBase) {
        this.canReupdateBase = canReupdateBase;
    }

    public String getCanChangeReferal() {
        return canChangeReferal;
    }

    public void setCanChangeReferal(String canChangeReferal) {
        this.canChangeReferal = canChangeReferal;
    }

    public String getCanChangeReferalPercent() {
        return canChangeReferalPercent;
    }

    public void setCanChangeReferalPercent(String canChangeReferalPercent) {
        this.canChangeReferalPercent = canChangeReferalPercent;
    }

    public String getCanAddAdmin() {
        return canAddAdmin;
    }

    public void setCanAddAdmin(String canAddAdmin) {
        this.canAddAdmin = canAddAdmin;
    }

    public String getCanAddToSaleProducts() {
        return canAddToSaleProducts;
    }

    public void setCanAddToSaleProducts(String canAddToSaleProducts) {
        this.canAddToSaleProducts = canAddToSaleProducts;
    }

    public String getCanClearDiscountChoice() {
        return canClearDiscountChoice;
    }

    public void setCanClearDiscountChoice(String canClearDiscountChoice) {
        this.canClearDiscountChoice = canClearDiscountChoice;
    }
}
