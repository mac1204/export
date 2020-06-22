package com.mac.export.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.mac.export.ExportWriter;
import com.mac.export.Utils;
import com.mac.export.annotation.HeaderNaming;
import com.mac.export.pojo.ColumnName;

public class CsvExportWriter<T> extends BaseExportWriter<T> implements ExportWriter<T> {

	private CSVPrinter csvPrinter = null;
	private BufferedWriter writer = null;

	public CsvExportWriter(String fileName) throws Exception {
		super();
		if (csvPrinter == null) {
			this.fileName = directory.concat("/").concat(fileName);
			writer = Files.newBufferedWriter(Paths.get(this.fileName));
			csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		}
	}

	public CsvExportWriter(String fileName, boolean requiredHeader) throws Exception {
		this(fileName);
		this.requiredHeader = requiredHeader;
	}

	@Override
	public void write(List<T> data) throws IOException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		// In this example we construct CSVPrinter on a File, can also do an
		// OutputStream
		if (fieldNames == null) {
			if (data.get(0).getClass().isAnnotationPresent(HeaderNaming.class)) {
				HeaderNaming headerNaming = data.get(0).getClass().getAnnotation(HeaderNaming.class);
				if (headerNaming != null) {
					headerNamingStrategy = headerNaming.value();
				}
			}
			List<ColumnName> columnNames = Utils.getColumnNamesForClass(data.get(0).getClass(), headerNamingStrategy);
			List<String> headers = new ArrayList<String>();
			fieldNames = new ArrayList<String>();
			for (ColumnName columnName : columnNames) {
				headers.add(columnName.getHeaderName());
				fieldNames.add(columnName.getFieldName());
			}
			if (requiredHeader) {
				csvPrinter.printRecord(headers);
			}
		}

		Class<? extends Object> classz = data.get(0).getClass();
		for (T t : data) {
			for (String fieldName : fieldNames) {
				Method method = null;
				try {
					method = classz.getMethod("get" + Utils.capitalize(fieldName));
				} catch (NoSuchMethodException nme) {
					method = classz.getMethod("get" + fieldName);
				}
				Object value = method.invoke(t, (Object[]) null);
				csvPrinter.print(value);

			}
			csvPrinter.println();
			dataRowCount++;
		}
	}

	@Override
	public void close() throws IOException {

		csvPrinter.flush();
		csvPrinter.close();

	}

	@Override
	public long getSize() {
		return dataRowCount;
	}

	@Override
	public void finalize() throws IOException {
		if (csvPrinter != null) {
			csvPrinter.close();
		}

		System.out.println("CSV Export writer is destroyed by the Garbage Collector");
	}

}
