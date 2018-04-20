package org.matthiaszimmermann.trading;

import java.util.Date;
import java.util.PriorityQueue;

/**
 * Order book implementation. 
 * Before any trades can be executed buy orders and sell orders need to be put into the order book first. 
 */
public final class OrderBook {

	private String symbol;
	private PriorityQueue<BuyOrder> buyQueue;
	private PriorityQueue<SellOrder> sellQueue;
	
	public OrderBook(String symbol) {
		this.setSymbol(symbol);
		
		buyQueue = new PriorityQueue<BuyOrder>();
		sellQueue = new PriorityQueue<SellOrder>();
	}

	public String getSymbol() {
		return symbol;
	}

	private void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public BuyOrder peekBuyOrder() {
		return buyQueue.peek();
	}
	
	public BuyOrder popBuyOrder() {
		return buyQueue.poll();
	}
	
	public SellOrder peekSellOrder() {
		return sellQueue.peek();
	}
	
	public SellOrder popSellOrder() {
		return sellQueue.poll();
	}
	
	/**
	 * Adds the specified order to this order book.
	 */
	public void put(Order order) {
		order.setTimestamp(new Date());
		
		if(order.isBuyOrder()) {
			buyQueue.add((BuyOrder) order);
		}
		else {
			sellQueue.add((SellOrder) order);
		}
	}
	
	/**
	 * Removes the specified order from the order book.
	 */
	public boolean remove(Order order) {
		if(!isPending(order)) {
			return false;
		}
		
		if(order.isBuyOrder()) {
			buyQueue.remove(order);
		}
		else {
			sellQueue.remove(order);
		}
		
		return true;
	}

	/**
	 * Checks if the specified order is still in the order book.
	 */
	public boolean isPending(Order order) {
		if(order.isBuyOrder()) {
			return buyQueue.contains(order);
		}
		else {
			return sellQueue.contains(order);
		}
	}

	/**
	 * Returns true if this order book contains (at least) one buy order that (partially) matches with a sell order. 
	 * If no match is found, false is returned.
	 */
	public boolean matchAvailable() {
		if(buyQueue.size() * sellQueue.size() == 0) {
			return false;
		}
		
		BuyOrder buy = buyQueue.peek();
		SellOrder sell = sellQueue.peek();
		
		// condition for partial matches
		if(buy.getPrice() < sell.getPrice()) {
			return false;
		}
		
		// conditions for exact matches
//		if(buy.getPrice() != sell.getPrice()) {
//			return false;
//		}
//		
//		if(buy.getQuantity() != sell.getQuantity()) {
//			return false;
//		}
		
		return true;
	}

	/**
	 * Executes the first best (partial) match in this order book.
	 * The owners of the matching buy/sell orders are notified accordingly. 
	 */
	public void executeMatch() {
		if(!matchAvailable()) {
			return;
		}
		
		BuyOrder buy = buyQueue.peek();
		SellOrder sell = sellQueue.peek();
		
		// calculate quantity and price for matching order
		int quantity = Math.min(buy.quantity(), sell.quantity());
		double price = (buy.getPrice() + sell.getPrice()) / 2.0;
		
		// update order book and notify order owners
		handleUpdate(sell, quantity, price);
		handleUpdate(buy, quantity, price);
	}
	
	/**
	 * Handles updating the order book for the specified order and match parameters.
	 * @param order the order that is to be (partially) executed
	 * @param executionQuantity the execution quantity
	 * @param price the execution price
	 */
	private void handleUpdate(Order order, int executionQuantity, double price) {
		boolean fullExecution = executionQuantity == order.quantity();
		
		// the full order is executed, remove the order from the order book
		if(fullExecution) {
			remove(order);
		}
		// partial match, update the remaining quantities
		else {
			order.setQuantity(order.quantity() - executionQuantity);
		}
		
		// owner notification
		String message = String.format("%s full: %s", order, fullExecution);
		order.getOwner().orderExecuted(order, executionQuantity, price, fullExecution, message);
	}
}
