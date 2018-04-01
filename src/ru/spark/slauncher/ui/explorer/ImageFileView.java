package ru.spark.slauncher.ui.explorer;

import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.util.FileUtil;

import javax.swing.filechooser.FileView;
import java.io.File;

public class ImageFileView extends FileView {

	@Override
	public String getTypeDescription(File f) {
		String extension = FileUtil.getExtension(f), localized = Localizable
				.nget("explorer.extension." + extension);

		return localized;
	}

}
