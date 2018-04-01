package ru.spark.slauncher.ui.animate;

import java.awt.*;

public class Animator {
	private final static int DEFAULT_TICK = 20;

	public static void move(Component comp, int destX, int destY, int tick) {
		comp.setLocation(destX, destY);
	}

	public static void move(Component comp, int destX, int destY) {
		move(comp, destX, destY, DEFAULT_TICK);
	}
}
