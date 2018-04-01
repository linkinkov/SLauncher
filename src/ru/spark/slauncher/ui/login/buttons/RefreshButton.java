package ru.spark.slauncher.ui.login.buttons;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.managers.ComponentManager;
import ru.spark.slauncher.managers.ComponentManagerListener;
import ru.spark.slauncher.managers.ComponentManagerListenerHelper;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.swing.ImageButton;
import ru.spark.slauncher.updater.AdParser.AdMap;
import ru.spark.slauncher.updater.Update;
import ru.spark.slauncher.updater.Updater;
import ru.spark.slauncher.updater.UpdaterListener;
import ru.spark.util.async.AsyncThread;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButton extends ImageButton implements Blockable,
		ComponentManagerListener, UpdaterListener {
	private static final long serialVersionUID = -1334187593288746348L;
	private final static int TYPE_REFRESH = 0;
	private final static int TYPE_CANCEL = 1;

	private LoginForm lf;
	private int type;
	private final Image refresh = loadImage("refresh.png"),
			cancel = loadImage("cancel.png");
	private Updater updaterFlag;

	private RefreshButton(LoginForm loginform, int type) {
		this.lf = loginform;

		this.rotation = ImageRotation.CENTER;
		this.setType(type, false);

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onPressButton();
			}
		});

		this.initImage();

		SLauncher.getInstance().getManager()
				.getComponent(ComponentManagerListenerHelper.class)
				.addListener(this);
		SLauncher.getInstance().getUpdater().addListener(this);
	}

	RefreshButton(LoginForm loginform) {
		this(loginform, TYPE_REFRESH);
	}

	private void onPressButton() {
		switch (type) {
			case TYPE_REFRESH:
				if(SLauncher.isBeta())
					SLauncher.getInstance().getUpdater().asyncFindUpdate();
				else if (updaterFlag != null)
					updaterFlag.asyncFindUpdate();
				else
					AsyncThread.execute(new Runnable() {
						@Override
						public void run() {
							lf.scene.infoPanel.updateAd(true);
						}
					});

				SLauncher.getInstance().getManager().startAsyncRefresh();
				break;
			case TYPE_CANCEL:
				SLauncher.getInstance().getManager().stopRefresh();
				break;
			default:
				throw new IllegalArgumentException("Unknown type: " + type
						+ ". Use RefreshButton.TYPE_* constants.");
		}

		lf.defocus();
	}

	void setType(int type) {
		this.setType(type, true);
	}

	void setType(int type, boolean repaint) {
		switch (type) {
			case TYPE_REFRESH:
				this.image = refresh;
				break;
			case TYPE_CANCEL:
				this.image = cancel;
				break;
			default:
				throw new IllegalArgumentException("Unknown type: " + type
						+ ". Use RefreshButton.TYPE_* constants.");
		}

		this.type = type;
	}

	@Override
	public void onUpdaterRequesting(Updater u) {
	}

	@Override
	public void onUpdaterRequestError(Updater u) {
		this.updaterFlag = u;
	}

	@Override
	public void onUpdateFound(Update upd) {
		this.updaterFlag = null;
	}

	@Override
	public void onUpdaterNotFoundUpdate(Updater u) {
		this.updaterFlag = null;
	}

	@Override
	public void onAdFound(Updater u, AdMap adMap) {
	}

	@Override
	public void onComponentsRefreshing(ComponentManager manager) {
		Blocker.block(this, LoginForm.REFRESH_BLOCK);
	}

	@Override
	public void onComponentsRefreshed(ComponentManager manager) {
		Blocker.unblock(this, LoginForm.REFRESH_BLOCK);
	}

	//

	@Override
	public void block(Object reason) {
		if (reason.equals(LoginForm.REFRESH_BLOCK))
			setType(TYPE_CANCEL);
		else
			setEnabled(false);
	}

	@Override
	public void unblock(Object reason) {
		if (reason.equals(LoginForm.REFRESH_BLOCK))
			setType(TYPE_REFRESH);
		setEnabled(true);
	}
}
