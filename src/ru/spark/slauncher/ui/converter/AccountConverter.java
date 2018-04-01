package ru.spark.slauncher.ui.converter;

import ru.spark.slauncher.managers.ProfileManager;
import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.loc.LocalizableStringConverter;

public class AccountConverter extends LocalizableStringConverter<Account> {
	private final ProfileManager pm;

	public AccountConverter(ProfileManager pm) {
		super(null);

		if (pm == null)
			throw new NullPointerException();

		this.pm = pm;
	}

	@Override
	public String toString(Account from) {
		if (from == null)
			return Localizable.get("account.empty");
		if (from.getUsername() == null)
			return null;

		return from.getUsername();
	}

	@Override
	public Account fromString(String from) {
		return pm.getAuthDatabase().getByUsername(from);
	}

	@Override
	public String toValue(Account from) {
		if (from == null || from.getUsername() == null)
			return null;
		return from.getUsername();
	}

	@Override
	public String toPath(Account from) {
		return null;
	}

	@Override
	public Class<Account> getObjectClass() {
		return Account.class;
	}
}
