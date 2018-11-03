package com.leaxxiv.rpgenetik.battle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.leaxxiv.rpgenetik.Fighter;
import com.leaxxiv.rpgenetik.Stat;

import static java.lang.Math.*;
import static com.leaxxiv.rpgenetik.battle.Action.*;

public class Battle {
	
	private final static double PERMANENT_RATIO = 0.5;
	private final static double HEALABLE_RATIO = 0.5;
	private final static double PERMANENT_RATIO_GUARDING = 0.75;
	private final static double HEALABLE_RATIO_GUARDING = 0.25;
	
	private int turn;
	private Fighter p1;
	private List<Action> actions1;
	private Fighter p2;
	private List<Action> actions2;
	
	private Deque<Action> actionQueue;
	private Deque<Fighter> orderQueue;
	
	private boolean guarding = false;
	
	public Battle(Fighter p1, Fighter p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.turn = 0;
		this.actions1 = new ArrayList<>();
		this.actions2 = new ArrayList<>();
		
		this.actionQueue = new ArrayDeque<>();
		this.orderQueue = new ArrayDeque<>();
	}
	
	public int enqueueActions(Action action1, Action action2) {
		if(orderQueue.size() > 0) {
			throw new IllegalStateException("Some actions are already queued.");
		}
		
		turn++;
		guarding = false;
		actions1.add(action1);
		actions2.add(action2);
		
		if(action1.equals(GUARD)) {
			actionQueue.push(action1);
			orderQueue.push(p1);
			orderQueue.push(p2);
			actionQueue.push(action2);
			orderQueue.push(p2);
			orderQueue.push(p1);
			return 1;
		} else if(action2.equals(GUARD)) {
			actionQueue.push(action2);
			orderQueue.push(p2);
			orderQueue.push(p1);
			actionQueue.push(action1);
			orderQueue.push(p1);
			orderQueue.push(p2);
			return 2;
		} else {
			if(p1.get(Stat.SPD) >= p2.get(Stat.SPD)) {
				actionQueue.push(action1);
				orderQueue.push(p1);
				orderQueue.push(p2);
				actionQueue.push(action2);
				orderQueue.push(p2);
				orderQueue.push(p1);
				return 1;
			} else {
				actionQueue.push(action2);
				orderQueue.push(p2);
				orderQueue.push(p1);
				actionQueue.push(action1);
				orderQueue.push(p1);
				orderQueue.push(p2);
				return 2;
			}
		}
	}
	
	public BattleData dequeAction() {
		if(orderQueue.size() <= 0) {
			throw new IllegalStateException("No actions queued.");
		}
		
		BattleData bd = null;
		double v = 0;
		Action a = actionQueue.removeLast();
		Fighter f = orderQueue.removeLast();
		Fighter other = orderQueue.removeLast();
		
		switch(a) {
		case HEAL: {
			v = f.getHPRemaining();
			f.heal();
			v -= f.getHPRemaining();
			bd = new BattleData(v, a, f);
		} break;
		case GUARD: {
			guarding = true;
			bd = new BattleData(v, a, f);
		} break;
		case ATTACK: {
			v = f.getAttackDamage();
			double permanentRatio;
			double healableRatio;
			if(guarding) {
				permanentRatio = PERMANENT_RATIO_GUARDING;
				healableRatio = HEALABLE_RATIO_GUARDING;
				v = max(1, v - other.getBlockedDamageEnhanced());
			} else {
				permanentRatio = PERMANENT_RATIO;
				healableRatio = HEALABLE_RATIO;
				v = max(1, v - other.getBlockedDamage());
			}
			other.takeDamage(v * permanentRatio, v * healableRatio);
			bd = new BattleData(v, a, f);
		} break;
		case RUN_AWAY: {
		} break;
		}
		
		
		
		return bd;
	}
	
	public Fighter getFighter1() {
		return p1;
	}
	
	public Fighter getFighter2() {
		return p2;
	}
	
	public int getTurn() {
		return this.turn;
	}
	
	public Action getLastAction1() {
		return actions1.get(actions1.size() - 1);
	}
	
