package com.example.ticket;

import java.util.logging.Logger;

public class Customer implements Runnable {
    private static final Logger logger = Logger.getLogger(Customer.class.getName());
    private final TicketPool ticketPool;
    private final int retrievalRateMs; // Milliseconds between each ticket retrieval

    public Customer(TicketPool ticketPool, int retrievalRateMs) {
        if (retrievalRateMs <= 0) {
            throw new IllegalArgumentException("Retrieval rate must be a positive number.");
        }
        this.ticketPool = ticketPool;
        this.retrievalRateMs = retrievalRateMs;
    }

    @Override
    public void run() {
        try {
            while (!ticketPool.isSystemCompleted()) {
                ticketPool.retrieveTicket();
                Thread.sleep(retrievalRateMs); // Delay according to the retrieval rate
            }
        } catch (InterruptedException e) {
            logger.severe("Customer thread interrupted.");
            Thread.currentThread().interrupt(); // Reset interrupt status
        } finally {
            logger.info("Customer has finished retrieving tickets.\nSystem Stopped, All Tickets Sold!!");
        }
    }
}
