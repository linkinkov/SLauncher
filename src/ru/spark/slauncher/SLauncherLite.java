package ru.spark.slauncher;

import joptsimple.OptionSet;
import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.slauncher.minecraft.crash.Crash;
import ru.spark.slauncher.minecraft.launcher.MinecraftException;
import ru.spark.slauncher.minecraft.launcher.MinecraftLauncher;
import ru.spark.slauncher.minecraft.launcher.MinecraftListener;
import ru.spark.slauncher.ui.console.Console.CloseAction;

public class SLauncherLite implements MinecraftListener {
	private final SLauncher tlauncher;
	private final OptionSet args;

	SLauncherLite(SLauncher tlauncher) {
		if (tlauncher == null)
			throw new NullPointerException();

		this.tlauncher = tlauncher;
		tlauncher.getVersionManager().startRefresh(true);
		tlauncher.getProfileManager().refreshComponent();

		this.args = tlauncher.getArguments();

		MinecraftLauncher launcher = new MinecraftLauncher(this, args);
		launcher.addListener(tlauncher.getMinecraftListener());
		launcher.addListener(this);

		if (launcher.getConsole() != null)
			launcher.getConsole().setCloseAction(CloseAction.EXIT);

		launcher.start();
		SLauncher.kill();
	}

	public SLauncher getLauncher() {
		return tlauncher;
	}

	@Override
	public void onMinecraftPrepare() {
	}

	@Override
	public void onMinecraftAbort() {
	}

	@Override
	public void onMinecraftLaunch() {
	}

	@Override
	public void onMinecraftClose() {
		if (!args.has("console")
				&& tlauncher.getSettings().getConsoleType()
				.equals(ConsoleType.NONE))
			SLauncher.kill();
	}

	@Override
	public void onMinecraftError(Throwable e) {
	}

	@Override
	public void onMinecraftKnownError(MinecraftException e) {
	}

	@Override
	public void onMinecraftCrash(Crash crash) {
	}

}
