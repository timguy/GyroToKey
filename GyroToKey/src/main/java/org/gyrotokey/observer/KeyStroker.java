package org.gyrotokey.observer;

/**
 * Presses a key out of gyro data for SuperTuxKart.
 * 
 * You have to adapt the constants depending on your board.
 * 
 * For pitch, roll, yaw, see: https://en.wikipedia.org/wiki/Aircraft_principal_axes#/media/File:Yaw_Axis_Corrected.svg
 * 
 * 
 */
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;

import org.gyrotokey.NavigationEvent;

public class KeyStroker implements Runnable {

	private static final double PITCH_EXTRA_NITRO = -0.29;
	private static final double PITCH_EXTREME_BACKWARD_ = 0.35;
	private static final double PITCH_TRESHHOLD = 0.1;

	private static final double ROLL_TRESHHOLD = 0.2;

	private static final double YAW_DISTANCE = 0.2;

	private double lastYaw;
	private Robot robot;
	
	private BlockingQueue<NavigationEvent> queue;

	public KeyStroker(BlockingQueue<NavigationEvent> queue) throws AWTException {
		this.queue = queue;
		robot = new Robot();
	}

	private void pressOrRelease(boolean push, int keycode) {
		//System.out.println("Push: " + push + " Keycode: "+ keycode);
		if (push) {
			robot.keyPress(keycode);
		} else {
			robot.keyRelease(keycode);
		}

	}

	@Override
	public void run() {
		while(true)
		{
			try {
				robot.waitForIdle(); //takes only ~28 ms. So most probably not really waiting for processing but only the method call cost
				robot.setAutoWaitForIdle(true);
				
				NavigationEvent nav = queue.take();
				// cursor navigation
				pressOrRelease(nav.getComplTiltPitch() < -PITCH_TRESHHOLD, KeyEvent.VK_UP);
				pressOrRelease(nav.getComplTiltPitch() > PITCH_TRESHHOLD, KeyEvent.VK_DOWN);

				pressOrRelease(nav.getComplTiltRoll() < -ROLL_TRESHHOLD, KeyEvent.VK_LEFT);
				pressOrRelease(nav.getComplTiltRoll() > ROLL_TRESHHOLD, KeyEvent.VK_RIGHT);

				// nitro (extra acceleration)
				pressOrRelease(nav.getComplTiltPitch() < PITCH_EXTRA_NITRO, KeyEvent.VK_N);

				// rescue
				pressOrRelease(nav.getComplTiltPitch() > PITCH_EXTREME_BACKWARD_, KeyEvent.VK_R);

				// fire - TODO adjust or invent new thing to recognize
				double currentYaw = Math.abs(nav.getComplYaw());
				pressOrRelease(currentYaw - lastYaw > YAW_DISTANCE, KeyEvent.VK_SPACE);
				lastYaw = currentYaw;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
