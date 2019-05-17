package it.coderdojo.bracciano.ga;

public class Solution {

	private Point[] points;
	double length = 0;

	/**
	 * Constructor
	 * 
	 * @param solutionLenght
	 */
	public Solution(int solutionLenght) {
		points = new Point[solutionLenght];
	}
	
	/**
	 * @return the points
	 */
	public Point[] getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 * @throws Exception 
	 */
	public void setPoints(Point[] points) throws Exception {
		if(this.points!=null && points!=null && points.length==this.points.length) {
			for (int i = 0; i < points.length; i++) {
				this.points[i] = points[i];
			}
			setLength();
		} else {
			throw new Exception("this.points or points are null, otherwise they have different length");
		}
	}	
	
	public double getLength() {	
		return length;
	}
	
	private void setLength() {
		
		length = 0;
		
		//somma delle distanze tra il punto [i] e il punto [i+1]
		//sum point by point distance
		for (int i = 0; i < points.length -1 ; i++) {
			length += Calculator.distance(points[i], points[i+1]);
		} 
	}
	
	public void randomize(final Point[] coordinates) throws Exception {
		setPoints(Calculator.createRandomSolution(coordinates));
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
