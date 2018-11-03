package com.leaxxiv.rpgenetik;

import com.leaxxiv.genetics.DNA;
import com.leaxxiv.neuralnetwork.ActivationFunction;
import com.leaxxiv.neuralnetwork.Matrix;
import com.leaxxiv.neuralnetwork.NeuralNetwork;
import com.leaxxiv.rpgenetik.battle.Action;

import static java.lang.Math.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Enemy extends Fighter {

	private static final ArrayList<String> NAMES = new ArrayList<>();
	public static final int NUMBER_RANDOM_GENES = 363;

	private DNA randomDna;
	private NeuralNetwork brain;
	
	static {
		try(InputStreamReader in = new InputStreamReader(Enemy.class.getResourceAsStream("/dat/names.dat"), "ISO-8859-1")) {
			StringBuffer sb = new StringBuffer();
			while(in.ready()) {
				char c = (char) in.read();
				if(c == '\n' || c == '\r') {
					if(sb.length() > 0) {
						NAMES.add(sb.toString());
						sb = new StringBuffer();
					}
				} else {
					sb.append(c);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Enemy() {
		super();
		randomDna = new DNA(NUMBER_RANDOM_GENES, -1, 1);
		setName(pickName());
		setBrain();
	}
	
	public static Enemy factory(DNA dna) {
		Enemy e = new Enemy();
		e.setStatsDna(dna);
		
		return e;
	}
	
	public void setRandomDna(DNA dna) {
		randomDna = dna;
		setName(pickName());
		setBrain();
	}
	
	public DNA getRandomDna() {
		return randomDna;
	}

	private void setBrain() {
		Matrix[] weights = computeWeigthsMatrixes();
		Matrix[] biases = computeBiasesMatrixes();
		brain = new NeuralNetwork(weights, biases);
		brain.setActivationFunction(ActivationFunction.SIGMOID);
	}

	public Action think(double[] inputs) {
		Matrix result = brain.predict(inputs);
		double attack = result.getValue(0, 0);
		double guard = result.getValue(1, 0);
		double heal = result.getValue(2, 0);

		if(attack > guard && attack > heal) {
			return Action.ATTACK;
		} else if(guard > attack && guard > heal) {
			return Action.GUARD;
		} else { // heal > attack && heal > guard
			return Action.HEAL;
		}
	}

	@Override
	public void computeFitness() {
		// Cannot compute fitness without additional data, use setFitness
	}
	
	@Override
	public void mutate(double mutationRate) {
		super.mutate(mutationRate);
		randomDna.mutate(mutationRate);
	}

	private String pickName() {
		double index = 0;
		for(int i = 0; i < NUMBER_RANDOM_GENES; i++) {
			index += randomDna.getGene(i);
		}
		return NAMES.get((int) abs(index) % NAMES.size());
	}

	private Matrix[] computeWeigthsMatrixes() {		
		Matrix[] weights = new Matrix[3];
		int[][] g = {
				{15, 4},
				{15, 15},
				{3, 15}
		};
		
		
		int index = 0;
		for(int i = 0; i < weights.length; i++) {
			double[][] d = new double[g[i][0]][g[i][1]];
			for(int j = 0; j < g[i][0]; j++) {
				for(int k = 0; k < g[i][1]; k++) {
					d[j][k] = randomDna.getGene(index++);
				}
			}
			weights[i] = new Matrix(d);
		}
		
		return weights;
	}

	private Matrix[] computeBiasesMatrixes() {
		Matrix[] biases = new Matrix[3];
		int[] g = {15, 15, 3};
		
		
		int index = 330;
		for(int i = 0; i < biases.length; i++) {
			double[][] d = new double[g[i]][1];
			for(int j = 0; j < g[i]; j++) {
					d[j][0] = randomDna.getGene(index++);
			}
			biases[i] = new Matrix(d);
		}
		
		return biases;
	}

}
