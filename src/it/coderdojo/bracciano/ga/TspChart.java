package it.coderdojo.bracciano.ga;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class TspChart extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743430273740703555L;

	private Solution[] solutions = null;
	private int howMany = 0;
	final XYSeriesCollection data = new XYSeriesCollection();

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title the frame title.
	 */
	public TspChart(final String title, Solution[] solutions, int howMany) {

		super(title);

		this.solutions = solutions;
		this.howMany = howMany;
		
		refreshSeries();

		final JFreeChart chart = ChartFactory.createXYLineChart("", "", "", data,
				PlotOrientation.VERTICAL, true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(600, 320));
		setContentPane(chartPanel);

	}

	public void refreshSeries() {
		
		@SuppressWarnings("unchecked")
		List<XYSeries> list = (List<XYSeries>)data.getSeries();
		
		for (int i = 0; i < solutions.length && i < howMany; i++) {
			
			XYSeries series = null;
			
			if(list.size()>i) {
				series = list.get(i);
			}
			
			if(series == null ) {
				series = new XYSeries("Solution " + i, false, true);
				data.addSeries(series);
			}
			
			series.clear();

			Point[] points = solutions[i].getPoints();
			
			for (int j = 0; j < points.length; j++) {
				series.add(points[j].getX(), points[j].getY());
			}
			
		}
	}
}