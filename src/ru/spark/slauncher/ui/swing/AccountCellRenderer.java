package ru.spark.slauncher.ui.swing;

import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.ui.images.ImageCache;
import ru.spark.slauncher.ui.loc.Localizable;

import javax.swing.*;
import java.awt.*;

public class AccountCellRenderer implements ListCellRenderer<Account> {
	public static final Account EMPTY = Account.randomAccount(), MANAGE = Account.randomAccount();

	private static final Icon MANAGE_ICON = ImageCache.getIcon("gear.png");
	private static final Icon PREMIUM_ICON = ImageCache.getIcon("premium.png");

	private final DefaultListCellRenderer defaultRenderer;
	private AccountCellType type;

	public AccountCellRenderer(AccountCellType type) {
		if (type == null)
			throw new NullPointerException("CellType cannot be NULL!");

		this.defaultRenderer = new DefaultListCellRenderer();
		this.type = type;
	}

	public AccountCellRenderer() {
		this(AccountCellType.PREVIEW);
	}

	public AccountCellType getType() {
		return type;
	}

	public void setType(AccountCellType type) {
		if (type == null)
			throw new NullPointerException("CellType cannot be NULL!");

		this.type = type;
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Account> list, Account value, int index,
			boolean isSelected, boolean cellHasFocus) {

		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		renderer.setAlignmentY(Component.CENTER_ALIGNMENT);

		if (value == null || value.equals(EMPTY)) {
			renderer.setText(Localizable.get("account.empty"));
		} else if (value.equals(MANAGE)) {
			renderer.setText(Localizable.get("account.manage"));
			renderer.setIcon(MANAGE_ICON);
		} else {

			if (value.isPremium()) {
				renderer.setIcon(PREMIUM_ICON);
				renderer.setFont(renderer.getFont().deriveFont(Font.BOLD));
			}

			switch (type) {
			case EDITOR:

				if (!value.hasUsername()) {
					renderer.setText(Localizable.get("account.creating"));
					renderer.setFont(renderer.getFont().deriveFont(Font.ITALIC));
				} else {
					renderer.setText(value.getUsername());
				}

				break;
			default:
				renderer.setText(value.getDisplayName());
			}
		}

		return renderer;
	}

	public enum AccountCellType {
		PREVIEW, EDITOR
	}
}
