package it.coderdojo.bracciano.ga;

public class Solution {

	private Point[] points;


	/**
	 * Constructor
	 * 
	 * @param lenght
	 */
	public Solution(int lenght) {
		points = new Point[lenght];
	}
	
	/**
	 * @return the points
	 */
	public Point[] getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Point[] points) {
		this.points = points;
	}	
	
	public double getLength() {
		
		double length = 0;
		
		//somma delle distanze tra il punto [i] e il punto [i+1]
		for (int i = 0; i < points.length -1 ; i++) {
			length = length + Calculator.distance(points[i], points[i+1]);
		} 
		
		return length;
	}
	
	public void randomize() {
		setPoints(Calculator.createRandomSolution());
	}

	public String getListToString() {
		
		StringBuffer sb = new StringBuffer("{");
		
		for (int i = 0; i < points.length; i++) {
			sb.append(points[i]).append(", ");
		}
		
		sb.delete(sb.length()-2, sb.length());
		
		sb.append("}");
		
		return sb.toString();
	}
}
