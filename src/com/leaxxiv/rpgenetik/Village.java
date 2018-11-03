package com.leaxxiv.rpgenetik;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.random;

import java.util.function.Function;
import java.util.function.Supplier;

import com.leaxxiv.genetics.DNA;
import com.leaxxiv.genetics.Population;

public class Village<T extends Enemy> extends Population<T> {

	public Village(int n, Supplier<T> supplier, Function<DNA, T> factory) {
		super(n, supplier, factory);
	}
	
	public void add(T t) {
		super.population.add(t);
	}
	
	@Override
	protected T copulate() {
			T parentA = pickOne();
			T parentB = pickOne();
			
			DNA childDna = combineGenomes(parentA, parentB);
			T child = super.function.apply(childDna);
			
			DNA childRandomDna = combineRandomGenomes(parentA, parentB);
			child.setRandomDna(childRandomDna);
			
			for(int i = 1; i <= (parentA.getLevel() + parentB.getLevel()) / 2; i++) {
				child.levelUp(new Stat[] {Stat.values()[(int) (floor(abs(child.getRandomDna().getGene(i)) * Stat.values().length))],
						Stat.values()[(int) (floor(abs(child.getRandomDna().getGene(i)) * Stat.values().length))]});
			}
			
			return child;
	}
	
	private DNA combineRandomGenomes(T parentA, T parentB) {
		DNA dnaA = parentA.getRandomDna();
		DNA dnaB = parentB.getRandomDna();
		
		double[] newDna = new double[dnaA.getNumGenes()];
		for(int i = 0; i < newDna.length; i++) {
			if(i % 2 == 0) {
				newDna[i] = dnaA.getGene(i);
			} else {
				newDna[i] = dnaB.getGene(i);
			}
		}
		
		return new DNA(newDna, dnaA.getMinGene(), dnaA.getMaxGene());
	}
	
	@Override
	protected DNA combineGenomes(T parentA, T parentB) {
		DNA dnaA = parentA.getDna();
		DNA dnaB = parentB.getDna();
		
		double[] newDna = new double[dnaA.getNumGenes()];
		for(int i = 0; i < newDna.length; i++) {
			if(i % 2 == 0) {
				newDna[i] = dnaA.getGene(i);
			} else {
				newDna[i] = dnaB.getGene(i);
			}
		}
		
		return new DNA(newDna, dnaA.getMinGene(), dnaA.getMaxGene());		
	}
	
	@Override
	public T pickRandomOne() {
		int index = (int) floor(random() * this.population.size());
		return this.population.remove(index);
	}

}
