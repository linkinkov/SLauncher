package ru.spark.slauncher.ui.block;

public interface Blockable {
	void block(Object reason);

	void unblock(Object reason);
}
