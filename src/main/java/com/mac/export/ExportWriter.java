package com.mac.export;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ExportWriter<T> {

	public void write(List<T> data) throws IOException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException;

	public void close() throws IOException, Exception;

	public long getSize();

	public String getFileName();

	public void finalize() throws IOException;

}
