package ru.spark.slauncher.ui.listener;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.Configuration.ActionOnLaunch;
import ru.spark.slauncher.configuration.LangConfiguration;
import ru.spark.slauncher.minecraft.crash.Crash;
import ru.spark.slauncher.minecraft.crash.CrashSignatureContainer.CrashSignature;
import ru.spark.slauncher.minecraft.launcher.MinecraftException;
import ru.spark.slauncher.minecraft.launcher.MinecraftListener;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.util.OS;
import ru.spark.util.U;

import java.io.File;
import java.net.URI;

public class MinecraftUIListener implements MinecraftListener {
	private final SLauncher t;
	private final LangConfiguration lang;

	public MinecraftUIListener(SLauncher tlauncher) {
		this.t = tlauncher;

		this.lang = t.getLang();
	}

	@Override
	public void onMinecraftPrepare() {
	}

	@Override
	public void onMinecraftAbort() {
	}

	@Override
	public void onMinecraftLaunch() {
		if(!t.getSettings().getActionOnLaunch().equals(ActionOnLaunch.NOTHING))
			t.hide();
	}

	@Override
	public void onMinecraftClose() {
		if (!t.getLauncher().isLaunchAssist())
			return;

		t.show();
	}

	@Override
	public void onMinecraftCrash(Crash crash) {
		if (!t.getLauncher().isLaunchAssist())
			t.show();

		String p = "crash.", title = Localizable.get(p + "title"), report = crash
				.getFile();

		if (!crash.isRecognized()) {
			Alert.showLocError(title, p + "unknown", null);
		} else {
			for (CrashSignature sign : crash.getSignatures()) {
				String path = sign.getPath(), message = p + path, url = message
						+ ".url";
				URI uri = U.makeURI(url);

				if (uri != null) {
					if (Alert.showLocQuestion(title, message, report))
						OS.openLink(uri);
				} else
					Alert.showLocMessage(title, message, report);
			}
		}

		if (report == null)
			return;

		if (Alert.showLocQuestion(p + "store")) {
			U.log("Removing crash report...");

			File file = new File(report);
			if (!file.exists())
				U.log("File is already removed. LOL.");
			else {

				if (!file.delete()) {
					U.log("Can't delete crash report file. Okay.");
					Alert.showLocMessage(p + "store.failed");
					return;
				}

				U.log("Yay, crash report file doesn't exist by now.");
			}
			Alert.showLocMessage(p + "store.success");
		}
	}

	@Override
	public void onMinecraftError(Throwable e) {
		Alert.showLocError("launcher.error.title", "launcher.error.unknown", e);
	}

	@Override
	public void onMinecraftKnownError(MinecraftException e) {
		Alert.showError(
				lang.get("launcher.error.title"),
				lang.get("launcher.error." + e.getLangPath(),
						(Object[]) e.getLangVars()), e);
	}

}
