package model;

import java.awt.Point;

import eventHandler.AbstractEvent;
import eventHandler.EventHandler;

public class Element {
	int memorySize;
	Manager manager;
	EventHandler eventHandler;
	Point position;

	public Element(int memorySize) {
		this.memorySize = memorySize;
		eventHandler = new EventHandler();
		this.position = new Point(0, 0);
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	// envoi des evenements
	public void send(AbstractEvent event) {
		eventHandler.send(event);
	}

	public void setPosition(Point position) {
		if (this.position.equals(position))
			return;
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}


	// enregistrement des listeners
	public void registerListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		eventHandler.registerListener(whichEventType, listener);
	}

	public void unregisterListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		eventHandler.unregisterListener(whichEventType, listener);
	}
}
