package it.coderdojo.bracciano.ga;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
	
	
	public static void main(String[] args) {
		double d = distance(new Point(0,0), new Point(1,1));
		System.out.println("Result: "+d);
		assert d == Math.sqrt(2) : "distance doesn't work, it should be " + Math.sqrt(2);
	}

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

}
