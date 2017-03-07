package org.matthiaszimmermann.trading;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/** 
 * Test class to check the scenario described here
 * http://stackoverflow.com/questions/13112062/which-are-the-order-matching-algorithms-most-commonly-used-by-electronic-financi

Id   Side    Time   Qty   Price   Qty    Time   Side  
---+------+-------+-----+-------+-----+-------+------
#3                        20.30   200   09:05   SELL  
#1                        20.30   100   09:01   SELL  
#2                        20.25   100   09:03   SELL  
#5   BUY    09:08   200   20.20                       
#4   BUY    09:06   100   20.15                       
#6   BUY    09:09   200   20.15     

 */
public class OrderBookTest {

	static final String SYMBOL = "symbol";
	
	static final BuyOrder BUY_ORDER_1 = new BuyOrder(SYMBOL, new Date(), 200, 20.20);
	static final BuyOrder BUY_ORDER_2 = new BuyOrder(SYMBOL, new Date(), 100, 20.15);
	static final BuyOrder BUY_ORDER_3 = new BuyOrder(SYMBOL, new Date(), 200, 20.15); 
	
	static final SellOrder SELL_ORDER_1 = new SellOrder(SYMBOL, new Date(), 200, 20.30);
	static final SellOrder SELL_ORDER_2 = new SellOrder(SYMBOL, new Date(), 100, 20.30);
	static final SellOrder SELL_ORDER_3 = new SellOrder(SYMBOL, new Date(), 100, 20.25); 
	
	@Test
	public void testBuyOrders() {
		OrderBook ob = new OrderBook(SYMBOL);
		ob.put(BUY_ORDER_3);
		ob.put(BUY_ORDER_1);
		ob.put(BUY_ORDER_2);
				
		BuyOrder firstOrder = ob.popBuyOrder();
		BuyOrder secondOrder = ob.popBuyOrder();
		
		Assert.assertEquals("expected order o1", BUY_ORDER_1, firstOrder);
		Assert.assertTrue("expected o2 or o3", secondOrder.equals(BUY_ORDER_2) || secondOrder.equals(BUY_ORDER_3));	
	}
	
	@Test
	public void testSellOrders() {
		OrderBook ob = new OrderBook(SYMBOL);
		ob.put(SELL_ORDER_1);
		ob.put(SELL_ORDER_3);
		ob.put(SELL_ORDER_2);
				
		SellOrder firstOrder = ob.popSellOrder();
		SellOrder secondOrder = ob.popSellOrder();
		
		Assert.assertEquals("expected order o3", SELL_ORDER_3, firstOrder);
		Assert.assertTrue("expected o2 or o3", secondOrder.equals(SELL_ORDER_1) || secondOrder.equals(SELL_ORDER_2));	
	}
}
