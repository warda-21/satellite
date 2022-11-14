package model;

import java.awt.Point;

import eventHandler.AbstractEvent;
import eventHandler.EventHandler;
import events.PositionChanged;

public class ElementMobile {
	Deplacement depl;
	Point position;
	EventHandler eventHandler;
	int memorySize;
	int dataSize;
	Manager manager;

	public ElementMobile(int memorySize) {
		eventHandler = new EventHandler();
		this.memorySize = memorySize;
		this.dataSize = 0;
		this.position = new Point(0, 0);
	}

	public int dataSize() {
		return this.dataSize;
	}

	public int memorySize() {
		return this.memorySize;
	}

	public Deplacement deplacement() {
		return depl;
	}
	
	public void setManager(Manager manager) {
		this.manager = manager;
		
	}
	
	protected void resetData() {
		this.dataSize = 0;
	}

	protected boolean memoryFull() {
		return (this.dataSize >= this.memorySize);
	}

	// enregistrement des listeners
	public void registerListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		eventHandler.registerListener(whichEventType, listener);
	}

	public void unregisterListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		eventHandler.unregisterListener(whichEventType, listener);
	}

	// envoi des evenements
	public void send(AbstractEvent event) {
		eventHandler.send(event);
	}

	public void tick() {
		this.bouge();
	}

	public void bouge() {
		this.depl.bouge(this);
		this.send(new PositionChanged(this));
	}

	public void setPosition(Point position) {
		if (this.position.equals(position))
			return;
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	public void setDeplacement(Deplacement depl) {
		this.depl = depl;
	}

	public Manager getManager() {
		return manager;
	}

}
