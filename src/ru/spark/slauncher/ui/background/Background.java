package ru.spark.slauncher.ui.background;

import ru.spark.slauncher.ui.swing.extended.ExtendedLayeredPane;

import java.awt.*;

public abstract class Background extends ExtendedLayeredPane {
	private static final long serialVersionUID = -1353975966057230209L;

	protected Color coverColor;

	public Background(BackgroundHolder holder, Color coverColor) {
		super(holder);

		this.coverColor = coverColor;
	}

	public Color getCoverColor() {
		return coverColor;
	}

	@Override
	public final void paint(Graphics g) {
		this.paintBackground(g);
		super.paint(g);
	}

	public abstract void paintBackground(Graphics g);
}
