package model;

import java.awt.Point;

import eventHandler.AbstractEvent;
import eventHandler.EventHandler;
import events.PositionChanged;

public class ElementMobile extends Element {
	Deplacement depl;
	int dataSize;

	public ElementMobile(int memorySize) {
		super(memorySize);
	}

	public int dataSize() {
		return this.dataSize;
	}

	public Deplacement deplacement() {
		return depl;
	}

	protected void resetData() {
		this.dataSize = 0;
	}

	protected boolean memoryFull() {
		return (this.dataSize >= this.memorySize);
	}

	public void tick() {
		this.bouge();
	}

	public void bouge() {
		this.depl.bouge(this);
		this.send(new PositionChanged(this));
	}

	public void setDeplacement(Deplacement depl) {
		this.depl = depl;
	}

}
