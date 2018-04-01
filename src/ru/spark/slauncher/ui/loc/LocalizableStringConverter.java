package ru.spark.slauncher.ui.loc;

import ru.spark.slauncher.ui.converter.StringConverter;

public abstract class LocalizableStringConverter<T> implements StringConverter<T> {
	private final String prefix;

	public LocalizableStringConverter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString(T from) {
		return Localizable.get(getPath(from));
	}

	String getPath(T from) {
		String prefix = getPrefix();

		if (prefix == null || prefix.isEmpty())
			return toPath(from);

		String path = toPath(from);
		return prefix + "." + path;
	}

	String getPrefix() {
		return prefix;
	}

	protected abstract String toPath(T from);
}
