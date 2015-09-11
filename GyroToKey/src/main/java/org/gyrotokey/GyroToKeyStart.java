package org.gyrotokey;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.gyrotokey.observable.RawDataReaderSensorFusion;
import org.gyrotokey.observable.UDPServer;
import org.gyrotokey.observer.DynamicChart;
import org.gyrotokey.observer.KeyStroker;

public class GyroToKeyStart {
	private static final int maxQueueSize = 90;

	public static void main(String[] args) throws IOException, AWTException, Exception {
		System.out
				.println("Our threads can run on #number of processors: " + Runtime.getRuntime().availableProcessors());

		BlockingQueue<NavigationEvent> queueForStroker = new ArrayBlockingQueue<NavigationEvent>(maxQueueSize);
		BlockingQueue<NavigationEvent> queueForChart = new ArrayBlockingQueue<NavigationEvent>(maxQueueSize);
		BlockingQueue<String> queueRawData = new ArrayBlockingQueue<String>(maxQueueSize);

		DynamicChart chart = new DynamicChart(queueForChart);
		KeyStroker stroker = new KeyStroker(queueForStroker);
		RawDataReaderSensorFusion rawDataReader = new RawDataReaderSensorFusion(queueForStroker, queueForChart,
				queueRawData);

		new Thread(chart, "Chart Thread").start();
		new Thread(stroker, "KeyStroker Thread").start();

		new Thread(rawDataReader, "RawDataReader Thread").start();

		new Thread(new UDPServer(queueRawData), "UDP Server Thread").start();

	}
}
