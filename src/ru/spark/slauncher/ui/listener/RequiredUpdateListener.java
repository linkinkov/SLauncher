package ru.spark.slauncher.ui.listener;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.updater.AdParser.AdMap;
import ru.spark.slauncher.updater.Update;
import ru.spark.slauncher.updater.Updater;
import ru.spark.slauncher.updater.UpdaterListener;

public class RequiredUpdateListener implements UpdaterListener {

	public RequiredUpdateListener(Updater updater) {

		updater.addListener(this);
	}

	@Override
	public void onUpdaterRequesting(Updater u) {
	}

	@Override
	public void onUpdaterRequestError(Updater u) {
	}

	@Override
	public void onUpdateFound(Update upd) {
		if(!upd.isRequired() && !SLauncher.isBeta()) return;

		String prefix = "updater.required.found.",
		title = prefix + "title",
		message  = prefix + "message";

		Alert.showWarning(Localizable.get(title), Localizable.get(message, upd.getVersion() +" ("+ upd.getCode() +")"), upd.getDescription());

		UpdateUIListener listener = new UpdateUIListener(upd);
		listener.push();
	}

	@Override
	public void onUpdaterNotFoundUpdate(Updater u) {
	}

	@Override
	public void onAdFound(Updater u, AdMap adMap) {
	}

}
