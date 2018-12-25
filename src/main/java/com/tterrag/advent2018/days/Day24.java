package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.experimental.NonFinal;

import com.tterrag.advent2018.util.Day;

public class Day24 extends Day {

    public static void main(String[] args) {
        new Day24().run();
    }
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//    	return Arrays.stream(("Immune System:\r\n17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2\r\n989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3\r\n\r\nInfection:\r\n801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1\r\n4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4").split("\r\n"));
//    }
////    
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//    	return Arrays.stream(("Immune System:\r\n2321 units each with 10326 hit points (immune to slashing) with an attack that does 42 fire damage at initiative 4\r\n2899 units each with 9859 hit points with an attack that does 32 slashing damage at initiative 11\r\n4581 units each with 7073 hit points (weak to slashing) with an attack that does 11 radiation damage at initiative 9\r\n5088 units each with 7917 hit points (weak to slashing; immune to bludgeoning, fire, radiation) with an attack that does 15 fire damage at initiative 17\r\n786 units each with 1952 hit points (immune to fire, bludgeoning, slashing, cold) with an attack that does 23 slashing damage at initiative 16\r\n3099 units each with 7097 hit points (weak to bludgeoning) with an attack that does 17 radiation damage at initiative 8\r\n4604 units each with 4901 hit points with an attack that does 8 fire damage at initiative 13\r\n7079 units each with 10328 hit points with an attack that does 14 bludgeoning damage at initiative 18\r\n51 units each with 11243 hit points with an attack that does 1872 cold damage at initiative 15\r\n4910 units each with 5381 hit points (immune to fire; weak to radiation) with an attack that does 10 slashing damage at initiative 19\r\n\r\nInfection:\r\n1758 units each with 23776 hit points with an attack that does 24 radiation damage at initiative 2\r\n4000 units each with 12869 hit points with an attack that does 5 cold damage at initiative 14\r\n2319 units each with 43460 hit points (weak to bludgeoning, cold) with an attack that does 33 radiation damage at initiative 3\r\n1898 units each with 44204 hit points (immune to cold; weak to radiation) with an attack that does 39 radiation damage at initiative 1\r\n2764 units each with 50667 hit points (weak to slashing, radiation) with an attack that does 31 radiation damage at initiative 5\r\n3046 units each with 27907 hit points (immune to radiation, fire) with an attack that does 16 slashing damage at initiative 7\r\n1379 units each with 8469 hit points (immune to cold) with an attack that does 8 cold damage at initiative 20\r\n1824 units each with 25625 hit points (immune to bludgeoning) with an attack that does 23 radiation damage at initiative 6\r\n115 units each with 41114 hit points (immune to fire; weak to slashing, bludgeoning) with an attack that does 686 slashing damage at initiative 10\r\n4054 units each with 51210 hit points (immune to radiation, cold, fire) with an attack that does 22 cold damage at initiative 12").split("\r\n"));
//    }
    
//    @Override
//    protected Stream<String> inputStream(BufferedReader r) {
//    	return Arrays.stream(("Immune System:\r\n933 units each with 3691 hit points with an attack that does 37 cold damage at initiative 15\r\n262 units each with 2029 hit points with an attack that does 77 cold damage at initiative 4\r\n3108 units each with 2902 hit points (weak to bludgeoning; immune to slashing, fire) with an attack that does 7 cold damage at initiative 13\r\n5158 units each with 9372 hit points (weak to bludgeoning; immune to cold) with an attack that does 17 radiation damage at initiative 16\r\n2856 units each with 4797 hit points with an attack that does 16 cold damage at initiative 20\r\n86 units each with 8311 hit points with an attack that does 724 slashing damage at initiative 14\r\n7800 units each with 3616 hit points (immune to radiation, cold, bludgeoning) with an attack that does 4 bludgeoning damage at initiative 7\r\n1374 units each with 8628 hit points (weak to fire, slashing) with an attack that does 61 radiation damage at initiative 1\r\n1661 units each with 4723 hit points with an attack that does 25 slashing damage at initiative 8\r\n1285 units each with 4156 hit points (weak to bludgeoning) with an attack that does 32 fire damage at initiative 18\r\n\r\nInfection:\r\n2618 units each with 29001 hit points (immune to bludgeoning, radiation, cold) with an attack that does 17 radiation damage at initiative 3\r\n31 units each with 20064 hit points (immune to slashing, bludgeoning; weak to radiation) with an attack that does 1082 bludgeoning damage at initiative 10\r\n281 units each with 15311 hit points (weak to fire, cold) with an attack that does 90 slashing damage at initiative 9\r\n1087 units each with 14744 hit points (immune to radiation; weak to cold, fire) with an attack that does 22 fire damage at initiative 12\r\n7810 units each with 48137 hit points (weak to fire, radiation) with an attack that does 10 slashing damage at initiative 5\r\n232 units each with 18762 hit points (weak to radiation, cold) with an attack that does 153 bludgeoning damage at initiative 2\r\n69 units each with 11032 hit points (immune to radiation, slashing, cold, fire) with an attack that does 296 slashing damage at initiative 6\r\n2993 units each with 10747 hit points (immune to slashing; weak to cold) with an attack that does 6 radiation damage at initiative 19\r\n273 units each with 7590 hit points (weak to radiation; immune to slashing, fire) with an attack that does 49 fire damage at initiative 17\r\n2041 units each with 38432 hit points (weak to bludgeoning) with an attack that does 29 cold damage at initiative 11").split("\r\n"));
//    }
    
