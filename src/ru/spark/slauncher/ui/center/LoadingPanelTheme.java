package ru.spark.slauncher.ui.center;

import java.awt.*;

public class LoadingPanelTheme extends DefaultCenterPanelTheme {
	protected final Color panelBackgroundColor = new Color(255, 255, 255, 168); // Half-white

	@Override
	public Color getPanelBackground() {
		return panelBackgroundColor;
	}
}
