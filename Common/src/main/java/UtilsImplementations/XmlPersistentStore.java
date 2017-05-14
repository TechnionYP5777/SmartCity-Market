package UtilsImplementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import UtilsContracts.IFilePathLocator;
import UtilsContracts.IPersistentStore;
import UtilsContracts.IXmlSerializerProvider;


/** IPersistentStore - An impl that represents persistence.
 * @author Shimon Azulay
 * @since 2016-03-28 */

public class XmlPersistentStore implements IPersistentStore {
	
	static Logger log = Logger.getLogger(XmlPersistentStore.class.getName());
	
	private IFilePathLocator mAppDirectories;
	private IXmlSerializerProvider mXmlSerializerProvider;

	@Inject
	public XmlPersistentStore(IFilePathLocator appDirectories, IXmlSerializerProvider xmlSerializerProvider) {
		mAppDirectories = appDirectories;
		mXmlSerializerProvider = xmlSerializerProvider;
		
		mXmlSerializerProvider.addSerializer(Object.class, Object.class);
	}

	@Override
	public void storeObject(Object requester, Object objectToStore) throws Exception {
		mXmlSerializerProvider.getSerializer(Object.class).serializeToFile(objectToStore,
				getXmlPath(requester, objectToStore.getClass()));
	}

	@Override
	public <TObject> TObject restoreObject(Object requester, Class<TObject> restoredType) throws Exception {
	
		mXmlSerializerProvider.addSerializer(restoredType, restoredType);
		String path = getXmlPath(requester, restoredType);
		if (!(new File(path).exists())) {
			log.info("Configuration file doesn't exist: " + path);
			log.debug("Stack trace : \n" + Arrays.toString(Thread.currentThread().getStackTrace()));
			throw new FileNotFoundException();
		}
		
		return mXmlSerializerProvider.getSerializer(restoredType).deserializeFromFile(path);
	}
	
	private String getXmlPath(Object requester, Class<?> objectType) {
		PersistenceData persistenceData = getPersistenceDataAnnotation(objectType);
		if (persistenceData != null)
			return mAppDirectories.getFilePath(requester.getClass(), persistenceData.relativePath());
		return mAppDirectories.getFilePath(getClass(),
				(new File(requester.getClass().getPackage().getName(), objectType.getCanonicalName() + ".xml"))
						.getPath());
	}
	
	private PersistenceData getPersistenceDataAnnotation(Class<?> objectType) {
		return objectType.getAnnotation(PersistenceData.class);
	}

}

