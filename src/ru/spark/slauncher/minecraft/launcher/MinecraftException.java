package ru.spark.slauncher.minecraft.launcher;

import ru.spark.slauncher.ui.loc.Localizable;

public class MinecraftException extends Exception {
	private static final long serialVersionUID = -2415374288600214879L;

	private final String langPath;
	private final String[] langVars;

	MinecraftException(String message, String langPath,
			Throwable cause, Object... langVars) {
		super(message, cause);

		if (langPath == null)
			throw new NullPointerException("Lang path required!");

		if (langVars == null)
			langVars = new Object[0];

		this.langPath = langPath;
		this.langVars = Localizable.checkVariables(langVars);
	}

	MinecraftException(String message, String langPath, Throwable cause) {
		this(message, langPath, cause, new Object[0]);
	}

	MinecraftException(String message, String langPath, Object... vars) {
		this(message, langPath, null, vars);
	}

	public String getLangPath() {
		return langPath;
	}

	public String[] getLangVars() {
		return langVars;
	}

}
