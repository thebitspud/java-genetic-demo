package io.thebitspud.genetic_demo;

import java.text.DecimalFormat;

/**
 * Represents a fitness function based on a gene matrix
 */
public class FitnessMatrix implements FitnessFunction {
    /** A location/gene matrix filled with values in [0,1] */
    float[][] values;

    /** Creates a new fitness matrix filled with random values */
    public FitnessMatrix() {
        values = new float[Main.GENOME_LENGTH][Main.UNIQUE_GENES];

        for (int i = 0; i < values.length; i++) {
            float max = 0;
            int maxIndex = 0;

            for (int j = 0; j < values[0].length; j++) {
                float value = Main.r.nextFloat();
                // Setting the value of gene j when at location i
                values[i][j] = value;

                if (value > max) {
                    max = value;
                    maxIndex = j;
                }
            }

            // Guarantee every location has a 1.0 gene
            if (Main.GUARANTEE_MAX) values[i][maxIndex] = 1;
        }
    }

    @Override
    public float evaluate(Genome g) {
        float fitness = 0;

        // Sum fitness of genes according to value matrix
        for (int i = 0; i < g.getLength(); i++) {
            fitness += values[i][g.getGene(i) - 65];
        }

        // Normalize fitness to [0,1]
        return fitness / g.getLength();
    }

    /** Returns a string representation of the value matrix */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < values[0].length; i++) {
            s.append("\n").append("[");
            for (int j = 0; j < values.length; j++) {
                if (j != 0) s.append(" ");
                s.append(df.format(values[j][i]));
            }
            s.append("]");
        }

        return s.toString();
    }
}
