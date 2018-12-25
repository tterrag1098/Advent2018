package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import com.tterrag.advent2018.util.Day;

public class Day17 extends Day {

    public static void main(String[] args) {
        new Day17().run();
    }
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//        return Arrays.stream(("x=499, y=2..7\r\n" + 
//                "y=7, x=495..501\r\n" + 
//                "x=501, y=3..7\r\n" + 
//                "x=498, y=2..4\r\n" + 
//                "x=506, y=1..2\r\n" + 
//                "x=498, y=10..13\r\n" + 
//                "x=504, y=10..13\r\n" + 
//                "y=13, x=498..504").split("\r\n"));
//    }
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//        return Arrays.stream((
//                "x=498, y=2..4\r\n" + 
//                "x=506, y=1..2\r\n" + 
//                "x=498, y=10..13\r\n" + 
//                "x=504, y=10..13\r\n" + 
//                "y=13, x=498..504").split("\r\n"));
//    }
    
    @Value
    private static class Clay {
        int minX, maxX, minY, maxY;
        
        private static final Pattern X = Pattern.compile("x=(\\d+)(?:\\.\\.(\\d+))?");
        private static final Pattern Y = Pattern.compile("y=(\\d+)(?:\\.\\.(\\d+))?");

        static Clay parse(String line) {
            Matcher m = X.matcher(line);
            m.find();
            int minX = Integer.parseInt(m.group(1));
            int maxX = m.group(2) != null ? Integer.parseInt(m.group(2)) : minX;
            m = Y.matcher(line);
            m.find();
            int minY = Integer.parseInt(m.group(1));
            int maxY = m.group(2) != null ? Integer.parseInt(m.group(2)) : minY;
            return new Clay(minX, maxX, minY, maxY);
        }
    }
    
    @Value
    @ToString(of = {"x", "y"}, includeFieldNames = false)
    @EqualsAndHashCode(of = {"x", "y"})
    private static class Node {
    	Node parent;
        int x, y;
        
        Node move(int x, int y) {
            return new Node(this, this.x + x, this.y + y);
        }
    }
    
    private boolean[][] clayMap;
    
    private boolean isClay(Node p) {
        return isClay(p.getX(), p.getY());
    }
    
    private boolean isClay(int x, int y) {
        if (x < 0 || x >= clayMap[0].length || y < 0 || y >= clayMap.length) {
            return false;
        }
        return clayMap[y][x];
    }
    
    private void printMap(Collection<Node> bounds, Predicate<Node> flowing, Predicate<Node> filled) {
    	IntSummaryStatistics xStats = bounds.stream().mapToInt(Node::getX).summaryStatistics();
    	IntSummaryStatistics yStats = bounds.stream().mapToInt(Node::getY).summaryStatistics();
        for (int y = Math.max(0, yStats.getMin() - 10); y <= yStats.getMax() + 10 && y < clayMap.length; y++) {
            for (int x = xStats.getMin() - 10; x <= xStats.getMax() + 10 && x < clayMap[0].length; x++) {
                System.out.print(isClay(x, y) ? '#' : filled.test(new Node(null, x, y)) ? '~' : flowing.test(new Node(null, x, y)) ? '|' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
    
    @Override
    protected Result doParts() {
        List<Clay> input = parseList(Clay::parse);
        IntSummaryStatistics xStats = input.stream().mapToInt(Clay::getMaxX).summaryStatistics();
        IntSummaryStatistics yStats = input.stream().mapToInt(Clay::getMinY).summaryStatistics();
        
        clayMap = new boolean[yStats.getMax() + 1][xStats.getMax() + 2];
        for (Clay clay : input) {
            for (int x = clay.getMinX(); x <= clay.getMaxX(); x++) {
                for (int y = clay.getMinY(); y <= clay.getMaxY(); y++) {
                    clayMap[y][x] = true;
                }
            }
        }

        int minY = yStats.getMin();
        
        int springX = 500;
        
        Set<Node> flowed = new HashSet<>();
        Set<Node> filled = new HashSet<>();
        List<Node> flowHeads = new ArrayList<>();
        flowHeads.add(new Node(null, springX, minY));
//        flowHeads.add(new Node(null, springX + 3, minY));
        while (!flowHeads.isEmpty()) {
        	System.out.println(flowHeads.size());
        	Collections.sort(flowHeads, Comparator.comparingInt(Node::getY).reversed());
        	ListIterator<Node> iter = flowHeads.listIterator();
//    		printMap(flowHeads, flowed::contains, filled::contains);
        	while (iter.hasNext()) {
        		Node head = iter.next();
        		if (head.getY() > yStats.getMax()) {
        			iter.remove();
        			continue;
        		}
        		flowed.add(head);
        		Node down = head.move(0, 1);
        		if (!isClay(down) && !filled.contains(down)) {
        			if (flowed.contains(down)) {
        				iter.remove();
        			} else {
        				iter.set(down);
        			}
        		} else {
        			int minX = 0;
        			int maxX = 0;
        			boolean fillL = false;
        			boolean fillR = false;
        			for (int xL = 0;; xL--) {
        				Node n = head.move(xL, 0);
        				Node left = n.move(-1, 0);
        				Node below = n.move(0, 1);
        				if (isClay(left)) {
        					minX = xL;
        					fillL = true;
        					break;
        				}
        				if (!isClay(below) && !filled.contains(below)) {
        					minX = xL;
        					break;
        				}
        			}
        			for (int xR = 0;; xR++) {
        				Node n = head.move(xR, 0);
        				Node right = n.move(1, 0);
        				Node below = n.move(0, 1);
        				if (isClay(right)) {
        					maxX = xR;
        					fillR = true;
        					break;
        				}
        				if (!isClay(below) && !filled.contains(below)) {
        					maxX = xR;
        					break;
        				}
        			}
        			if (fillL && fillR) {
        				for (int i = minX; i <= maxX; i++) {
        					Node n = head.move(i, 0);
        					filled.add(n);
        					flowed.remove(n);
        				}
        				iter.set(head.getParent());
        			} else {
        				iter.remove();
        				if (!fillL) {
        					Node n = head.move(minX, 0);
        					if (!flowed.contains(n)) {
        						iter.add(n);
        					}
        				}
        				if (!fillR) {
        					Node n = head.move(maxX, 0);
        					if (!flowed.contains(n)) {
        						iter.add(n);
        					}
        				}
        				for (int i = minX; i <= maxX; i++) {
        					flowed.add(head.move(i, 0));
        				}
        			}
        		}
        	}
        	
//        	try {
//				Thread.sleep(150);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        
        return new Result(flowed.size() + filled.size(), filled.size());
    }
}
