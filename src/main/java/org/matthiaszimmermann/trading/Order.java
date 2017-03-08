package org.matthiaszimmermann.trading;

import java.util.Date;
import java.util.UUID;

/**
 * Parent order class. Buy- and sell-orders are specialized classes with their own comparable implementation.  
 */
public abstract class Order implements Comparable<Order> {

	public enum Type { BUY, SELL };

	private UUID id;
	private Participant owner;

	protected Type type;
	protected String symbol;
	protected Date timestamp;
	protected int quantity;
	protected double price;

	public Order(String symbol, int quantity, double price) {
		id = UUID.randomUUID();
		this.symbol = symbol;
		this.timestamp = new Date();

		setQuantity(quantity);
		setPrice(price);
	}

	public UUID getId() {
		return id;
	}

	public Participant getOwner() {
		return owner;
	}

	public void setOwner(Participant owner) {
		this.owner = owner;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Type getType() {
		return type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if(quantity <= 0) {
			throw new IllegalArgumentException("Order quantity must be postive");
		}

		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if(price <= 0) {
			throw new IllegalArgumentException("Order price must be postive");
		}

		this.price = price;
	}

	@Override
	public String toString() {
		String ownerName = owner == null ? "<unk>" : owner.getName();
		String orderType = type == Type.BUY ? orderType = "BUY" : "SELL";  

		return String.format("%s %s %s %d %s %s", ownerName, symbol, orderType, quantity, price, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Order) {
			UUID thatOrder = ((Order) obj).getId();
			return id.equals(thatOrder);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
