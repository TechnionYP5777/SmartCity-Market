package UtilsImplementations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * InjectionFactory - Use this class to get instance of binded implementation to
 * interface
 * 
 * @author Shimon Azulay
 * @since 2017-01-06
 *
 */
public class InjectionFactory {

	static Injector injector;

	public static void createInjector(Module... diConfigurators) {
		injector = Guice.createInjector(diConfigurators);
	}

	public static <T> T getInstance(Class<T> classType) {	
		return injector == null ? null : injector.getInstance(classType);		
	}
}
