package ru.spark.slauncher.ui.converter;

import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.slauncher.ui.loc.LocalizableStringConverter;

public class ConsoleTypeConverter extends
		LocalizableStringConverter<ConsoleType> {

	public ConsoleTypeConverter() {
		super("settings.console");
	}

	@Override
	public ConsoleType fromString(String from) {
		return ConsoleType.get(from);
	}

	@Override
	public String toValue(ConsoleType from) {
		if (from == null)
			return null;
		return from.toString();
	}

	@Override
	public String toPath(ConsoleType from) {
		if (from == null)
			return null;
		return from.toString();
	}

	@Override
	public Class<ConsoleType> getObjectClass() {
		return ConsoleType.class;
	}

}
