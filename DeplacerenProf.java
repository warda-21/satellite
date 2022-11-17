package simulation;

import model.Balise;

public interface DeplacerenProf {
	/**
	 * Strategy pour garder qu'une seule fonction descendre et remonter pour une balise
	 * @param target
	 */
	public void bouge(Balise target);

}
