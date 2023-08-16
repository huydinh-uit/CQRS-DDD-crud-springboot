/**
* File Name  : ExtraInfoUtil.java
* Package    : vn.com.vng.mcrusprofile.util
* Author     : sonnd
* Created    : Aug 10, 2018 3:41:38 PM

* This source code is part of the mcrusprofile, and is copyrighted by A4B. 
* All rights reserved.  No part of this work may be reproduced, stored in a retrieval system,
* adopted or transmitted in any form or by any means,electronic, mechanical, photographic,
* graphic, optic recording or otherwise,translated in any language or computer language,
* without the prior written permission of A4B 

Copyright © 2017 - 2018 by A4B
-------------------------------------------------------------------
* Version Control:
*       $LastChangedRevision$
*       $LastChangedBy$
*       $LastChangedDate$
* 
-------------------------------------------------------------------
*/
package vn.com.vng.mcrusprofile.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.PropertyUtils;
import org.postgresql.util.PGobject;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import vn.com.vng.mcrusprofile.annotation.ControllingField;
import vn.com.vng.mcrusprofile.annotation.UnmodifiableField;
import vn.com.vng.mcrusprofile.constant.Constants;
import vn.com.vng.mcrusprofile.constant.DataType;
import vn.com.vng.mcrusprofile.dto.PropertyDto;

/**
 * @author sonnd
 *
 */
public class ExtraInfoUtil {

	public static String detectObjectDataType(Object val) {
		if (val == null) {
			return null;
		}

		if (val instanceof Boolean) {
			return DataType.BOOLEAN;
		} else if (val instanceof Date) {
			return DataType.DATE;
		} else if (val instanceof Number) {
			return DataType.NUMERIC;
		} else if (val instanceof String) {
			return DataType.STRING;
		} else if(val instanceof Collection){
			return DataType.JSON_ARRAY;
		} else {
			return DataType.JSON_OBJECT;
		}
	}

