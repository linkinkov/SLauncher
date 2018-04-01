package ru.spark.slauncher.updater;

public interface UpdateListener {
	void onUpdateError(Update u, Throwable e);

	void onUpdateDownloading(Update u);

	void onUpdateDownloadError(Update u, Throwable e);

	void onUpdateReady(Update u);

	void onUpdateApplying(Update u);

	void onUpdateApplyError(Update u, Throwable e);
}
