package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import events.SatelliteMoved;

public class Central extends Element {
	List<Integer> datas;
	String name;

	public Central(int memorySize, String nom) {
		super(memorySize);
		this.name = nom;
		this.datas = new ArrayList<>();
	}

	public void tick() {
		this.send(new SatelliteMoved(this));
	}
	
	public boolean dataIsNoVide() {
		if (datas == null) {
			return false;
		}else {
			if(datas.size()==0) {
				return false;
			}
			return true;
		}
	}

	public void addListData(List<Integer> dataList) {
		if (this.datas == null) {
			this.datas = new ArrayList<>();
		}
		for (int data : dataList) {
			this.datas.add(data);
		}
	}

	public void enregistrementData() {
		if (this.datas == null)
			return;
		Collections.sort(this.datas);
		String information = "";
		for (int data : this.datas) {
			information += "Satellite enregiste data => " + data + " \n";
		}
		Rapport rapport = new Rapport();
		rapport.genererRapport(information, this.datas.size());
	}

}
