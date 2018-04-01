package ru.spark.slauncher.ui.center;

import java.awt.*;

public class DefaultCenterPanelTheme extends CenterPanelTheme {
	protected final Color backgroundColor = new Color(255, 255, 255, 255); // White

	protected final Color panelBackgroundColor = new Color(215, 215, 215, 128); // Half-white

	protected final Color focusColor = new Color(0, 0, 0, 255); // Black
	protected final Color focusLostColor = new Color(128, 128, 128, 255); // Gray

	protected final Color successColor = new Color(78, 196, 78, 255); // Green
	protected final Color failureColor = new Color(255, 255, 255, 128); // Pink

	protected final Color borderColor = new Color(255, 255, 255, 128);//new Color(28, 128, 28, 255); // Dark green
	protected final Color delPanelColor = successColor;

	@Override
	public Color getBackground() {
		return backgroundColor;
	}

	@Override
	public Color getPanelBackground() {
		return panelBackgroundColor;
	}

	@Override
	public Color getFocus() {
		return focusColor;
	}

	@Override
	public Color getFocusLost() {
		return focusLostColor;
	}

	@Override
	public Color getSuccess() {
		return successColor;
	}

	@Override
	public Color getFailure() {
		return failureColor;
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
