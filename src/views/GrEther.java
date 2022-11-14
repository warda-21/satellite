package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import nicellipse.component.NiRectangle;

public class GrEther extends NiRectangle {
	private static final long serialVersionUID = -7500849956681054391L;

	List<GrElementMobile> q;
	
	public GrEther() {
		q = new ArrayList<>();
	}
	
	public void startSync(GrElementMobile elem) {
		q.add(elem);
		this.repaint();
	}
	public void stopSync(GrElementMobile elem) {
		q.remove(elem);
		this.repaint();
	}
	
	public void paintSynchronisation(Graphics2D g, GrElementMobile e) {
		Rectangle bounds = e.getBounds();
		Point l = e.getParent().getLocation();
		l.x += e.getLocation().x;
		l.y += e.getLocation().y;
		g.setColor(Color.ORANGE);
		g.setStroke(new BasicStroke(2));
		for (int i = 10; i < 150; i += 25) {
			g.drawOval(l.x-i,l.y-i,bounds.width+i+i,bounds.height+i+i);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (q.isEmpty()) return;
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		for (GrElementMobile sync : this.q) {
			this.paintSynchronisation(g2d, sync);
		}
		g2d.dispose();
	}

	
}
