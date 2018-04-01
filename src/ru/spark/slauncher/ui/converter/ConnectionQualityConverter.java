package ru.spark.slauncher.ui.converter;

import ru.spark.slauncher.configuration.Configuration.ConnectionQuality;
import ru.spark.slauncher.ui.loc.LocalizableStringConverter;

public class ConnectionQualityConverter extends
		LocalizableStringConverter<ConnectionQuality> {

	public ConnectionQualityConverter() {
		super("settings.connection");
	}

	@Override
	public ConnectionQuality fromString(String from) {
		return ConnectionQuality.get(from);
	}

	@Override
	public String toValue(ConnectionQuality from) {
		return from.toString();
	}

	@Override
	public String toPath(ConnectionQuality from) {
		return from.toString();
	}

	@Override
	public Class<ConnectionQuality> getObjectClass() {
		return ConnectionQuality.class;
	}

}
