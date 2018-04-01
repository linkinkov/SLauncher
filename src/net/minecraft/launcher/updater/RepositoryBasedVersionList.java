package net.minecraft.launcher.updater;

import com.google.gson.JsonSyntaxException;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Version;
import ru.spark.slauncher.repository.Repository;
import ru.spark.util.OS;

import java.io.IOException;

public class RepositoryBasedVersionList extends RemoteVersionList {
	private final Repository repository;

	RepositoryBasedVersionList(Repository repository) {
		if (repository == null)
			throw new NullPointerException();

		this.repository = repository;
	}

	@Override
	public RawVersionList getRawList() throws IOException {
		RawVersionList rawList = super.getRawList();

		for (Version version : rawList.getVersions())
			version.setSource(repository);

		return rawList;
	}

	@Override
	public CompleteVersion getCompleteVersion(Version version)
			throws JsonSyntaxException, IOException {
		CompleteVersion complete = super.getCompleteVersion(version);

		complete.setSource(repository);

		return complete;
	}

	@Override
	public boolean hasAllFiles(CompleteVersion paramCompleteVersion,
			OS paramOperatingSystem) {
		return true;
	}

	@Override
	protected String getUrl(String uri) throws IOException {
		return repository.getUrl(uri);
	}

}
