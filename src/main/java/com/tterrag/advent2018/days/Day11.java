package com.tterrag.advent2018.days;

import java.io.BufferedReader;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

public class Day11 extends Day {

    public static void main(String[] args) {
        new Day11().run();
    }
    
    @Override
    protected Stream<String> inputStream(BufferedReader r) {
        return Stream.empty();
    }
    
    private final int serial = 6548;
    
    private int powerLevel(int x, int y) {
        int rack = x + 10;
        int power = rack * y;
        power += serial;
        power *= rack;
        power = (power / 100) % 10;
        return power - 5;
    }
    
    @Override
    protected Object part1() {
        int maxPower = 0;
        int maxX = 0, maxY = 0;
        for (int x = 1; x <= 298; x++) {
            for (int y = 1; y <= 298; y++) {
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        sum += powerLevel(x + i, y + j);
                    }
                }
                if (sum > maxPower) {
                    maxPower = sum;
                    maxX = x;
                    maxY = y;
                }
            }
        }
        return maxX + "," + maxY;
    }
    
    @Override
    protected Object part2() {
        int maxPower = 0;
        int maxX = 0, maxY = 0;
        int maxSize = 0;
        for (int x = 1; x <= 300; x++) {
            for (int y = 1; y <= 300; y++) {
                for (int size = 1; size <= 300 - (Math.max(x, y) - 1); size++) {
                    int sum = 0;
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            sum += powerLevel(x + i, y + j);
                        }
                    }
                    if (sum > maxPower) {
                        maxPower = sum;
                        maxX = x;
                        maxY = y;
                        maxSize = size;
                    }
                }
            }
        }
        return maxX + "," + maxY + "," + maxSize;
    }
}
