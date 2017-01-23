package UtilsImplementations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * InjectionFactory - Use this class to get instance of binded implementation to
 * interface
 * 
 * @author Shimon Azulay
 *
 */
public class InjectionFactory {

	static Injector injector;

	public static void createInjector(AbstractModule diConfigurator) {
		injector = Guice.createInjector(diConfigurator);
	}

	public static <T> T getInstance(Class<T> classType) {
		return injector == null ? null : injector.getInstance(classType);
	}
}
