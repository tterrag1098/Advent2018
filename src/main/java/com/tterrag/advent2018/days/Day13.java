package com.tterrag.advent2018.days;

import static com.tterrag.advent2018.days.Day13.Direction.DOWN;
import static com.tterrag.advent2018.days.Day13.Direction.LEFT;
import static com.tterrag.advent2018.days.Day13.Direction.RIGHT;
import static com.tterrag.advent2018.days.Day13.Direction.UP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class Day13 extends Day {

    public static void main(String[] args) {
        new Day13().run();
    }
    
    @RequiredArgsConstructor
    enum Direction {
        UP('^', 0, -1),
        LEFT('<', -1, 0),
        DOWN('v', 0, 1),
        RIGHT('>', 1, 0);
        
        private final char key;
        private final int x, y;
        
        static Direction parse(char key) {
            for (Direction d : values()) {
                if (d.key == key) {
                    return d;
                }
            }
            return null;
        }
    }
    
    enum Track {
        NONE(' ', null),
        VERTICAL('|', UP, DOWN),
        HORIZONTAL('-', LEFT, RIGHT),
        TURN_UR('\\', UP, RIGHT),
        TURN_DR('/', DOWN, RIGHT),
        TURN_UL('/', UP, LEFT),
        TURN_DL('\\', DOWN, LEFT),
        INTERSECTION('+', UP, DOWN, LEFT, RIGHT);
        
        private final char key;
        final EnumSet<Direction> directions;
        
        private Track(char key, Direction dir, Direction... rest) {
            this.key = key;
            this.directions = dir == null ? EnumSet.noneOf(Direction.class) : EnumSet.of(dir, rest);
        }
        
        static Track parse(Track prev, char key) {
            for (Track t : values()) {
                if (key == t.key) {
                    switch(t) {
                    case NONE:
                    case VERTICAL:
                    case HORIZONTAL:
                    case INTERSECTION:
                        return t;
                    case TURN_UL:
                    case TURN_DL:
                        if (prev.directions.contains(RIGHT)) {
                            return t;
                        }
                        break;
                    case TURN_UR:
                    case TURN_DR:
                        if (!prev.directions.contains(RIGHT)) {
                            return t;
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException(prev + "/" + key);
        }
    }
    
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString(includeFieldNames = false)
    private static class Point {
        int x, y;
        
        void move(Direction dir) {
            x += dir.x;
            y += dir.y;
        }
    }
    
    @Getter
    @AllArgsConstructor
    @ToString
    private static class Cart {
        @Setter
        private Direction dir;
        private int turns;
        private final Point pos;
        @Setter
        private boolean crashed;
        
        void turned() {
            turns++;
        }
    }

    @Override
    protected Result doParts() {
        String[] lines = linesArray();
        
        Track[][] tracks = new Track[lines.length][lines[0].length()];
        List<Cart> carts = new ArrayList<>();
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            Track prev = Track.NONE;
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char c = chars[x];
                Direction cart = Direction.parse(c);
                if (cart != null) {
                    carts.add(new Cart(cart, 0, new Point(x, y), false));
                    tracks[y][x] = cart.x == 0 ? Track.VERTICAL : Track.HORIZONTAL;
                } else {
                    tracks[y][x] = Track.parse(prev, c);
                    prev = tracks[y][x];
                }
            }
        }
        
        Point part1 = null;
        while (carts.size() > 1) {
            carts.sort(Comparator.<Cart>comparingInt(c -> c.getPos().getY()).thenComparingInt(c -> c.getPos().getX()));
            for (Cart cart : carts) {
                if (cart.isCrashed()) {
                    continue;
                }
                Point pos = cart.getPos();
                Track track = tracks[pos.getY()][pos.getX()];
                if (track == null) {
                    track = Track.NONE;
                }
                if (track == Track.INTERSECTION) {
                    Direction left = Direction.values()[(cart.getDir().ordinal() + 1) % 4];
                    Direction right = Direction.values()[((cart.getDir().ordinal() + 4) - 1) % 4];
                    int choice = cart.getTurns() % 3;
                    Direction dir = cart.getDir();
                    if (choice == 0) {
                        dir = left;
                    } else if (choice == 2) {
                        dir = right;
                    }
                    pos.move(dir);
                    cart.setDir(dir);
                    cart.turned();
                } else if (track.directions.contains(cart.getDir())) {
                    pos.move(cart.getDir());
                } else {
                    Direction dir = Direction.values()[((cart.getDir().ordinal() + 4) - 1) % 4];
                    if (!track.directions.contains(dir)) {
                        dir = Direction.values()[(cart.getDir().ordinal() + 1) % 4];
                    }
                    pos.move(dir);
                    cart.setDir(dir);
                }
                List<Cart> colliding = carts.stream().filter(c -> c.getPos().equals(cart.getPos())).collect(Collectors.toList());
                if (colliding.size() > 1) {
                    if (part1 == null) {
                        part1 = colliding.get(0).getPos();
                    }
                    colliding.forEach(c -> c.setCrashed(true));
                }
            }
            carts.removeIf(Cart::isCrashed);
        }
        return new Result(part1, carts.get(0).getPos());
    }
}
