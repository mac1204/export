package com.mac.export.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.mac.export.ExportWriter;
import com.mac.export.naming.impl.BaseHeaderNamingStrategy;

public abstract class BaseExportWriter<T> implements ExportWriter<T> {

	protected List<String> fieldNames = null;
	protected long dataRowCount = 0;
	protected boolean requiredHeader = true;
	protected Class<? extends BaseHeaderNamingStrategy> headerNamingStrategy = BaseHeaderNamingStrategy.class;
	protected String directory = System.getProperty("java.io.tmpdir");
	protected String fileName = null;

	public abstract void write(List<T> data) throws IOException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException;

	public abstract void close() throws IOException, Exception;

	public long getSize() {
		return dataRowCount;
	}

	public String getFileName() {
		return fileName;
	}

	public abstract void finalize() throws IOException;

}
