package com.example.ticket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Set up logs to display in console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        // Log the starting point of the program
        logger.info("Ticket System has Started.");

        // Get inputs from user with validation
        int totalTickets = getValidInput(scanner, "Enter Total Number of Tickets: ");
        int maxTicketsInPool = getValidInput(scanner, "Enter Maximum Tickets in Pool (Pool limit): ");
        int releaseRate = getValidInput(scanner, "Enter Ticket Release Rate (in milliseconds): ");
        int retrievalRate = getValidInput(scanner, "Enter Customer Retrieval Rate (in milliseconds): ");

        // Save the inputs to a text file
        savingInputs(totalTickets, maxTicketsInPool, releaseRate, retrievalRate);

        // Initialize the ticket pool with total and maximum tickets
        TicketPool ticketPool = new TicketPool(totalTickets, maxTicketsInPool);

        // Create the vendor and customer threads
        Vendor vendor = new Vendor(ticketPool, releaseRate);
        Customer customer = new Customer(ticketPool, retrievalRate);

        Thread vendorThread = new Thread(vendor);
        Thread customerThread = new Thread(customer);

        // Start the vendor and customer threads
        vendorThread.start();
        customerThread.start();

        try {
            // Wait for both vendor and customer threads to finish
            vendorThread.join();
            customerThread.join();
        } catch (InterruptedException e) {
            logger.severe("Main thread interrupted.");
            Thread.currentThread().interrupt(); // Reset interrupt status
        }

        // After both threads are done, print the system stopped message
        logger.info("System stopped. All tickets have been sold.");
    }

    /**
     * Validate user input to ensure it's a positive integer.
     */
    private static int getValidInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) {
                    break; // Valid input
                } else {
                    System.out.println("Invalid input. Please enter a positive number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive number.");
                scanner.next(); // Clear invalid input
            }
        }
        return value;
    }

    /**
     * Save inputs to a text file for future reference.
     */
    private static void savingInputs(int totalTickets, int maxTicketsInPool, int releaseRate, int retrievalRate) {
        File file = new File("ticket_system_inputs.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Total Number of Tickets: " + totalTickets);
            writer.newLine();
            writer.write("Maximum Tickets in Pool: " + maxTicketsInPool);
            writer.newLine();
            writer.write("Ticket Release Rate (ms): " + releaseRate);
            writer.newLine();
            writer.write("Customer Retrieval Rate (ms): " + retrievalRate);
            writer.newLine();
            writer.write("Recorded at: " + LocalTime.now());
            writer.newLine();

            logger.info("Inputs saved to ticket_system_inputs.txt.");
        } catch (IOException e) {
            logger.severe("Error writing to file: " + e.getMessage());
        }
    }
}
