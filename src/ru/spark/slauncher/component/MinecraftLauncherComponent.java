package ru.spark.slauncher.component;

import ru.spark.slauncher.minecraft.launcher.MinecraftLauncher;
import ru.spark.slauncher.minecraft.launcher.MinecraftLauncherAssistant;

public interface MinecraftLauncherComponent {
	MinecraftLauncherAssistant getAssistant(MinecraftLauncher launcher);
}
