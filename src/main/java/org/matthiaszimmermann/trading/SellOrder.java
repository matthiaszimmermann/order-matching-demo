package org.matthiaszimmermann.trading;

/**
 * Buy orders for the demo trading platform.
 */
public final class SellOrder extends Order {
	
	public SellOrder(String symbol, int quantity, double price) {
		super(symbol, quantity, price, Type.SELL);
	}

	/**
	 * Comparator that sorts sell orders by (1) increasing price (2) increasing time stamps. 
	 */
	public int compareTo(Order o) {
		int sig =  (int) Math.signum(getPrice() - o.getPrice());
		
		if(sig != 0) {
			return sig;
		}
		
		return timestamp().compareTo(o.timestamp());
	}
}
