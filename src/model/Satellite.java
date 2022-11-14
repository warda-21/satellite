package model;

import events.SatelliteMoved;

public class Satellite extends ElementMobile {
			
	public Satellite(int memorySize) {
		super(memorySize);
	}
	
	public void bouge () {
		super.bouge();
		this.send(new SatelliteMoved(this));		
	}
}
