package com.mac.export.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mac.export.naming.impl.BaseHeaderNamingStrategy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderNaming {

	public Class<? extends BaseHeaderNamingStrategy> value() default BaseHeaderNamingStrategy.class;

}
