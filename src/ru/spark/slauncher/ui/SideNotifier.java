package ru.spark.slauncher.ui;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.images.ImageCache;
import ru.spark.slauncher.ui.listener.UpdateUIListener;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.swing.ImagePanel;
import ru.spark.slauncher.updater.AdParser.AdMap;
import ru.spark.slauncher.updater.Update;
import ru.spark.slauncher.updater.Updater;
import ru.spark.slauncher.updater.UpdaterListener;

import java.awt.*;

public class SideNotifier extends ImagePanel implements UpdaterListener {
	private static final String LANG_PREFIX = "notifier.";

	private NotifierStatus status;
	private Update update;

	public SideNotifier() {
		super((Image) null, 1.0F, 0.75F, false, true);

		SLauncher.getInstance().getUpdater().addListener(this);
	}

	public NotifierStatus getStatus() {
		return status;
	}

	public void setStatus(NotifierStatus status) {
		if(status == null)
			throw new NullPointerException();

		this.status = status;

		this.setImage(status.getImage());
		if(status == NotifierStatus.NONE) hide(); else show();
	}

	@Override
	protected boolean onClick() {
		boolean result = processClick();

		if(result)
			hide();

		return result;
	}

	private boolean processClick() {
		if(!super.onClick())
			return false;

		switch(status) {
		case FAILED:
			Alert.showLocAsyncWarning(LANG_PREFIX + status.toString());

			break;
		case FOUND:

			if(update == null)
				throw new IllegalStateException("Update is NULL!");

			String
			prefix = LANG_PREFIX + status.toString() +".",
			title = prefix + "title",
			question = prefix + "question";

			boolean ask = Alert.showQuestion(
					Localizable.get(title),
					Localizable.get( question, update.getVersion() +" ("+ update.getCode() +")" ), update.getDescription()
					);

			if(!ask)
				return false;

			UpdateUIListener listener = new UpdateUIListener(update);
			listener.push();

			break;
		case NONE:
			break;
		default:
			throw new IllegalStateException("Unknown status: "+ status);
		}

		return true;
	}

	@Override
	public void onUpdaterRequesting(Updater u) {
		setFoundUpdate(null);
	}

	@Override
	public void onUpdaterRequestError(Updater u) {
		setStatus(NotifierStatus.FAILED);
	}

	@Override
	public void onUpdateFound(Update upd) {
		if(upd.isRequired()) return; // Ingore required update, let it be processed by RequiredUpdateListener
		setFoundUpdate(upd);
	}

	@Override
	public void onUpdaterNotFoundUpdate(Updater u) {
		setFoundUpdate(null);
	}

	@Override
	public void onAdFound(Updater u, AdMap adMap) {
	}

	private void setFoundUpdate(Update upd) {
		this.update = upd;

		setStatus(upd == null? NotifierStatus.NONE : NotifierStatus.FOUND);

		if(upd == null || SLauncher.getInstance().isLauncherWorking() || SLauncher.getInstance().getSettings().getDouble("update.asked") == upd.getVersion())
			return;

		processClick();
		SLauncher.getInstance().getSettings().set("update.asked", upd.getVersion());
	}

	public enum NotifierStatus {
		FAILED("warning.png"), FOUND("down32.png"), NONE;

		private final Image image;

		NotifierStatus(String imagePath) {
			this.image = imagePath == null? null : ImageCache.getImage(imagePath);
		}

		NotifierStatus() {
			this(null);
		}

		public Image getImage() {
			return image;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
