package ru.spark.slauncher.ui.versions;

import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.managers.VersionManager;

import java.util.List;

public interface VersionHandlerListener {
	void onVersionRefreshing(VersionManager vm);
	void onVersionRefreshed(VersionManager vm);
	void onVersionSelected(List<VersionSyncInfo> versions);
	void onVersionDeselected();
	void onVersionDownload(List<VersionSyncInfo> list);
}
