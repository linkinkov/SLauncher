package ru.spark.slauncher.ui.login.buttons;

import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.loc.LocalizableMenuItem;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.scenes.DefaultScene.SidePanel;
import ru.spark.slauncher.ui.swing.ImageButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsButton extends ImageButton implements Blockable {
	private static final long serialVersionUID = 1321382157134544911L;

	private final LoginForm lf;

	private final JPopupMenu popup;

	private final LocalizableMenuItem accountManager, versionManager, settings;

	SettingsButton(LoginForm loginform) {
		this.lf = loginform;

		this.image = loadImage("settings.png");
		this.rotation = ImageRotation.CENTER;

		this.popup = new JPopupMenu();

		this.settings = new LocalizableMenuItem("loginform.button.settings.launcher");
		settings.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lf.scene.setSidePanel(SidePanel.SETTINGS);
			}
		});
		popup.add(settings);

		this.versionManager = new LocalizableMenuItem("loginform.button.settings.version");
		versionManager.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//lf.pane.openVersionManager();
			}
		});
		popup.add(versionManager);

		this.accountManager = new LocalizableMenuItem("loginform.button.settings.account");
		accountManager.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//lf.pane.openAccountEditor();
			}
		});
		popup.add(accountManager);


		this.setPreferredSize(new Dimension(30, getHeight()));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				callPopup();
			}
		});

		this.initImage();
	}

	void callPopup() {
		lf.defocus();
		popup.show(this, 0, getHeight());
	}

	@Override
	public void block(Object reason) {
		if(reason.equals(LoginForm.AUTH_BLOCK) || reason.equals(LoginForm.LAUNCH_BLOCK))
			Blocker.blockComponents(reason, accountManager, versionManager);
	}

	@Override
	public void unblock(Object reason) {
		Blocker.unblockComponents(reason, accountManager, versionManager);
	}

}
