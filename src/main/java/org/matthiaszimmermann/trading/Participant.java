package org.matthiaszimmermann.trading;

/**
 * Simplistic participant for the demo trading platform. 
 */
public class Participant {
	
	private String name;

	public Participant(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Submit an order to the specified order book.
	 * @param book the order book
	 * @param order the order to submit
	 */
	public void submit(OrderBook book, Order order) {
		order.setOwner(this);
		book.put(order);
	}
	
	/**
	 * Callback function for the order book.
	 * Whenever an order from this participant is (partially) executed the order book uses this callback function to notify the owner of the order.
	 * @param order the reference order in the order book
	 * @param quantity the executed quantity
	 * @param price the executed price
	 * @param fullExecution indicates if the order was executed in full or only partially
	 * @param message some debug message
	 */
	public void orderExecuted(Order order, int quantity, double price, boolean fullExecution, String message) {
		System.out.println(message + " " + quantity + " " + price);
	} 

}
