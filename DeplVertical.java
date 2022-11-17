package model;

import java.awt.Point;

public class DeplVertical extends Deplacement  {
	Integer min;
	Integer max;
	Boolean monte = false;

	public DeplVertical(Integer min, Integer max) {
		//super (null);
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void bouge(ElementMobile target) {
		Point p = target.getPosition();
		int y = p.y;
		if (monte) {
			y -= 3;
			if (y < min) monte = false;
			
		} else {
			y += 3;
			if (y > max) monte = true;
		}
		System.out.println(y);System.out.println(monte);
		target.setPosition(new Point(p.x, y));
	}

}
