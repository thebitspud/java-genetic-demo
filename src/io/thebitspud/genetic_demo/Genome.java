package io.thebitspud.genetic_demo;

/**
 * Represents a candidate solution
 */
public class Genome {
    /** Represents solution parameters as genetic information */
    private char[] genes;
    private int generation;

    /** Creates a new genome of specified length with random genes */
    public Genome(int length, int generation) {
        this.generation = generation;
        genes = new char[length];

        for (int i = 0; i < length; i++) {
            // Generate random number, then convert to char code
            genes[i] = (char) (Main.r.nextInt(Main.UNIQUE_GENES) + 65);
        }
    }

    /** Creates a new genome via cloning, then mutating */
    public Genome(Genome parent, int generation) {
        this.generation = generation;
        genes = parent.genes.clone();
        mutate();
    }

    /** Creates a new genome via crossover, then mutating */
    public Genome(Genome p1, Genome p2, int generation) {
        this.generation = generation;
        genes = new char[p1.getLength()];

        // Random uniform crossover
        for (int i = 0; i < getLength(); i++) {
            genes[i] = Main.r.nextBoolean() ? p1.getGene(i) : p2.getGene(i);
        }

        mutate();
    }

    /** Randomly performs substitution mutations on genes */
    public void mutate() {
        if (Main.UNIQUE_GENES < 2) return;

        // Probabilities can be modeled using a binomial distribution
        for (int i = 0; i < getLength(); i++) {
            if (Main.r.nextFloat() < Main.MUTATION_RATE) {
                char newGene;

                // Preventing identical substitutions from occurring
                do newGene = (char) (Main.r.nextInt(Main.UNIQUE_GENES) + 65);
                while (newGene == genes[i]);

                genes[i] = newGene;
            }
        }
    }

    /** Returns the genome's length (number of gene locations) */
    public int getLength() {
        return genes.length;
    }

    /** Returns the gene value at the specified index */
    public char getGene(int index) {
        return genes[index];
    }

    /** Returns the generation in which this genome was created */
    public int getGeneration() {
        return generation;
    }

    /** Returns a string representation of the gene array */
    @Override
    public String toString() {
        return String.valueOf(genes);
    }
}
