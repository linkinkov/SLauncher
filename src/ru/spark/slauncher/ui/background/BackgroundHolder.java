package ru.spark.slauncher.ui.background;

import ru.spark.slauncher.ui.MainPane;
import ru.spark.slauncher.ui.background.slide.SlideBackground;
import ru.spark.slauncher.ui.swing.extended.ExtendedLayeredPane;

import java.awt.*;

public class BackgroundHolder extends ExtendedLayeredPane {
	private static final long serialVersionUID = 8722087129402330131L;

	public final MainPane pane;
	private Background currentBackground;

	public final BackgroundCover cover;

	public final SlideBackground SLIDE_BACKGROUND;

	public BackgroundHolder(MainPane parent) {
		super(parent);

		this.pane = parent;
		this.cover = new BackgroundCover(this);

		this.SLIDE_BACKGROUND = new SlideBackground(this);

		add(cover, Integer.valueOf(Integer.MAX_VALUE));
	}

	public Background getBackgroundPane() {
		return currentBackground;
	}

	public void setBackground(Background background, boolean animate) {
		if (background == null)
			throw new NullPointerException();

		Color coverColor = background.getCoverColor();
		if (coverColor == null)
			coverColor = Color.black;

		cover.setColor(coverColor, animate);

		cover.makeCover(animate);

		if (currentBackground != null)
			remove(currentBackground);
		currentBackground = background;
		add(currentBackground);

		cover.removeCover(animate);
	}

	public void showBackground() {
		cover.removeCover();
	}

	public void hideBackground() {
		cover.makeCover();
	}

	public void startBackground() {
		if (currentBackground == null)
			return;

		if (currentBackground instanceof AnimatedBackground)
			((AnimatedBackground) currentBackground).startBackground();
	}

	public void suspendBackground() {
		if (currentBackground == null)
			return;

		if (currentBackground instanceof AnimatedBackground)
			((AnimatedBackground) currentBackground).suspendBackground();
	}

	public void stopBackground() {
		if (currentBackground == null)
			return;

		if (currentBackground instanceof AnimatedBackground)
			((AnimatedBackground) currentBackground).stopBackground();
	}

}
