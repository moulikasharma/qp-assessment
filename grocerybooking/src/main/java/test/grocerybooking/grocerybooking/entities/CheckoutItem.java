package test.grocerybooking.grocerybooking.entities;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CheckoutItem {
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private BigDecimal price;
	private Long quantity;
	private BigDecimal amount;
	
	public CheckoutItem() {}
	
	public CheckoutItem(Long id, String name, BigDecimal price, Long quantity, BigDecimal amount) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "CheckoutItem [id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", amount="
				+ amount + "]";
	}

}
