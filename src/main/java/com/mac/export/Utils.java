package com.mac.export;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mac.export.annotation.HeaderName;
import com.mac.export.naming.HeaderNamingStrategy;
import com.mac.export.naming.impl.BaseHeaderNamingStrategy;
import com.mac.export.pojo.ColumnName;

public final class Utils {

	// retrieve field names from a POJO class
	public static List<String> getFieldNamesForClass(Class<?> clazz) {
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	// retrieve field names and headerName from a POJO class
	public static List<ColumnName> getColumnNamesForClass(Class<?> clazz,
			Class<? extends BaseHeaderNamingStrategy> headerNamingStrategyClazz)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		List<ColumnName> columnNames = new ArrayList<ColumnName>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			ColumnName columnName = new ColumnName();
			columnName.setFieldName(field.getName());
			HeaderNamingStrategy headerNamingStrategy = headerNamingStrategyClazz.getDeclaredConstructor()
					.newInstance();
			String headerName = headerNamingStrategy.translate(columnName.getFieldName());
			if (field.isAnnotationPresent(HeaderName.class)) {
				HeaderName name = field.getAnnotation(HeaderName.class);
				if (name != null && StringUtils.isNotBlank(name.value())) {
					headerName = name.value();
				}
			}
			columnName.setHeaderName(headerName);
			columnNames.add(columnName);
		}
		return columnNames;
	}

	// capitalize the first letter of the field name for retriving value of the
	// field later
	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

}
