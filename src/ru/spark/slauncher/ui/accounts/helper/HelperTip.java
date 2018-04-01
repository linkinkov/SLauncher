package ru.spark.slauncher.ui.accounts.helper;

import ru.spark.slauncher.ui.center.CenterPanel;
import ru.spark.slauncher.ui.loc.LocalizableLabel;

import java.awt.*;

class HelperTip extends CenterPanel {

	public final String name;
	public final LocalizableLabel label;
	public final Component component, parent;
	public final byte alignment;

	public final HelperState[] states;

	HelperTip(String name, Component component, Component parent,
			byte alignment, HelperState... states) {
		super(tipTheme, smallSquareInsets);

		if (name == null)
			throw new NullPointerException("Name is NULL");

		if (name.isEmpty())
			throw new IllegalArgumentException("Name is empty");

		if (component == null)
			throw new NullPointerException("Component is NULL");

		if (parent == null)
			throw new NullPointerException("Parent is NULL");

		if (alignment > AccountEditorHelper.DOWN)
			throw new IllegalArgumentException("Unknown alignment");

		if (states == null)
			throw new NullPointerException("State array is NULL");

		this.name = name;
		this.component = component;
		this.parent = parent;
		this.alignment = alignment;
		this.label = new LocalizableLabel();

		this.states = states;

		this.add(label);
	}
}
