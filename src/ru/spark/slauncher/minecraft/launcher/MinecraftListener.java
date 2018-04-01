package ru.spark.slauncher.minecraft.launcher;

import ru.spark.slauncher.minecraft.crash.Crash;

public interface MinecraftListener {
	void onMinecraftPrepare();

	void onMinecraftAbort();

	void onMinecraftLaunch();

	void onMinecraftClose();

	void onMinecraftError(Throwable e);

	void onMinecraftKnownError(MinecraftException e);

	void onMinecraftCrash(Crash crash);
}
