package simulation;

import java.awt.Point;

import model.Balise;
import model.Deplacement;
import model.DeplacementBaliseTODELETE;

public class Monter extends DeplacementBaliseTODELETE implements DeplacerenProf {

	public Monter(Deplacement next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void bouge(Balise target) {
		// TODO Auto-generated method stub
		Point p = target.getPosition();
		int y = p.y;
		if (y > 0) {
			y -= 3;
			if (y < 0) y = 0;
			target.setPosition(new Point(p.x, y));
		} else {
			target.getManager().baliseReadyForSynchro((Balise) target);
			target.setDeplacement(this.next);			
		}
	}

}
