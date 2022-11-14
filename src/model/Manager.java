package model;

import java.util.ArrayList;

import events.SatelliteMoved;

public class Manager {
	ArrayList<Satellite> sats = new ArrayList<Satellite>();
	ArrayList<Balise> bals = new ArrayList<Balise>();
	
	public void addBalise(Balise bal) {
		bals.add(bal);
		bal.setManager(this);
	}
	public void addSatellite(Satellite sat) {
		this.sats.add(sat);
		sat.setManager(this);
	}
	public void tick() {
		for (Balise b : this.bals) {
			b.tick();
		}
		for (Satellite s : this.sats) {
			s.tick();
		}
	}
	
	public void baliseReadyForSynchro(Balise b) {
		for (Satellite s : this.sats) {			
			s.registerListener(SatelliteMoved.class, b);
		}
	}
	public void baliseSynchroDone(Balise b) {
		for (Satellite s : this.sats) {			
			s.unregisterListener(SatelliteMoved.class, b);
		}
	}

}
