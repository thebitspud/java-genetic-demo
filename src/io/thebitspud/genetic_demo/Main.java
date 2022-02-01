package io.thebitspud.genetic_demo;

import java.util.SplittableRandom;
import java.util.Scanner;

/**
 * Runs a command line simulation of the genetic algorithm
 */
public class Main {
    // Simulation config constants
    public static final int GENOME_LENGTH = 64; // > 0
    public static final int UNIQUE_GENES = 26; // >= 2
    public static final int POPULATION_SIZE = 50; // >= 2
    /** Number of genomes that survive and reproduce each generation */
    public static final int ELITISM = 10; // > 0
    public static final boolean DO_CROSSOVER = true;
    /** Average mutations per gene location */
    public static final float MUTATION_RATE = 1f / GENOME_LENGTH; // in (0,1)
    /** Guarantee every gene location has a 1.0 allele */
    public static final boolean GUARANTEE_MAX = true;

    public static final long SEED = (new SplittableRandom()).nextLong();
    public static final SplittableRandom r = new SplittableRandom(SEED);

    public static void main(String[] args) {
	    Population pop = new Population(POPULATION_SIZE);
        printHelp();
        System.out.println("\n" + pop);

        Scanner in = new Scanner(System.in);
        boolean running = true;

        // Application loop
        while (running) {
            String[] input = in.nextLine().toLowerCase().split(" ");

            switch (input[0]) {
                case "x":
                    running = false;
                    break;
                case "h":
                    printHelp();
                    break;
                case "i":
                    System.out.println("Seed: " + SEED + "\n");
                    pop.printDetails();
                    break;
                case "r":
                    System.out.println("Resetting simulation...");
                    pop = new Population(POPULATION_SIZE);
                    System.out.println(pop);
                    break;
                case "n":
                    tryAdvance(pop, input);
                    break;
                default:
                    System.out.println("Invalid input '" + input[0] + "'");
                    System.out.println("Type 'h' for help.");
                    break;
            }
        }
    }

    /** Prints out a list of valid console commands */
    public static void printHelp() {
        System.out.println("Commands:");
        System.out.println("'n <c>' to simulate c generations (c > 0: default 1)");
        System.out.println("'i' to display raw population data");
        System.out.println("'r' to reset simulation");
        System.out.println("'x' to exit");
    }

    /** Attempts to advance the simulation according to provided parameters */
    public static void tryAdvance(Population pop, String[] input) {
        if (input.length > 1) {
            try {
                int count = Integer.parseInt(input[1]);
                if (count < 1) {
                    System.out.println("Invalid parameter '" + input[1] + "'");
                    return;
                }

                // Advance only if a valid parameter is provided
                pop.simulate(count);
            } catch (NumberFormatException e) {
                System.out.println("Invalid parameter '" + input[1] + "'");
                return;
            }
        } else {
            pop.simulate();
        }

        System.out.println(pop);
    }
}
