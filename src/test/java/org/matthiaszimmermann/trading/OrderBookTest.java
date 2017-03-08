package org.matthiaszimmermann.trading;

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
	
	static final SellOrder SELL_ORDER_1 = new SellOrder(SYMBOL, 100, 20.30);
	static final SellOrder SELL_ORDER_2 = new SellOrder(SYMBOL, 100, 20.25);
	static final SellOrder SELL_ORDER_3 = new SellOrder(SYMBOL, 200, 20.30); 
	
	static final BuyOrder BUY_ORDER_1 = new BuyOrder(SYMBOL, 100, 20.15);
	static final BuyOrder BUY_ORDER_2 = new BuyOrder(SYMBOL, 200, 20.20);
	static final BuyOrder BUY_ORDER_3 = new BuyOrder(SYMBOL, 200, 20.15); 
	
	@Test
	public void testSellOrders() throws Exception {
		OrderBook ob = new OrderBook(SYMBOL);
		ob.put(SELL_ORDER_1); Thread.sleep(100);
		ob.put(SELL_ORDER_2); Thread.sleep(100);
		ob.put(SELL_ORDER_3);
		
		Assert.assertEquals("expected order o2", SELL_ORDER_2, ob.popSellOrder());
		Assert.assertEquals("expected order o1", SELL_ORDER_1, ob.popSellOrder());
		Assert.assertEquals("expected order o3", SELL_ORDER_3, ob.popSellOrder());
		Assert.assertTrue("order book not empty", ob.peekSellOrder() == null);
	}
	
	@Test
	public void testBuyOrders() throws Exception {
		OrderBook ob = new OrderBook(SYMBOL);
		ob.put(BUY_ORDER_1); Thread.sleep(100);
		ob.put(BUY_ORDER_2); Thread.sleep(100);
		ob.put(BUY_ORDER_3);
		
		Assert.assertEquals("expected order o2", BUY_ORDER_2, ob.popBuyOrder());
		Assert.assertEquals("expected order o1", BUY_ORDER_1, ob.popBuyOrder());
		Assert.assertEquals("expected order o3", BUY_ORDER_3, ob.popBuyOrder());
		Assert.assertTrue("order book not empty", ob.peekBuyOrder() == null);
	}
	
	
/* scenario: place order "buy 250 shares at 20.35"
 * result:
 
    100 shares at 20.25 (order #2)
    100 shares at 20.30 (order #1)
    50 shares at 20.30 (order #3)

Id   Side    Time   Qty   Price   Qty    Time   Side  
---+------+-------+-----+-------+-----+-------+------
#3                        20.30   150   09:05   SELL  
#5   BUY    09:08   200   20.20                       
#4   BUY    09:06   100   20.15                       
#6   BUY    09:09   200   20.15       
 */

	@Test
	public void testAddMatchingOrder() throws Exception {
		OrderBook ob = new OrderBook(SYMBOL);
		Participant a = new Participant("A");
		Participant b = new Participant("B");
		Participant c = new Participant("C");
		Participant d = new Participant("D");
		
		a.submit(ob, SELL_ORDER_1); Thread.sleep(100);
		a.submit(ob, SELL_ORDER_2); Thread.sleep(100);
		b.submit(ob, SELL_ORDER_3); Thread.sleep(100);
		c.submit(ob, BUY_ORDER_1); Thread.sleep(100);
		c.submit(ob, BUY_ORDER_2); Thread.sleep(100);
		d.submit(ob, BUY_ORDER_3);
		
		Assert.assertFalse("There should be now match now", ob.matchAvailable());
		
		// make d submitting a matching buy order
		BuyOrder matchingOrder = new BuyOrder(SYMBOL, 250, 20.35);
		d.submit(ob, matchingOrder);
		
		Assert.assertTrue("There should be a match now", ob.matchAvailable());
		System.out.println("Matching order: " + matchingOrder);
		
		ob.executeMatch();
		Assert.assertFalse("Sell order 2 should be executed", ob.isPending(SELL_ORDER_2));
		Assert.assertTrue("Matching order should still be pending", ob.isPending(matchingOrder));
		System.out.println("Matching order: " + matchingOrder);
		
		Assert.assertTrue("There should be a match now", ob.matchAvailable());
		
		ob.executeMatch();
		Assert.assertFalse("Sell order 1 should be executed", ob.isPending(SELL_ORDER_1));
		Assert.assertTrue("Matching order should still be pending", ob.isPending(matchingOrder));
		System.out.println("Matching order: " + matchingOrder);
		
		Assert.assertTrue("There should be a match now", ob.matchAvailable());
		
		ob.executeMatch();
		Assert.assertTrue("Sell order 3 should still be pending", ob.isPending(SELL_ORDER_3));
		Assert.assertFalse("Matching order should be executed", ob.isPending(matchingOrder));
		System.out.println("Matching order: " + matchingOrder);
		
		Assert.assertFalse("No more matches expected", ob.matchAvailable());		
		System.out.println("Matching order: " + matchingOrder);
	}
}
