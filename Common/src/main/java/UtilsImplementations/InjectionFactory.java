package UtilsImplementations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * InjectionFactory - Use this class to get instance of binded implementation to interface
 * @author Shimon Azulay
 *
 */
public class InjectionFactory {

	public static <T> T getInstance(Class<T> classType, AbstractModule diConfigurator) {
		return Guice.createInjector(diConfigurator).getInstance(classType);
	}

}
