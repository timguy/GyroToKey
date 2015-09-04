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
import java.util.Observable;
import java.util.Observer;

import org.gyrotokey.NavigationEvent;

public class KeyStroker implements Observer {

	private static final double PITCH_EXTRA_NITRO = -0.29;
	private static final double PITCH_EXTREME_BACKWARD_ = 0.35;
	private static final double PITCH_TRESHHOLD = 0.1;

	private static final double ROLL_TRESHHOLD = 0.2;

	private static final double YAW_DISTANCE = 0.2;

	private double lastYaw;
	private Robot robot;

	public KeyStroker() throws AWTException {
		robot = new Robot();
	}

	private void pressOrRelease(boolean push, int keycode) {
		if (push) {
			robot.keyPress(keycode);
		} else {
			robot.keyRelease(keycode);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		NavigationEvent nav = (NavigationEvent) arg;

		// cursor navigation
		pressOrRelease(nav.getComplTiltPitch() < -PITCH_TRESHHOLD, KeyEvent.VK_UP);
		pressOrRelease(nav.getComplTiltPitch() > PITCH_TRESHHOLD, KeyEvent.VK_DOWN);

		pressOrRelease(nav.getComplTiltRoll() < -ROLL_TRESHHOLD, KeyEvent.VK_LEFT);
		pressOrRelease(nav.getComplTiltRoll() > ROLL_TRESHHOLD, KeyEvent.VK_RIGHT);

		// nitro (extra acceleration)
		pressOrRelease(nav.getComplTiltPitch() < PITCH_EXTRA_NITRO, KeyEvent.VK_N);

		// rescue
		pressOrRelease(nav.getComplTiltPitch() > PITCH_EXTREME_BACKWARD_, KeyEvent.VK_R);

		// fire
		double currentYaw = Math.abs(nav.getComplYaw());
		pressOrRelease(currentYaw - lastYaw > YAW_DISTANCE, KeyEvent.VK_SPACE);
		lastYaw = currentYaw;
	}

}
