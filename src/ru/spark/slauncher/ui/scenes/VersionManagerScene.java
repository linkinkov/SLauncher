package ru.spark.slauncher.ui.scenes;

import ru.spark.slauncher.ui.MainPane;
import ru.spark.slauncher.ui.versions.VersionHandler;

public class VersionManagerScene extends PseudoScene {
	private static final long serialVersionUID = 758826812081732720L;

	final VersionHandler handler;

	public VersionManagerScene(MainPane main) {
		super(main);

		this.handler = new VersionHandler(this);
		add(handler.list);
	}

	@Override
	public void onResize() {
		super.onResize();

		handler.list.setLocation(
				getWidth() / 2 - handler.list.getWidth() / 2,
				getHeight() / 2 - handler.list.getHeight() / 2);
	}

}
