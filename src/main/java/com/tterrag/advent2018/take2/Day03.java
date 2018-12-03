package com.tterrag.advent2018.take2;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

import lombok.Value;

public class Day03 extends Day {

    public static void main(String[] args) {
        new Day03().run();
    }

    @Value
    private static class Claim {
        int id, x, y, w, h;
        
        Stream<int[]> indices() {
            return IntStream.range(x, x + w)
                            .mapToObj(i -> IntStream.range(y, y + h)
                                                    .mapToObj(j -> new int[] {i, j}))
                            .flatMap(Function.identity());
        }
        
        void fill(int[][] grid) {
            indices().forEach(xy -> grid[xy[0]][xy[1]]++);
        }
        
        boolean collided(int[][] grid) {
            return indices().anyMatch(xy -> grid[xy[0]][xy[1]] > 1);
        }
        
        private static Pattern PATTERN = Pattern.compile("#(\\d+)\\s@\\s(\\d+),(\\d+):\\s(\\d+)x(\\d+)");
        
        static Claim parse(String s) {
            Matcher m = PATTERN.matcher(s);
            if (!m.matches()) {
                throw new IllegalArgumentException();
            }
            return new Claim(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
        }
    }
    
    private int[][] grid = new int[1000][1000];
    
    @Override
    protected Result doParts() {
        List<Claim> claims = parseList(Claim::parse);
        
        claims.forEach(c -> c.fill(grid));
        
        int part1 = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 1) {
                    part1++;
                }
            }
        }
        
        return new Result(part1, claims.stream()
                                       .filter(c -> !c.collided(grid))
                                       .findFirst()
                                       .get().getId());
    }
}
