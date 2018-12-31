package com.tterrag.advent2018.days;

import java.util.Collection;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;
import lombok.Value;

public class Day23 extends Day {

    public static void main(String[] args) {
        new Day23().run();
    }

    @Value
    private static class Point {
    	int x, y, z;
    	
    	Point move(Axis a, int amt) {
    		return new Point(x + (a.x * amt), y + (a.y * amt), z + (a.z * amt));
    	}
    	
    	int manhattanDistance(Point other) {
    		return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z);
    	}

		public Point sub(Point p) {
			return new Point(x - p.x, y - p.y, z - p.z);
		}

		public int dot(Point p) {
			return (x * p.x) + (y * p.y) + (z * p.z);
		}

		public Point inverse() {
			return new Point(-x, -y, -z);
		}

		public Point mul(double d) {
			return new Point((int) (x * d), (int) (y * d), (int) (z * d));
		}

		public Point add(Point p) {
			return new Point(x + p.x, y + p.y, z + p.z);
		}
    }
    
    @Value
    private static class Vector {
    	Point origin;
    	int length;
    }
    
    @RequiredArgsConstructor
    enum Axis {
    	XP(1, 0, 0),
    	XN(-1, 0, 0),
    	YP(0, 1, 0),
    	YN(0, -1, 0),
    	ZP(0, 0, 1),
    	ZN(0, 0, -1);
    	
    	private final int x, y, z;
    }
    
    @Value
    private static class Drone {
    	Point origin;
    	int range;
    	
    	private static final Pattern PATTERN = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");
    	static Drone parse(String line) {
    		Matcher m = PATTERN.matcher(line);
    		m.matches();
    		return new Drone(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))), Integer.parseInt(m.group(4)));
    	}
    	
    	int distance(Point other) {
    		return origin.manhattanDistance(other);
    	}

    	boolean inRange(Point p) {
    		return distance(p) <= range;
    	}
    	
    	boolean intersects(Area area) {
    		if (area.contains(origin)) {
    			return true;
    		}
    		if (area.center().manhattanDistance(origin) <= range) {
    			return true;
    		}
    		for (Axis a : Axis.values()) {
    			Point extreme = getOrigin().move(a, range);
    			if (area.contains(extreme)) {
    				return true;
    			}
    		}
    		if (area.vertices().filter(this::inRange).count() > 0) {
    			return true;
    		}
    		for (Axis a : Axis.values()) {
    			Point v0 = area.face(a);
    			Point n = new Point(a.x, a.y, a.z);
    			for (Axis a1 : new Axis[] { Axis.YP, Axis.YN }) {
	    			for (Axis a2 : new Axis[] { Axis.XP, Axis.XN, Axis.ZP, Axis.ZN }) {
	        			Point p0 = origin.move(a1, range);
	        			Point w = p0.sub(v0);
	        			if (n.dot(w) != 0) {
		        			Point p1 = origin.move(a2, range);
		        			Point u = p1.sub(p0);
		        			double s1 = (n.inverse().dot(w)) / (double) (n.dot(u));
	        				Point intersect = u.mul(s1).add(p0);
	        				if (intersect.manhattanDistance(origin) <= range && area.contains(intersect)) {
	        					return true;
	        				}
	        			}
	    			}
    			}
    		}
    		return false;
    	}
    }

    @Value
    @RequiredArgsConstructor
    private static class Area {
    	int x1, y1, z1, x2, y2, z2;
    	long intersected;
    	
    	Area(int x1, int y1, int z1, int x2, int y2, int z2, Collection<Drone> drones) {
    		this.x1 = x1;
    		this.y1 = y1;
    		this.z1 = z1;
    		this.x2 = x2;
    		this.y2 = y2;
    		this.z2 = z2;
    		this.intersected = drones.stream().filter(d -> d.intersects(this)).count();
    	}
    	
    	Point center() {
    		int centerX = x1 + ((x2 - x1) / 2);
    		int centerY = y1 + ((y2 - y1) / 2);
    		int centerZ = z1 + ((z2 - z1) / 2);
    		return new Point(centerX, centerY, centerZ);
    	}
    	
    	Stream<Area> bisect(Collection<Drone> drones) {
    		Point c = center();
    		return Stream.of(
    				new Area(x1, y1, z1, c.x, c.y, c.z, drones),
    				new Area(c.x + 1, y1, z1, x2, c.y, c.z, drones),
    				new Area(x1, c.y + 1, z1, c.x, y2, c.z, drones),
    				new Area(c.x + 1, c.y + 1, z1, x2, y2, c.z, drones),
    				new Area(x1, y1, c.z + 1, c.x, c.y, z2, drones),
    				new Area(c.x + 1, y1, c.z + 1, x2, c.y, z2, drones),
    				new Area(x1, c.y + 1, c.z + 1, c.x, y2, z2, drones),
    				new Area(c.x + 1, c.y + 1, c.z + 1, x2, y2, z2, drones));
    	}
    	
    	boolean contains(Point p) {
    		return p.x >= x1 && p.y >= y1 && p.z >= z1 && p.x <= x2 && p.y <= y2 && p.z <= z2;
    	}
    	
    	Stream<Point> vertices() {
    		return Stream.of(
    				new Point(x1, y1, z1),
    				new Point(x2, y1, z1),
    				new Point(x1, y2, z1),
    				new Point(x2, y2, z1),
    				new Point(x1, y1, z2),
    				new Point(x2, y1, z2),
    				new Point(x1, y2, z2),
    				new Point(x2, y2, z2));
    	}
    	
    	boolean isPoint() {
    		return x1 == x2 && y1 == y2 && z1 == z2;
    	}
    	
    	Point face(Axis a) {
    		int x = a.x < 0 ? x1 : x2;
    		int y = a.y < 0 ? y1 : y2;
    		int z = a.z < 0 ? z1 : z2;
    		return new Point(x, y, z);
    	}
    }
    
    @Override
    protected Result doParts() {
    	List<Drone> input = parseList(Drone::parse);
    	
    	Drone strongest = input.stream().max(Comparator.comparingInt(Drone::getRange)).get();
    	
    	IntSummaryStatistics xStats = input.stream().flatMapToInt(d -> IntStream.of(d.getOrigin().x - d.getRange(), d.getOrigin().x + d.getRange())).summaryStatistics();
    	IntSummaryStatistics yStats = input.stream().flatMapToInt(d -> IntStream.of(d.getOrigin().y - d.getRange(), d.getOrigin().y + d.getRange())).summaryStatistics();
    	IntSummaryStatistics zStats = input.stream().flatMapToInt(d -> IntStream.of(d.getOrigin().z - d.getRange(), d.getOrigin().z + d.getRange())).summaryStatistics();
    	
    	PriorityQueue<Area> areas = new PriorityQueue<>(Comparator.comparingLong(Area::getIntersected).reversed());
    	areas.add(new Area(xStats.getMin(), yStats.getMin(), zStats.getMin(), xStats.getMax(), yStats.getMax(), zStats.getMax(), input));
    	Point res = null;
    	
    	while (res == null) {
    		Area containsMost = areas.poll();
    		if (containsMost.isPoint()) {
    			res = new Point(containsMost.getX1(), containsMost.getY1(), containsMost.getZ1());
    		} else {
    			areas.addAll(containsMost.bisect(input).collect(Collectors.toList()));
    		}
    	}
    	
    	final Point p = res;
    	System.out.println(input.stream().filter(d -> d.inRange(p)).count());
    	System.out.println(p);

        return new Result(input.stream().filter(d -> strongest.inRange(d.getOrigin())).count(), res.manhattanDistance(new Point(0, 0, 0)));
    }
}
