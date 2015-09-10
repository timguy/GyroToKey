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
		String[] gyroCoords = rawInput.split(" ");

		// timeInMillis: gyroCoords[0]
		// "complement tilt"
		double pitch = Double.valueOf(gyroCoords[1]);
		double roll = Double.valueOf(gyroCoords[2]);

		// "complement yaw" data is used from the app
		double yaw = Double.valueOf(gyroCoords[3]);

		NavigationEvent nav = new NavigationEvent(pitch, roll, yaw);
		return nav;
	}

	public void start() throws IOException {

		System.out.println("StandardCharset:" + Charset.defaultCharset());
		DatagramSocket serverSocket = new DatagramSocket(5588);
		byte[] receiveData = new byte[80]; // we usually receive between 75 and 78 bytes
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receiveData, 0, receivePacket.getLength(), Charset.forName("UTF-8"));

			System.out.println("Received: " + sentence + "\tSize" + sentence.getBytes().length);

			NavigationEvent nav = splitData(sentence);
			this.setChanged();
			this.notifyObservers(nav);
		}
	}

}