package model;

import java.util.ArrayList;
import java.util.List;

import events.SatelitteMoveListener;
import events.SatelliteMoved;

public class Satellite extends ElementMobile implements SatelitteMoveListener {
	private List<Integer> datas;
	private boolean isSynchro;

	public void setSynchro(boolean isSynchro) {
		this.isSynchro = isSynchro;
	}

	public Satellite(int memorySize) {
		super(memorySize);
		this.isSynchro = true;
	}

	public void bouge() {
		super.bouge();
		this.send(new SatelliteMoved(this));
	}

	public void tick() {
		// System.out.println(this.datas);
		if (isSynchro) {
			this.isSynchro = false;
		}
		super.tick();
	}

	public List<Integer> getDatas() {
		return datas;
	}

	public void addData(int data) {
		if (this.datas == null) {
			this.datas = new ArrayList<>();
		}
		datas.add(data);

	}

	public void removeData() {
		this.datas = null;
	}

	@Override
	public void whenSatelitteMoved(SatelliteMoved arg) {
		DeplSatellite dpl = (DeplSatellite) this.depl;
		dpl.whenSatelitteMoved(arg, this);
		if (this.isSynchro) {
			this.removeData();
		}
	}
}
