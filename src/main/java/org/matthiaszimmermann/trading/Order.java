package org.matthiaszimmermann.trading;

import java.util.Date;
import java.util.UUID;

/**
 * Parent order class. Buy- and sell-orders are specialized classes with their own comparable implementation.  
 */
public abstract class Order implements Comparable<Order> {

	public enum Type { BUY, SELL };

	private final String id;
	private final Type type;
	private final String symbol;
	private final int initialQuantity;
	private final double price;

	/**
	 * Set when the order is submitted to the order book
	 */
	private Date timestamp;
	private Participant owner;
	private int quantity;

	protected Order(String symbol, int quantity, double price, Type type) {
		this.symbol = symbol;
		this.type = type;

		setQuantity(quantity);

		if(price <= 0) {
			throw new IllegalArgumentException("Order price must be positive");
		}
		this.price = price;

		initialQuantity = quantity;

		id = type + "-" + UUID.randomUUID();
	}

	public int initialQuantity() { return initialQuantity; }

	public boolean isBuyOrder(){ return type == Type.BUY; }

	public String id() {
		return id;
	}

	public Participant getOwner() {
		return owner;
	}

	public void setOwner(Participant owner) {
		this.owner = owner;
	}

	public String symbol() {
		return symbol;
	}

	public Date timestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int quantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if(quantity <= 0) {
			throw new IllegalArgumentException("Order quantity must be positive");
		}

		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		String ownerName = owner == null ? "<unk>" : owner.getName();
		String orderType = type == Type.BUY ? "BUY" : "SELL";

		return String.format("%s %s %s %d %s %s", ownerName, symbol, orderType, quantity, price, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Order) {
			String thatOrder = ((Order) obj).id();
			return id.equals(thatOrder);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
