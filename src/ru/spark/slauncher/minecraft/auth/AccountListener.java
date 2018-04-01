package ru.spark.slauncher.minecraft.auth;

public interface AccountListener {
	void onAccountsRefreshed(AuthenticatorDatabase db);

}