	public Action getLastAction2() {
		return actions2.get(actions2.size() - 1);
	}
	
	public Result getResult() {
		if(p1.isDead() && p2.isDead()) {
			return Result.DRAW;
		}
		if(p1.isDead() && !p2.isDead()) {
			return Result.LOSE;
		}
		if(!p1.isDead() && p2.isDead()) {
			return Result.WIN;
		}
		return null;
	}
	
	public boolean isFinished() {
		return p1.isDead() || p2.isDead();
	}
	
	
	public void assignFitness() {
		p1.forceFitness(pow(p1.getHPRemaining() / p1.get(Stat.HP) - p2.getHPRemaining() / p2.get(Stat.HP) + 1, 2) / 4);
		p2.forceFitness(pow(p2.getHPRemaining() / p2.get(Stat.HP) - p1.getHPRemaining() / p1.get(Stat.HP) + 1, 2) / 4);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public Result runTurn() {
//		System.out.println("\n\n------------------ TURN " + turn + " ------------------\n\n");
//		System.out.println(p1.getName() + ": " + p1.getHPRemaining() + "/" + p1.get(Stat.HP));
//		System.out.println(p2.getName() + ": " + p2.getHPRemaining() + "/" + p2.get(Stat.HP));
//		
//		
//		double lastAction1 = turn == 0 ? 0.0 : actions1.get(turn - 1) == Action.ATTACK ? 0.333333333 : actions1.get(turn - 1) == Action.GUARD ? 0.666666666 : 0.999999999;
//		double lastAction2 = turn == 0 ? 0.0 : actions2.get(turn - 1) == Action.ATTACK ? 0.333333333 : actions2.get(turn - 1) == Action.GUARD ? 0.666666666 : 0.999999999;
//		
//		double[] input1 = {min(turn, 50) / 50, p1.getHPRemaining() / p1.get(Stat.HP), p2.getHPRemaining() / p2.get(Stat.HP), lastAction1};
//		double[] input2 = {min(turn, 50) / 50, p2.getHPRemaining() / p2.get(Stat.HP), p1.getHPRemaining() / p1.get(Stat.HP), lastAction2};
//		
//		Action action1 = p1.think(input1);
//		Action action2 = p2.think(input2);
//		
//		actions1.add(action1);
//		actions2.add(action2);
//		
//		if(action1 == Action.HEAL) {
//			System.out.println(p1.getName() + " heals.");
//			p1.heal();
//		}
//		if(action2 == Action.HEAL) {
//			System.out.println(p2.getName() + " heals.");
//			p2.heal();
//		}
//		
//		if(action1 == Action.GUARD) {
//			System.out.println(p1.getName() + " guards.");
//		}
//		if(action2 == Action.GUARD) {
//			System.out.println(p2.getName() + " guards.");
//		}
//		
//		if(action1 == Action.ATTACK) {
//			double damage = p1.getAttackDamage();
//			if(action2 == Action.GUARD) {
//				damage = max(1, damage - p2.getBlockedDamageEnhanced());
//			} else {
//				damage = max(1, damage - p2.getBlockedDamage());
//			}
//			System.out.println(p1.getName() + " attacks.");
//			p2.takeDamage(damage * 0.5, damage * 0.5);
//		}
//		if(action2 == Action.ATTACK) {
//			double damage = p2.getAttackDamage();
//			if(action1 == Action.GUARD) {
//				damage = max(1, damage - p1.getBlockedDamageEnhanced());
//			} else {
//				damage = max(1, damage - p1.getBlockedDamage());
//			}
//			System.out.println(p2.getName() + " attacks.");
//			p1.takeDamage(damage * 0.5, damage * 0.5);
//		}				
//				
//		turn++;
//		
//		if(p1.isDead() && p2.isDead()) {
//			return Result.DRAW;
//		}
//		if(p1.isDead() && !p2.isDead()) {
//			return Result.LOSE;
//		}
//		if(!p1.isDead() && p2.isDead()) {
//			return Result.WIN;
//		}
//		return null;
//	}

}