#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.base.util;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class ReflectionUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getValueOfFieldAnnotatedWith(Class<? extends Annotation> ann, Object obj,
			Class<T> expectedFieldType) {
		for (Field f : getFields(obj)) {
			f.setAccessible(true);
			if (f.getAnnotation(ann) != null) {
				try {
					return (T) f.get(obj);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getNameOfFieldAnnotatedWith(Class<? extends Annotation> ann, Object obj) {
		Field f = getFieldAnnotatedWith(ann, obj);
		if (f != null) {
			return f.getName();
		} else {
			return null;
		}
	}

	public static Field getFieldAnnotatedWith(Class<? extends Annotation> ann, Object obj) {
		for (Field f : getFields(obj)) {
			if (f.getAnnotation(ann) != null) {
				return f;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<? extends Enum<?>>> getEnumFieldsTypes(Object obj) {
		List<Class<? extends Enum<?>>> enumList = newArrayList();

		for (Field f : getFields(obj)) {
			if (f.getType().isEnum()) {
				enumList.add((Class<? extends Enum<?>>) f.getType());
			}
		}
		return enumList;
	}

	public static void injectIntoField(Object targetObj, String targetField, Object value) {
		for (Field f : getFields(targetObj)) {
			if (f.getName().equals(targetField)) {
				f.setAccessible(true);
				try {
					f.set(targetObj, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Enum<?>> findEnumType(Enum<?> en) {
		// enums with "body" are sub-classes of the formal type
		Class<?> ec = en.getClass();
		if (ec.getSuperclass() != Enum.class) {
			ec = ec.getSuperclass();
		}
		return (Class<? extends Enum<?>>) ec;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Field f, Object target, Class<T> asCls) {
		Object fieldValue = getFieldValue(f, target);
		return (T) fieldValue;
	}

	public static Object getFieldValue(Field f, Object target) {
		f.setAccessible(true);
		try {
			return f.get(target);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param en
	 * @return the array with possible values of Enum<?> for an unknown
	 *         enumeration.
	 */
	public static Enum<?>[] getEnumConstants(Enum<?> en) {
		Class<? extends Enum<?>> cls = findEnumType(en);
		Enum<?>[] values = cls.getEnumConstants();
		return values;
	}

	public static List<Field> getFields(Object obj) {
		List<Field> fields = new ArrayList<Field>();

		Class<?> cls = obj.getClass();
		fields.addAll(asList(cls.getDeclaredFields()));

		Class<?> superClass = cls.getSuperclass();
		while (superClass != null) {
			fields.addAll(asList(superClass.getDeclaredFields()));
			superClass = superClass.getSuperclass();
		}

		return fields;
	}

	public static Map<String, Field> getAllFieldsAsMap(Object obj) {
		Map<String, Field> fieldMap = Maps.newHashMap();

		for (Field f : getFields(obj)) {
			fieldMap.put(f.getName(), f);
		}

		return fieldMap;
	}

	public static void setFieldValue(Object target, Field targetField, Object sourceVal) {
		try {
			targetField.setAccessible(true);
			targetField.set(target, sourceVal);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
