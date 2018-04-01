package ru.spark.slauncher.managers;

import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.downloader.DownloadableContainer;

public class VersionSyncInfoContainer extends DownloadableContainer {
	private final VersionSyncInfo version;
	
	public VersionSyncInfoContainer(VersionSyncInfo version) {
		if(version == null)
			throw new NullPointerException();
		
		this.version = version;
	}
	
	public VersionSyncInfo getVersion() {
		return version;
	}

}
