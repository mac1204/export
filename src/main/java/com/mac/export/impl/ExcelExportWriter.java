package com.mac.export.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.itextpdf.text.DocumentException;
import com.mac.export.ExportWriter;
import com.mac.export.Utils;
import com.mac.export.annotation.HeaderNaming;
import com.mac.export.pojo.ColumnName;

public class ExcelExportWriter<T> extends BaseExportWriter<T> implements ExportWriter<T> {

	private OutputStream fos = null;
	private HSSFWorkbook workbook = null;
	private File file = null;
	private Sheet sheet = null;
	private int rowCount = 0;
	private int columnCount = 0;

	public ExcelExportWriter(String fileName) throws FileNotFoundException, DocumentException {
		super();
		if (file == null) {
			this.fileName = directory.concat(".").concat(fileName);
			file = new File(this.fileName);
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet();
			fos = new FileOutputStream(file);
		}
	}

	public ExcelExportWriter(String fileName, boolean requiredHeader) throws FileNotFoundException, DocumentException {
		this(fileName);
		this.requiredHeader = requiredHeader;

	}

	@Override
	public void write(List<T> data) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
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
				Row row = sheet.createRow(rowCount++);
				for (String header : headers) {
					Cell cell = row.createCell(columnCount++);
					cell.setCellValue(header);
				}
			}
		}
		Class<? extends Object> classz = data.get(0).getClass();
		for (T t : data) {
			Row row = sheet.createRow(rowCount++);
			columnCount = 0;
			for (String fieldName : fieldNames) {
				Cell cell = row.createCell(columnCount);
				Method method = null;
				try {
					method = classz.getMethod("get" + Utils.capitalize(fieldName));
				} catch (NoSuchMethodException nme) {
					method = classz.getMethod("get" + fieldName);
				}
				Object value = method.invoke(t, (Object[]) null);
				if (value != null) {
					if (value instanceof String) {
						cell.setCellValue((String) value);
					} else if (value instanceof Long) {
						cell.setCellValue((Long) value);
					} else if (value instanceof Integer) {
						cell.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cell.setCellValue((Double) value);
					} else if (value instanceof Float) {
						cell.setCellValue((Float) value);
					} else {
						cell.setCellValue(value.toString());
					}
				}
				columnCount++;
			}
			dataRowCount++;
		}

	}

	@Override
	public void close() throws IOException {
		workbook.write(fos);
		fos.flush();
		if (fos != null) {
			fos.close();
		}
		if (workbook != null) {
			workbook.close();
		}
	}

	public long getSize() {
		return dataRowCount;
	}

	public void finalize() throws IOException {
		if (fos != null) {
			fos.close();
		}
		if (workbook != null) {
			workbook.close();
		}
		System.out.println("Excel Export writer is destroyed by the Garbage Collector");
	}

}