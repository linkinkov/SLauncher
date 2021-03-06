package ru.spark.slauncher.ui.settings;

import net.minecraft.launcher.versions.ReleaseType;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.Configuration.ActionOnLaunch;
import ru.spark.slauncher.configuration.Configuration.ConnectionQuality;
import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.slauncher.managers.VersionLists;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.converter.*;
import ru.spark.slauncher.ui.editor.*;
import ru.spark.slauncher.ui.explorer.FileExplorer;
import ru.spark.slauncher.ui.explorer.ImageFileExplorer;
import ru.spark.slauncher.ui.loc.LocalizableButton;
import ru.spark.slauncher.ui.loc.LocalizableMenuItem;
import ru.spark.slauncher.ui.login.AutoLogin;
import ru.spark.slauncher.ui.login.LoginException;
import ru.spark.slauncher.ui.login.LoginForm.LoginProcessListener;
import ru.spark.slauncher.ui.scenes.DefaultScene;
import ru.spark.slauncher.ui.scenes.DefaultScene.SidePanel;
import ru.spark.slauncher.ui.swing.ImageButton;
import ru.spark.slauncher.ui.swing.extended.BorderPanel;
import ru.spark.util.Direction;
import ru.spark.util.IntegerArray;
import ru.spark.util.OS;
import ru.spark.util.Range;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class SettingsPanel extends TabbedEditorPanel implements LoginProcessListener {

	private final DefaultScene scene;

	// First tab: Minecraft settings
	private final EditorPanelTab minecraftTab;
	public final EditorFieldHandler
	directory, resolution, fullscreen, javaArgs, mcArgs, javaPath, memory;
	public final EditorGroupHandler versionHandler;

	// Second tab: SLauncher settings
	private final EditorPanelTab tlauncherTab;
	public final EditorFieldHandler
	launcherResolution, background, loginFormDirection, autologinTimeout, console, connQuality, launchAction, locale;

	// Third tab: About
	private final EditorPanelTab aboutTab;
	public final AboutPage about;

	// General buttons
	private final BorderPanel buttonPanel;
	private final LocalizableButton saveButton, defaultButton;
	private final ImageButton homeButton;

	// Popup menu
	private final JPopupMenu popup;
	private final LocalizableMenuItem infoItem, defaultItem;
	private EditorHandler selectedHandler;

	public SettingsPanel(DefaultScene sc) {
		super(tipTheme, new Insets(5, 10, 10, 10));

		if(tabPane.getExtendedUI() != null)
			tabPane.getExtendedUI().setTheme(settingsTheme);

		this.scene = sc;

		FocusListener warning = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setMessage("settings.warning");
			}

			@Override
			public void focusLost(FocusEvent e) {
				setMessage(null);
			}
		}, restart = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setMessage("settings.restart");
			}

			@Override
			public void focusLost(FocusEvent e) {
				setMessage(null);
			}
		};

		// Minecraft Tab

		this.minecraftTab = new EditorPanelTab("settings.tab.minecraft");

		this.directory = new EditorFieldHandler("minecraft.gamedir",
				new EditorFileField("settings.client.gamedir.prompt", new FileExplorer(FileExplorer.DIRECTORIES_ONLY, true)),
				warning);
		directory.addListener(new EditorFieldChangeListener() {

			@Override
			protected void onChange(String oldValue, String newValue) {
				if (!tlauncher.isReady())
					return;

				try {
					tlauncher.getManager().getComponent(VersionLists.class)
					.updateLocal();
				} catch (IOException e) {
					Alert.showLocError("settings.client.gamedir.noaccess", e);
					return;
				}

				tlauncher.getVersionManager().asyncRefresh();
				tlauncher.getProfileManager().recreate();
			}
		});
		minecraftTab.add(new EditorPair("settings.client.gamedir.label", directory));

		this.resolution = new EditorFieldHandler("minecraft.size",
				new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height",
						global.getDefaultClientWindowSize(), false),
						restart);
		this.fullscreen = new EditorFieldHandler("minecraft.fullscreen",
				new EditorCheckBox("settings.client.resolution.fullscreen"));
		minecraftTab.add(new EditorPair("settings.client.resolution.label",
				resolution, fullscreen));
		minecraftTab.nextPane();

		ReleaseType[] releaseTypes = ReleaseType.getDefinable();
		EditorFieldHandler[] versions = new EditorFieldHandler[releaseTypes.length];

		for (int i = 0; i < releaseTypes.length; i++) {
			ReleaseType releaseType = releaseTypes[i];

			versions[i] =
					new EditorFieldHandler("minecraft.versions."+ releaseType,
							new EditorCheckBox("settings.versions."+ releaseType));
		}

		this.versionHandler = new EditorGroupHandler(versions);
		versionHandler.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldvalue, String newvalue) {
				SLauncher.getInstance().getVersionManager().updateVersionList();
			}
		});
		minecraftTab.add(new EditorPair("settings.versions.label", versions));
		minecraftTab.nextPane();

		this.javaArgs = new EditorFieldHandler("minecraft.javaargs",
				new EditorTextField("settings.java.args.jvm", true), warning);
		this.mcArgs = new EditorFieldHandler("minecraft.args",
				new EditorTextField("settings.java.args.minecraft", true),
				warning);

		minecraftTab.add(new EditorPair("settings.java.args.label", javaArgs, mcArgs));

		final boolean isWindows = OS.WINDOWS.isCurrent();

		this.javaPath =
				new EditorFieldHandler("minecraft.javadir", new EditorFileField("settings.java.path.prompt", true,
						new FileExplorer(isWindows ? FileExplorer.FILES_ONLY : FileExplorer.DIRECTORIES_ONLY, true)) {

					@Override
					public boolean isValueValid() {
						if (checkPath())
							return true;

						Alert.showLocAsyncError("settings.java.path.doesnotexist");
						return false;
					}

					private boolean checkPath() {
						if (!isWindows)
							return true;

						String path = getSettingsValue();
						if (path == null)
							return true;

						if(!path.endsWith(".exe"))
							return false;

						File javaDir = new File(path);
						return javaDir.isFile();

					}

				}, warning);
		minecraftTab.add(new EditorPair("settings.java.path.label", javaPath));

		minecraftTab.nextPane();

		this.memory = new EditorFieldHandler("minecraft.memory", new SettingsMemorySlider(), warning);
		minecraftTab.add(new EditorPair("settings.java.memory.label", memory));

		add(minecraftTab);

		// SLauncher Tab

		tlauncherTab = new EditorPanelTab("settings.tab.slauncher");

		this.launcherResolution = new EditorFieldHandler("gui.size",
				new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height",
						global.getDefaultLauncherWindowSize(), true));
		launcherResolution.addListener(new EditorFieldListener() {
			@Override
			protected void onChange(EditorHandler handler, String oldValue,
					String newValue) {
				if(!tlauncher.isReady())
					return;

				IntegerArray arr = IntegerArray.parseIntegerArray(newValue);
				tlauncher.getFrame().setSize(arr.get(0), arr.get(1));
			}
		});

		tlauncherTab.add(new EditorPair("settings.clientres.label", launcherResolution));
		tlauncherTab.nextPane();

		this.loginFormDirection = new EditorFieldHandler("gui.direction.loginform",
				new EditorComboBox<Direction>(new DirectionConverter(), Direction.values()));
		loginFormDirection.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldValue, String newValue) {
				if(!tlauncher.isReady())
					return;

				//tlauncher.getFrame().mp.defaultScene.updateDirection();
			}
		});

		tlauncherTab.add(new EditorPair("settings.direction.label", loginFormDirection));

		this.autologinTimeout = new EditorFieldHandler("login.auto.timeout",
				new EditorIntegerRangeField(new Range<Integer>(AutoLogin.MIN_TIMEOUT, AutoLogin.MAX_TIMEOUT)));

		tlauncherTab.add(new EditorPair("settings.slauncher.autologin.label", autologinTimeout));
		tlauncherTab.nextPane();

		this.background = new EditorFieldHandler("gui.background",
				new EditorFileField("settings.slide.list.prompt", true,
						new ImageFileExplorer()));
		background.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldValue, String newValue) {
				if (!tlauncher.isReady())
					return;
				//tlauncher.getFrame().mp.background.SLIDE_BACKGROUND.getThread().asyncRefreshSlide();
			}
		});

		tlauncherTab.add(new EditorPair("settings.slide.list.label", background));
		tlauncherTab.nextPane();

		this.console = new EditorFieldHandler("gui.console",
				new EditorComboBox<ConsoleType>(new ConsoleTypeConverter(),
						ConsoleType.values()));
		console.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldvalue, String newvalue) {
				if (newvalue == null)
					return;
				switch (ConsoleType.get(newvalue)) {
				case GLOBAL:
					SLauncher.getConsole().show(false);
					break;
				case MINECRAFT:
				case NONE:
					SLauncher.getConsole().hide();
					break;
				default:
					throw new IllegalArgumentException("Unknown console type!");
				}
			}
		});
		tlauncherTab.add(new EditorPair("settings.console.label", console));

		this.connQuality = new EditorFieldHandler("connection",
				new EditorComboBox<ConnectionQuality>(
						new ConnectionQualityConverter(),
						ConnectionQuality.values()));
		connQuality.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldValue, String newValue) {
				tlauncher.getDownloader().setConfiguration(
						global.getConnectionQuality());
			}
		});
		tlauncherTab.add(new EditorPair("settings.connection.label", connQuality));

		this.launchAction = new EditorFieldHandler("minecraft.onlaunch",
				new EditorComboBox<ActionOnLaunch>(
						new ActionOnLaunchConverter(), ActionOnLaunch.values()));
		tlauncherTab.add(new EditorPair("settings.launch-action.label", launchAction));

		tlauncherTab.nextPane();

		this.locale = new EditorFieldHandler("locale",
				new EditorComboBox<Locale>(new LocaleConverter(),
						global.getLocales()));
		locale.addListener(new EditorFieldChangeListener() {
			@Override
			protected void onChange(String oldvalue, String newvalue) {
				if (tlauncher.getFrame() != null)
					tlauncher.getFrame().updateLocales();
			}
		});
		tlauncherTab.add(new EditorPair("settings.lang.label", locale));

		add(tlauncherTab);

		this.aboutTab = new EditorPanelTab("settings.tab.about");

		this.about = new AboutPage();
		aboutTab.add(about);

		add(aboutTab);

		// General buttons
		this.saveButton = new LocalizableButton("settings.save");
		saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveValues();
			}

		});

		this.defaultButton = new LocalizableButton("settings.default");
		defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Alert.showLocQuestion("settings.default.warning"))
					resetValues();
			}
		});

		this.homeButton = new ImageButton("home.png");
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateValues();
				scene.setSidePanel(null);
			}
		});

		Dimension size = homeButton.getPreferredSize();
		if(size != null)
			homeButton.setPreferredSize(new Dimension(size.width*2, size.height));

		this.buttonPanel = new BorderPanel();
		buttonPanel.setCenter(sepPan(saveButton, defaultButton));
		buttonPanel.setEast(uSepPan(homeButton));

		tabPane.addChangeListener(new ChangeListener() {
			private final String aboutBlock = "abouttab";
			@Override
			public void stateChanged(ChangeEvent e) {
				if(tabPane.getSelectedComponent() == aboutTab.getScroll())
					Blocker.blockComponents(aboutBlock, buttonPanel);
				else
					Blocker.unblockComponents(aboutBlock, buttonPanel);
			}
		});

		container.setSouth(buttonPanel);

		// Popup
		this.popup = new JPopupMenu();

		this.infoItem = new LocalizableMenuItem("settings.popup.info");
		infoItem.setEnabled(false);
		popup.add(infoItem);

		this.defaultItem = new LocalizableMenuItem("settings.popup.default");
		defaultItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedHandler == null)
					return;

				resetValue(selectedHandler);
			}
		});
		popup.add(defaultItem);

		for (final EditorHandler handler : this.handlers) {
			Component handlerComponent = handler.getComponent();

			handlerComponent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON3)
						return;
					callPopup(e, handler);
				}
			});
		}

		updateValues();
	}

	void updateValues() {
		boolean globalUnSaveable = !global.isSaveable();
		for (EditorHandler handler : handlers) {
			String path = handler.getPath(), value = global.get(path);

			handler.updateValue(value);
			setValid(handler, true);

			if (globalUnSaveable || !global.isSaveable(path))
				Blocker.block(handler, "unsaveable");
		}
	}

	public boolean saveValues() {
		if (!checkValues())
			return false;

		for (EditorHandler handler : handlers) {
			String path = handler.getPath(), value = handler.getValue();

			global.set(path, value, false);

			handler.onChange(value);
		}

		global.store();

		return true;
	}

	void resetValues() {
		for (EditorHandler handler : handlers)
			resetValue(handler);
	}

	void resetValue(EditorHandler handler) {
		String path = handler.getPath();

		if (!global.isSaveable(path))
			return;

		String value = global.getDefault(path);

		log("Resetting:", handler.getClass().getSimpleName(), path, value);

		handler.setValue(value);

		log("Reset!");
	}

	boolean canReset(EditorHandler handler) {
		String key = handler.getPath();

		return global.isSaveable(key)
				&& global.getDefault(handler.getPath()) != null;
	}

	void callPopup(MouseEvent e, EditorHandler handler) {
		if (popup.isShowing())
			popup.setVisible(false);

		defocus();

		int x = e.getX(), y = e.getY();
		this.selectedHandler = handler;

		this.updateResetMenu();
		this.infoItem.setVariables(handler.getPath());
		this.popup.show((JComponent) e.getSource(), x, y);
	}

	@Override
	public void block(Object reason) {
		Blocker.blockComponents(minecraftTab, reason);
		this.updateResetMenu();
	}

	@Override
	public void unblock(Object reason) {
		Blocker.unblockComponents(minecraftTab, reason);
		this.updateResetMenu();
	}

	private void updateResetMenu() {
		if(selectedHandler != null)
			defaultItem.setEnabled(!Blocker.isBlocked(selectedHandler));
	}

	@Override
	public void logginingIn() throws LoginException {
		if (checkValues())
			return;

		scene.setSidePanel(SidePanel.SETTINGS);
		throw new LoginException("Invalid settings!");
	}

	@Override
	public void loginFailed() {
	}

	@Override
	public void loginSucceed() {
	}
}
