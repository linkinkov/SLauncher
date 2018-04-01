package ru.spark.slauncher.configuration;

import ru.spark.exceptions.ParseException;
import ru.spark.slauncher.configuration.Configuration.ActionOnLaunch;
import ru.spark.slauncher.configuration.Configuration.ConnectionQuality;
import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.util.IntegerArray;
import ru.spark.util.StringUtil;

class PlainParser {
	public static void parse(Object plainValue, Object defaultValue)
			throws ParseException {
		if (defaultValue == null)
			return; // Ignore value

		if (plainValue == null)
			throw new ParseException("Value is NULL");

		String value = plainValue.toString();

		try {
			if (defaultValue instanceof Integer)
				Integer.parseInt(value);

			else if (defaultValue instanceof Boolean)
				StringUtil.parseBoolean(value);

			else if (defaultValue instanceof Double)
				Double.parseDouble(value);

			else if (defaultValue instanceof Long)
				Long.parseLong(value);

			else if (defaultValue instanceof IntegerArray)
				IntegerArray.parseIntegerArray(value);

			else if (defaultValue instanceof ActionOnLaunch) {
				if (!ActionOnLaunch.parse(value))
					throw new ParseException("Cannot parse ActionOnLaunch");
			}

			else if (defaultValue instanceof ConsoleType) {
				if (!ConsoleType.parse(value))
					throw new ParseException("Cannot parse ConsoleType");
			}

			else if (defaultValue instanceof ConnectionQuality) {
				if (!ConnectionQuality.parse(value))
					throw new ParseException("Cannot parse ConnectionQuality");
			}
		} catch (Exception e) {
			if (e instanceof ParseException)
				throw (ParseException) e;

			throw new ParseException("Cannot parse input value!", e);
		}
	}
}
