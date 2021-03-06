package ru.spark.slauncher.ui.accounts;

import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.ui.accounts.UsernameField.UsernameState;
import ru.spark.slauncher.ui.center.CenterPanel;
import ru.spark.slauncher.ui.loc.LocalizableButton;
import ru.spark.slauncher.ui.loc.LocalizableCheckbox;
import ru.spark.slauncher.ui.progress.ProgressBar;
import ru.spark.slauncher.ui.scenes.AccountEditorScene;
import ru.spark.slauncher.ui.swing.CheckBoxListener;
import ru.spark.slauncher.ui.swing.Del;
import ru.spark.slauncher.ui.text.ExtendedPasswordField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountEditor extends CenterPanel {
	private static final long serialVersionUID = 7061277150214976212L;

	private final AccountEditorScene scene;

	public final UsernameField username;
	public final ExtendedPasswordField password;

	public final LocalizableCheckbox premiumBox;
	public final LocalizableButton save;

	private final ProgressBar progressBar;

	public AccountEditor(AccountEditorScene sc) {
		super(squareInsets);

		this.scene = sc;

		ActionListener enterHandler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defocus();
				scene.handler.saveEditor();
			}
		};

		this.username = new UsernameField(this, UsernameState.USERNAME);
		username.addActionListener(enterHandler);

		this.password = new ExtendedPasswordField();
		password.addActionListener(enterHandler);
		password.setEnabled(false);

		premiumBox = new LocalizableCheckbox("account.premium");
		premiumBox.addItemListener(new CheckBoxListener() {
			@Override
			public void itemStateChanged(boolean newstate) {
				if (newstate && !password.hasPassword())
					password.setText(null);

				password.setEnabled(newstate);
				username.setState(newstate ? UsernameState.EMAIL
						: UsernameState.USERNAME);

				defocus();
			}
		});

		save = new LocalizableButton("account.save");
		save.addActionListener(enterHandler);

		progressBar = new ProgressBar();
		progressBar.setPreferredSize(new Dimension(200, 20));

		this.add(del(Del.CENTER));
		this.add(sepPan(username));
		this.add(sepPan(premiumBox));
		this.add(sepPan(password));
		this.add(del(Del.CENTER));
		this.add(sepPan(save));
		this.add(sepPan(progressBar));
	}

	public void fill(Account account) {
		this.premiumBox.setSelected(account.isPremium());
		this.username.setText(account.getUsername());
		this.password.setText(null);
	}

	public void clear() {
		this.premiumBox.setSelected(false);
		this.username.setText(null);
		this.password.setText(null);
	}

	public Account get() {
		Account account = new Account();
		account.setUsername(username.getValue());

		if (premiumBox.isSelected()) {
			account.setPremium(true);

			if (password.hasPassword())
				account.setPassword(password.getPassword());
		}

		return account;
	}

	@Override
	public Insets getInsets() {
		return squareInsets;
	}

	@Override
	public void block(Object reason) {
		super.block(reason);

		password.setEnabled(premiumBox.isSelected());

		if (!reason.equals("empty"))
			progressBar.setIndeterminate(true);
	}

	@Override
	public void unblock(Object reason) {
		super.unblock(reason);

		password.setEnabled(premiumBox.isSelected());

		if (!reason.equals("empty"))
			progressBar.setIndeterminate(false);
	}
}
