package ru.spark.slauncher.ui.center;

import java.awt.*;

public class SettingsPanelTheme extends DefaultCenterPanelTheme {
	protected final Color panelBackgroundColor = new Color(255, 255, 255, 128);

	protected final Color borderColor = new Color(255, 255, 255, 128);
	protected final Color delPanelColor = new Color(50, 80, 190, 255);

	@Override
	public Color getPanelBackground() {
		return panelBackgroundColor;
	}

	@Override
	public Color getBorder() {
		return borderColor;
	}

	@Override
	public Color getDelPanel() {
		return delPanelColor;
	}

}
