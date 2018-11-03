package com.leaxxiv.rpgenetik;

import java.awt.Container;

import javax.swing.JOptionPane;

import com.leaxxiv.rpgenetik.battle.Action;

public class Player extends Fighter {
	
	public Player(String name) {
		super();
		setName(name);
	}

	@Override
	public void computeFitness() {
	}
	
	public Action think(Container parent) {
		Action[] options = {Action.ATTACK, Action.GUARD, Action.HEAL};
//		int answer = JOptionPane.showInternalOptionDialog(parent, "", "Choose your action", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, Action.ATTACK);
		int answer = JOptionPane.showOptionDialog(null, "", "Choose your action", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, Action.ATTACK);
		return options[answer];
	}
	
	@Override
	public Player clone() {
		Player clone = new Player(this.getName());
		
		clone.dna = this.dna;
		clone.setLevel(this.getLevel());
		clone.setStats();
		for(int i = 0; i < NUMBER_STATS_GENES; i++) {
			clone.trainStat(Stat.values()[i], this.trainedStats.get(Stat.values()[i]));
		}
		clone.setPermaDamage(this.getPermaDamage());
		clone.setHealDamage(this.getHealDamage());
		
		return clone;
	}

}
