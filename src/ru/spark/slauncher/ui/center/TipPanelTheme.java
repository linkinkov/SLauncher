package ru.spark.slauncher.ui.center;

import java.awt.*;

public class TipPanelTheme extends DefaultCenterPanelTheme {
	private final Color borderColor = failureColor;

	@Override
	public Color getBorder() {
		return borderColor;
	}

}
