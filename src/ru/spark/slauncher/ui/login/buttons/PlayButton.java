package ru.spark.slauncher.ui.login.buttons;

import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.loc.LocalizableButton;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.login.LoginForm.LoginState;
import ru.spark.slauncher.ui.login.LoginForm.LoginStateListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayButton extends LocalizableButton implements Blockable, LoginStateListener {
	private static final long serialVersionUID = 6944074583143406549L;

	private PlayButtonState state;

	private final LoginForm loginForm;

	public PlayButton(LoginForm lf) {
		this.loginForm = lf;

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(state) {
				case CANCEL:
					loginForm.stopLauncher();
					break;
				default:
					loginForm.startLauncher();
					break;

				}
			}
		});
		this.setIcon(null);
		this.setFont(getFont().deriveFont(Font.BOLD).deriveFont(16.0F));
		this.setState(PlayButtonState.PLAY);
	}

	public PlayButtonState getState() {
		return state;
	}

	public void setState(PlayButtonState state) {
		if (state == null)
			throw new NullPointerException();

		this.state = state;
		this.setText(state.getPath());

		if(state == PlayButtonState.CANCEL)
			setEnabled(true);
	}

	public void updateState() {
		VersionSyncInfo vs = loginForm.versions.getVersion();

		if (vs == null)
			return;

		boolean
		installed = vs.isInstalled();

		if (!installed)
			setState(PlayButtonState.INSTALL);
		else
			setState(PlayButtonState.PLAY);
	}

	public enum PlayButtonState {
		REINSTALL("loginform.enter.reinstall"),
		INSTALL("loginform.enter.install"),
		PLAY("loginform.enter"),
		CANCEL("loginform.enter.cancel");

		private final String path;

		PlayButtonState(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}

	@Override
	public void loginStateChanged(LoginState state) {
		if(state == LoginState.LAUNCHING) {
			setState(PlayButtonState.CANCEL);
		} else {
			updateState();
			setEnabled(!Blocker.isBlocked(this));
		}
	}

	@Override
	public void block(Object reason) {
		if(state != PlayButtonState.CANCEL)
			setEnabled(false);
	}

	@Override
	public void unblock(Object reason) {
		setEnabled(true);
	}
}
