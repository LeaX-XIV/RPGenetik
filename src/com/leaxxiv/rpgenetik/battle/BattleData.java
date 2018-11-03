package com.leaxxiv.rpgenetik.battle;

import com.leaxxiv.rpgenetik.Fighter;

public class BattleData {
	
	private double value;
	private Action action;
	private Fighter fighter;
	
	public BattleData(double value, Action action, Fighter fighter) {
		this.value = value;
		this.action = action;
		this.fighter = fighter;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public Fighter getFighter() {
		return this.fighter;
	}
	
}