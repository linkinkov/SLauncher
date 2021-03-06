package ru.spark.slauncher.ui.swing.extended;

import java.awt.*;
import java.awt.event.ComponentEvent;

public class ExtendedComponentAdapter extends ExtendedComponentListener {

	public ExtendedComponentAdapter(Component component, int tick) {
		super(component, tick);
	}
	
	public ExtendedComponentAdapter(Component component) {
		super(component);
	}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void onComponentResizing(ComponentEvent e) {}

	@Override
	public void onComponentResized(ComponentEvent e) {}

	@Override
	public void onComponentMoving(ComponentEvent e) {}

	@Override
	public void onComponentMoved(ComponentEvent e) {}

}
