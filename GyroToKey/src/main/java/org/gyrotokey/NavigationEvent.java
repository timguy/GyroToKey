package org.gyrotokey;

public class NavigationEvent {
	private double complTiltPitch;

	private double complTiltRoll;
	private double complYaw;

	public NavigationEvent(double complTiltPitch, double complTiltRoll, double complYaw) {
		super();
		this.complTiltPitch = complTiltPitch;
		this.complTiltRoll = complTiltRoll;
		this.complYaw = complYaw;
	}

	public double getComplTiltPitch() {
		return complTiltPitch;
	}

	public double getComplTiltRoll() {
		return complTiltRoll;
	}

	public double getComplYaw() {
		return complYaw;
	}

	public void setComplTiltPitch(double complTiltPitch) {
		this.complTiltPitch = complTiltPitch;
	}

	public void setComplTiltRoll(double complTiltRoll) {
		this.complTiltRoll = complTiltRoll;
	}

	@Override
	public String toString() {
		return "NavigationEvent [complTiltRoll=" + complTiltRoll + ", complTiltPitch=" + complTiltPitch + "]";
	}

}