    @Value
    private static class Group implements Comparable<Group> {
    	@NonFinal
    	int units; 
    	int hp;
    	Set<String> immune, weak;
    	int attack;
    	String attackType;
    	int initiative;
    	
    	long effectivePower() {
    		return units * attack;
    	}
    	
    	long damageVS(Group defending) {
    		if (defending.immune.contains(attackType)) {
    			return 0;
    		}
    		long attack = effectivePower();
    		if (defending.weak.contains(attackType)) {
    			attack *= 2;
    		}
    		return attack;
    	}
    	
    	boolean attack(Group attacking) {
    		if (attacking.getUnits() <= 0) {
    			return true;
    		}
    		long attack = attacking.damageVS(this);
    		while (attack >= hp) {
    			units--;
    			attack -= hp;
    		}
    		return units > 0;
    	}
    	
    	private static final Comparator<Group> COMPARATOR =
    			Comparator.comparingLong(Group::effectivePower)
    					  .thenComparingInt(Group::getInitiative)
    					  .reversed();
    	
    	@Override
    	public int compareTo(Group o) {
    		return COMPARATOR.compare(this, o);
    	}
    	
    	private static final Pattern PATTERN = Pattern.compile(
    			"^(\\d+) units each with (\\d+) hit points \\(?(.+?)?\\)?\\s?with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)$");
    	
    	private static final Pattern SPECIAL = Pattern.compile("(weak|immune) to (.+?)([;)]|$)");
    	
    	static Group parse(int boost, String line) {
    		Matcher m = PATTERN.matcher(line);
    		m.matches();
    		int units = Integer.parseInt(m.group(1));
    		int hp = Integer.parseInt(m.group(2));
    		String special = m.group(3);
    		Set<String> immune = new HashSet<>();
    		Set<String> weak = new HashSet<>();
    		if (special != null) {
    			String[] specials = special.split("; ");
    			for (String s : specials) {
    				Matcher m2 = SPECIAL.matcher(s);
    				m2.matches();
    				String type = m2.group(1);
    				Set<String> damage = Arrays.stream(m2.group(2).split(", ")).collect(Collectors.toSet());
    				if (type.equals("immune")) {
    					immune = damage;
    				} else {
    					weak = damage;
    				}
    			}
    		}
    		int attack = Integer.parseInt(m.group(4)) + boost;
    		String attackType = m.group(5);
    		int initiative = Integer.parseInt(m.group(6));
    		return new Group(units, hp, immune, weak, attack, attackType, initiative);
    	}
    }
    
    private void getBestTarget(Group attacker, List<Group> potentials, Map<Group, Group> targets) {
    	potentials.stream()
    			.filter(g -> !targets.values().contains(g))
    			.filter(g -> attacker.damageVS(g) > 0)
    			.sorted(Comparator.comparing(attacker::damageVS)
    						      .thenComparingLong(Group::effectivePower)
    						      .thenComparingInt(Group::getInitiative)
    						      .reversed())
    		    .findFirst()
    			.ifPresent(g -> targets.put(attacker, g));
    }
    
    @Value
    private static class SimResult {
    	boolean won;
    	List<Group> groups;
    }
    
    @Override
    protected Object part1() {
    	return simulate(0).getGroups().stream().mapToInt(Group::getUnits).sum();
    }
    
    @Override
    protected Object part2() {
    	int boost = 0;
    	while (true) {
    		SimResult res = simulate(boost++);
    		if (res.won) {
    			return res.getGroups().stream().mapToInt(Group::getUnits).sum();
    		}
    	}
    }

    private SimResult simulate(int boost) {
    	List<Group> immuneSystem = new ArrayList<>();
    	List<Group> infection = new ArrayList<>();
    	List<Group> current = null;
    	
    	for (String s : linesList()) {
    		if (s.equals("Immune System:")) {
    			current = immuneSystem;
    		} else if (s.equals("Infection:")) {
    			current = infection;
    		} else if (!s.isEmpty()) {
    			current.add(Group.parse(current == immuneSystem ? boost : 0, s));
    		}
    	}
    	
    	while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
    		Collections.sort(immuneSystem);
    		Collections.sort(infection);
    		
    		NavigableMap<Group, Group> targets = new TreeMap<>(Comparator.comparingInt(Group::getInitiative).reversed());
    		for (Group g : immuneSystem) {
    			getBestTarget(g, infection, targets);
    		}
    		for (Group g : infection) {
    			getBestTarget(g, immuneSystem, targets);
    		}
    		
    		if (targets.entrySet().stream()
    				.filter(e -> e.getKey().damageVS(e.getValue()) > e.getValue().getHp())
    				.count() == 0) {
    			return new SimResult(false, null);
    		}
    		
    		Iterator<Entry<Group, Group>> iter = targets.entrySet().iterator();
    		while (iter.hasNext()) {
    			Entry<Group, Group> e = iter.next();
    			if (!e.getValue().attack(e.getKey())) {
    				immuneSystem.remove(e.getValue());
    				infection.remove(e.getValue());
    			}
    		}
    	}
    	
    	List<Group> winner = immuneSystem.isEmpty() ? infection : immuneSystem;

        return new SimResult(winner == immuneSystem, winner);
    }
}
