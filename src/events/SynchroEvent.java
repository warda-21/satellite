package events;

import eventHandler.AbstractEvent;
import model.Deplacement;

public class SynchroEvent extends AbstractEvent {
	private static final long serialVersionUID = 480096146703824993L;

	public SynchroEvent(Object source) {
		super(source);
	}

	public void sendTo(Object target) {
		SynchroEventListener listener = (SynchroEventListener) target;
		Deplacement depl = (Deplacement) this.getSource();
		if (depl.synchroStarted())
			listener.whenStartSynchro(this, depl.isCentral());
		else
			listener.whenStopSynchro(this);

	}
}
