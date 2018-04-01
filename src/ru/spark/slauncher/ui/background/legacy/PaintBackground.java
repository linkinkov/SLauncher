package ru.spark.slauncher.ui.background.legacy;

import ru.spark.slauncher.ui.background.Background;
import ru.spark.slauncher.ui.background.BackgroundHolder;

import java.awt.*;
import java.awt.image.VolatileImage;

abstract class PaintBackground extends Background {
	private static final long serialVersionUID = 1251234865840478018L;

	int width;
	int height;
	double relativeSize = 1;
	private VolatileImage vImage;

	protected PaintBackground(BackgroundHolder holder) {
		super(holder, Color.black);
	}

	@Override
	public void update(Graphics g0) {
		super.update(g0);
	}

	@Override
	public void paintBackground(Graphics g0) {
		g0.drawImage(draw(g0), 0, 0, getWidth(), getHeight(), null);
	}

	VolatileImage draw(Graphics g0) {
		int iw = getWidth(), w = (int) (iw * relativeSize), ih = getHeight(), h = (int) (ih * relativeSize);
		boolean force = w != width || h != height;

		width = w;
		height = h;
		if (vImage == null || vImage.getWidth() != w || vImage.getHeight() != h)
			vImage = createVolatileImage(w, h);

		Graphics2D g = (Graphics2D) vImage.getGraphics();

		this.draw(g, force);

		vImage.validate(getGraphicsConfiguration());

		return vImage;
	}

	protected abstract void draw(Graphics2D g, boolean force);
}
