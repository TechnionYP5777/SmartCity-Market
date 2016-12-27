package UtilsImplementations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectionFactory {

	static public <T> T getInstance(Class<T> classType, AbstractModule diConfigurator) {
		Injector injector = Guice.createInjector(diConfigurator);
		return injector.getInstance(classType);
	}

}
