package ru.spark.slauncher.ui.accounts;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.managers.ProfileManager;
import ru.spark.slauncher.managers.ProfileManagerListener;
import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.minecraft.auth.AuthenticatorDatabase;
import ru.spark.slauncher.ui.block.Unblockable;
import ru.spark.slauncher.ui.center.CenterPanel;
import ru.spark.slauncher.ui.loc.LocalizableLabel;
import ru.spark.slauncher.ui.scenes.AccountEditorScene;
import ru.spark.slauncher.ui.swing.AccountCellRenderer;
import ru.spark.slauncher.ui.swing.AccountCellRenderer.AccountCellType;
import ru.spark.slauncher.ui.swing.ImageButton;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountList extends CenterPanel {
	private static final long serialVersionUID = 3280495266368287215L;

	private final AccountEditorScene scene;

	public final DefaultListModel<Account> model;
	public final JList<Account> list;

	public final ImageButton add;
	public final ImageButton remove;
	public final ImageButton help;
	public final ImageButton back;

	public AccountList(AccountEditorScene sc) {
		super(squareInsets);

		this.scene = sc;

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setOpaque(false);

		LocalizableLabel label = new LocalizableLabel("account.list");
		panel.add("North", label);

		this.model = new DefaultListModel<Account>();
		this.list = new JList<Account>(model);
		list.setCellRenderer(new AccountCellRenderer(AccountCellType.EDITOR));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Account account = list.getSelectedValue();
				scene.handler.refreshEditor(account);
			}
		});

		JScrollPane scroll = new JScrollPane(list);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add("Center", scroll);

		JPanel buttons = new JPanel(new GridLayout(0, 4));
		buttons.setOpaque(false);

		this.add = new ImageButton("plus.png");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.handler.addAccount();
				defocus();
			}
		});
		buttons.add(add);

		this.remove = new ImageButton("minus.png");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.handler.removeAccount();
				defocus();
			}
		});
		buttons.add(remove);

		this.help = new ImageButton("info.png");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defocus();
				scene.handler.callPopup();
			}
		});
		buttons.add(help);

		this.back = new UnblockableImageButton("home.png");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.handler.exitEditor();
			}
		});
		buttons.add(back);

		panel.add("South", buttons);

		this.add(panel);

		ProfileManagerListener listener = new ProfileManagerListener() {
			@Override
			public void onProfilesRefreshed(ProfileManager pm) {
				refreshFrom(pm.getAuthDatabase());
			}

			@Override
			public void onProfileManagerChanged(ProfileManager pm) {
				refreshFrom(pm.getAuthDatabase());
			}

			@Override
			public void onAccountsRefreshed(AuthenticatorDatabase db) {
				refreshFrom(db);
			}
		};
		SLauncher.getInstance().getProfileManager().addListener(listener);
	}

	void refreshFrom(AuthenticatorDatabase db) {
		model.clear();

		for (Account account : db.getAccounts())
			model.addElement(account);

		if (model.isEmpty())
			scene.handler.notifyEmpty();
	}

	class UnblockableImageButton extends ImageButton implements Unblockable {
		public UnblockableImageButton(String imagepath) {
			super(imagepath);
		}
	}

}
