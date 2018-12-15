package com.tterrag.advent2018.days;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public class Day15 extends Day {

    public static void main(String[] args) {
        new Day15().run();
    }
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//        return ("#######\n" + 
//                "#.G...#\n" + 
//                "#...EG#\n" + 
//                "#.#.#G#\n" + 
//                "#..G#E#\n" + 
//                "#.....#\n" + 
//                "#######").lines();
//    }
//    
//  @Override
//  protected Stream<String> inputStream(BufferedReader r) {
//      return ("#######\n" + 
//              "#G..#E#\n" + 
//              "#E#E.E#\n" + 
//              "#G.##.#\n" + 
//              "#...#E#\n" + 
//              "#...E.#\n" + 
//              "#######").lines();
//  }
    
//  @Override
//  protected Stream<String> inputStream(BufferedReader r) {
//      return ("#######\n" + 
//              "#E..EG#\n" + 
//              "#.#G.E#\n" + 
//              "#E.##E#\n" + 
//              "#G..#.#\n" + 
//              "#..E#.#\n" + 
//              "#######").lines();
//  }
//  @Override
//  protected Stream<String> inputStream(BufferedReader r) {
//      return ("#######\n" + 
//              "#E.G#.#\n" + 
//              "#.#G..#\n" + 
//              "#G.#.G#\n" + 
//              "#G..#.#\n" + 
//              "#...E.#\n" + 
//              "#######").lines();
//  }
//  @Override
//  protected Stream<String> inputStream(BufferedReader r) {
//      return ("#######\n" + 
//              "#.E...#\n" + 
//              "#.#..G#\n" + 
//              "#.###.#\n" + 
//              "#E#G#G#\n" + 
//              "#...#G#\n" + 
//              "#######").lines();
//  }
//    
//  @Override
//  protected Stream<String> inputStream(BufferedReader r) {
//      return ("#########\n" + 
//              "#G......#\n" + 
//              "#.E.#...#\n" + 
//              "#..##..G#\n" + 
//              "#...##..#\n" + 
//              "#...#...#\n" + 
//              "#.G...G.#\n" + 
//              "#.....G.#\n" + 
//              "#########").lines();
//  }
    @RequiredArgsConstructor
    enum State {
        EMPTY('.'),
        WALL('#'),
        ELF('E'),
        GOBLIN('G'),
        ;
        
        final char key;
        
        static State byKey(char c) {
            for (State state : values()) {
                if (state.key == c) {
                    return state;
                }
            }
            throw new IllegalArgumentException();
        }
    }
    
    @RequiredArgsConstructor
    enum Direction {
        UP(0, -1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        DOWN(0, 1),
        ;
        public final int x, y;
    }
    
    @Getter
    @ToString
    private static class Unit implements Comparable<Unit> {
        
        private final State type;
        
        private int hp = 200;
        
        private int x, y;
        
        Unit(State type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }
        
        void setPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        void attack() {
            hp -= 3;
        }
        
        boolean isDead() {
            return hp <= 0;
        }
        
        private static final Comparator<Unit> COMPARATOR =
                Comparator.<Unit>comparingInt(u -> u.y).thenComparingInt(u -> u.x);

        @Override
        public int compareTo(Unit o) {
            return COMPARATOR.compare(this, o);
        }
    }
    
    private final State[][] input = parse(String::chars)
                                   .map(chars -> chars.mapToObj(c -> State.byKey((char) c))
                                                      .toArray(State[]::new))
                                   .toArray(State[][]::new);
    
    private final List<Unit> units = new ArrayList<>();
    
    @Value
    @ToString(includeFieldNames = false)
    @EqualsAndHashCode(of = {"x", "y"})
    private static class Node implements Comparable<Node> {
        int x, y;
        List<Node> path;
        
        Node move(Direction dir) {
            List<Node> newPath = new ArrayList<>(path);
            newPath.add(this);
            return new Node(x + dir.x, y + dir.y, newPath);
        }
        
        private static final Comparator<Node> COMPARATOR =
                Comparator.<Node>comparingInt(p -> p.path.size()).thenComparingInt(p -> p.y).thenComparingInt(p -> p.x);
        
        @Override
        public int compareTo(Node o) {
            return COMPARATOR.compare(this, o);
        }
    }
    
    private Unit getUnit(int x, int y) {
        return units.stream().filter(u -> u.x == x && u.y == y).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    protected Result doParts() {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input.length; x++) {
                State state = input[y][x];
                if (state == State.GOBLIN || state == State.ELF) {
                    units.add(new Unit(state, x, y));
                }
            }
        }
        
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                System.out.print(input[y][x].key);
            }
            System.out.println();
        }
        System.out.println();
        
        int turns = 0;
        main:
        while (true) {
            Collections.sort(units);
            System.out.println(turns + ": " + Arrays.toString(units.stream().map(u -> u.type.key + "(" + u.hp + ")").toArray()));

            for (Unit u : units) {
                
                if (units.stream().filter(un -> !un.isDead()).collect(Collectors.groupingBy(Unit::getType)).size() == 1) {
                    break main;
                }

                if (u.isDead()) {
                    continue;
                }
                Queue<Node> search = new ArrayDeque<>();
                Set<Node> seen = new HashSet<>();
                TreeSet<Node> found = new TreeSet<>();
                search.add(new Node(u.x, u.y, new ArrayList<>()));
                while (!search.isEmpty()) {
                    Node head = search.poll();
                    for (Direction dir : Direction.values()) {
                        Node candidate = head.move(dir);
                        if (seen.add(candidate)) {
                            State stateAt = input[candidate.y][candidate.x];
                            if (stateAt == State.EMPTY) {
                                search.add(candidate);
                            } else if (stateAt != State.WALL && stateAt != u.type && !getUnit(candidate.x, candidate.y).isDead()) {
                                found.add(head);
                            }
                        }
                    }
                }
                Node targetPos = found.pollFirst();
                if (targetPos == null) {
                    continue;
                }
                List<Node> path = targetPos.getPath();
                Node toMove = path.size() > 1 ? path.get(1) : targetPos;
                input[u.y][u.x] = State.EMPTY;
                u.setPos(toMove.x, toMove.y);
                input[u.y][u.x] = u.type;
                
                List<Unit> neighbors = new ArrayList<>();
                for (Direction d : Direction.values()) {
                    State neighbor = input[u.y + d.y][u.x + d.x];
                    if (u.getType() != neighbor && neighbor != State.WALL && neighbor != State.EMPTY) {
                        Unit target = getUnit(u.x + d.x, u.y + d.y);
                        if (!target.isDead()) {
                            neighbors.add(target);
                        }
                    }
                }
                Optional<Unit> target = neighbors.stream().sorted(Comparator.comparingInt(Unit::getHp).thenComparing(Function.identity())).findFirst();
                if (target.isPresent()) {
                    target.get().attack();
                }
            }

            
            Iterator<Unit> iter = units.iterator();
            while (iter.hasNext()) {
                Unit u = iter.next();
                if (u.isDead()) {
                    iter.remove();
                    input[u.y][u.x] = State.EMPTY;
                }
            }

            for (int y = 0; y < input.length; y++) {
                for (int x = 0; x < input[0].length; x++) {
                    System.out.print(input[y][x].key);
                }
                System.out.println();
            }
            System.out.println();
            
            turns++;
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        units.removeIf(Unit::isDead);
        
        Collections.sort(units);
        System.out.println(turns + ": " + Arrays.toString(units.stream().map(u -> u.type.key + "(" + u.hp + ")").toArray()));

        return new Result(units.stream().mapToInt(Unit::getHp).sum() * turns, "");
    }
}
