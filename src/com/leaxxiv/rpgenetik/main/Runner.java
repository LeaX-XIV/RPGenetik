package com.leaxxiv.rpgenetik.main;

import static java.lang.Math.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.leaxxiv.rpgenetik.Enemy;
import com.leaxxiv.rpgenetik.Fighter;
import com.leaxxiv.rpgenetik.Player;
import com.leaxxiv.rpgenetik.Stat;
import com.leaxxiv.rpgenetik.Village;
import com.leaxxiv.rpgenetik.battle.Action;
import com.leaxxiv.rpgenetik.battle.Battle;
import com.leaxxiv.rpgenetik.battle.BattleData;
import com.leaxxiv.rpgenetik.gui.components.SimpleInterface;

public class Runner {
	
	public static void main(String[] args) {
		String name = JOptionPane.showInputDialog(null, "Choose yor hero's name");
		Player hero = new Player(name);
		
		Village<Enemy> v = new Village<>(50, Enemy::new, Enemy::factory);
		Enemy enemy = v.pickRandomOne();
		
		while(!hero.isDead()) {
			List<Enemy> everyone = v.getPopulation();
			List<Battle> battles = new ArrayList<>(everyone.size());
			Battle focussed = new Battle(hero, enemy);
			SimpleInterface si = new SimpleInterface(hero, enemy);
			si.updateProgressBars(100);
			
			for(Enemy e : everyone) {
				battles.add(new Battle(hero.clone(), e));
			}
			
			while(!focussed.isFinished()) {				
				try {
					BattleData bd = focussed.dequeAction();
					System.out.println(bd.getFighter().getName() + " " + bd.getAction().name() + " " + bd.getValue());
					focussed.assignFitness();
					System.out.println(hero.getFitness());
					System.out.println(enemy.getFitness());
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					si.updateProgressBars(100);
				} catch(IllegalStateException e) {
					double lastEnemyAction = focussed.getTurn() == 0 ? 0.0 : focussed.getLastAction2() == Action.ATTACK ? 0.333333333 : focussed.getLastAction2() == Action.GUARD ? 0.666666666 : 0.999999999;
					double[] input2 = {min(focussed.getTurn(), 50) / 50, enemy.getHPRemaining() / enemy.get(Stat.HP), hero.getHPRemaining() / hero.get(Stat.HP), lastEnemyAction};
					Action enemyAction = enemy.think(input2);
					Action heroAction = hero.think(si.getInputPanel());
					focussed.enqueueActions(heroAction, enemyAction);
					
					for(Battle b : battles) {
						if(!b.isFinished()) {
							Fighter h = b.getFighter1();
							Enemy en = (Enemy) b.getFighter2();
							double lastEAction = b.getTurn() == 0 ? 0.0 : b.getLastAction2() == Action.ATTACK ? 0.333333333 : b.getLastAction2() == Action.GUARD ? 0.666666666 : 0.999999999;
							double[] i2 = {min(b.getTurn(), 50) / 50, en.getHPRemaining() / en.get(Stat.HP), h.getHPRemaining() / h.get(Stat.HP), lastEAction};
							Action eAction = enemy.think(i2);
							b.enqueueActions(heroAction, eAction);
							
							b.dequeAction();
							b.dequeAction();
							b.assignFitness();
						}
					}
				}
			}
			
			if(!hero.isDead()) {
				// EVOLUTION
				v.add(enemy);
				v.calculateFitness();
				enemy = v.getBestDick();
				double healHeal = -enemy.getHealDamage();
				double permaHeal = -enemy.getPermaDamage();
				enemy.takeDamage(permaHeal, healHeal);
				for(Enemy e : v.getPopulation()) {
					for(int i = 0; i < 1; i++) {
						int index = (e.getLevel() - 1) * 2 + i;
						e.levelUp(new Stat[] {Stat.values()[(int) (floor(abs(e.getRandomDna().getGene(index)) * Stat.values().length))],
								Stat.values()[(int) (floor(abs(e.getRandomDna().getGene(index)) * Stat.values().length))]});
					}
				}
				v.newGeneration();
				si.dispose();
				
				// TODO: LEVEL UP HERO
				hero.takeDamage(0, -hero.getHealDamage());
				int stat1 = JOptionPane.showOptionDialog(null, "First stat to Upgrade", "Level Up to " + (hero.getLevel() + 1), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Stat.values(), null);
				int stat2 = JOptionPane.showOptionDialog(null, "Second stat to Upgrade", "Level Up to " + (hero.getLevel() + 1), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Stat.values(), null);
				hero.levelUp(new Stat[] {Stat.values()[stat1], Stat.values()[stat2]});
			}
		}
		
		System.out.println("You DED");
		
	}

//	public static void main(String[] args) {
//		String name = JOptionPane.showInputDialog(null, "Choose yor hero's name");
//		Player hero = new Player(name);
//		Enemy enemy = new Enemy();
//
//		for(int i = 0; i < 100; i++) {
//			hero.levelUp(new Stat[] {Stat.values()[(int) (floor(random() * Stat.values().length))], Stat.values()[(int) (floor(random() * Stat.values().length))]});
//			enemy.levelUp(new Stat[] {Stat.values()[(int) (floor(random() * Stat.values().length))], Stat.values()[(int) (floor(random() * Stat.values().length))]});
//		}
//
//		SimpleInterface f = new SimpleInterface(hero, enemy);
//
//
//		Battle bf = new Battle(hero, enemy);
//
//		while(!bf.isFinished()) {
//			f.updateProgressBars(100);
//			
//			try {
//				BattleData bd = bf.dequeAction();
//				System.out.println(bd.getFighter().getName() + " " + bd.getAction().name() + " " + bd.getValue());
//				bf.assignFitness();
//				System.out.println(hero.getFitness());
//				System.out.println(enemy.getFitness());
//				try {
//					Thread.sleep(2500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} catch(IllegalStateException e) {
//				double lastEnemyAction = bf.getTurn() == 0 ? 0.0 : bf.getLastAction2() == Action.ATTACK ? 0.333333333 : bf.getLastAction2() == Action.GUARD ? 0.666666666 : 0.999999999;
//				double[] input2 = {min(bf.getTurn(), 50) / 50, enemy.getHPRemaining() / enemy.get(Stat.HP), hero.getHPRemaining() / hero.get(Stat.HP), lastEnemyAction};
//				Action enemyAction = enemy.think(input2);
//				Action heroAction = hero.think(f.getInputPanel());
//				bf.enqueueActions(heroAction, enemyAction);
//			}
//			
//			
//		}
//
//		f.updateProgressBars(100);
//		System.out.println(bf.getResult());
////		f.dispose();
//	}

}
