package ru.spark.slauncher.minecraft.launcher;

import ru.spark.slauncher.downloader.Downloader;

public abstract class MinecraftLauncherAssistant {
	private final MinecraftLauncher launcher;

	MinecraftLauncherAssistant(MinecraftLauncher launcher) {
		if (launcher == null)
			throw new NullPointerException();

		this.launcher = launcher;
	}

	public MinecraftLauncher getLauncher() {
		return launcher;
	}

	protected abstract void collectInfo() throws MinecraftException;

	protected abstract void collectResources(Downloader d)
			throws MinecraftException;

	protected abstract void constructJavaArguments() throws MinecraftException;

	protected abstract void constructProgramArguments()
			throws MinecraftException;
}
