package net.minecraft.launcher.process;

public interface JavaProcessListener {
	void onJavaProcessLog(JavaProcess jp, String line);

	void onJavaProcessEnded(JavaProcess jp);

	void onJavaProcessError(JavaProcess jp, Throwable e);
}