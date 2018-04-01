package ru.spark.slauncher.ui.loc;

import javax.swing.*;
import java.awt.event.ItemListener;

public class LocalizableRadioButton extends JRadioButton implements
		LocalizableComponent {
	private static final long serialVersionUID = 1L;

	private String path;

	public LocalizableRadioButton() {
		init();
	}

	public LocalizableRadioButton(String path) {
		init();
		this.setLabel(path);
	}

	@Override
	@Deprecated
	public void setLabel(String path) {
		this.setText(path);
	}

	@Override
	public void setText(String path) {
		this.path = path;
		super.setText((Localizable.get() == null) ? path : Localizable.get()
				.get(path));
	}

	public String getLangPath() {
		return path;
	}

	public void addListener(ItemListener l) {
		super.getModel().addItemListener(l);
	}

	public void removeListener(ItemListener l) {
		super.getModel().removeItemListener(l);
	}

	@Override
	public void updateLocale() {
		this.setLabel(path);
	}

	private void init() {
		this.setOpaque(false);
	}
}
