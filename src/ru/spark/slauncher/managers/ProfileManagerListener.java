package ru.spark.slauncher.managers;

import ru.spark.slauncher.minecraft.auth.AccountListener;

public interface ProfileManagerListener extends AccountListener {
	void onProfilesRefreshed(ProfileManager pm);

	void onProfileManagerChanged(ProfileManager pm);
}
