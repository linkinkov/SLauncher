package ru.spark.slauncher.ui.swing;

import ru.spark.slauncher.ui.converter.StringConverter;

import javax.swing.*;

public abstract class ConverterCellRenderer<T> implements ListCellRenderer<T> {
	protected final StringConverter<T> converter;

	ConverterCellRenderer(StringConverter<T> converter) {
		if (converter == null)
			throw new NullPointerException();

		this.converter = converter;
	}

	public StringConverter<T> getConverter() {
		return converter;
	}

}
