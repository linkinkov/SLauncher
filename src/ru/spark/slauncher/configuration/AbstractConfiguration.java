package ru.spark.slauncher.configuration;

import java.io.IOException;

/**
 * Abstract Configuration interface
 * 
 * @author Artur Khusainov
 * 
 */
public interface AbstractConfiguration {
	String get(String key);

	int getInteger(String key);

	double getDouble(String key);

	float getFloat(String key);

	long getLong(String key);

	boolean getBoolean(String key);

	String getDefault(String key);

	int getDefaultInteger(String key);

	double getDefaultDouble(String key);

	float getDefaultFloat(String key);

	long getDefaultLong(String key);

	boolean getDefaultBoolean(String key);

	void set(String key, Object value);

	void clear();

	void save() throws IOException;
}
