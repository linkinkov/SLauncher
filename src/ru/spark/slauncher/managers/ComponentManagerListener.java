package ru.spark.slauncher.managers;

public interface ComponentManagerListener {
	void onComponentsRefreshing(ComponentManager manager);

	void onComponentsRefreshed(ComponentManager manager);
}
