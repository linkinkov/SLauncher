package ru.spark.slauncher.ui.loc;

import ru.spark.slauncher.ui.SLauncherFrame;
import ru.spark.slauncher.ui.center.CenterPanel;
import ru.spark.slauncher.ui.text.ExtendedTextField;

public class LocalizableTextField extends ExtendedTextField implements
LocalizableComponent {
	private static final long serialVersionUID = 359096767189321072L;

	protected String placeholderPath;
	protected String[] variables;

	public LocalizableTextField(CenterPanel panel, String placeholderPath,
			String value) {
		super(panel, null, value);

		this.setValue(value);
		this.setPlaceholder(placeholderPath);
		this.setFont(getFont().deriveFont(SLauncherFrame.fontSize));
	}

	public LocalizableTextField(CenterPanel panel, String placeholderPath) {
		this(panel, placeholderPath, null);
	}

	public LocalizableTextField(String placeholderPath) {
		this(null, placeholderPath, null);
	}

	public LocalizableTextField() {
		this(null, null, null);
	}

	public void setPlaceholder(String placeholderPath, Object... vars) {
		this.placeholderPath = placeholderPath;
		this.variables = Localizable.checkVariables(vars);

		String value = Localizable.get(placeholderPath);

		for (int i = 0; i < variables.length; i++)
			value = value.replace("%" + i, variables[i]);

		super.setPlaceholder(value);
	}

	@Override
	public void setPlaceholder(String placeholderPath) {
		setPlaceholder(placeholderPath, Localizable.EMPTY_VARS);
	}

	public String getPlaceholderPath() {
		return this.placeholderPath;
	}

	@Override
	public void updateLocale() {
		setPlaceholder(placeholderPath, (Object[]) variables);
	}
}
