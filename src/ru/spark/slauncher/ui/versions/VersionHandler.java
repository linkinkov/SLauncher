package ru.spark.slauncher.ui.versions;

import net.minecraft.launcher.updater.VersionFilter;
import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.downloader.Downloader;
import ru.spark.slauncher.managers.VersionManager;
import ru.spark.slauncher.managers.VersionManagerListener;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.scenes.VersionManagerScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VersionHandler implements Blockable, VersionHandlerListener {
	static final int ELEM_WIDTH = 300;

	public static final String
	REFRESH_BLOCK = LoginForm.REFRESH_BLOCK,
	SINGLE_SELECTION_BLOCK = "single-select",
	START_DOWNLOAD = "start-download",
	STOP_DOWNLOAD = "stop-download",
	DELETE_BLOCK = "deleting";

	private final List<VersionHandlerListener> listeners;
	private final VersionHandler instance;

	public final VersionManagerScene scene;

	final VersionHandlerThread thread;

	public final VersionList list;

	final VersionManager vm;
	final Downloader downloader;

	List<VersionSyncInfo> selected, downloading;
	VersionFilter filter;

	public VersionHandler(VersionManagerScene scene) {
		this.instance = this;
		this.scene = scene;

		this.listeners = Collections.synchronizedList(new ArrayList<VersionHandlerListener>());
		this.downloading = Collections.synchronizedList(new ArrayList<VersionSyncInfo>());

		SLauncher launcher = SLauncher.getInstance();
		this.vm = launcher.getVersionManager();
		this.downloader = launcher.getDownloader();

		this.list = new VersionList(this);

		this.thread = new VersionHandlerThread(this);

		vm.addListener(new VersionManagerListener() {
			@Override
			public void onVersionsRefreshing(VersionManager manager) {
				instance.onVersionRefreshing(manager);
			}

			@Override
			public void onVersionsRefreshed(VersionManager manager) {
				instance.onVersionRefreshed(manager);
			}

			@Override
			public void onVersionsRefreshingFailed(VersionManager manager) {
				onVersionsRefreshed(manager);
			}
		});

		this.onVersionDeselected();
	}

	void addListener(VersionHandlerListener listener) {
		this.listeners.add(listener);
	}

	void update() {
		if(selected != null)
			onVersionSelected(selected);
	}

	void refresh() {
		vm.startRefresh(true);
	}

	void asyncRefresh() {
		vm.asyncRefresh();
	}

	public void stopRefresh() {
		vm.stopRefresh();
	}

	void exitEditor() {
		list.deselect();
		scene.getMainPane().openDefaultScene();
	}

	VersionSyncInfo getSelected() {
		return selected == null || selected.size() != 1? null : selected.get(0);
	}

	List<VersionSyncInfo> getSelectedList() {
		return selected;
	}

	@Override
	public void block(Object reason) {
		Blocker.block(reason, list, scene.getMainPane().defaultScene);
	}

	@Override
	public void unblock(Object reason) {
		Blocker.unblock(reason, list, scene.getMainPane().defaultScene);
	}

	@Override
	public void onVersionRefreshing(VersionManager vm) {
		Blocker.block(instance, REFRESH_BLOCK);

		for(VersionHandlerListener listener : listeners)
			listener.onVersionRefreshing(vm);
	}

	@Override
	public void onVersionRefreshed(VersionManager vm) {
		Blocker.unblock(instance, REFRESH_BLOCK);

		for(VersionHandlerListener listener : listeners)
			listener.onVersionRefreshed(vm);
	}

	@Override
	public void onVersionSelected(List<VersionSyncInfo> version) {
		this.selected = version;

		if(version == null || version.isEmpty() || version.get(0).getID() == null)
			onVersionDeselected();
		else
			for(VersionHandlerListener listener : listeners)
				listener.onVersionSelected(version);
	}

	@Override
	public void onVersionDeselected() {
		this.selected = null;

		for(VersionHandlerListener listener : listeners)
			listener.onVersionDeselected();
	}

	@Override
	public void onVersionDownload(List<VersionSyncInfo> list) {
		this.downloading = list;

		for(VersionHandlerListener listener : listeners)
			listener.onVersionDownload(list);
	}
}
