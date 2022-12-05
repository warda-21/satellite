package model;

import events.SatelliteMoved;
import events.SynchroEvent;

public class DeplSynchronisation extends Deplacement {
	private int synchroTime;
	private Satellite synchro;
	protected Deplacement next;
	
	public Boolean synchroStarted() {
		return this.synchro != null;
	}
	
	public DeplSynchronisation(Deplacement next) {
		this.next = next;
		this.setCentral(false);
		this.synchroTime = 10;
		this.synchro = null;
	}
	
	//@Override
	public void whenSatelitteMoved(SatelliteMoved arg, Balise target) {
		if (this.synchro != null) return;
		Satellite sat = (Satellite) arg.getSource();
		int satX = sat.getPosition().x;
		int tarX = target.getPosition().x;
		if (satX > tarX - 10 && satX < tarX + 10) {
			this.synchro = sat;
			target.send(new SynchroEvent(this));
			this.synchro.send(new SynchroEvent(this));
			sat.addData(target.getMessage());
			target.setSynchro(true);
		}
	}
	

	@Override
	public void bouge(ElementMobile target) {
		Balise balise = (Balise) target;
		if (this.synchro == null) return;
		this.synchroTime--;
		if (synchroTime <= 0) {
			Satellite sat = this.synchro;
			this.synchro = null;
			this.synchroTime = 10;
			balise.send(new SynchroEvent(this));
			sat.send(new SynchroEvent(this));
			balise.getManager().baliseSynchroDone(balise);
			balise.setDeplacement(next);
		}		
	}
}
