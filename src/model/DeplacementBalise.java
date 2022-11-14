package model;

import events.SatelliteMoved;

public class DeplacementBalise extends Deplacement {

	protected Deplacement next;
	
	public DeplacementBalise (Deplacement next) {
		this.next = next;
	}
	
	public void bouge(Balise target) {
	}

	@Override
	public void bouge(ElementMobile target) {
		this.bouge((Balise) target);
	}

	public void whenSatelitteMoved(SatelliteMoved arg, Balise target) { }

}
