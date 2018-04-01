package ru.spark.slauncher.managers;

public interface VersionManagerListener {
	void onVersionsRefreshing(VersionManager manager);

	void onVersionsRefreshingFailed(VersionManager manager);

	void onVersionsRefreshed(VersionManager manager);
}
