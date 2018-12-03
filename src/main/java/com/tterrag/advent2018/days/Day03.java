package com.tterrag.advent2018.days;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tterrag.advent2018.util.Day;

import lombok.Value;

public class Day03 extends Day {

    public static void main(String[] args) {
        new Day03().run();
    }

    @Value
    private static class Claim {
        int id;
        int x, y, w, h;
        
        void fill(long[][] grid) {
            for (int i = x; i < x + w && i < grid.length; i++) {
                for (int j = y; j < y + h && j < grid[i].length; j++) {
                    grid[i][j]++;
                }
            }
        }
        
        boolean collided(long[][] grid) {
            for (int i = x; i < x + w && i < grid.length; i++) {
                for (int j = y; j < y + h && j < grid[i].length; j++) {
                    if (grid[i][j] > 1) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        boolean collides(Claim other) {
            return inside(other.getX(), other.getY())
                || inside(other.getX() + other.getW() - 1, other.getY())
                || inside(other.getX() + other.getW() - 1, other.getY() + other.getH() - 1)
                || inside(other.getX(), other.getY() + other.getH() - 1);
        }
        
        boolean inside(int x, int y) {
            return x >= this.x && x < this.x + w && y >= this.y && y < this.y + h;
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
    
    private long[][] grid = new long[2000][2000];
    
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
        
        for (int i = 0; i < claims.size(); i++) {
            if (!claims.get(i).collided(grid)) {
                return new Result(part1, claims.get(i).getId());
            }
        }
    
        return null;
    }
}
