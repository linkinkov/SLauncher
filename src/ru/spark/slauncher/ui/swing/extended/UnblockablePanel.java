package ru.spark.slauncher.ui.swing.extended;

import ru.spark.slauncher.ui.block.Unblockable;

import java.awt.*;

public class UnblockablePanel extends ExtendedPanel implements Unblockable {
	private static final long serialVersionUID = -5273727580864479391L;
	
	public UnblockablePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public UnblockablePanel(LayoutManager layout) {
		super(layout);
	}

	public UnblockablePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public UnblockablePanel() {
	}

}
