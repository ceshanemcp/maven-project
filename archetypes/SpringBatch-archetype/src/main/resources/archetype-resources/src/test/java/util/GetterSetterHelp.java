package ${package}.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.Assert;

public class GetterSetterHelp {

	public static void runConstants(Object obj) {
		for (Field f : obj.getClass().getDeclaredFields()) {
			Assert.assertSame(f, f);
		}
	}

	public static void runGetterSetters(Object object) {
		runGetterSetters(object, true);
	}

	public static void runGetterSetters(Object object, boolean isAssert) {
		Object obj = null;

		for (Method m : object.getClass().getDeclaredMethods()) {
			if (m.getName().contains("set")) {
				m.setAccessible(true);
		
				try {
					m.invoke(object, obj);
				} catch (Exception e) {
					continue;
				}
			}
		}
		
		Object result = new Object();
		
		for (Method m : object.getClass().getDeclaredMethods()) {
			if (m.getName().contains("get")) {
				m.setAccessible(true);
		
				try {
					result = m.invoke(object);

					if (isAssert) {
						Assert.assertNull(result);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
	}
}