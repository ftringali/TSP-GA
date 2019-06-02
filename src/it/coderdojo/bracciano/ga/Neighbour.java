package it.coderdojo.bracciano.ga;

public class Neighbour {
	
	private int index;
	private double distance;
	
	Neighbour(int index, double distance) {
		this.index = index;
		this.distance = distance;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
