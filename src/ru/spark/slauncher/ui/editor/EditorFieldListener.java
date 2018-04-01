package ru.spark.slauncher.ui.editor;

public abstract class EditorFieldListener {
	protected abstract void onChange(EditorHandler handler, String oldValue,
			String newValue);
}
