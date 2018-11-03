package com.leaxxiv.rpgenetik;

import java.util.EnumMap;

import com.leaxxiv.genetics.DNA;
import com.leaxxiv.genetics.DickStuckInFan;

import static com.leaxxiv.rpgenetik.Stat.*;
import static java.lang.Math.*;

public abstract class Fighter extends DickStuckInFan implements Cloneable {
	
	public static final int NUMBER_STATS_GENES = Stat.values().length;
	public static final double STATISTICS_GROWTH = 10.0;
	public static final double HEAL_PERCENTAGE = 0.75;
	
	private String name;
	public EnumMap<Stat, Integer> baseStats;
	public EnumMap<Stat, Integer> trainedStats;
	private int level;
	private double permaDamage;
	private double healDamage;
	
	protected Fighter() {
		setStatsDna();
		setLevel(1);
		setStats();
		setPermaDamage(0);
		setHealDamage(0);
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setStatsDna() {
		this.dna = new DNA(NUMBER_STATS_GENES, 0.25, 1.25);
	}
	
	public void setStatsDna(DNA dna) {
		this.dna = dna;
		setStats();
	}
	
	protected void setStats() {
		baseStats = new EnumMap<>(Stat.class);
		trainedStats = new EnumMap<>(Stat.class);
		setBaseStat(HP, dna.getGene(0));
		setBaseStat(ATK, dna.getGene(1));
		setBaseStat(DEF, dna.getGene(2));
		setBaseStat(SPD, dna.getGene(3));
		setBaseStat(CUR, dna.getGene(4));
		trainedStats.put(HP, 0);
		trainedStats.put(ATK, 0);
		trainedStats.put(DEF, 0);
		trainedStats.put(SPD, 0);
		trainedStats.put(CUR, 0);
	}
	
	protected void setBaseStat(Stat statistic, double gene) {
		int value = (int) (STATISTICS_GROWTH * getLevel() * gene);
		this.baseStats.put(statistic, value);
	}
	
	protected void trainStat(Stat statistic, int trainingValue) {
		this.trainedStats.put(statistic, this.trainedStats.get(statistic) + trainingValue);
	}
	
	protected void setLevel(int level) {
		this.level = level;
	}
	
	protected void setPermaDamage(double permaDamage) {
		this.permaDamage = permaDamage;
	}
	
	protected void setHealDamage(double healDamage) {
		this.healDamage = healDamage;
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public int get(Stat statistic) {
		return this.baseStats.get(statistic) + this.trainedStats.get(statistic);
	}

	public double getPermaDamage() {
		return this.permaDamage;
	}

	public double getHealDamage() {
		if(isDead()) {
			setPermaDamage(get(HP));
			setHealDamage(0);
		}
		return this.healDamage;
	}
	
	public double getHPRemaining() {
		return max(0.0, (double)(get(HP)) - permaDamage - healDamage);
	}
	
	public void levelUp(Stat[] uppees) {
		setLevel(getLevel() + 1);
		for(Stat stat : uppees) {
			trainStat(stat, 1);
			setBaseStat(stat, dna.getGene(stat.ordinal()));
		}
	}
	
	public double getAttackDamage() {
		double randomFactor = random() * 0.25 + 0.75;
		return randomFactor * get(ATK);
	} 
	
	public double getBlockedDamage() {
		double randomFactor = random() * 0.25 + 0.5;
		return randomFactor * get(DEF);
	}
	
	public double getBlockedDamageEnhanced() {
		double randomFactor = random() * 0.25 + 0.75;
		return randomFactor * get(DEF);
	}
	
	public void heal() {
		int cur = get(CUR);
		double randomFactor = random() * 0.25 + 0.75;
		double healed = randomFactor * cur;
		setHealDamage(max(0.0, getHealDamage() - healed));
	}
	
	public void takeDamage(double permanent, double healable) {
		setPermaDamage(permanent + getPermaDamage());
		setHealDamage(healable + getHealDamage());
	}
	
	public boolean isDead() {
		return getHPRemaining() <= 0;
	}

}
