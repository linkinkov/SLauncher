package ru.spark.slauncher.updater;

import ru.spark.slauncher.updater.AdParser.AdMap;

public interface UpdaterListener {
	void onUpdaterRequesting(Updater u);

	void onUpdaterRequestError(Updater u);

	void onUpdateFound(Update upd);

	void onUpdaterNotFoundUpdate(Updater u);

	void onAdFound(Updater u, AdMap adMap);
}
