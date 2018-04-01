package ru.spark.slauncher.managers;

public interface ServerListManagerListener {
	void onServersRefreshing(ServerListManager sm);

	void onServersRefreshingFailed(ServerListManager sm);

	void onServersRefreshed(ServerListManager sm);

}
