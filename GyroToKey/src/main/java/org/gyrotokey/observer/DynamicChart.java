package org.gyrotokey.observer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import org.gyrotokey.NavigationEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration application showing a time series chart where you can
 * dynamically add (random) data by clicking on a button.
 *
 */
public class DynamicChart extends ApplicationFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The time series data. */
	private TimeSeries pitchSeries;

	private TimeSeries rollSeries;
	
	private BlockingQueue<NavigationEvent> queue;

	/**
	 * Constructs a new demonstration application.
	 * @param queue 
	 *
	 * @param title
	 *            the frame title.
	 */
	public DynamicChart(BlockingQueue<NavigationEvent> queue) {

		super("Gyro Data");
		
		this.queue = queue;
		
		this.pitchSeries = new TimeSeries("Pitch", Millisecond.class);
		this.rollSeries = new TimeSeries("Roll", Millisecond.class);
		final TimeSeriesCollection datasetPitch = new TimeSeriesCollection(this.pitchSeries);
		final TimeSeriesCollection datasetRoll = new TimeSeriesCollection(this.rollSeries);

		final JFreeChart chart = createChart(datasetPitch, datasetRoll);

		final ChartPanel chartPanel = new ChartPanel(chart);

		final JPanel content = new JPanel(new BorderLayout());
		content.add(chartPanel);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(content);

		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * @param datasetRoll
	 * 
	 * @return A sample chart.
	 */
	private JFreeChart createChart(final XYDataset dataset, XYDataset datasetRoll) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart("Gyrosensor data", "Time", "Value", dataset,
				true, true, false);

		final XYPlot plot = result.getXYPlot();
		plot.setDataset(1, datasetRoll);

		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
		plot.setRenderer(1, renderer1);
		plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);

		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0); // 60 seconds
		axis = plot.getRangeAxis();
		axis.setRange(-4.0, 4.0);
		return result;
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				NavigationEvent nav = queue.take();
				// plot new data
				this.pitchSeries.addOrUpdate(new Millisecond(), nav.getComplTiltPitch());
				this.rollSeries.addOrUpdate(new Millisecond(), nav.getComplTiltRoll());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
	}

}