	/*public static Object getExtraInfoValue(CstbExtraInfo extraInfo) {
		Object val = null;
		switch (extraInfo.getDataType()) {
		case DataType.STRING:
			val = extraInfo.getValueString();
			break;
		case DataType.NUMERIC:
			val = extraInfo.getValueNumeric();
			break;
		case DataType.BOOLEAN:
			val = extraInfo.getValueNumeric() != null ? extraInfo.getValueNumeric().intValue() == 1 : null;
			break;
		case DataType.DATE:
			val = extraInfo.getValueDate();
			break;
		case DataType.JSON_ARRAY:
			val = extraInfo.getValueJson();
			break;
		case DataType.JSON_OBJECT:
			val = extraInfo.getValueJson();
			break;
		default:
			break;
		}

		return val;
	}

	public static Object getConnectionAttributeValue(CstbConnectionAttribute attribute) {
		Object val = null;
		switch (attribute.getDataType()) {
		case DataType.STRING:
			val = attribute.getValueString();
			break;
		case DataType.NUMERIC:
			val = attribute.getValueNumeric();
			break;
		case DataType.BOOLEAN:
			val = attribute.getValueNumeric() != null ? attribute.getValueNumeric().intValue() == 1 : null;
			break;
		case DataType.DATE:
			val = attribute.getValueDate();
			break;
		case DataType.JSON_ARRAY:
			val = attribute.getValueJson();
			break;
		case DataType.JSON_OBJECT:
			val = attribute.getValueJson();
			break;
		default:
			break;
		}

		return val;
	}

	public static void setExtraInfoValue(CstbExtraInfo extraInfo, Object value, String dataType) {
		extraInfo.setDataType(dataType);

		switch (extraInfo.getDataType()) {
		case DataType.STRING:
			String strValue = (String) value;
			extraInfo.setValueString(strValue);
			break;
		case DataType.NUMERIC:
			Double numericVal = value != null ? Double.parseDouble(String.valueOf(value)) : null;
			extraInfo.setValueNumeric(numericVal);
			break;
		case DataType.BOOLEAN:
			Double booleanVal = value != null ? (Boolean.TRUE.equals(value) ? 1d : 0d) : null;
			extraInfo.setValueNumeric(booleanVal);
			break;
		case DataType.DATE:
			if (value instanceof Date) {
				extraInfo.setValueDate((Date) value);
			} else if (value instanceof Number) {
				Double interval = Double.parseDouble(String.valueOf(value));
				extraInfo.setValueDate(new Date(interval.longValue()));
			} else {
				extraInfo.setValueDate(null);
			}
			break;
		case DataType.JSON_ARRAY:
		case DataType.JSON_OBJECT:
			String jsonValue = null;
			if (value instanceof String) {
				jsonValue = (String) value;
			} else {
				try {
					jsonValue = JsonUtils.getObjectMapper().writeValueAsString(value);
				} catch (JsonProcessingException e) {
				}
			}
			extraInfo.setValueJson(jsonValue);
			break;
		default:
			break;
		}
	}

	public static void setConnectionAttributeValue(CstbConnectionAttribute extraInfo, Object value, String dataType) {
		extraInfo.setDataType(dataType);

		switch (extraInfo.getDataType()) {
		case DataType.STRING:
			String strValue = (String) value;
			extraInfo.setValueString(strValue);
			break;
		case DataType.NUMERIC:
			Double numericVal = value != null ? Double.parseDouble(String.valueOf(value)) : null;
			extraInfo.setValueNumeric(numericVal);
			break;
		case DataType.BOOLEAN:
			Double booleanVal = value != null ? (Boolean.TRUE.equals(value) ? 1d : 0d) : null;
			extraInfo.setValueNumeric(booleanVal);
			break;
		case DataType.DATE:
			if (value instanceof Date) {
				extraInfo.setValueDate((Date) value);
			} else if (value instanceof Number) {
				Double interval = Double.parseDouble(String.valueOf(value));
				extraInfo.setValueDate(new Date(interval.longValue()));
			} else {
				extraInfo.setValueDate(null);
			}
			break;
		case DataType.JSON_ARRAY:
		case DataType.JSON_OBJECT:
			String jsonValue = null;
			if (value instanceof String) {
				jsonValue = (String) value;
			} else {
				try {
					jsonValue = JsonUtils.getObjectMapper().writeValueAsString(value);
				} catch (JsonProcessingException e) {
				}
			}
			extraInfo.setValueJson(jsonValue);
			break;
		default:
			break;
		}
	}*/

	public static boolean validateValue(String validationExpression, Object value, Object payload) {
		try {
			ExpressionParser expressionParser = new SpelExpressionParser();
			Expression expression = expressionParser.parseExpression(validationExpression);
			EvaluationContext context = new StandardEvaluationContext();
			context.setVariable("value", value);
			context.setVariable("payload", payload);
			Object result = expression.getValue(context);
			return Boolean.TRUE.equals(result);
		} catch (ParseException ex) {
			return false;
		}
	}

	/*public static Map<String, Object> extractExtraInfoValues(List<CstbExtraInfo> extraInfos) {
		Map<String, Object> map = new HashMap<>();

		if (extraInfos == null || extraInfos.isEmpty()) {
			return map;
		}

		for (CstbExtraInfo info : extraInfos) {
			Object value = getExtraInfoValue(info);
			map.put(info.getKeyName(), value);
		}

		return map;
	}*/

