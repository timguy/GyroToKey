package org.gyrotokey.observable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

public class UDPServer implements Runnable {

	private BlockingQueue<String> queue;
	
	public UDPServer(BlockingQueue<String> queue) throws Exception {
		this.queue = queue;
	}

	@Override
	public void run() {
		System.out.println("StandardCharset:" + Charset.defaultCharset());
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(5588);
			byte[] receiveData = new byte[80]; // we usually receive between 75 and 78 bytes
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receiveData, 0, receivePacket.getLength(), Charset.forName("UTF-8"));
				
				System.out.println("Received: " + sentence + "\tSize" + sentence.getBytes().length);
				
				queue.add(sentence);
				System.out.println("Raw queue size: " + queue.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		
	}

}