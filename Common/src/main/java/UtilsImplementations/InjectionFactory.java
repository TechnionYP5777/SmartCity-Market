package UtilsImplementations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public class InjectionFactory {

	public static <T> T getInstance(Class<T> classType, AbstractModule diConfigurator) {
		return Guice.createInjector(diConfigurator).getInstance(classType);
	}

}