	public static Map<String, PropertyDto> extractEntityProperties(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Map<String, PropertyDto> properties = new HashMap<>();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if(Modifier.isFinal(modifiers)) {
				continue;
			}
			ControllingField controllingFieldAnno = AnnotationUtils.getAnnotation(field, ControllingField.class);
			if(controllingFieldAnno != null) {
				continue;
			}
			NotNull notNullAnno = AnnotationUtils.getAnnotation(field, NotNull.class);
			UnmodifiableField unmodifiableFieldAnno = AnnotationUtils.getAnnotation(field, UnmodifiableField.class);
			Boolean required = notNullAnno != null;
			boolean pivotable = isPivotable(clazz.getName(), field.getName());
			boolean allowAgg = isMeasurable(field.getType());
			boolean editable = (unmodifiableFieldAnno == null);
			boolean groupable = pivotable;
			boolean securedField = isSecuredField(clazz.getName(), field.getName());
			PropertyDto prop = new PropertyDto(field.getName(), getDataType(field), required, pivotable, allowAgg, editable, groupable, securedField, null);
			properties.put(field.getName(), prop);
		}

		return properties;
	}
	
	public static Map<String, PropertyDto> extractEntityPropertiesPivot(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Map<String, PropertyDto> properties = new HashMap<>();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if(Modifier.isFinal(modifiers)) {
				continue;
			}
			String dataType = getDataType(field);
			if(DataType.JSON_ARRAY.equals(dataType) || DataType.JSON_OBJECT.equals(dataType)) {
				continue;
			}
			
			NotNull notNullAnno = AnnotationUtils.getAnnotation(field, NotNull.class);
			UnmodifiableField unmodifiableFieldAnno = AnnotationUtils.getAnnotation(field, UnmodifiableField.class);
			Boolean required = notNullAnno != null;
			boolean pivotable = isPivotable(clazz.getName(), field.getName());
			boolean allowAgg = isMeasurable(field.getType());
			boolean editable = (unmodifiableFieldAnno == null);
			boolean groupable = pivotable;
			boolean securedField = isSecuredField(clazz.getName(), field.getName());
			PropertyDto prop = new PropertyDto(/*columnName*/field.getName(), dataType, required, pivotable, allowAgg, editable, groupable, securedField, null);
			properties.put(field.getName(), prop);
		}

		return properties;
	}
	
	public static boolean isPivotable(String entityClassName, String fieldName) {
		return !isSecuredField(entityClassName, fieldName);
	}
	
	public static boolean isSecuredField(String entityClassName, String fieldName) {
		/*FieldSecurityMappingProvider fieldSecurityMappingProvider = ApplicationContextStore.getApplicationContext().getBean(FieldSecurityMappingProvider.class);
		Map<String, EntitySecInfo> mapping = fieldSecurityMappingProvider.getMapping();
		if(mapping == null) {
			return true;
		}
		
		EntitySecInfo secInfo = mapping.get(entityClassName);
		if(secInfo == null) {
			return false;
		}
		
		for(FieldSecInfo f : secInfo.getFields()) {
			if(f.getName().equals(fieldName)) {
				return true;
			}
		}*/
		
		return false;
	}
	
	public static boolean isMeasurable(Class fieldType) {
		return Number.class.isAssignableFrom(fieldType);
	}

	private static String getDataType(Field field) {
		if(String.class.isAssignableFrom(field.getType())) {
			return DataType.STRING;
		}
		else if(Number.class.isAssignableFrom(field.getType())) {
			return DataType.NUMERIC;
		}
		else if(Date.class.isAssignableFrom(field.getType())) {
			return DataType.DATE;
		}
		else if(Boolean.class.isAssignableFrom(field.getType())) {
			return DataType.BOOLEAN;
		}
		else if(Collection.class.isAssignableFrom(field.getType())) {
			return DataType.JSON_ARRAY;
		}
		else {
			return DataType.JSON_OBJECT;
		}
	}
	
	public static String getClassName(String dataType) {
		if(DataType.STRING.equals(dataType)) {
			return String.class.getName();
		}
		else if(DataType.NUMERIC.equals(dataType)) {
			return Number.class.getName();
		}
		else if(DataType.DATE.equals(dataType)) {
			return Date.class.getName();
		}
		else if(DataType.BOOLEAN.equals(dataType)) {
			return Boolean.class.getName();
		}
		else if(DataType.JSON_ARRAY.equals(dataType)) {
			return ArrayList.class.getName();
		}
		else if(DataType.JSON_OBJECT.equals(dataType)) {
			return HashMap.class.getName();
		}
		
		return null;
	}
	
	public static Class getDataClass(String dataType) {
		if(DataType.STRING.equals(dataType)) {
			return String.class;
		}
		else if(DataType.NUMERIC.equals(dataType)) {
			return Number.class;
		}
		else if(DataType.DATE.equals(dataType)) {
			return Date.class;
		}
		else if(DataType.BOOLEAN.equals(dataType)) {
			return Boolean.class;
		}
		else if(DataType.JSON_ARRAY.equals(dataType)) {
			return ArrayList.class;
		}
		else if(DataType.JSON_OBJECT.equals(dataType)) {
			return HashMap.class;
		}
		
		return null;
	}
	
	public static String getValueColumnName(String dataType) {
		if(DataType.STRING.equals(dataType)) {
			return "value_string";
		}
		else if(DataType.NUMERIC.equals(dataType)) {
			return "value_numeric";
		}
		else if(DataType.DATE.equals(dataType)) {
			return "value_date";
		}
		else if(DataType.BOOLEAN.equals(dataType)) {
			return "value_numeric";
		}
		else if(DataType.JSON_ARRAY.equals(dataType)) {
			return "value_json";
		}
		else if(DataType.JSON_OBJECT.equals(dataType)) {
			return "value_json";
		}
		
		return null;
	}
	
	public static <T> T mapToDto(Map<String, Object> map, Class<T> clazz) {
		try {
			T obj = clazz.newInstance();
			Map<String, Object> extraInfo = new HashMap<>();
			for (Entry<String, Object> entry : map.entrySet()) {
				String propName = entry.getKey();
				Object value = entry.getValue();
				
				
				if (propName.startsWith(Constants.EXTRA_INFO_FILTER_PREFIX)) {
					extraInfo.put(propName.substring(Constants.EXTRA_INFO_FILTER_PREFIX.length()), value);
				} else {
					setPropertyValue(obj, propName, value);
				}
			}
			if(PropertyUtils.isWriteable(obj, "extraInfo")) {
				PropertyUtils.setProperty(obj, "extraInfo", extraInfo);
			}
			return obj;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to convert data", ex);
		}
	}
	
	private static void setPropertyValue(Object obj, String propName, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(PropertyUtils.isWriteable(obj, propName)) {
			Object val = null;
			if(value instanceof Timestamp) {
				val = new Date(((Timestamp) value).getTime());
			}
			else if(value instanceof BigDecimal) {
				Class clazz = PropertyUtils.getPropertyType(obj, propName);
				if(Long.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).longValue();
				}
				else if(Integer.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).intValue();
				}
				else if(Short.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).shortValue();
				}
				else {
					val = value;
				}
			}else if (value instanceof PGobject) {
				String jsonProps = ((PGobject) value).getValue();
				Map<String, Object> props = new HashMap<>();
				props.putAll(JsonUtils.getGson().fromJson(jsonProps, Map.class));
				val = props;
			}
			else {
				val = value;
			}
			PropertyUtils.setProperty(obj, propName, val);
		}
	}
	
	public static <T> Map<String, Object> dtoToMap(T obj) {
		try {
			Map<String, Object> map = new HashMap<>();
			PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
			for(PropertyDescriptor pd : propertyDescriptors) {
				String propName = pd.getName();
				Object val = PropertyUtils.getProperty(obj, propName);
				if (propName.equals(Constants.EXTRA_INFO_PROP)) {
					if(val instanceof Map) {
						Map<String, Object> extraInfo = (Map<String, Object>) val;
						for(Entry<String, Object> entry : extraInfo.entrySet()) {
							map.put(Constants.EXTRA_INFO_FILTER_PREFIX + entry.getKey(), entry.getValue());
						}
					}
				} else {
					map.put(propName, val);
				}
			}
			return map;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to convert data", ex);
		}
	}
}
