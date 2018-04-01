package ru.spark.slauncher.exceptions;

public class SLauncherException extends RuntimeException {
	private static final long serialVersionUID = 5812333186574527445L;

	public SLauncherException(String message, Throwable e) {
		super(message, e);

		e.printStackTrace();
	}

	public SLauncherException(String message) {
		super(message);
	}
}
