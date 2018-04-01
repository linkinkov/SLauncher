package ru.spark.slauncher.ui.login;

import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.managers.VersionManager;
import ru.spark.slauncher.managers.VersionManagerListener;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.loc.LocalizableComponent;
import ru.spark.slauncher.ui.login.LoginForm.LoginProcessListener;
import ru.spark.slauncher.ui.login.LoginWaitException.LoginWaitTask;
import ru.spark.slauncher.ui.swing.SimpleComboBoxModel;
import ru.spark.slauncher.ui.swing.VersionCellRenderer;
import ru.spark.slauncher.ui.swing.extended.ExtendedComboBox;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

public class VersionComboBox extends ExtendedComboBox<VersionSyncInfo>
implements Blockable, VersionManagerListener, LocalizableComponent,
LoginProcessListener {
	private static final long serialVersionUID = -9122074452728842733L;

	private static final VersionSyncInfo LOADING = VersionCellRenderer.LOADING;
	private static final VersionSyncInfo EMPTY = VersionCellRenderer.EMPTY;

	private final VersionManager manager;
	private final LoginForm loginForm;

	private final SimpleComboBoxModel<VersionSyncInfo> model;

	private String selectedVersion;

	VersionComboBox(LoginForm lf) {
		super(new VersionCellRenderer());

		this.loginForm = lf;
		this.model = getSimpleModel();

		this.manager = SLauncher.getInstance().getVersionManager();
		manager.addListener(this);

		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//loginForm.buttons.play.updateState();

				VersionSyncInfo selected = getVersion();
				if (selected != null)
					selectedVersion = selected.getID();
			}
		});
		this.selectedVersion = lf.global.get("login.version");
	}

	public VersionSyncInfo getVersion() {
		VersionSyncInfo selected = (VersionSyncInfo) getSelectedItem();
		return (selected == null || selected.equals(LOADING) || selected
				.equals(EMPTY)) ? null : selected;
	}

	@Override
	public void logginingIn() throws LoginException {
		VersionSyncInfo selected = getVersion();

		if (selected == null)
			throw new LoginWaitException("Version list is empty, refreshing",
					new LoginWaitTask() {
				@Override
				public void runTask() throws LoginException {
					manager.startRefresh();

					if (getVersion() == null)
						Alert.showLocError("versions.notfound");

					throw new LoginException(
							"Giving user a second chance to choose correct version...");
				}
			});

		if (!selected.hasRemote() || !selected.isInstalled()
				|| selected.isUpToDate())
			return;

		if (!Alert.showLocQuestion("versions.found-update")) {
			try {
				CompleteVersion complete = manager.getLocalList()
						.getCompleteVersion(selected.getLocal());
				complete.setUpdatedTime(selected.getLatestVersion()
						.getUpdatedTime());

				manager.getLocalList().saveVersion(complete);
			} catch (IOException e) {
				Alert.showLocError("versions.found-update.error");
			}
			return;
		}

		//loginForm.checkbox.forceupdate.setSelected(true);
	}

	@Override
	public void loginFailed() {
	}

	@Override
	public void loginSucceed() {
	}

	@Override
	public void updateLocale() {
		updateList(manager.getVersions(), null);
	}

	@Override
	public void onVersionsRefreshing(VersionManager vm) {
		updateList(null, null);
	}

	@Override
	public void onVersionsRefreshingFailed(VersionManager vm) {
		updateList(manager.getVersions(), null);
	}

	@Override
	public void onVersionsRefreshed(VersionManager vm) {
		updateList(manager.getVersions(), null);
	}

	void updateList(List<VersionSyncInfo> list, String select) {
		if (select == null && selectedVersion != null)
			select = selectedVersion;

		removeAllItems();

		if (list == null) {
			addItem(LOADING);
			return;
		}

		if (list.isEmpty())
			addItem(EMPTY);
		else {
			model.addElements(list);

			for (VersionSyncInfo version : list)
				if(select != null && version.getID().equals(select))
					setSelectedItem(version);
		}
	}

	@Override
	public void block(Object reason) {
		this.setEnabled(false);
	}

	@Override
	public void unblock(Object reason) {
		this.setEnabled(true);
	}

}
