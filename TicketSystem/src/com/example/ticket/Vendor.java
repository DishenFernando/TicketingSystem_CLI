package com.example.ticket;

import java.util.logging.Logger;

public class Vendor implements Runnable {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    private final TicketPool ticketPool;
    private final int releaseRateMs; // Milliseconds between each ticket release

    public Vendor(TicketPool ticketPool, int releaseRateMs) {
        if (releaseRateMs <= 0) {
            throw new IllegalArgumentException("Release rate must be a positive number.");
        }
        this.ticketPool = ticketPool;
        this.releaseRateMs = releaseRateMs;
    }

    @Override
    public void run() {
        try {
            while (!ticketPool.isSystemCompleted()) {
                ticketPool.addTickets();
                Thread.sleep(releaseRateMs); // Delay according to the release rate
            }
        } catch (InterruptedException e) {
            logger.severe("Vendor thread interrupted.");
            Thread.currentThread().interrupt(); // Reset interrupt status
        } finally {
            logger.info("Vendor has stopped releasing tickets.");
        }
    }
}
