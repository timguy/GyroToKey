package org.gyrotokey.observable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.util.Observable;
import java.util.Observer;

import org.gyrotokey.NavigationEvent;

public class UDPServer extends Observable {

	public UDPServer(Observer... observers) throws Exception {
		for (Observer ob : observers) {
			this.addObserver(ob);
		}
	}

	public NavigationEvent splitData(String rawInput) {
		// use data from app: "sensor fusion" from lunds universitet
		// "complement tilt" data is used from the app

		String[] gyroCoords = rawInput.split(" ");

		// timeInMillis: gyroCoords[0]
		double pitch = Double.valueOf(gyroCoords[1]);

		// double roll = Double.valueOf(gyroCoords[2]);
		// The following is only needed as the last parameter sometimes ends in
		// a NumverFormatException as sometimes invalid values are send
		// valid: -9.883113458410296E-4568, invalid: 0.002645135879114789-46,
		// invalid: -0.0536865758390879E-, invalid: 6.309824565047179E-4-4356
		double roll = 0.0;
		try {
			roll = Double.valueOf(gyroCoords[2].replaceAll("\\d-\\d", "E-").replaceAll("E-$", ""));
		} catch (NumberFormatException e) {
			System.out.println("invalid value:" + e.getMessage());
		}
		// invalid

		// use data from app: "sensor fusion" from lunds universitet
		// "complement yaw" data is used from the app
		double yaw = Double.valueOf(gyroCoords[3]);

		NavigationEvent nav = new NavigationEvent(pitch, roll, yaw);
		return nav;
	}

	public void start() throws IOException {

		System.out.println("StandardCharset:" + Charset.defaultCharset());
		DatagramSocket serverSocket = new DatagramSocket(5588);
		byte[] receiveData = new byte[80]; // we usally receive between 75 and
											// 78 bytes
		long startWaitingTime;
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			startWaitingTime = System.currentTimeMillis();
			serverSocket.receive(receivePacket);
			System.out.println("Waited for package:" + (System.currentTimeMillis() - startWaitingTime) + " ms");

			String sentence = new String(receivePacket.getData(), Charset.forName("UTF-8"));

			String sentence2 = new String(receiveData, 0, receivePacket.getLength(), Charset.forName("UTF-8"));

			System.out.println("Received: " + sentence);
			System.out.println("Size" + sentence.getBytes().length + " size2:" + sentence2.getBytes().length);

			NavigationEvent nav = splitData(sentence);
			this.setChanged();
			this.notifyObservers(nav);
		}
	}

}