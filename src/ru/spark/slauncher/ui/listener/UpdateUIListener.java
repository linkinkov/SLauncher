package ru.spark.slauncher.ui.listener;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.updater.Update;
import ru.spark.slauncher.updater.UpdateListener;
import ru.spark.slauncher.updater.Updater;
import ru.spark.util.OS;

import java.net.URI;

public class UpdateUIListener implements UpdateListener {

	private final SLauncher t;
	private final Update u;

	public UpdateUIListener(Update u) {
		if(u == null)
			throw new NullPointerException();

		this.t = SLauncher.getInstance();
		this.u = u;

		u.addListener(this);
	}

	public void push() {
		if (Updater.isAutomode()) {
			block();
			u.download(true);
		}
		else
			openUpdateLink( u.getDownloadLink() );
	}

	@Override
	public void onUpdateError(Update u, Throwable e) {
		if (Alert.showLocQuestion("updater.error.title", "updater.download-error", e))
			openUpdateLink(u.getDownloadLink());

		unblock();
	}

	@Override
	public void onUpdateDownloading(Update u) {
	}

	@Override
	public void onUpdateDownloadError(Update u, Throwable e) {
		this.onUpdateError(u, e);
	}

	@Override
	public void onUpdateReady(Update u) {
		onUpdateReady(u, false, false);
	}

	private static void onUpdateReady(Update u, boolean force, boolean showChangeLog) {
		Alert.showLocWarning("updater.downloaded", showChangeLog? u.getDescription() : null);
		u.apply();
	}

	@Override
	public void onUpdateApplying(Update u) {
	}

	@Override
	public void onUpdateApplyError(Update u, Throwable e) {
		if (Alert.showLocQuestion("updater.save-error", e))
			openUpdateLink(u.getDownloadLink());

		unblock();
	}

	private static boolean openUpdateLink(URI uri) {
		if(OS.openLink(uri, false))
			return true;

		Alert.showLocError("updater.found.cannotopen", uri);
		return false;
	}

	private void block() {
		Blocker.block(t.getFrame().mp, "updater");
	}

	private void unblock() {
		Blocker.unblock(t.getFrame().mp, "updater");
	}
}
