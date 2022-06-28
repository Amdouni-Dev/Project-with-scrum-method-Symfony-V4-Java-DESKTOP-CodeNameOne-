package com.espritx.client.utils;

/**
 * I have never seen such an asshole design.
 */
public class ComboBoxPopulation {
    public Integer[] ids; // because a compiler gets fucking confused :) Ha Walid rahi tla mchet...
    public String[] labels;

    public ComboBoxPopulation(Integer[] ids, String[] labels) {
        this.ids = ids;
        this.labels = labels;
    }
}
