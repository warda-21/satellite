package views;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nicellipse.component.NiImage;

public class GrBalise extends GrElementMobile {
	private static final long serialVersionUID = -8672390241177685075L;

	public GrBalise(GrEther ether) {
		super(ether);
		File path = new File("balise.png");
		BufferedImage rawImage = null;
		try {
			rawImage = ImageIO.read(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.add(new NiImage(rawImage));
		this.setDimension(new Dimension(rawImage.getWidth(), rawImage.getHeight()));
	}

}
