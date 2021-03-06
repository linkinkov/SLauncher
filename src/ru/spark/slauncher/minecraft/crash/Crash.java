package ru.spark.slauncher.minecraft.crash;

import ru.spark.slauncher.minecraft.crash.CrashSignatureContainer.CrashSignature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crash {
	private String file;
	private List<CrashSignature> signatures = new ArrayList<CrashSignature>();

	void addSignature(CrashSignature sign) {
		signatures.add(sign);
	}

	void removeSignature(CrashSignature sign) {
		signatures.remove(sign);
	}

	void setFile(String path) {
		this.file = path;
	}

	public String getFile() {
		return this.file;
	}

	public List<CrashSignature> getSignatures() {
		return Collections.unmodifiableList(signatures);
	}

	public boolean hasSignature(CrashSignature s) {
		return signatures.contains(s);
	}

	public boolean isRecognized() {
		return !signatures.isEmpty();
	}
}
