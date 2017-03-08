# Java Demo Trading Platform
Demo for Order Matching Alogrithm for continuous Trading according to [Stackoverflow](http://stackoverflow.com/questions/13112062/which-are-the-order-matching-algorithms-most-commonly-used-by-electronic-financi).

## Main Classes
* BuyOrder: order that specifies "thing", quantity and price the buyer tries to buy
* SellOrder: order that specifies "thing", quantity and price the sellers is prepared to sell
* OrderBook: contains all buy and sell orders that have not yet been executed, can execute orders
* Participant: represents clients that submit buy and sell orders to the order book

## Order Book Dynamics

Illustration of an instance/state of an order book (without matching orders)
```
Id   Side    Time   Qty   Price   Qty    Time   Side  
---+------+-------+-----+-------+-----+-------+------
#3                        20.30   200   09:05   SELL  
#1                        20.30   100   09:01   SELL  
#2                        20.25   100   09:03   SELL  
#5   BUY    09:08   200   20.20                       
#4   BUY    09:06   100   20.15                       
#6   BUY    09:09   200   20.15
```

After adding a matching order (BUY, Qty:250, Price:20.35) at 10:11, the order book will look like this
```
Id   Side    Time   Qty   Price   Qty    Time   Side  
---+------+-------+-----+-------+-----+-------+------
#3                        20.30   200   09:05   SELL  
#1                        20.30   100   09:01   SELL  
#2                        20.25   100   09:03   SELL
#7   BUY    10:11   250   20.35
#5   BUY    09:08   200   20.20                       
#4   BUY    09:06   100   20.15                       
#6   BUY    09:09   200   20.15
```

In the order book a buyer is now represented that is prepared to pay 20.35 which machtes with the seller that is prepared to sell for 20.25. In this implementation, the order book calculates the price average for matching orders. Iteratively executing matches the following sequence of transactions follows:

- SELL order #2 is (fully) executed for Qty: 100 and Price:20.30
- BUY order #7 is (partially) executed for Qty: 100 and Price:20.30 (remaining quantity: 150)
- SELL order #1 is (fully) executed for Qty: 100 and Price: 20.325
- BUY order #7 is (partially) executed for Qty: 100 and Price 20.325 (remaining quantity: 50)
- SELL order #3 is (partially) executed for Qty: 50 and Price 20.325 (remaining quantity: 150)
- BUY order #7 is now completed for the remaining Qty 50 for Price 20.325

At the end, this leaves the order book in the following state:
```
Id   Side    Time   Qty   Price   Qty    Time   Side  
---+------+-------+-----+-------+-----+-------+------
#3                        20.30   150   09:05   SELL  
#5   BUY    09:08   200   20.20                       
#4   BUY    09:06   100   20.15                       
#6   BUY    09:09   200   20.15
```

## Prerequisites to run the Demo
* Java
* Maven
* Git

## Run the Demo

- Clone the repo to some directory
- cd into the directory
- run Maven

```
mvn clean verify
```

This should lead to an output similar to:
```
...
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.matthiaszimmermann.trading.OrderBookTest
Matching order: D symbol BUY 250 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129
A symbol SELL 100 20.25 8811aae1-da5c-4dfc-9b97-7df771458ded full: true 100 20.3
D symbol BUY 150 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129 full: false 100 20.3
Matching order: D symbol BUY 150 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129
A symbol SELL 100 20.3 5c058e92-cf9a-42a7-b4f7-9cb0a584d11b full: true 100 20.325000000000003
D symbol BUY 50 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129 full: false 100 20.325000000000003
Matching order: D symbol BUY 50 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129
B symbol SELL 150 20.3 081f303f-9c64-4fdf-b4b0-025b037c009f full: false 50 20.325000000000003
D symbol BUY 50 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129 full: true 50 20.325000000000003
Matching order: D symbol BUY 50 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129
Matching order: D symbol BUY 50 20.35 fba68f99-0b32-43e6-90b4-81c3ad1b8129
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.257 sec

Results :

Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ OrderMatchingDemo ---
[INFO] Building jar: c:\github\order-matching-demo\target\OrderMatchingDemo-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.793 s
[INFO] Finished at: 2017-03-09T00:12:26+01:00
[INFO] Final Memory: 15M/269M
[INFO] ------------------------------------------------------------------------
```

Yay :-)
