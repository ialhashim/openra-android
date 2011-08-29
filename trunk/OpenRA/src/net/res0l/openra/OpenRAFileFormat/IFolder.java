package net.res0l.openra.OpenRAFileFormat;

import java.io.InputStream;
import java.util.Map;

public interface IFolder {
	InputStream GetContent(String filename);
	boolean Exists(String filename);
	Iterable<Long> AllFileHashes();
	void Write(Map<String, byte[]> contents);
	int Priority = 0;
}
