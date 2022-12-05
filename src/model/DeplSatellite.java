package model;

import java.awt.Point;

import events.SatelliteMoved;
import events.SynchroEvent;

public class DeplSatellite extends Deplacement {
	Integer start;
	Integer end;
	int vitesse;
	
	private int synchroTime;
	private Central synchro;

	@Override
	public Boolean synchroStarted() {
		return this.synchro != null;
	}

	public DeplSatellite(Integer start, Integer end, int vitesse) {
		this.start = start;
		this.end = end;
		this.vitesse = vitesse;
		this.synchroTime = 10;
		this.synchro = null;
		this.setCentral(true);
	}

	@Override
	public void bouge(ElementMobile target) {
		Point p = target.getPosition();
		int x = p.x;
		x += vitesse;
		if (x > end) {
			x = start;
			target.getManager().baliseReadyForSynchro((Satellite) target);
		}
		target.setPosition(new Point(x, p.y));
		this.synchro((Satellite)target);
	}
	
	public void synchro(Satellite target) {
		if (this.synchro == null)
			return;
		this.synchroTime--;
		if (synchroTime <= 0) {
			Central sat = this.synchro;
			this.synchro = null;
			this.synchroTime = 10;
			target.send(new SynchroEvent(this));
			sat.send(new SynchroEvent(this));
			target.getManager().baliseSynchroDone(target);
		}
	}
	
	
	public void whenSatelitteMoved(SatelliteMoved arg, Satellite target) {
		
		if (this.synchro != null)
			return;
		if (target.getDatas() == null )
			return;
		Central central = (Central) arg.getSource();
		this.synchro = central;
		int satX = central.getPosition().x;
		int tarX = target.getPosition().x;
		if (satX > tarX - 10 && satX < tarX + 10) {
			target.send(new SynchroEvent(this));
			this.synchro.send(new SynchroEvent(this));
			target.setSynchro(true);
			central.addListData(target.getDatas());			
		}
		
		
	}

}
