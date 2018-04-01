package ru.spark.slauncher;

import com.google.gson.Gson;
import com.spark.slauncher.PreloaderFrame;
import joptsimple.OptionSet;
import ru.spark.slauncher.Bootstrapper.LoadingStep;
import ru.spark.slauncher.configuration.ArgumentParser;
import ru.spark.slauncher.configuration.Configuration;
import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.slauncher.configuration.LangConfiguration;
import ru.spark.slauncher.downloader.Downloader;
import ru.spark.slauncher.handlers.ExceptionHandler;
import ru.spark.slauncher.handlers.LocalServer;
import ru.spark.slauncher.handlers.SimpleHostnameVerifier;
import ru.spark.slauncher.managers.ComponentManager;
import ru.spark.slauncher.managers.ComponentManagerListenerHelper;
import ru.spark.slauncher.managers.ProfileManager;
import ru.spark.slauncher.managers.ServerList.Server;
import ru.spark.slauncher.managers.VersionManager;
import ru.spark.slauncher.minecraft.launcher.MinecraftLauncher;
import ru.spark.slauncher.minecraft.launcher.MinecraftListener;
import ru.spark.slauncher.ui.SLauncherFrame;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.console.Console;
import ru.spark.slauncher.ui.console.Console.CloseAction;
import ru.spark.slauncher.ui.listener.MinecraftUIListener;
import ru.spark.slauncher.ui.listener.RequiredUpdateListener;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.scenes.SwingFXWebViewNew;
import ru.spark.slauncher.updater.Stats;
import ru.spark.slauncher.updater.Updater;
import ru.spark.util.*;
import ru.spark.util.stream.MirroredLinkedStringStream;
import ru.spark.util.stream.PrintLogger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class SLauncher {
	// Баги? Есть такое.
	private static final double VERSION = 1.77;
	private static final boolean BETA = false;

	private static SLauncher instance;
	private static String[] sargs;
	private static File directory;

	private static PrintLogger print;
	private static Console console;

	private static Gson gson;

	private SLauncherState state;

	private LangConfiguration lang;
	private Configuration settings;
	private Downloader downloader;
	private Updater updater;

	private SLauncherFrame frame;
	private SLauncherLite lite;

	private ComponentManager manager;
	private VersionManager versionManager;
	private ProfileManager profileManager;

	private OptionSet args;

	private MinecraftLauncher launcher;

	private RequiredUpdateListener updateListener;
	private MinecraftUIListener minecraftListener;

	private boolean ready;

	private SLauncher(SLauncherState state, OptionSet set) throws Exception {
		if (state == null)
			throw new IllegalArgumentException("SLauncherState can't be NULL!");

		U.log("SLauncher is loading in state", state);

		Time.start(this);
		instance = this;
		this.state = state;
		this.args = set;

		gson = new Gson();

		File oldConfig = MinecraftUtil.getSystemRelatedFile("slauncher.cfg"), newConfig = MinecraftUtil.getSystemRelatedDirectory(SETTINGS);

		if(!oldConfig.isFile())
			oldConfig = MinecraftUtil.getSystemRelatedFile(".slauncher/slauncher.properties");

		if(oldConfig.isFile() && !newConfig.isFile()) {
			boolean copied = true;

			try {
				FileUtil.createFile(newConfig);
				FileUtil.copyFile(oldConfig, newConfig, true);
			} catch(IOException ioE) {
				U.log("Cannot copy old configuration to the new place", oldConfig, newConfig, ioE);
				copied = false;
			}

			if(copied) {
				U.log("Old configuration successfully moved to the new place:", newConfig);
				FileUtil.deleteFile(oldConfig);
			}
		}

		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.25F, "Загрузка конфигурации...");
		}
		U.setLoadingStep(LoadingStep.LOADING_CONFIGURATION);
		settings = Configuration.createConfiguration(set);

		reloadLocale();

		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.3F, "Загрузка консоли...");
		}
		U.setLoadingStep(LoadingStep.LOADING_CONSOLE);
		console = new Console(settings, print, "SLauncher Dev Console",
		settings.getConsoleType() == ConsoleType.GLOBAL);
		if (state.equals(SLauncherState.MINIMAL))
			console.setCloseAction(CloseAction.KILL);
		Console.updateLocale();

		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.4F, "Загрузка менеджеров...");
		}
		U.setLoadingStep(LoadingStep.LOADING_MANAGERS);
		manager = new ComponentManager(this);

		versionManager = manager.loadComponent(VersionManager.class);
		profileManager = manager.loadComponent(ProfileManager.class);

		manager.loadComponent(ComponentManagerListenerHelper.class); // TODO invent something better
		init();

		U.log("Started! (" + Time.stop(this) + " ms.)");

		this.ready = true;
		U.setLoadingStep(LoadingStep.SUCCESS);

	}


	private void init() {

        downloader = new Downloader(this);
		minecraftListener = new MinecraftUIListener(this);

		switch (state) {
		case FULL:

			updater = new Updater(this);
			updateListener = new RequiredUpdateListener(updater);

			U.setLoadingStep(LoadingStep.LOADING_WINDOW);
			if (PreloaderFrame.getInstance() != null) {
				PreloaderFrame.getInstance().setProgress(0.43F, "Проверка обновлений и версий игры...");
			}
			frame = new SLauncherFrame(this);

			LoginForm lf = frame.mp.defaultScene.loginForm;

			U.setLoadingStep(LoadingStep.REFRESHING_INFO);
			//if (lf.autologin.isEnabled()) {
			//	versionManager.startRefresh(true);
			//	lf.autologin.setActive(true);
			//} else {
				versionManager.asyncRefresh();
				updater.asyncFindUpdate();
			//}

			profileManager.refresh();


			break;
		case MINIMAL:
			lite = new SLauncherLite(this);
			break;
		}
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public LangConfiguration getLang() {
		return lang;
	}

	public Configuration getSettings() {
		return settings;
	}

	public Updater getUpdater() {
		return updater;
	}

	public OptionSet getArguments() {
		return args;
	}

	public SLauncherFrame getFrame() {
		return frame;
	}

	public SLauncherLite getLoader() {
		return lite;
	}

	public static Console getConsole() {
		return console;
	}

	public static Gson getGson() {
		return gson;
	}

	public ComponentManager getManager() {
		return manager;
	}

	public VersionManager getVersionManager() {
		return versionManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public MinecraftLauncher getLauncher() {
		return launcher;
	}

	public MinecraftUIListener getMinecraftListener() {
		return minecraftListener;
	}

	public RequiredUpdateListener getUpdateListener() {
		return updateListener;
	}

	public boolean isReady() {
		return ready;
	}

	public void reloadLocale() throws IOException {
		Locale locale = settings.getLocale();
		U.log("Selected locale: " + locale);

		if (lang == null)
			lang = new LangConfiguration(settings.getLocales(), locale);
		else
			lang.setSelected(locale);

		Localizable.setLang(lang);

		Alert.prepareLocal();
	}

	public void launch(MinecraftListener listener, Server server, boolean forceupdate) {
		this.launcher = new MinecraftLauncher(this, forceupdate);

		launcher.addListener(minecraftListener);
		launcher.addListener(listener);
		launcher.addListener(frame.mp.getProgress());

		launcher.setServer(server);

		launcher.start();
	}

	public boolean isLauncherWorking() {
		return launcher != null && launcher.isWorking();
	}

	public static void kill() {
		U.log("Good bye!");
		System.exit(0);
	}

	public void hide() {
		U.log("I'm hiding!");

		if (frame != null)
			frame.setVisible(false);
	}

	public void show() {
		U.log("Here I am!");

		if (frame != null)
			frame.setVisible(true);
	}

	/* ___________________________________ */

	public static void main(String[] args) {
		ExceptionHandler handler = ExceptionHandler.getInstance();

		Thread.setDefaultUncaughtExceptionHandler(handler);
		Thread.currentThread().setUncaughtExceptionHandler(handler);

		HttpsURLConnection.setDefaultHostnameVerifier(SimpleHostnameVerifier.getInstance());

		U.setPrefix("[SLauncher]");

		MirroredLinkedStringStream stream = new MirroredLinkedStringStream();
		stream.setMirror(System.out);

		print = new PrintLogger(stream);
		stream.setLogger(print);
		System.setOut(print);

		U.setLoadingStep(LoadingStep.INITALIZING);
		SwingUtil.initLookAndFeel();

		try {
			launch(args);
		} catch (Throwable e) {
			U.log("Error launching SLauncher:");
			e.printStackTrace(print);

			Alert.showError(e, true);
		}
	}

	private static void launch(String[] args) throws Exception {
		U.log("Hello!");
		U.log("Starting SLauncher", BRAND, VERSION, "by", DEVELOPER);
		U.log("Have question? Find my e-mail in lang files.");
		U.log("Machine info:", OS.getSummary());
		U.log("Startup time:", Calendar.getInstance().getTime());

		U.log("---");

		SLauncher.sargs = args;

		OptionSet set = ArgumentParser.parseArgs(args);
		if (set == null) {
			new SLauncher(SLauncherState.FULL, null);
			return;
		}
		if (set.has("help"))
			ArgumentParser.getParser().printHelpOn(System.out);

		SLauncherState state = SLauncherState.FULL;
		if (set.has("nogui"))
			state = SLauncherState.MINIMAL;

		new SLauncher(state, set);
	}

	public static String[] getArgs() {
		if (sargs == null)
			sargs = new String[0];
		return sargs;
	}

	public static File getDirectory() {
		if (directory == null)
			directory = new File(".");
		return directory;
	}

	public static SLauncher getInstance() {
		return instance;
	}

	public void newInstance() {
		Bootstrapper.main(sargs);
	}

	public enum SLauncherState {
		FULL, MINIMAL
	}

	/* ___________________________________ */

	private final static String SETTINGS = "slauncher/slauncher.properties", BRAND = "Simple", FOLDER = "minecraft", DEVELOPER = "Spark1337";
	private final static String[] DEFAULT_UPDATE_REPO = {"http://slauncher.ru/update/update.json", "http://spark1337.ru/update/update.json"};
	private final static String[]
			OFFICIAL_REPO = {"http://s3.amazonaws.com/Minecraft.Download/" },
			EXTRA_REPO = {"http://tlauncher.ru/repo/", "http://u.tlauncher.ru/repo/"};
	private final static String[]
			LIBRARY_REPO = { "https://libraries.minecraft.net/" },
			ASSETS_REPO = { "http://resources.download.minecraft.net/" },
			SERVER_LIST = {};


	public static double getVersion() {
		return VERSION;
	}

	public static boolean isBeta() {
		return BETA;
	}

	public static String getBrand() {
		return BRAND;
	}

	public static String getDeveloper() {
		return DEVELOPER;
	}

	public static String getFolder() {
		return FOLDER;
	}

	public static String[] getUpdateRepos() {
		return DEFAULT_UPDATE_REPO;
	}

	public static String getSettingsFile() {
		return SETTINGS;
	}

	public static String[] getOfficialRepo() {
		return OFFICIAL_REPO;
	}

	public static String[] getExtraRepo() {
		return EXTRA_REPO;
	}

	public static String[] getLibraryRepo() {
		return LIBRARY_REPO;
	}

	public static String[] getAssetsRepo() {
		return ASSETS_REPO;
	}

	public static String[] getServerList() { return SERVER_LIST;
	}
}
