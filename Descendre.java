package simulation;

import java.awt.Point;

import model.Balise;
import model.Deplacement;
import model.DeplacementBaliseTODELETE;

public class Descendre extends DeplacementBaliseTODELETE implements DeplacerenProf {
	int profondeur;
	
	public Descendre(Deplacement next) {
		super(next);
		// TODO Auto-generated constructor stub
	}
	

	public Descendre(Deplacement next, int profondeur) {
		super(next);
		this.profondeur = profondeur;
	}


	@Override
	public void bouge(Balise target) {
		// TODO Auto-generated method stub
		Point p = target.getPosition();
		int y = p.y;
		if (y <= this.profondeur) {
		
			y += 3;
			if (y > this.profondeur) y = this.profondeur;
			target.setPosition(new Point(p.x, y));
		}  else {
			target.setDeplacement(next);
		}
	}

}
