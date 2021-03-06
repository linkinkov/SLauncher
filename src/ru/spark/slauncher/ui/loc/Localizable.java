package ru.spark.slauncher.ui.loc;

import ru.spark.slauncher.configuration.LangConfiguration;
import ru.spark.util.Reflect;
import ru.spark.util.U;

import java.awt.*;

public class Localizable {
	public static final Object[] EMPTY_VARS = new Object[0];
	public static final LocalizableFilter defaultFilter = new LocalizableFilter() {
		@Override
		public boolean localize(Component comp) {
			return true;
		}
	};

	private static LangConfiguration lang;

	public static void setLang(LangConfiguration l) {
		lang = l;
	}

	public static LangConfiguration get() {
		return lang;
	}

	public static boolean exists() {
		return lang != null;
	}

	public static String get(String path) {
		return lang != null ? lang.get(path) : path;
	}

	public static String get(String path, Object... vars) {
		return lang != null ? lang.get(path, vars) : path + " {"
				+ U.toLog(vars) + "}";
	}

	public static String nget(String path) {
		return lang != null ? lang.nget(path) : null;
	}

	public static String[] checkVariables(Object[] check) {
		if (check == null)
			throw new NullPointerException();

		String[] string = new String[check.length];

		for (int i = 0; i < check.length; i++) {
			if (check[i] == null)
				throw new NullPointerException("Variable at index " + i
						+ " is NULL!");
			string[i] = check[i].toString();
		}

		return string;
	}

	public static void updateContainer(Container container,
			LocalizableFilter filter) {
		for (Component c : container.getComponents()) {
			LocalizableComponent asLocalizable = Reflect.cast(c, LocalizableComponent.class);

			if (asLocalizable != null && filter.localize(c))
				asLocalizable.updateLocale();

			if (c instanceof Container)
				updateContainer((Container) c, filter);
		}
	}

	public static void updateContainer(Container container) {
		updateContainer(container, defaultFilter);
	}

	public interface LocalizableFilter {
		boolean localize(Component comp);
	}
}
