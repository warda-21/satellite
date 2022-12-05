package simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Balise;
import model.Central;
import model.DeplHorizontal;
import model.DeplSatellite;
import model.DeplVertical;
import model.Deplacement;
import model.Element;
import model.Manager;
import model.Satellite;
import nicellipse.component.NiRectangle;
import nicellipse.component.NiSpace;
import views.GrBalise;
import views.GrCentral;
import views.GrElementMobile;
import views.GrEther;
import views.GrSatellite;

public class Simulation {
	final int FPS_MIN = 2;
	final int FPS_MAX = 500;
	final int FPS_INIT = 10;
	final int startDelay = 500 / FPS_INIT;
	Timer animation;
	Manager manager = new Manager();
	Dimension worldDim = new Dimension(1000, 700);
	NiSpace world = new NiSpace("Satellite & Balises", this.worldDim);
	GrEther ether = new GrEther();

	public void animation() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manager.tick();
				ether.repaint();

			}
		};
		this.animation = new Timer(this.startDelay, taskPerformer);
		this.animation.setRepeats(true);
		this.animation.start();
	}
	/**
	 * Creation Des buttons
	 * 
	 * @return {@link JPanel}
	 */
	private JPanel buttonPanel() {
		JButton start = new JButton("Start");
		JButton stop = new JButton("Pause");
		JButton save = new JButton("Save");

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.getCentral().enregistrementData();
				save.setEnabled(true);
			}
		});
		start.setEnabled(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animation.start();
				start.setEnabled(false);
				stop.setEnabled(true);
			}
		});

		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animation.stop();
				start.setEnabled(true);
				stop.setEnabled(false);
			}
		});
		// Lay out the buttons from left to right.
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(save);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(start);
		panel.add(Box.createHorizontalGlue());
		panel.add(this.fpsSliderPanel());
		panel.add(stop);
		return panel;
	}

	/**
	 * 
	 * @return {@link JPanel}
	 */
	private JPanel fpsSliderPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel label = new JLabel(" FPS :", JLabel.RIGHT);
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);

		framesPerSecond.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int fps = (int) source.getValue();
					int newDelay = 1000 / fps;
					animation.setDelay(newDelay);
					animation.setInitialDelay(newDelay * 10);
				}
			}
		});

		// Turn on labels at major tick marks.
		framesPerSecond.setMajorTickSpacing(50);
		framesPerSecond.setMinorTickSpacing(10);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(false);

		panel.add(label);
		panel.add(framesPerSecond);
		// panel.add(this.buttonPanel());
		return panel;
	}

	/**
	 * 
	 * @param sea
	 * @param memorySize
	 * @param startPos
	 * @param depl
	 */
	public void addBalise(JPanel sea, int memorySize, Point startPos, Deplacement depl) {
		Balise bal = new Balise(memorySize);
		bal.setPosition(startPos);
		bal.setDeplacement(depl);
		sea.add(addElement(bal, TypeElement.BALISE));
	}

	public void addCentral(JPanel central, int memorySize, Point startPos, int vitesse) {
		Central centrl = new Central(memorySize, "central");
		centrl.setPosition(startPos);
		central.add(addElement(centrl, TypeElement.CENTRAL));
	}

	/**
	 * Methode qui ajoute les satellites
	 * 
	 * @param sky        ciel
	 * @param memorySize taille memoire
	 * @param startPos   position du depart
	 * @param vitesse    vitesse du deplacement.
	 */
	public void addSatelitte(JPanel sky, int memorySize, Point startPos, int vitesse) {
		Satellite sat = new Satellite(memorySize);
		sat.setPosition(startPos);
		sat.setDeplacement(new DeplSatellite(-10, 1000, vitesse));
		sky.add(addElement(sat, TypeElement.SATELLITE));
	}

	

	public GrElementMobile addElement(Element sat, TypeElement typeElmt) {
		GrElementMobile grSat = null;
		switch (typeElmt) {
		case CENTRAL:
			grSat = new GrCentral(this.ether);
			manager.createCentral((Central) sat);
			break;
		case BALISE:
			grSat = new GrBalise(this.ether);
			manager.addElement((Balise) sat);
			break;
		case SATELLITE:
			grSat = new GrSatellite(this.ether);
			manager.addElement((Satellite) sat);
			break;
		}
		grSat.setModel(sat);
		return grSat;
	}

	public void launch() {
		JLayeredPane main = new JLayeredPane();
		main.setOpaque(true);
		main.setSize(this.worldDim);

		this.ether.setBorder(null);
		this.ether.setBackground(Color.gray);
		this.ether.setOpaque(false);
		this.ether.setDimension(this.worldDim);

		NiRectangle sky = new NiRectangle();
		sky.setBackground(Color.white);
		sky.setDimension(new Dimension(this.worldDim.width, this.worldDim.height / 2));
		NiRectangle sea = new NiRectangle();
		sea.setBackground(Color.blue);
		sea.setDimension(new Dimension(this.worldDim.width, this.worldDim.height / 2));
		sea.setLocation(new Point(50, this.worldDim.height / 2));

		// Satellite
		this.addSatelitte(sky, 100000, new Point(10, 50), 3);
		this.addSatelitte(sky, 100000, new Point(100, 10), 2);
		this.addSatelitte(sky, 100000, new Point(500, 140), 4);
		

		// Central
		this.addCentral(sky, 100000, new Point(-5, 230), 4);

		// Balise
		this.addBalise(sea, 200, new Point(0, 160), new DeplHorizontal(0, 800));
		this.addBalise(sea, 80, new Point(200, 100), new DeplVertical(130, 270));
		this.addBalise(sea, 150, new Point(300, 100), new DeplHorizontal(200, 600));

		main.add(sky, JLayeredPane.DEFAULT_LAYER);
		main.add(sea, JLayeredPane.DEFAULT_LAYER);
		main.add(this.ether, JLayeredPane.POPUP_LAYER);

		this.world.setLayout(new BoxLayout(this.world, BoxLayout.Y_AXIS));
		this.world.add(main);
		this.world.add(this.buttonPanel());
		this.world.openInWindow();
		this.animation();
	}

	public static void main(String[] args) {
		new Simulation().launch();
	}

}
