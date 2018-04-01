package ru.spark.slauncher.ui.login.buttons;

import ru.spark.slauncher.ui.loc.LocalizableButton;
import ru.spark.slauncher.ui.login.LoginForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancelAutoLoginButton extends LocalizableButton {
	private static final long serialVersionUID = 353522972818099436L;

	/*CancelAutoLoginButton(final LoginForm lf) {
		super("loginform.cancel", lf.autologin.getTimeout());

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lf.autologin.setEnabled(false);
				lf.tlauncher.getVersionManager().asyncRefresh();
			}
		});
	}*/

}
