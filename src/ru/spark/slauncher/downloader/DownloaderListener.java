package ru.spark.slauncher.downloader;

public interface DownloaderListener {
	void onDownloaderStart(Downloader d, int files);

	void onDownloaderAbort(Downloader d);

	void onDownloaderProgress(Downloader d, double progress, double speed);

	void onDownloaderFileComplete(Downloader d, Downloadable file);

	void onDownloaderComplete(Downloader d);
}
