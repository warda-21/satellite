package model;

import events.SatelliteMoved;
import events.SynchroEvent;

public class DeplSynchronisation extends DeplacementBalise {
	private int synchroTime;
	private Satellite synchro;
	
	public Boolean synchroStarted() {
		return this.synchro != null;
	}
	
	public DeplSynchronisation(Deplacement next) {
		super(next);
		this.synchroTime = 10;
		this.synchro = null;
	}
	
	@Override
	public void whenSatelitteMoved(SatelliteMoved arg, Balise target) {
		if (this.synchro != null) return;
		Satellite sat = (Satellite) arg.getSource();
		int satX = sat.getPosition().x;
		int tarX = target.getPosition().x;
		if (satX > tarX - 10 && satX < tarX + 10) {
			this.synchro = sat;
			target.send(new SynchroEvent(this));
			this.synchro.send(new SynchroEvent(this));
		}
	}

	@Override
	public void bouge(Balise target) {
		if (this.synchro == null) return;
		this.synchroTime--;
		if (synchroTime <= 0) {
			Satellite sat = this.synchro;
			this.synchro = null;
			this.synchroTime = 10;
			target.send(new SynchroEvent(this));
			sat.send(new SynchroEvent(this));
			target.getManager().baliseSynchroDone(target);
			target.setDeplacement(next);
		}		
	}
}
