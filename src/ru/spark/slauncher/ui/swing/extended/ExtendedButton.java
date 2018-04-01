package ru.spark.slauncher.ui.swing.extended;

import ru.spark.slauncher.ui.SLauncherFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExtendedButton extends JButton {
	private static final long serialVersionUID = -2009736184875993130L;

	protected ExtendedButton() {
		super();
		init();
	}

	public ExtendedButton(Icon icon) {
		super(icon);
		init();
	}

	protected ExtendedButton(String text) {
		super(text);
		init();
	}

	public ExtendedButton(Action a) {
		super(a);
		init();
	}

	public ExtendedButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

	private void init() {
		this.setFont(getFont().deriveFont(SLauncherFrame.fontSize));
		this.setOpaque(false);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Component parent = findRootParent(getParent());
				
				if (parent == null)
					return;
				
				parent.requestFocusInWindow();
			}

			private Component findRootParent(Component comp) {
				if (comp == null)
					return null;
				if (comp.getParent() == null)
					return comp;

				return findRootParent(comp.getParent());
			}

		});
	}

}
