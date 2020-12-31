package it.coderdojo.bracciano.ga;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.SwingUtilities;

import org.jfree.ui.RefineryUtilities;

public class GA {

	//TEST BED
	public static final Point[] TEST_SCENARIO = KroA100.coordinates;
	
	//GA PARAMETERS
	public static final int POPULATION_SIZE = 30000;
	public static final float CROSSOVER_PERC = 0.5f;
	public static final float MUTATION_PERC = 0.1f;
	public static final int GENERATIONS = 500;
	public static final int RANDOMIZE_COUNTER_MAX = 100;
	public static final int NEAREST_NEIGHBOURS_LIST_LENGTH = 10;
	
	public static void main(String[] args) {
		
		Solution[] solutions = new Solution[POPULATION_SIZE];
		try {
			createRandomSolutions(solutions, null, TEST_SCENARIO.length, 0, POPULATION_SIZE);
			
			Comparator<Solution> comparator = new SolutionComparator();
			Arrays.parallelSort(solutions, comparator);

			System.out.println("Generation 0");
			printSolutions(solutions, 1, true);
			printSolutions(solutions, 1, false);

			final TspChart panel = new TspChart("TSP Solutions", solutions, 10);
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
				Arrays.parallelSort(newGeneration, comparator);

				//it's life!
				for (int j = 0, deadIndex = firstDeadIndex; j < newGeneration.length; j++, deadIndex++) {
					solutions[deadIndex].setPoints(newGeneration[j].getPoints()); 
				}

				Arrays.parallelSort(solutions, comparator);
				
				double bestValue = solutions[0].getLength();
				double worstSurvivorValue = solutions[firstDeadIndex-1].getLength();
				
				System.out.println("Generation "+(i+1));
				//the best
				printSolutions(solutions, 1, true);
				//the worst
				printSolutions(solutions, 1, false);
				
				System.out.println("Diff => "+((worstSurvivorValue-bestValue)/bestValue));

				refreshPanel(panel);
				
			}
			
			printFinalSolutions(solutions, 100, true);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void refreshPanel(final TspChart panel) {

		SwingUtilities.invokeLater(new Runnable() {
		      public void run()
		      {
		    	  panel.refreshSeries();
		      }
		});
		
	}
	
	private static void createSiblingsSolutions(Solution[] newGeneration, final Solution[] parents, int solutionLength, int from, int to, int offset) throws Exception {
		
		for (int i = from; i < to; i++) {
			if(newGeneration[i] == null) {
				newGeneration[i] = new Solution(solutionLength);
			}
			
			Solution parent1 = parents[(int) Math.floor(offset*Math.random())];
			Solution parent2 = parents[(int) Math.floor(offset*Math.random())];
			
			Point[] points = new Point[parent1.getPoints().length];
			
			////// PMX crossover algorithm
			
			// let's choose two cutting points
			int cuttingPoint1 = (int) Math.floor(solutionLength*Math.random());
			int cuttingPoint2 = (int) Math.floor(solutionLength*Math.random());
			
			while (cuttingPoint1 == cuttingPoint2) {
				//System.out.println("replay cutting point selection "+cuttingPoint2);
				//Thread.currentThread().sleep(10);
				cuttingPoint2 = (int) Math.floor(solutionLength*Math.random());
			}
			
			int firstCuttingPoint, secondCuttingPoint;
			if(cuttingPoint1 < cuttingPoint2) {
				firstCuttingPoint = cuttingPoint1;
				secondCuttingPoint = cuttingPoint2;
			} else {
				firstCuttingPoint = cuttingPoint2;
				secondCuttingPoint = cuttingPoint1;
			}

			// cutting points chosen, inititializing child with parent2 solution
			for (int j = 0; j < points.length; j++) {
				Point a = parent2.getPoints()[j];
				points[j] = new Point(a.getX(), a.getY(), a.getOriginalPosition());
			}
			
			// let's set parent1 segment into child solution
			for (int j = firstCuttingPoint; j < secondCuttingPoint; j++) {
				
				Point pointToSetOnP1 = parent1.getPoints()[j];
				Point pointToReplaceOnP2 = points[j];
				
				for (int k = 0; k < points.length; k++) {
					if(points[k].getX()==pointToSetOnP1.getX() && points[k].getY()==pointToSetOnP1.getY()) {
						points[k] = new Point(pointToReplaceOnP2.getX(), pointToReplaceOnP2.getY(), pointToReplaceOnP2.getOriginalPosition());
						break;
					}
				}
				
				points[j] = new Point(pointToSetOnP1.getX(), pointToSetOnP1.getY(), pointToSetOnP1.getOriginalPosition());
			}
			
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
