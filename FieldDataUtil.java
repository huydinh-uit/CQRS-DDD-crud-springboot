package vn.com.vng.mcrusprofile.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;

import vn.com.vng.core.ag.ag_grid.builder.AgGridConstants;
import vn.com.vng.mcrusprofile.constant.DataType;

/**
 * @author sonnd
 *
 */
public class FieldDataUtil {

	public static final Logger LOG = LoggerFactory.getLogger(FieldDataUtil.class);
	
	public static String detectDataType(Object val) {
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
		} else if (val instanceof Collection) {
			return DataType.JSON_ARRAY;
		} else {
			return DataType.JSON_OBJECT;
		}
	}
	
	public static <T> T mapToDto(Map<String, Object> map, Class<T> clazz) {
		try {
			T obj = clazz.newInstance();
			Map<String, Object> properties = new HashMap<>();
			for (Entry<String, Object> entry : map.entrySet()) {
				String propName = entry.getKey();
				Object value = entry.getValue();

				if (propName.equals(AgGridConstants.PROPERTIES_COLUMN_NAME)) {
					if (value instanceof PGobject) {
						String jsonProperties = ((PGobject) value).getValue();
						properties.putAll(JsonUtils.getGson().fromJson(jsonProperties, Map.class));
					}
				} else if (propName.startsWith(AgGridConstants.PROPERTIES_PREFIX)) {
					properties.put(propName.substring(AgGridConstants.PROPERTIES_PREFIX.length()), value);
				} else {
					setPropertyValue(obj, propName, value);
				}
			}
			if (PropertyUtils.isWriteable(obj, AgGridConstants.PROPERTIES_COLUMN_NAME)) {
				PropertyUtils.setProperty(obj, AgGridConstants.PROPERTIES_COLUMN_NAME, properties);
			}
			return obj;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to convert data", ex);
		}
	}

	private static void setPropertyValue(Object obj, String propName, Object value)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (PropertyUtils.isWriteable(obj, propName)) {
			Object val = null;
			if (value instanceof Timestamp) {
				val = new Date(((Timestamp) value).getTime());
			} else if (value instanceof BigDecimal) {
				Class clazz = PropertyUtils.getPropertyType(obj, propName);
				if (Long.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).longValue();
				} else if (Integer.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).intValue();
				} else if (Short.class.isAssignableFrom(clazz)) {
					val = ((BigDecimal) value).shortValue();
				} else {
					val = value;
				}
			} else {
				val = value;
			}
			PropertyUtils.setProperty(obj, propName, val);
		}
	}

	public static <T> Map<String, Object> dtoToMap(T obj) {
		try {
			Map<String, Object> map = new HashMap<>();
			PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
			for (PropertyDescriptor pd : propertyDescriptors) {
				String propName = pd.getName();
				Object val = PropertyUtils.getProperty(obj, propName);
				if (propName.equals(AgGridConstants.PROPERTIES_COLUMN_NAME)) {
					if (val instanceof Map) {
						Map<String, Object> properties = (Map<String, Object>) val;
						for (Entry<String, Object> entry : properties.entrySet()) {
							map.put(AgGridConstants.PROPERTIES_PREFIX + entry.getKey(), entry.getValue());
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

	public static boolean isPivotable(String entityClassName, String fieldName) {
		return !isSecuredField(entityClassName, fieldName);
	}

	public static boolean isSecuredField(String entityClassName, String fieldName) {
		return false;
	}

	public static boolean isMeasurable(Class fieldType) {
		return Number.class.isAssignableFrom(fieldType);
	}
	
	public static List<Map<String, Object>> normalizeData(List<Map<String, Object>> data) {
		if(data == null) {
			return null;
		}
		List<Map<String, Object>> newData = new ArrayList<>(data.size());
		for(Map<String, Object> item : data) {
			Map<String, Object> dt = new HashMap<>();
			for(Entry<String, Object> entry : item.entrySet()) {
				String fieldName = getFieldName(entry.getKey());
				dt.put(fieldName, entry.getValue());
			}
			newData.add(dt);
		}
		
		return newData;
	}
    
    private static String getDBColumnName(String name) {
    	return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }
    
    private static String getFieldName(String columnName) {
    	return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }
}
