package it.coderdojo.bracciano.ga;

public class Point {
	
	private int originalPosition;
	private int x;
	private int y;
	
	/**
	 * Costructor
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y, int originalPosition) {
		this.x = x;
		this.y = y;
		this.originalPosition = originalPosition;
	}

	/**
	 * @return the originalPosition
	 */
	public int getOriginalPosition() {
		return originalPosition;
	}

	/**
	 * @param originalPosition the originalPosition to set
	 */
	public void setOriginalPosition(int originalPosition) {
		this.originalPosition = originalPosition;
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return ""+x+" "+y+ " pos "+originalPosition;
	}
	
}
