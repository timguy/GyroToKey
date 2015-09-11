package org.gyrotokey.observable;

import java.util.concurrent.BlockingQueue;

import org.gyrotokey.NavigationEvent;

public class RawDataReaderSensorFusion implements Runnable {

	private BlockingQueue<NavigationEvent> queueForStroker;
	private BlockingQueue<NavigationEvent> queueForChart;
	private BlockingQueue<String> queueFromServer;

	public RawDataReaderSensorFusion(BlockingQueue<NavigationEvent> queueForStroker,
			BlockingQueue<NavigationEvent> queueForChart, BlockingQueue<String> queueFromServer) {
		this.queueForStroker = queueForStroker;
		this.queueForChart = queueForChart;
		this.queueFromServer = queueFromServer;
	}

	@Override
	public void run() {
		while (true) {
			String rawInput;
			try {
				rawInput = queueFromServer.take();
				String[] gyroCoords = rawInput.split(" ");

				// timeInMillis: gyroCoords[0]
				// "complement tilt"
				double pitch = Double.valueOf(gyroCoords[1]);
				double roll = Double.valueOf(gyroCoords[2]);

				// "complement yaw" data is used from the app
				double yaw = Double.valueOf(gyroCoords[3]);

				NavigationEvent nav = new NavigationEvent(pitch, roll, yaw);

				queueForChart.add(nav);
				queueForStroker.add(nav);
				System.out.println("queueForChart size: " + queueForChart.size() + " queueForStroker size: "
						+ queueForStroker.size());

			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
