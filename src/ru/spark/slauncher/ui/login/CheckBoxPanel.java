package ru.spark.slauncher.ui.login;

import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.block.BlockablePanel;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.loc.LocalizableCheckbox;
import ru.spark.slauncher.ui.login.LoginForm.LoginProcessListener;
import ru.spark.slauncher.ui.swing.CheckBoxListener;
import ru.spark.util.U;

import javax.swing.*;

public class CheckBoxPanel extends BlockablePanel implements LoginProcessListener {
	private static final long serialVersionUID = 768489049585749260L;
	private static final String[] phrases = {
			"OH, SHI~, STOP IT!",
			"PLS NOOO!",
			"Y R U DOIN THIS?",
			"NOOOOOO!!!!"
	};

	public final LocalizableCheckbox autologin;

	public final LocalizableCheckbox forceupdate;
	private boolean state;

	private final LoginForm loginForm;

	CheckBoxPanel(LoginForm lf) {
		BoxLayout lm = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(lm);
		setOpaque(false);
		setAlignmentX(CENTER_ALIGNMENT);

		this.loginForm = lf;

		autologin = new LocalizableCheckbox("loginform.checkbox.autologin",
				lf.global.getBoolean("login.auto"));
		autologin.addItemListener(new CheckBoxListener() {
			@Override
			public void itemStateChanged(boolean newstate) {
				//loginForm.autologin.setEnabled(newstate);
				if (newstate)
					Alert.showLocAsyncMessage(
							"loginform.checkbox.autologin.tip.title",
							"loginform.checkbox.autologin.tip",
							Localizable
									.get("loginform.checkbox.autologin.tip.arg"));
			}
		});

		forceupdate = new LocalizableCheckbox("loginform.checkbox.forceupdate");
		forceupdate.addItemListener(new CheckBoxListener() {
			private byte clicks = 0;

			@Override
			public void itemStateChanged(boolean newstate) {
				if(++clicks == 10) {
					forceupdate.setText(U.getRandom(phrases));
					clicks = 0;
				}

				state = newstate;
				//loginForm.buttons.play.updateState();
			}
		});

		add(autologin);
		add(Box.createHorizontalGlue());
		add(forceupdate);
	}

	@Override
	public void logginingIn() throws LoginException {
		VersionSyncInfo syncInfo = loginForm.versions.getVersion();

		if (syncInfo == null)
			return; // Will be caught in the next listener

		boolean supporting = syncInfo.hasRemote(), installed = syncInfo
				.isInstalled();

		if (state) {
			if (!supporting) {
				Alert.showLocError("forceupdate.local");
				throw new LoginException("Cannot update local version!");
			}

			if (installed && !Alert.showLocQuestion("forceupdate.question"))
				throw new LoginException("User has cancelled force updating.");
		}
	}

	@Override
	public void loginFailed() {
	}

	@Override
	public void loginSucceed() {
	}

}

