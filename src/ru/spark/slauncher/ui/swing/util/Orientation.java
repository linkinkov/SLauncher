package ru.spark.slauncher.ui.swing.util;

import javax.swing.*;

public enum Orientation {
	TOP(SwingConstants.TOP), LEFT(SwingConstants.LEFT), BOTTOM(SwingConstants.BOTTOM),
	RIGHT(SwingConstants.RIGHT), CENTER(SwingConstants.CENTER);

	private final int swingAlias;

	Orientation(int swingAlias) {
		this.swingAlias = swingAlias;
	}

	public int getSwingAlias() {
		return swingAlias;
	}

	public static Orientation fromSwingConstant(int orientation) {
		for(Orientation current : values())
			if(orientation == current.getSwingAlias())
				return current;
		return null;
	}

}
