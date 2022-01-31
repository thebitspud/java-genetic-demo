package io.thebitspud.genetic_demo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents a set of candidate solutions to a fitness function
 */
public class Population {
    private ArrayList<Genome> genomes = new ArrayList<>();
    private FitnessFunction fit = new FitnessMatrix();

    private int size, generation;
    private float max, min, med, mean, sd;

    /** Creates a new population of the specified size */
    public Population(int size) {
        this.size = size;

        // Initialize a set of random genomes
        for (int i = 0; i < size; i++) {
            genomes.add(new Genome(Main.GENOME_LENGTH, generation));
        }

        sortGenomes();
        computeStats();
    }

    /** Advances the simulation by the specified number of generations */
    public void advance(int count) {
        for (int i = 0; i < count; i++) {
            generation++;
            select();
            propagate();
            computeMedian();
        }

        // Only doing this once at the end (computeStats is costly)
        System.out.println("Advancing " + count + " generations...");
        computeStats();
    }

    /** Advances the simulation by one generation via genetic operations */
    public void advance() {
        generation++;
        select();
        propagate();
        computeStats();
    }

    /** Reduces the population via a selection operation */
    public void select() {
        // Performing a truncation selection operation by removing
        // the bottom (least fit) genomes from the population
        int start = Math.round(size * Main.ELITISM_RATE);
        start = Math.max(Math.min(start, size - 1), 1);
        genomes.subList(start, size).clear();
    }

    /** Grows the population to capacity via propagation operations */
    public void propagate() {
        int validParents = genomes.size();

        if (Main.DO_CROSSOVER && validParents > 1) {
            // Crossover
            while (genomes.size() < size) {
                final Genome p1 = genomes.get(Main.r.nextInt(validParents));
                final Genome p2 = genomes.get(Main.r.nextInt(validParents));
                genomes.add(new Genome(p1, p2, generation));
            }
        } else {
            // Cloning
            while (genomes.size() < size) {
                genomes.add(new Genome(genomes.get(0), generation));
            }
        }

        sortGenomes();
    }

    /** Sorts genomes by fitness in descending order */
    public void sortGenomes() {
        genomes.sort(Comparator.comparingDouble(g -> -fit.evaluate(g)));
    }

    /** Computes the population median separately */
    public void computeMedian() {
        float[] values = new float[size];

        for (int i = 0; i < size; i++) {
            values[i] = fit.evaluate(genomes.get(i));
        }

        Arrays.sort(values);
        med = (values[size / 2] + values[(size - 1) / 2]) / 2;
    }

    /** Computes common population parameters */
    public void computeStats() {
        float total = 0, total2 = 0;
        max = 0f;
        min = 1f;

        for (int i = 0; i < size; i++) {
            float eval = fit.evaluate(genomes.get(i));
            max = Math.max(max, eval);
            min = Math.min(min, eval);
            total += eval;
            total2 += Math.pow(eval, 2);
        }

        mean = total / size;
        sd = (float) Math.sqrt(Math.max(0, total2 / size - Math.pow(mean, 2)));

        computeMedian();
    }

    /** Prints the population's raw genome and fitness data */
    public void printDetails() {
        DecimalFormat df = new DecimalFormat("0.00");

        // Setting a consistent number of digits for the index when printed
        int order = (int) (Math.log10(size) + 1);
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < order; i++) digits.append("0");
        DecimalFormat df2 = new DecimalFormat(digits.toString());

        // Printing genome information
        for (int i = 0; i < size; i++) {
            Genome g = genomes.get(i);
            String data = df2.format(i + 1) + " " + g
                    + "  Fit:" + df.format(fit.evaluate(g))
                    + "  Gen:" + g.getGeneration();
            System.out.println(data);
        }

        // Printing fitness matrix values
        System.out.println("\nFitness Matrix:" + fit);
    }

    /** Returns a string representation of population details and parameters */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.000");

        return "Generation: " + generation
                + "  Population: " + size
                + "\nMax: " + df.format(max)
                + "  Min: " + df.format(min)
                + "  Mean: " + df.format(mean)
                + "  Med: " + df.format(med)
                + "  SD: " + df.format(sd);
    }
}
