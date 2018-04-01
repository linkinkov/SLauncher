package ru.spark.slauncher.ui.converter.dummy;

import net.minecraft.launcher.versions.json.DateTypeAdapter;

import java.util.Date;

public class DummyDateConverter extends DummyConverter<Date> {
	private final DateTypeAdapter dateAdapter;
	
	public DummyDateConverter() {
		this.dateAdapter = new DateTypeAdapter();
	}

	@Override
	public Date fromDummyString(String from) throws RuntimeException {
		return dateAdapter.toDate(from);
	}

	@Override
	public String toDummyValue(Date value) throws RuntimeException {
		return dateAdapter.toString(value);
	}
	
	@Override
	public Class<Date> getObjectClass() {
		return Date.class;
	}

}
