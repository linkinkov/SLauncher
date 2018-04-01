package ru.spark.slauncher.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.SimpleConfiguration;
import ru.spark.slauncher.downloader.Downloader;
import ru.spark.slauncher.exceptions.SLauncherException;
import ru.spark.slauncher.ui.listener.UpdateUIListener;
import ru.spark.slauncher.updater.AdParser.AdMap;
import ru.spark.util.FileUtil;
import ru.spark.util.U;
import ru.spark.util.async.AsyncThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Updater {
	private static final String[] links = SLauncher.getUpdateRepos();
	private static final URI[] URIs = makeURIs();

	private final Downloader d;

	private final List<UpdaterListener> listeners = Collections.synchronizedList(new ArrayList<UpdaterListener>());

	public void addListener(UpdaterListener l) {
		listeners.add(l);
	}

	public void removeListener(UpdaterListener l) {
		listeners.remove(l);
	}

	private Update found;
	private SimpleConfiguration parsed;

	private UpdaterState state;

	public Updater(SLauncher t) {
		this.d = t.getDownloader();

		if (!PackageType.isCurrent(PackageType.JAR)) {
			File oldfile = Updater.getTempFile();
			if (oldfile.delete())
				log("Old version has been deleted (.update)");
		}

		log("Initialized.");
		log("Package type:", PackageType.getCurrent());
	}

	public UpdaterState getState() {
		return state;
	}

	UpdaterState findUpdate() {
		try {
			return (this.state = findUpdate_());
		} catch (Throwable e) {
			this.state = UpdaterState.ERROR;
		}
		return this.state;
	}

	private static JsonObject readUrl(URL url) throws Exception {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			Gson gson = new Gson();
			return gson.fromJson(buffer.toString(), JsonObject.class);
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	private UpdaterState findUpdate_() {
		log("Requesting an update...");
		this.onUpdaterRequests();

		int attempt = 0;
		for (URI uri : URIs) {
			++attempt;
			log("Attempt #" + attempt + ". URL:", uri);
			try {

				JsonObject obj = readUrl(uri.toURL());


				Update update = new Update(this, d, obj);
				double version = update.getVersion();

				log("Success! Found:", version);

				AdMap adMap = AdParser.parse(obj);
				if (adMap != null)
					onAdFound(adMap);


				if (SLauncher.getVersion() > version)
					log("Found version is older than running:", version, "("+ SLauncher.getVersion() +")");

				if (update.getDownloadLink() == null)
					log("An update for current package type is not available.");
				else if (SLauncher.getVersion() < version) {
					log("Found actual version:", version);
					this.found = update;

					onUpdateFound(update);
					return UpdaterState.FOUND;
				}

				noUpdateFound();
				return UpdaterState.NOT_FOUND;
			} catch (Exception e) {
				log("Cannot get update information", e);
			}
		}

		log("Updating is impossible - cannot get any information.");
		this.onUpdaterRequestError();

		return UpdaterState.ERROR;
	}

	public void notifyAboutUpdate() {
		if (found == null)
			return;

		this.onUpdateFound(found);
	}

	public Update getUpdate() {
		return found;
	}

	public SimpleConfiguration getParsed() {
		return parsed;
	}

	public boolean isRequesting() {
		return this.state == Updater.UpdaterState.READY;
	}

	public void asyncFindUpdate() {
		AsyncThread.execute(new Runnable() {
			@Override
			public void run() {
				findUpdate();
			}
		});
	}

	private void onUpdaterRequests() {
		synchronized (listeners) {
			for (UpdaterListener l : listeners)
				l.onUpdaterRequesting(this);
		}
	}

	private void onUpdaterRequestError() {
		synchronized (listeners) {
			for (UpdaterListener l : listeners)
				l.onUpdaterRequestError(this);
		}
	}

	private void onUpdateFound(Update u) {
		synchronized (listeners) {
			for (UpdaterListener l : listeners)
				l.onUpdateFound(u);
			//new UpdateUIListener(u).push();
		}
	}

	private void noUpdateFound() {
		synchronized (listeners) {
			for (UpdaterListener l : listeners)
				l.onUpdaterNotFoundUpdate(this);
		}
	}

	private void onAdFound(AdMap ad) {
		synchronized (listeners) {
			for (UpdaterListener l : listeners)
				l.onAdFound(this, ad);
		}
	}

	private static boolean isAutomodeFor(PackageType pt) {
		if (pt == null)
			throw new NullPointerException("PackageType is NULL!");

		switch (pt) {
			case EXE:
			case JAR:
				return true;
			default:
				throw new IllegalArgumentException("Unknown PackageType!");
		}
	}

	public static boolean isAutomode() {
		return isAutomodeFor(PackageType.getCurrent());
	}

	public static File getFileFor(PackageType pt) {
		if (pt == null)
			throw new NullPointerException("PackageType is NULL!");

		switch (pt) {
			case EXE:
			case JAR:
				return FileUtil.getRunningJar();
			default:
				throw new IllegalArgumentException("Unknown PackageType!");
		}
	}

	public static File getFile() {
		return getFileFor(PackageType.getCurrent());
	}

	public static File getUpdateFileFor(PackageType pt) {
		return new File(getFileFor(pt).getAbsolutePath() + ".update");
	}

	public static File getUpdateFile() {
		return getUpdateFileFor(PackageType.getCurrent());
	}

	private static File getTempFileFor(PackageType pt) {
		return new File(getFileFor(pt).getAbsolutePath() + ".replace");
	}

	private static File getTempFile() {
		return getTempFileFor(PackageType.getCurrent());
	}

	private static URI[] makeURIs() {
		int len = links.length;
		URI[] r = new URI[len];

		for (int i = 0; i < len; i++)
			try {
				r[i] = new URL(links[i]).toURI();
			} catch (Exception e) {
				throw new SLauncherException("Cannot create link from at i:"
						+ i, e);
			}

		return r;
	}

	private static void log(Object... obj) {
		U.log("[Updater]", obj);
	}

	public enum UpdaterState {
		READY, FOUND, NOT_FOUND, ERROR
	}
}
