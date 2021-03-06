package ru.spark.slauncher.ui.background.slide;

import ru.spark.util.Reflect;
import ru.spark.util.U;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Slide {
	private final URL url;
	private Image image;

	public Slide(URL url) {
		if (url == null)
			throw new NullPointerException();

		this.url = url;

		if (isLocal())
			load();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		Slide slide = Reflect.cast(o, Slide.class);
		if (slide == null)
			return false;

		return url.equals(slide.url);
	}

	public URL getURL() {
		return url;
	}

	public boolean isLocal() {
		return url.getProtocol().equals("file");
	}

	public Image getImage() {
		if (image == null)
			load();
		return image;
	}

	private void load() {
		log("Loading from:", url);

		BufferedImage tempImage = null;

		try {
			tempImage = ImageIO.read(url);
		} catch (Throwable e) {
			log("Cannot load slide!", e);
			return;
		}

		if (tempImage == null) {
			log("Image seems to be corrupted.");
			return;
		}

		log("Loaded successfully!");
		this.image = tempImage;
	}

	protected void log(Object... w) {
		U.log("[" + getClass().getSimpleName() + "]", w);
	}
}
