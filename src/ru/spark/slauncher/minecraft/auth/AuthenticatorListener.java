package ru.spark.slauncher.minecraft.auth;

public interface AuthenticatorListener {
	void onAuthPassing(Authenticator auth);

	void onAuthPassingError(Authenticator auth, Throwable e);

	void onAuthPassed(Authenticator auth);
}
