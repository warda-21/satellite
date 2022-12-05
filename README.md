# satellite
## 1. Explication et résolution du bug:
Le problème c'est que quand une balise remonte sur la surface reste bloqué même après avoir communiqué avec le satellite, ceci  arrive quand dataSize atteind la valeur de memorySize, au lieu de transmettre les data au satellite , il on a dataSize qui est remise automatiquement à 0 ce qui cause un blocage sur la surface.
Comme solution on a suggéré deux variables booleans isSurface qui est remise à true si notre Balise est remonté et prête à envoyer les data et isSynchro qui est remise à true quand la balise et le satellite communiquent entre eux.
```java
...
public class Balise extends ElementMobile implements SatelitteMoveListener {
	private boolean isSynchro;
	private boolean isSurface;
public Balise(int memorySize) {
		super(memorySize);
		this.isSynchro = true;
		this.isSurface = false;
	}
...
public void tick() {
		System.out.println("Memory sise " + this.memorySize);
		System.out.println("Data size " + this.dataSize);
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
...
}
```
Ainsi dans la classe DeplSynchronisation
```java
...
public class DeplSynchronisation extends DeplacementBalise {
	private int synchroTime;
	private Satellite synchro;
	...
  public void whenSatelitteMoved(SatelliteMoved arg, Balise target) {
        if (this.synchro != null) return;
        Satellite sat = (Satellite) arg.getSource();
        int satX = sat.getPosition().x;
        int tarX = target.getPosition().x;
        if (satX > tarX - 10 && satX < tarX + 10) {
            this.synchro = sat;
            target.send(new SynchroEvent(this));
            this.synchro.send(new SynchroEvent(this));
            target.setSynchro(true);
        }
    }
    ...
    }
```
## 2. Amélioration des deux fonction addBalise et addSatellite:
Ces deux fonctions contiennent un code sémilaire dans la classe Simulation, ce qui donne la possibilité de le factoriser et creer qu'une seule fonction addElement :
```java
	public void addElement(JPanel pannel, int memorySize, Point startPos, Deplacement depl,boolean isbalise,int vitesse) {
		ElementMobile element=new ElementMobile(memorySize);
		GrElementMobile grbal=null;
		
		if (isbalise) {
			element = new Balise(memorySize);
			element.setDeplacement(depl);
			grbal = new GrBalise(this.ether);
		}else {
			element = new Satellite(memorySize);
			element.setDeplacement(new DeplSatellite(-10, 1000, vitesse));
			 grbal = new GrSatellite(this.ether);
		}
		element.setPosition(startPos);
		manager.addElement(element);
		grbal.setModel(element);
		pannel.add(grbal);
	}
	
```
Cette modification implique une nouvelle méthode ajouté dans la Class Manager qui est  _addElement_  qui permets d'ajouter un élément mobile peu importe son type(Satellite ou Balise).
## 3. Amélioration du code de la Class Manager :
```java 
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
		if (this.central != null) this.central.tick();
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
	public void baliseReadyForSynchro(Satellite b) {
		central.registerListener(SatelliteMoved.class, b);
	}
	public void baliseSynchroDone(Satellite b) {
		central.unregisterListener(SatelliteMoved.class, b);
	}
}
```


## 4. Ajout d'un élément graphique et une fonctionnalité:
Ajout d'une Centrale pour communiquer avec les satelites de la façon suivante: 
	Une balise envoit des informations à un Satelite , et ce dernier transmet les données reçus à La Centrale.
### a)Création de l'énumération TypeElement: 
```java 
package simulation;

public enum TypeElement {
	CENTRAL, BALISE, SATELLITE;

}
```
### b)Ajout des boutton Start et Pause pour commencer et arrêter la simulation: 
Dans la Class Simulation 
```java
...
private JPanel buttonPanel() {
		JButton start = new JButton("Start");
		JButton stop = new JButton("Pause");...
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
...
```
### c)Ajout du boutton Save  pour enregistrer les données actuel de notre simulation et envoyer un  rapport nommé "raport_satellite.txt" : 
Dans la Class simulation
```java
...
private JPanel buttonPanel() {
		JButton start = new JButton("Start");
		JButton stop = new JButton("Pause");
		JButton save = new JButton("Save");

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.getCentral().enregistrementData();
				save.setEnabled(true);
			}
		});....
...
```
 les données sont récupérées et enregistrés grace à la fonction suivante dans la Classe Centrale:
```java
 ...
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
...
```
 Et enfin le fichier rapport est généré  avec la Classe Rapport suivante :
```java
 public class Rapport {
	private String RAPPORT = "Le rapport du @date@ \n"
			+ "Nombre de données recoltées : @nbInfortion@ \n"
			+ "@Information@ "
			+ "=========================================================\n";
	private static final String NOM_FICHIER = "raport_satellite.txt";
	
	public void genererRapport(String information, int nbInf) {
		
		Date aujourdhui = new Date();
		DateFormat fullDateFormat = DateFormat.getDateTimeInstance(
		        DateFormat.FULL,
		        DateFormat.FULL);
		String date = fullDateFormat.format(aujourdhui);
		RAPPORT = RAPPORT.replaceAll("@date@", date);
		RAPPORT = RAPPORT.replaceAll("@nbInfortion@", ""+nbInf);
		RAPPORT = RAPPORT.replaceAll("@Information@", information);
		
		//System.out.println(RAPPORT);
		
		Path path = Paths.get(NOM_FICHIER);
		try {
			
			FileWriter myWriter = new FileWriter("filename.txt");
		      myWriter.write(RAPPORT);
		      myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
```
### d)Ajout de la méthode addCentral dans la simulation pour Creer la Centrale: 
```java 
...
public void addCentral(JPanel central, int memorySize, Point startPos, int vitesse) {
		Central centrl = new Central(memorySize, "central");
		centrl.setPosition(startPos);
		central.add(addElement(centrl, TypeElement.CENTRAL));
	}
	...
```

### e)Ajout du cas de la Centrale dans la méthode addElement dans la Simulation:
```java
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
```

### f)Ajout du cas de la Centrale dans Classe deplSatellit pour gérer la communication entre le Satellit et la Central: 

```java 
 ...
 public DeplSatellite(Integer start, Integer end, int vitesse) {
		this.start = start;
		this.end = end;
		this.vitesse = vitesse;
		this.synchroTime = 10;
		this.synchro = null;
		this.setCentral(true);
	}
 ...
```



## 5. Suite
