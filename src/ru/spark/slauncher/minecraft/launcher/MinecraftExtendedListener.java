package ru.spark.slauncher.minecraft.launcher;

public interface MinecraftExtendedListener extends MinecraftListener {
	void onMinecraftCollecting();

	void onMinecraftComparingAssets();

	void onMinecraftDownloading();

	void onMinecraftReconstructingAssets();

	void onMinecraftUnpackingNatives();

	void onMinecraftDeletingEntries();

	void onMinecraftConstructing();

	@Override
	void onMinecraftLaunch();

	void onMinecraftPostLaunch();
}
