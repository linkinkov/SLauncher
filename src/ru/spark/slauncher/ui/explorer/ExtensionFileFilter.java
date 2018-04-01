package ru.spark.slauncher.ui.explorer;

import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.util.FileUtil;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ExtensionFileFilter extends FileFilter {
	private final String extension;
	private final boolean acceptNull;
	
	public ExtensionFileFilter(String extension, boolean acceptNullExtension) {
		if(extension == null)
			throw new NullPointerException("Extension is NULL!");
		
		if(extension.isEmpty())
			throw new IllegalArgumentException("Extension is empty!");
		
		this.extension = extension;
		
		this.acceptNull = acceptNullExtension;
	}
	
	public ExtensionFileFilter(String extension) {
		this(extension, true);
	}
	
	public String getExtension() {
		return extension;
	}
	
	public boolean acceptsNull() {
		return acceptNull;
	}

	@Override
	public boolean accept(File f) {
		String currentExtension = FileUtil.getExtension(f);
		
		if(acceptNull && currentExtension == null)
			return true;
		
		return extension.equals(currentExtension);
	}

	@Override
	public String getDescription() {
		return Localizable.get("explorer.extension.format", extension.toUpperCase());
	}

}
