package net.minecraft.launcher.updater;

import ru.spark.slauncher.repository.Repository;

public class OfficialVersionList extends RepositoryBasedVersionList {
	public OfficialVersionList() {
		super(Repository.OFFICIAL_VERSION_REPO);
	}
}
