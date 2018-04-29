package com.drpicox.fishingLagoon;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.DoubleSupplier;

public class TestPerf {

    @Test
    public void overhead() {
        measure("overhead", () -> 3);
    }

    @Test
    public void list_copy() {
        var list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        measure("list_copy", () -> {
            var copy = new ArrayList<>(list);
            return copy.size();
        });
    }

    @Test
    public void array_list_copy() {
        var list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        measure("array_list_copy", () -> {
            var copy = new ArrayList<>(list);
            return copy.size();
        });
    }

    @Test
    public void array_list_unmodifiagle() {
        var list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        measure("array_list_unmodifiagle", () -> {
            var copy = Collections.unmodifiableList(list);
            return copy.size();
        });
    }


    public void measure(String name, DoubleSupplier action) {
        double result = 0;
        int times = 0;
        long startNanos = System.currentTimeMillis();
        long elapsedNanos;
        do {
            result += action.getAsDouble();
            times += 1;
            elapsedNanos = System.currentTimeMillis() - startNanos;
        } while (elapsedNanos < 1000);

        DecimalFormat df = new DecimalFormat("#.###");
        double actionTime = (elapsedNanos * 1000) / (double)times;
        System.out.println(name + " " + df.format(actionTime) + "ns" + " times:" + times + " (" + elapsedNanos + "ms) /" + (result % 10) + "/");
    }
}
