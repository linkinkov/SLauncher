package ru.spark.slauncher.minecraft.auth;

import java.util.List;
import java.util.Map;

public class User {
	private String id;
	private List<Map<String, String>> properties;

	public String getID() {
		return this.id;
	}

	public List<Map<String, String>> getProperties() {
		return properties;
	}
}
