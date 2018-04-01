package ru.spark.slauncher.ui;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import ru.spark.slauncher.ui.background.BackgroundHolder;
import ru.spark.slauncher.ui.progress.LaunchProgress;
import ru.spark.slauncher.ui.progress.ProgressBar;
import ru.spark.slauncher.ui.scenes.AccountEditorScene;
import ru.spark.slauncher.ui.scenes.DefaultScene;
import ru.spark.slauncher.ui.scenes.PseudoScene;
import ru.spark.slauncher.ui.scenes.VersionManagerScene;
import ru.spark.slauncher.ui.swing.extended.ExtendedLayeredPane;
import ru.spark.util.OS;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainPane extends ExtendedLayeredPane {
	private static final long serialVersionUID = -8854598755786867602L;

	private final SLauncherFrame rootFrame;
	private final boolean repaintEveryTime;

	private PseudoScene scene;

	public final LaunchProgress progress;

	public final DefaultScene defaultScene;



	MainPane(SLauncherFrame frame) {
		frame.setMaximumSize(new Dimension(1265,662));
		this.rootFrame = frame;
		this.repaintEveryTime = OS.LINUX.isCurrent(); // Yup, Swing under Linux doesn't work well.

		this.defaultScene = new DefaultScene(this);
		this.add(defaultScene);


		this.progress = new LaunchProgress(frame);
		this.add(progress);

		this.setScene(defaultScene, false);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onResize(); // Fix
			}
		});
	}

	public PseudoScene getScene() {
		return scene;
	}

	public void setScene(PseudoScene scene) {
		this.setScene(scene, true);
	}

	public void setScene(PseudoScene newscene, boolean animate) {
		if (newscene == null)
			throw new NullPointerException();

		if (newscene.equals(this.scene))
			return;

		for (Component comp : getComponents())
			if (!comp.equals(newscene) && comp instanceof PseudoScene)
				((PseudoScene) comp).setShown(false, animate);

		this.scene = newscene;
		this.scene.setShown(true);

		if(repaintEveryTime)
			repaint();
	}

	public void openDefaultScene() {
		setScene(defaultScene);
	}


	public SLauncherFrame getRootFrame() {
		return rootFrame;
	}

	public LaunchProgress getProgress() {
		return progress;
	}

	@Override
	public void onResize() {
		progress.setBounds(0, getHeight() - ProgressBar.DEFAULT_HEIGHT + 1,
				getWidth(), ProgressBar.DEFAULT_HEIGHT);
	}

	/**
	 * Location of some components can be determined only with
	 * <code>getLocationOnScreen()</code> method. This method should help to
	 * find out the location of a <code>Component</code> on the
	 * <code>MainPane</code>.
	 *
	 */
	public Point getLocationOf(Component comp) {
		Point compLocation = comp.getLocationOnScreen(), paneLocation = getLocationOnScreen();

		return new Point(compLocation.x - paneLocation.x, compLocation.y
				- paneLocation.y);
	}
}
