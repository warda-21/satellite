package events;

import eventHandler.AbstractEvent;
import model.DeplSynchronisation;

public class SynchroEvent extends AbstractEvent {
	private static final long serialVersionUID = 480096146703824993L;

	public SynchroEvent(Object source) {
		super(source);
	}

	public void sendTo(Object target) {
		SynchroEventListener listener = (SynchroEventListener) target;
		DeplSynchronisation depl = (DeplSynchronisation) this.getSource();
		if (depl.synchroStarted())
			listener.whenStartSynchro(this);
		else 
			listener.whenStopSynchro(this);
	}
}
