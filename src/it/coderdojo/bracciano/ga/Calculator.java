package it.coderdojo.bracciano.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Calculator {
	
	private static int[][] nearestNeighboursMatrix = null;

	/**
	 * @param a
	 * @param b
	 * @return distance between 2 points
	 */
	public static double distance(Point a, Point b) {
		
		// teorema che porta il nome di un noto matematico greco
		// famous theorem discovered by an ancient Greek mathematician
		return Math.sqrt(
				Math.pow(a.getX() - b.getX(), 2) + 
				Math.pow(a.getY() - b.getY(), 2)
			   );
	}


	/**
	 * @return a random solution
	 */
	public static Point[] createRandomSolution(final Point[] coordinates) {
		
		List<Point> list = new ArrayList<Point>();
		
		for (int i = 0; i < coordinates.length; i++) {
			list.add(coordinates[i]);
		} 

		Point[] points = new Point[list.size()];
		
		for (int i = 0; i < points.length; i++) {
			int index = (int) Math.floor((list.size() * Math.random()));
			points[i] = list.remove(index);
		}
		
		return points;
	}

	/**
	 * @return a random near neighbour solution
	 */
	public static Point[] createRandomNearestNeighboursSolution(final Point[] coordinates) {
		
		int howManyNeighbours = coordinates.length<GA.NEAREST_NEIGHBOURS_LIST_LENGTH?coordinates.length:GA.NEAREST_NEIGHBOURS_LIST_LENGTH;

		if(nearestNeighboursMatrix == null) {
			initializeNearestNeighboursMatrix(coordinates, howManyNeighbours);
		}
		
		List<Point> list = new ArrayList<Point>();
		
		for (int i = 0; i < coordinates.length; i++) {
			list.add(coordinates[i]);
		} 

		Point[] points = new Point[list.size()];
		
		for (int i = 0; i < points.length; i++) {
			if(i==0) {
				int index = (int) Math.floor(list.size() * Math.random());
				points[i] = list.remove(index);
			} else {
				int randomNeighbourPosition = (int) Math.floor(howManyNeighbours * Math.random());
				int neighbourPosition = nearestNeighboursMatrix[points[i-1].getOriginalPosition()][randomNeighbourPosition];
				boolean found = false;
				for (int j = 0; j < list.size(); j++) {
					Point a = list.get(j);
					if(a.getOriginalPosition() == neighbourPosition) {
						points[i] = list.remove(j);
						found = true;
					}
				}
				if(!found) {
					int index = (int) Math.floor(list.size() * Math.random());
					points[i] = list.remove(index);
				}
			}
		}
		
		return points;
	}
	
	static void initializeNearestNeighboursMatrix(Point[] coordinates, int howManyNeighbours) {
		
		nearestNeighboursMatrix = new int[coordinates.length][howManyNeighbours];
		
		Comparator<Neighbour> comparator = new NeighbourComparator();
		
		System.out.println("Initializing nearest neighbours matrix");
		for (int i = 0; i < coordinates.length; i++) {
			
			LinkedList<Neighbour> neighbours = new LinkedList<Neighbour>();
			
			for (int j = 0; j < coordinates.length; j++) {
				if(i!=j) {
					neighbours.add(new Neighbour(coordinates[j].getOriginalPosition(), distance(coordinates[i], coordinates[j])));
				}
			}
			
			neighbours.sort(comparator);
			
			for (int j = 0; j < howManyNeighbours; j++) {
				nearestNeighboursMatrix[i][j]=neighbours.get(j).getIndex();
			}
		}
		System.out.println("Nearest neighbours matrix initialized.");

	}
	
	static class NeighbourComparator implements Comparator<Neighbour> {

		@Override
		public int compare(Neighbour a, Neighbour b) {
			return (int) (a.getDistance()-b.getDistance());
		}
		
	}
}
