package it.coderdojo.bracciano.ga;

import java.util.Arrays;
import java.util.Comparator;

import org.jfree.ui.RefineryUtilities;

public class GA {

	//TEST BED
	public static final Point[] TEST_SCENARIO = KroA100.coordinates;
	
	//GA PARAMETERS
	public static final int POPULATION_SIZE = 10000;
	public static final float CROSSOVER_PERC = 0.0f;
	public static final float MUTATION_PERC = 0.5f;
	public static final int GENERATIONS = 10000;
	public static final int RANDOMIZE_COUNTER_MAX = 1000;
	
	public static void main(String[] args) {
		
		Solution[] solutions = new Solution[POPULATION_SIZE];
		try {
			createRandomSolutions(solutions, null, TEST_SCENARIO.length, 0, POPULATION_SIZE);
			
			Comparator<Solution> comparator = new SolutionComparator();
			Arrays.sort(solutions, comparator);

			System.out.println("Generation 0");
			printSolutions(solutions, 1, true);
			printSolutions(solutions, 1, false);

			final TspChart panel = new TspChart("TSP Solutions", solutions, 3);
			panel.pack();
			RefineryUtilities.centerFrameOnScreen(panel);
			panel.setVisible(true);
			
			int generationSize = Math.round(POPULATION_SIZE*(CROSSOVER_PERC+MUTATION_PERC));
			int crossoverSize = Math.round(POPULATION_SIZE*CROSSOVER_PERC);
			int mutationSize = generationSize - crossoverSize;
			int firstDeadIndex = POPULATION_SIZE-generationSize;
			
			Solution[] newGeneration = new Solution[generationSize];
			
			for (int i = 0; i < GENERATIONS; i++) {
				
				//mutation  
				createRandomSolutions(newGeneration, solutions, TEST_SCENARIO.length, 0, mutationSize);
				
				//crossover => sizes are split for didactical reason, dimensioni separate per evidenziare la composizione
				createSiblingsSolutions(newGeneration, solutions, TEST_SCENARIO.length, mutationSize, mutationSize+crossoverSize, firstDeadIndex);
				Arrays.sort(newGeneration, comparator);

				//it's life!
				for (int j = 0, deadIndex = firstDeadIndex; j < newGeneration.length; j++, deadIndex++) {
					solutions[deadIndex].setPoints(newGeneration[j].getPoints()); 
				}

				Arrays.sort(solutions, comparator);

				System.out.println("Generation "+(i+1));
				//the best
				printSolutions(solutions, 1, true);
				//the worst
				printSolutions(solutions, 1, false);

				panel.refreshSeries();
			}
			
			printFinalSolutions(solutions, 100, true);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static void createSiblingsSolutions(Solution[] newGeneration, final Solution[] parents, int solutionLength, int from, int to, int offset) throws Exception {
//		for (int i = from, j = 0; i < to; i++, j++) {
//			if(newGeneration[i] == null) {
//				newGeneration[i] = new Solution(solutionLength);
//			}
//			
//			newGeneration[i].setPoints(parents[offset+j].getPoints()); 
//		}
		
		//idea: campionare pezzi di percorso da due soluzioni e ricondurli ad una singola
		for (int i = from; i < to; i++) {
			if(newGeneration[i] == null) {
				newGeneration[i] = new Solution(solutionLength);
			}
			
			Solution parent1 = parents[(int) Math.floor(offset*Math.random())];
			Solution parent2 = parents[(int) Math.floor(offset*Math.random())];
			
			Point[] points = new Point[parent1.getPoints().length];
			
			// TODO create a crossover algorithm
			// creare un algoritmo che incrocia due soluzioni della generazione precedente 
			// per creare la i-esima nuova soluzione

			
			newGeneration[i].setPoints(points); 
		}
		
		
	}


	static void createRandomSolutions(Solution[] newGeneration, final Solution[] parents, int solutionLength, int from, int to) throws Exception {

		for (int i = from; i < to; i++) {
			if(newGeneration[i] == null) {
				newGeneration[i] = new Solution(solutionLength);
			}

			int counter = 0;
			while(counter < RANDOMIZE_COUNTER_MAX) {
				newGeneration[i].randomize(TEST_SCENARIO);

				boolean found = false;
				for (int j = 0; j < i; j++) {
					// we have to avoid equals on new generation => when length is the same we do another round
					if(newGeneration[i].length==newGeneration[j].length) {
						found = true;
						break;
					} 
				}
				
				if(parents != null) {
					for (int k = 0; k < parents.length; k++) {
						// we have to avoid equals on old generation => when length is the same we do another round
						if(newGeneration[i].length==parents[k].length) {
							found = true;
							break;
						} 
					}
				}

				if(!found) {
					break;
				}
				counter++;
			}
			if (counter==RANDOMIZE_COUNTER_MAX) {
				System.out.print(".");
			}
		}
		
	}

	static void printSolutions(Solution[] solutions, int howMany, boolean fromTop) {

		if(fromTop) {
			for (int i = 0; i < solutions.length && i < howMany; i++) {
				System.out.println("                Top "+i+" : "+solutions[i].getLength());
			}
		} else {
			for (int i = solutions.length-1; i > 0 && i > solutions.length-1-howMany; i--) {
				System.out.println("                                               Bottom "+i+" : "+solutions[i].getLength());
			}
		}
		
	}

	static void printFinalSolutions(Solution[] solutions, int howMany, boolean fromTop) {

		if(fromTop) {
			for (int i = 0; i < solutions.length && i < howMany; i++) {
				System.out.println("Top "+i+" : "+solutions[i].getLength()+ " => "+solutions[i].getListToString());
			}
		} else {
			for (int i = solutions.length-1; i > 0 && i > solutions.length-1-howMany; i--) {
				System.out.println("Bottom "+i+" : "+solutions[i].getLength()+ " => "+solutions[i].getListToString());
			}
		}
		
	}

	static class SolutionComparator implements Comparator<Solution> {

		@Override
		public int compare(Solution a, Solution b) {
			return (int) (a.getLength()-b.getLength());
		}
		
	}
}
