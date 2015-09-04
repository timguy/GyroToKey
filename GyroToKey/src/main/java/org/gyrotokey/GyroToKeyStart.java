package org.gyrotokey;

import java.awt.AWTException;
import java.io.IOException;

import org.gyrotokey.observable.UDPServer;
import org.gyrotokey.observer.DynamicChart;
import org.gyrotokey.observer.KeyStroker;

public class GyroToKeyStart {

	public static void main(String[] args) throws IOException, AWTException, Exception {
		if (args.length == 0) {
			// without chart
			new UDPServer(new KeyStroker()).start();
		} else if (args.length == 1 && args[0].equalsIgnoreCase("g")) {
			// Gui stuff - switch on for finding the right treshholds
			DynamicChart chart = new DynamicChart("Gyro Data");
			new UDPServer(new KeyStroker(), chart).start();
		} else {
			System.out.println(
					"You have two options to start:\n java -jar GyroToKey.jar\n java -jar GyroToKey.jar g\n the last one starts with graphical chart output.");
		}

	}
}
