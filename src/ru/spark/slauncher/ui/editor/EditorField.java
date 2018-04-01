package ru.spark.slauncher.ui.editor;

import ru.spark.slauncher.ui.block.Blockable;

public interface EditorField extends Blockable {
	String getSettingsValue();

	void setSettingsValue(String value);

	boolean isValueValid();
}
