package model;

import events.SatelitteMoveListener;
import events.SatelliteMoved;

public class Balise extends ElementMobile implements SatelitteMoveListener {
	private boolean isSynchro;
	private boolean isSurface;
	private int message;
	private boolean estMessage;

	public Balise(int memorySize) {
		super(memorySize);
		this.isSynchro = true;
		this.isSurface = false;
		this.message = 0;
		this.estMessage = false;
	}

	public int profondeur() {
		return this.getPosition().y;
	}

	/**
	 * 
	 */
	protected void readSensors() {
		if (!this.memoryFull() && !this.isSurface) {
			this.dataSize++;
		}
		if (this.memoryFull() && !this.estMessage) {
			this.message = (1 + (int) (Math.random() * ((2000 - 1) + 1))) * 100;
			this.estMessage = true;
		}
	}

	/**
	 * 
	 */
	public void tick() {
		this.readSensors();
		if (this.memoryFull() && this.isSynchro) {
			Deplacement redescendre = new Redescendre(this.deplacement(), this.profondeur());
			Deplacement deplSynchro = new DeplSynchronisation(redescendre);
			Deplacement nextDepl = new MonteSurfacePourSynchro(deplSynchro);
			this.setDeplacement(nextDepl);
			this.isSynchro = false;
		}
		isSurface = false;
		super.tick();
	}

	public boolean isSynchro() {
		return isSynchro;
	}

	public void setSynchro(boolean isSynchro) {
		this.isSynchro = isSynchro;
	}

	@Override
	public void whenSatelitteMoved(SatelliteMoved arg) {
		DeplSynchronisation dp = (DeplSynchronisation) this.depl;
		dp.whenSatelitteMoved(arg, this);
		this.isSurface = true;
		if (this.isSynchro) {
			this.estMessage = false;
			this.message = 0;
			this.resetData();
		}
	}

	public int getMessage() {
		return message;
	}

}
