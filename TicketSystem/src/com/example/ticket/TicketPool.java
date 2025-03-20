package com.example.ticket;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int totalTickets;
    private final int maxTicketsInPool;
    private int soldTickets = 0;
    private int ticketsCounter = 0;

    public TicketPool(int totalTickets, int maxTicketsInPool) {
        if (totalTickets <= 0 || maxTicketsInPool <= 0) {
            throw new IllegalArgumentException("Total tickets and maximum tickets in pool must be positive numbers.");
        }
        this.totalTickets = totalTickets;
        this.maxTicketsInPool = maxTicketsInPool;
    }

    // Add tickets to the pool, but only up to the maximum allowed in the pool
    public synchronized void addTickets() throws InterruptedException {
        while (tickets.size() >= maxTicketsInPool || ticketsCounter >= totalTickets) {
            wait(); // Wait if the pool is full or all tickets are sold
        }

        if (ticketsCounter < totalTickets) {
            ticketsCounter++;
            tickets.add(ticketsCounter);
            logger.info("Vendor released Ticket " + ticketsCounter + ". Pool size: " + tickets.size());
        }

        notifyAll(); // Notify waiting threads
    }

    // Retrieve ticket from the pool
    public synchronized void retrieveTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait if no tickets are available for retrieval
        }

        if (!tickets.isEmpty()) {
            int ticketNumber = tickets.poll();
            soldTickets++;
            logger.info("Customer retrieved Ticket " + ticketNumber + ". Tickets sold: " + soldTickets);
        }

        notifyAll(); // Notify waiting threads
    }

    // Check if all tickets are sold
    public synchronized boolean isSystemCompleted() {
        return soldTickets >= totalTickets;
    }
}
