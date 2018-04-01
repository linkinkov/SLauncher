package ru.spark.slauncher.ui.swing.extended;

import ru.spark.slauncher.ui.block.BlockableLayeredPane;
import ru.spark.slauncher.ui.swing.ResizeableComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public abstract class ExtendedLayeredPane extends BlockableLayeredPane
		implements ResizeableComponent {
	private static final long serialVersionUID = -1L;

	private Integer LAYER_COUNT = 0;
	protected final JComponent parent;

	protected ExtendedLayeredPane() {
		this.parent = null;
	}

	protected ExtendedLayeredPane(JComponent parent) {
		this.parent = parent;

		if (parent == null)
			return;

		parent.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				onResize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
				onResize();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	@Override
	public Component add(Component comp) {
		super.add(comp, LAYER_COUNT++);
		return comp;
	}

	public void add(Component... components) {
		if (components == null)
			throw new NullPointerException();

		for (Component comp : components)
			add(comp);
	}

	@Override
	public void onResize() {
		if (parent == null)
			return;

		setSize(parent.getWidth(), parent.getHeight());

		for (Component comp : getComponents())
			if (comp instanceof ResizeableComponent)
				((ResizeableComponent) comp).onResize();
	}
}
