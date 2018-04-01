package ru.spark.slauncher.ui.converter;

import ru.spark.slauncher.configuration.Configuration.ActionOnLaunch;
import ru.spark.slauncher.ui.loc.LocalizableStringConverter;

public class ActionOnLaunchConverter extends
		LocalizableStringConverter<ActionOnLaunch> {

	public ActionOnLaunchConverter() {
		super("settings.launch-action");
	}

	@Override
	public ActionOnLaunch fromString(String from) {
		return ActionOnLaunch.get(from);
	}

	@Override
	public String toValue(ActionOnLaunch from) {
		return from.toString();
	}

	@Override
	public String toPath(ActionOnLaunch from) {
		return from.toString();
	}

	@Override
	public Class<ActionOnLaunch> getObjectClass() {
		return ActionOnLaunch.class;
	}
}
