package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tterrag.advent2018.util.Day;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

public class Day17 extends Day {

    public static void main(String[] args) {
        new Day17().run();
    }
    
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
        while (!flowHeads.isEmpty()) {
        	Collections.sort(flowHeads, Comparator.comparingInt(Node::getY).reversed());
        	ListIterator<Node> iter = flowHeads.listIterator();
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
        }
        
        return new Result(flowed.size() + filled.size(), filled.size());
    }
}
