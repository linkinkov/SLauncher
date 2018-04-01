package ru.spark.slauncher.ui.explorer;

import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.util.FileUtil;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class ImageFileFilter extends FileFilter {
	public static final Pattern extensionPattern = Pattern.compile(
			"^(?:jp(?:e|)g|png)$", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean accept(File f) {
		String extension = FileUtil.getExtension(f);

		if (extension == null)
			return true;

		return extensionPattern.matcher(extension).matches();
	}

	@Override
	public String getDescription() {
		return Localizable.get("explorer.type.image");
	}

}
