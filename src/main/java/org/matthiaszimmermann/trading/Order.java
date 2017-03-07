package org.matthiaszimmermann.trading;

import java.util.Date;
import java.util.UUID;

public abstract class Order implements Comparable<Order> {

	public enum Type { BUY, SELL };
	
	private UUID id;
	
	protected Type type;
	protected String symbol;
	protected Date timestamp;
	protected int quantity;
	protected double price;
	
	public Order(String symbol, Date timestamp, int quantity, double price) {
		id = UUID.randomUUID();
		
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.quantity = quantity;
		this.price = price;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		if(type == Type.BUY) {
			return String.format("%s BUY %d %s", symbol, quantity, price);
		}
		else {
			return String.format("%s SELL %d %s", symbol, quantity, price);
		}
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
