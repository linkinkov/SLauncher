package net.minecraft.launcher.versions;

import net.minecraft.launcher.updater.VersionList;
import ru.spark.slauncher.repository.Repository;

import java.util.Date;

public interface Version {
	String getID();

	void setID(String id);

	ReleaseType getReleaseType();

	Repository getSource();

	void setSource(Repository repository);

	Date getUpdatedTime();

	Date getReleaseTime();

	VersionList getVersionList();

	void setVersionList(VersionList list);
}
