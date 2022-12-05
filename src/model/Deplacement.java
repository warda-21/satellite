package model;

public abstract class Deplacement {
	private boolean isCentral;
	public boolean isCentral() {
		return isCentral;
	}
	public void setCentral(boolean isCentral) {
		this.isCentral = isCentral;
	}
	abstract public void bouge(ElementMobile target) ;
	abstract public Boolean synchroStarted();
	public Deplacement replacement() { return this; }
}
