package ru.spark.slauncher.ui.editor;

import ru.spark.slauncher.ui.center.CenterPanel;
import ru.spark.slauncher.ui.center.CenterPanelTheme;
import ru.spark.slauncher.ui.images.ImageCache;
import ru.spark.slauncher.ui.images.ImageIcon;
import ru.spark.slauncher.ui.loc.LocalizableLabel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEditorPanel extends CenterPanel {
	protected final List<EditorHandler> handlers;

	public AbstractEditorPanel(CenterPanelTheme theme, Insets insets) {
		super(theme, insets);

		this.handlers = new ArrayList<EditorHandler>();
	}

	public AbstractEditorPanel(Insets insets) {
		this(null, insets);
	}

	public AbstractEditorPanel() {
		this(null, null);
	}

	protected boolean checkValues() {
		boolean allValid = true;

		for (EditorHandler handler : handlers) {
			boolean valid = handler.isValid();

			setValid(handler, valid);

			if (!valid)
				allValid = false;
		}

		return allValid;
	}

	protected void setValid(EditorHandler handler, boolean valid) {
		Color color = valid? getTheme().getBackground() : getTheme().getFailure();
		handler.getComponent().setBackground(color);
	}

	protected JComponent createTip(String label, boolean warning) {
		LocalizableLabel tip = new LocalizableLabel(label);

		if(warning)
			ImageIcon.setup(tip, ImageCache.getIcon("warning.png", 16, 16));

		return tip;
	}
}
