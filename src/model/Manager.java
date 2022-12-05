package model;

import java.util.ArrayList;

import events.SatelliteMoved;

public class Manager {

	ArrayList<ElementMobile> listeelements = new ArrayList<ElementMobile>();

	Central central;

	public void addElement(ElementMobile element) {
		listeelements.add(element);
		element.setManager(this);
	}

	public void tick() {
		for (ElementMobile b : this.listeelements) {
			b.tick();
		}
		if (this.central != null) {
			this.central.tick();
		}
	}

	public Central getCentral() {
		return central;
	}

	public void createCentral(Central ctr) {
		this.central = ctr;
		ctr.setManager(this);
	}

	public void baliseReadyForSynchro(Balise b) {
		for (ElementMobile s : this.listeelements) {
			s.registerListener(SatelliteMoved.class, b);
		}

	}

	public void baliseSynchroDone(Balise b) {
		for (ElementMobile s : this.listeelements) {
			s.unregisterListener(SatelliteMoved.class, b);
		}
	}

	/**
	 * Balise pret pour a synchronisation Méthode permettant d'enregistrer la balise
	 * dans le listener de tous satelites pour attendre les info.
	 * 
	 * @param b {@link Balise}
	 */

	public void baliseReadyForSynchro(Satellite b) {
		central.registerListener(SatelliteMoved.class, b);
	}

	public void baliseSynchroDone(Satellite b) {
		central.unregisterListener(SatelliteMoved.class, b);
	}
	/**
	 * Balise pret pour a synchronisation Méthode permettant supprimer la balise
	 * dans le listener de tous satelites pour attendre les info.
	 * 
	 * @param b {@link Balise}
	 */

}
