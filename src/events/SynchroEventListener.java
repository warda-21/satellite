package events;

public interface SynchroEventListener {
	public void whenStartSynchro(SynchroEvent arg);
	public void whenStopSynchro(SynchroEvent arg);
}
