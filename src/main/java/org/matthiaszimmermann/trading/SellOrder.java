package org.matthiaszimmermann.trading;

import java.util.Date;

public class SellOrder extends Order {
	
	public SellOrder(String symbol, Date timestamp, int quantity, double price) {
		super(symbol, timestamp, quantity, price);
		type = Type.SELL;
	}

	public int compareTo(Order o) {
		return (int) Math.signum(getPrice() - o.getPrice());
	}
}
