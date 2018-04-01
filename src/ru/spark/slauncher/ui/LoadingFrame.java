package ru.spark.slauncher.ui;

import ru.spark.slauncher.ui.images.ImageCache;
import ru.spark.slauncher.ui.progress.ProgressBar;
import ru.spark.util.OS;
import ru.spark.util.SwingUtil;

import javax.swing.*;
import java.awt.*;

public class LoadingFrame extends JFrame {
	private final ProgressBar progress;

	public LoadingFrame() {
		SwingUtil.initLookAndFeel();
		setLayout(new BorderLayout());

		this.progress = new ProgressBar();
		progress.setPreferredSize(new Dimension(250, 18));

		add(progress, BorderLayout.CENTER);
		add(new JLabel(ImageCache.getIcon("fav32.png")), BorderLayout.WEST);

		if(OS.JAVA_VERSION > 1.6)
			setType(Type.UTILITY);

		pack();
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
	}

	public ProgressBar getProgressBar() {
		return progress;
	}

	public void setProgress(int percent) {
		progress.setIndeterminate(false);
		progress.setValue(percent);
		progress.setCenterString(String.valueOf(percent) +'%');
	}

}
