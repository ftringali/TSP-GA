package it.coderdojo.bracciano.ga;

import java.util.Arrays;
import java.util.Comparator;

public class GA {

	public static void main(String[] args) {
		Solution[] solutions = new Solution[1000];
		
		for (int i = 0; i < solutions.length; i++) {
			solutions[i] = new Solution(KroA100.coordinates.length);
			solutions[i].randomize();
		}
		
		Comparator<Solution> comparator = new SolutionComparator();
		Arrays.sort(solutions, comparator);
		
		for (int i = 0; i < solutions.length; i++) {
			System.out.println("Soluzione "+i+" : "+solutions[i].getLength()+ " => "+solutions[i].getListToString());
		}
	}
	
	static class SolutionComparator implements Comparator<Solution> {

		@Override
		public int compare(Solution a, Solution b) {

			return (int) (a.getLength()-b.getLength());
		}
		
	}
}
