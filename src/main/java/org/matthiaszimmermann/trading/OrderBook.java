package org.matthiaszimmermann.trading;

import java.util.PriorityQueue;

public class OrderBook {

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

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void put(Order order) {
		if(order instanceof BuyOrder) {
			buyQueue.add((BuyOrder) order);
		}
		else {
			sellQueue.add((SellOrder) order);
		}
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
}
