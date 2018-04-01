package ru.spark.slauncher.ui.login.buttons;

import ru.spark.slauncher.ui.block.Unblockable;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.swing.ImageButton;
import ru.spark.util.MinecraftUtil;
import ru.spark.util.OS;
import ru.spark.util.async.AsyncThread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FolderButton extends ImageButton implements Unblockable {
	final LoginForm lf;

	FolderButton(LoginForm loginform) {
		this.lf = loginform;

		this.image = loadImage("folder.png");

		final Runnable run = new Runnable() {
			@Override
			public void run() {
				OS.openFolder(MinecraftUtil.getWorkingDirectory());
			}
		};

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AsyncThread.execute(run);
			}
		});
	}
}
