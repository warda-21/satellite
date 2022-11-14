package events;

import eventHandler.AbstractEvent;

public class SatelliteMoved extends AbstractEvent {
	private static final long serialVersionUID = 480096146703824993L;

	public SatelliteMoved(Object source) {
		super(source);
	}

	public void sendTo(Object target) {
		SatelitteMoveListener listener = (SatelitteMoveListener) target;
		listener.whenSatelitteMoved(this);
	}
}
