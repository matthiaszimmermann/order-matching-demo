package org.matthiaszimmermann.trading;

import java.util.Date;

public class BuyOrder extends Order {
	
	public BuyOrder(String symbol, Date timestamp, int quantity, double price) {
		super(symbol, timestamp, quantity, price);
		type = Type.BUY;
	}

	public int compareTo(Order o) {
		return (int) Math.signum(o.getPrice() - getPrice());
	}
}
