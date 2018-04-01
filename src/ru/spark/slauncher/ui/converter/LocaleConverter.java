package ru.spark.slauncher.ui.converter;

import ru.spark.slauncher.configuration.Configuration;

import java.util.Locale;

public class LocaleConverter implements StringConverter<Locale> {
	@Override
	public String toString(Locale from) {
		if(from == null)
			return null;
		return from.getDisplayCountry(Locale.US) + " (" + from.toString() + ")";
	}

	@Override
	public Locale fromString(String from) {
		return Configuration.getLocaleOf(from);
	}

	@Override
	public String toValue(Locale from) {
		if (from == null)
			return null;
		return from.toString();
	}

	@Override
	public Class<Locale> getObjectClass() {
		return Locale.class;
	}

}
