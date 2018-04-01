package ru.spark.slauncher.ui.login;

public class LoginWaitException extends LoginException {
	private final LoginWaitTask waitTask;

	public LoginWaitException(String reason, LoginWaitTask waitTask) {
		super(reason);

		if (waitTask == null)
			throw new NullPointerException("wait task");

		this.waitTask = waitTask;
	}

	public LoginWaitTask getWaitTask() {
		return waitTask;
	}

	public interface LoginWaitTask {
		void runTask() throws LoginException;
	}
}
