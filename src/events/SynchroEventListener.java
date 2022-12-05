package events;

public interface SynchroEventListener {
	public void whenStartSynchro(SynchroEvent arg, boolean isCentral);
	public void whenStopSynchro(SynchroEvent arg);
}
