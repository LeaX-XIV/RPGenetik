package com.leaxxiv.rpgenetik.gui.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leaxxiv.rpgenetik.Fighter;
import com.leaxxiv.rpgenetik.Stat;

import static com.leaxxiv.rpgenetik.Stat.*;

public class SimpleInterface extends JFrame {
	
	private Fighter hero;
	private Fighter enemy;
	
	private JPanel heroP;
	private JLabel heroL;
	private DoubleProgressBar heroPB;
	
	private JPanel enemyP;
	private JLabel enemyL;
	private DoubleProgressBar enemyPB;
	
	private JPanel inputPanel;

	public SimpleInterface(Fighter hero, Fighter enemy) {
		this.hero = hero;
		String heroStats = "LV: " + hero.getLevel() + " HP: " + hero.get(HP) + " ATK: " + hero.get(ATK) + " DEF: " + hero.get(DEF) + " SPD: " + hero.get(SPD) + " CUR: " + hero.get(CUR);
		this.enemy = enemy;
		String enemyStats = "LV: " + enemy.getLevel() + " HP: " + enemy.get(HP) + " ATK: " + enemy.get(ATK) + " DEF: " + enemy.get(DEF) + " SPD: " + enemy.get(SPD) + " CUR: " + enemy.get(CUR);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(720, 650, 500, 100);
		heroP = new JPanel();
		heroP.setLayout(new GridLayout(1, 2));
		heroL = new JLabel(hero.getName());
		heroL.setHorizontalAlignment(JLabel.CENTER);
		heroL.setToolTipText(heroStats);
		heroPB = new DoubleProgressBar(0, hero.get(Stat.HP));
	
		heroP.add(heroL);
		heroP.add(heroPB);
	
		enemyP = new JPanel();
		enemyP.setLayout(new GridLayout(1, 2));
		enemyL = new JLabel(enemy.getName());
		enemyL.setHorizontalAlignment(JLabel.CENTER);
		enemyL.setToolTipText(enemyStats);
		enemyPB = new DoubleProgressBar(0, enemy.get(Stat.HP));
	
		enemyP.add(enemyL);
		enemyP.add(enemyPB);
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
	
		getContentPane().setLayout(new GridLayout(3, 1));
		getContentPane().add(heroP);
		getContentPane().add(enemyP);
		getContentPane().add(inputPanel);
		pack();
		setVisible(true);
	}
	
	public void updateProgressBars(int steps) {
		heroPB.setTragetValues(hero.getHPRemaining(), hero.getHPRemaining() + hero.getHealDamage(), steps);
		enemyPB.setTragetValues(enemy.getHPRemaining(), enemy.getHPRemaining() + enemy.getHealDamage(), steps);
	}
	
	public Container getInputPanel() {
		return this.inputPanel;
	}

}
