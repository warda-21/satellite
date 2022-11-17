# satellite
## 1. Explication et résolution du beug:
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
Cette modification implique une nouvelle méthode ajouté dans la Class Manager qui est _ _addElement_ _ qui permets d'ajouter un élément mobile peu importe son type.
