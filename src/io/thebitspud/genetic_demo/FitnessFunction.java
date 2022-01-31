package io.thebitspud.genetic_demo;

/**
 * Represents a fitness function for the genetic algorithm
 */
public interface FitnessFunction {
    /**
     * Returns the normalized fitness of the specified genome
     * @return float in [0,1]
     */
    float evaluate(Genome g);
}
