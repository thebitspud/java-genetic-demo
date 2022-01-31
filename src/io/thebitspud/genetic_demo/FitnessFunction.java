package io.thebitspud.genetic_demo;

/**
 * Represents the genetic algorithm's fitness function
 */
public interface FitnessFunction {
    /**
     * Returns the normalized fitness of the specified genome
     * @return float in [0,1]
     */
    float evaluate(Genome g);
}
