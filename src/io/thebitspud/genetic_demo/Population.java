package io.thebitspud.genetic_demo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
    public void simulate(int count) {
        System.out.println("Advancing " + count + " generations...");
        for (int i = 0; i < count; i++) nextGeneration();

        // Only doing this once at the end (computeStats is costly)
        computeStats();
    }

    /** Advances the simulation by one generation */
    public void simulate() {
        nextGeneration();
        computeStats();
    }

    /** Performs genetic operations on genomes */
    private void nextGeneration() {
        generation++;
        select();
        propagate();
        sortGenomes();
    }

    /** Reduces the population via a selection operation */
    public void select() {
        // Performing a truncation selection operation by removing
        // the bottom (least fit) genomes from the population
        genomes.subList(Main.ELITISM, size).clear();
    }

    /** Grows the population to capacity via propagation operations */
    public void propagate() {
        int validParents = genomes.size();

        if (Main.DO_CROSSOVER && validParents > 1) {
            // Crossover
            while (genomes.size() < size) {
                Genome p1 = genomes.get(Main.r.nextInt(validParents));
                Genome p2 = genomes.get(Main.r.nextInt(validParents));
                genomes.add(new Genome(p1, p2, generation));
            }
        } else {
            // Cloning
            while (genomes.size() < size) {
                Genome parent = genomes.get(Main.r.nextInt(validParents));
                genomes.add(new Genome(parent, generation));
            }
        }
    }

    /** Sorts genomes by fitness in descending order */
    public void sortGenomes() {
        HashMap<Genome, Float> evals = new HashMap<>();
        for (Genome g: genomes) evals.put(g, -fit.evaluate(g));
        genomes.sort(Comparator.comparing(evals::get));
    }

    /** Computes common population parameters */
    public void computeStats() {
        float total = 0, total2 = 0;
        max = 0f;
        min = 1f;

        for (Genome g: genomes) {
            float eval = fit.evaluate(g);
            max = Math.max(max, eval);
            min = Math.min(min, eval);
            total += eval;
            total2 += Math.pow(eval, 2);
        }

        mean = total / size;
        sd = (float) Math.sqrt(Math.max(0, total2 / size - Math.pow(mean, 2)));

        float med1 = fit.evaluate(genomes.get(size / 2));
        float med2 = fit.evaluate(genomes.get((size - 1) / 2));
        med = (med1 + med2) / 2;
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
