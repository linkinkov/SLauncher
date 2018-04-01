package ru.spark.slauncher.ui.editor;

import ru.spark.slauncher.ui.block.Blocker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusListener;

public class EditorFieldHandler extends EditorHandler {
	private final EditorField field;
	private final JComponent comp;

	public EditorFieldHandler(String path, JComponent component, FocusListener focus) {
		super(path);

		if(component == null)
			throw new NullPointerException("comp");

		if (!(component instanceof EditorField))
			throw new IllegalArgumentException();

		if (focus != null)
			addFocus(component, focus);

		this.comp = component;
		this.field = (EditorField) component;
	}

	public EditorFieldHandler(String path, JComponent comp) {
		this(path, comp, null);
	}

	@Override
	public JComponent getComponent() {
		return comp;
	}

	@Override
	public String getValue() {
		return field.getSettingsValue();
	}

	@Override
	protected void setValue0(String s) {
		field.setSettingsValue(s);
	}

	@Override
	public boolean isValid() {
		return field.isValueValid();
	}

	private void addFocus(Component comp, FocusListener focus) {
		comp.addFocusListener(focus);

		if (comp instanceof Container)
			for (Component curComp : ((Container) comp).getComponents())
				addFocus(curComp, focus);
	}

	@Override
	public void block(Object reason) {
		Blocker.blockComponents(reason, getComponent());
	}

	@Override
	public void unblock(Object reason) {
		Blocker.unblockComponents(reason, getComponent());
	}
}
