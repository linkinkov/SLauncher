package ru.spark.slauncher.ui.editor;

import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.loc.LocalizableHTMLLabel;
import ru.spark.slauncher.ui.loc.LocalizableLabel;
import ru.spark.slauncher.ui.swing.extended.BorderPanel;
import ru.spark.slauncher.ui.swing.extended.ExtendedLabel;
import ru.spark.slauncher.ui.swing.extended.ExtendedPanel;
import ru.spark.util.IntegerArray;

import javax.swing.*;
import java.awt.*;

public class EditorResolutionField extends BorderPanel implements
EditorField {
	private static final long serialVersionUID = -5565607141889620750L;

	private EditorIntegerField w;
	private EditorIntegerField h;
	private ExtendedLabel x;

	private final LocalizableLabel hint;
	private final int[] defaults;

	public EditorResolutionField(String promptW, String promptH, int[] defaults, boolean showDefault) {
		if (defaults == null)
			throw new NullPointerException();
		if (defaults.length != 2)
			throw new IllegalArgumentException("Illegal array size");

		this.defaults = defaults;

		ExtendedPanel container = new ExtendedPanel();
		container.setAlignmentX(CENTER_ALIGNMENT);
		container.setAlignmentY(CENTER_ALIGNMENT);

		this.w = new EditorIntegerField(promptW);
		w.setColumns(4);
		w.setHorizontalAlignment(SwingConstants.CENTER);

		this.h = new EditorIntegerField(promptH);
		h.setColumns(4);
		h.setHorizontalAlignment(SwingConstants.CENTER);

		this.x = new ExtendedLabel("X", SwingConstants.CENTER);

		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;

		c.gridx = 0;
		c.weightx = 0.5;
		c.insets.set(0, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		container.add(w, c);

		c.gridx = 1;
		c.weightx = 0;
		c.insets.set(0, 5, 0, 5);
		c.fill = GridBagConstraints.VERTICAL;
		container.add(x, c);

		c.gridx = 2;
		c.weightx = 0.5;
		c.insets.set(0, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		container.add(h, c);

		setCenter(container);

		hint = new LocalizableHTMLLabel("settings.res.def", defaults[0], defaults[1]);
		hint.setFont(hint.getFont().deriveFont(hint.getFont().getSize() - 2F));

		if(showDefault)
			setSouth(hint);
	}

	@Override
	public String getSettingsValue() {
		return w.getSettingsValue() + IntegerArray.defaultDelimiter
				+ h.getSettingsValue();
	}

	int[] getResolution() {
		try {
			IntegerArray arr = IntegerArray
					.parseIntegerArray(getSettingsValue());
			return arr.toArray();
		} catch (Exception e) {
			return new int[2];
		}
	}

	@Override
	public boolean isValueValid() {
		int[] size = getResolution();

		return size[0] >= 1 && size[1] >= 1;
	}

	@Override
	public void setSettingsValue(String value) {
		String width, height;

		try {
			IntegerArray arr = IntegerArray.parseIntegerArray(value);
			width = String.valueOf(arr.get(0));
			height = String.valueOf(arr.get(1));
		} catch (Exception e) {
			width = "";
			height = "";
		}

		w.setText(width);
		h.setText(height);
	}

	@Override
	public void setBackground(Color bg) {
		if (w != null)
			w.setBackground(bg);
		if (h != null)
			h.setBackground(bg);
	}

	@Override
	public void block(Object reason) {
		Blocker.blockComponents(reason, w, h);
	}

	@Override
	public void unblock(Object reason) {
		Blocker.unblockComponents(Blocker.UNIVERSAL_UNBLOCK, w, h);
	}

}
