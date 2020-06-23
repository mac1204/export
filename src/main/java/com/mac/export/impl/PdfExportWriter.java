package com.mac.export.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mac.export.ExportWriter;
import com.mac.export.Utils;
import com.mac.export.annotation.HeaderNaming;
import com.mac.export.pojo.ColumnName;

public class PdfExportWriter<T> extends BaseExportWriter<T> implements ExportWriter<T> {

	private Document document = null;
	private List<String> fieldNames = null;
	private PdfPTable table = null;

	public PdfExportWriter(String fileName) throws FileNotFoundException, DocumentException {
		super();
		if (document == null) {
			this.fileName = directory.concat("/").concat(fileName);
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(this.fileName));
			document.open();
		}
	}

	public PdfExportWriter(String fileName, boolean requiredHeader) throws FileNotFoundException, DocumentException {
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
			table = new PdfPTable(headers.size());
			if (requiredHeader) {
				for (String header : headers) {
					PdfPCell headerCell = new PdfPCell();
					headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					headerCell.setBorderWidth(2);
					headerCell.setPhrase(new Phrase(header));
					table.addCell(headerCell);
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
					if (value != null) {
						if (value instanceof String) {
							table.addCell((String) value);
						} else if (value instanceof Long) {
							table.addCell(((Long) value).toString());
						} else if (value instanceof Integer) {
							table.addCell(((Integer) value).toString());
						} else if (value instanceof Double) {
							table.addCell(((Double) value).toString());
						} else if (value instanceof Float) {
							table.addCell(((Float) value).toString());
						} else {
							table.addCell(value.toString());
						}
					} else {
						table.addCell("");
					}
				}
				dataRowCount++;
			}

		}

	}

	@Override
	public void close() throws DocumentException {
		document.add(table);
		document.close();

	}

	@Override
	public void finalize() throws IOException {
		if (document != null) {
			document.close();
		}

		System.out.println("PDF Export writer is destroyed by the Garbage Collector");
	}

}
