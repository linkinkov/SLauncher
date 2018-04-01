package ru.spark.slauncher.ui.block;

import ru.spark.slauncher.ui.swing.extended.ExtendedPanel;

import java.awt.*;

public class BlockablePanel extends ExtendedPanel implements Blockable {
	private static final long serialVersionUID = 1L;

	public BlockablePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public BlockablePanel(LayoutManager layout) {
		super(layout);
	}

	public BlockablePanel() {
		super();
	}

	@Override
	public void block(Object reason) {
		setEnabled(false);
		Blocker.blockComponents(this, reason);
	}

	@Override
	public void unblock(Object reason) {
		setEnabled(true);
		Blocker.unblockComponents(this, reason);
	}

}
