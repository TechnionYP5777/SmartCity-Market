package UtilsImplementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import UtilsContracts.IFilePathLocator;
import UtilsContracts.IPersistentStore;
import UtilsContracts.IXmlSerializer;
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
		super();
		mAppDirectories = appDirectories;
		mXmlSerializerProvider = xmlSerializerProvider;
		
		mXmlSerializerProvider.addSerializer(Object.class, Object.class);
	}

	@Override
	public void storeObject(Object requester, Object objectToStore) throws Exception {
		String path = getXmlPath(requester, objectToStore.getClass());
		IXmlSerializer<Object> xmlSerializer = mXmlSerializerProvider.getSerializer(Object.class);
		xmlSerializer.serializeToFile(objectToStore, path);
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
		
		IXmlSerializer<TObject> xmlSerializer = mXmlSerializerProvider.getSerializer(restoredType);
		TObject restored = xmlSerializer.deserializeFromFile(path);
		return restored;
	}
	
	private String getXmlPath(Object requester, Class<?> objectType) throws IOException {
		PersistenceData persistenceData = getPersistenceDataAnnotation(objectType);
		if (persistenceData != null) {
			return mAppDirectories.getFilePath(requester.getClass(), persistenceData.relativePath());
		}
		else {
			File file = new File(requester.getClass().getPackage().getName(), objectType.getCanonicalName() + ".xml");
			return mAppDirectories.getFilePath(getClass(), file.getPath());
		}
	}
	
	private PersistenceData getPersistenceDataAnnotation(Class<?> objectType) {
		return objectType.getAnnotation(PersistenceData.class);
	}

}

