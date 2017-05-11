package UtilsImplementations;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import UtilsContracts.IXmlSerializer;
import UtilsContracts.IXmlSerializerProvider;

/**
 * XmlSerializerProvider - This class is used to serialize xml.
 * 
 * @author Shimon Azulay
 * @since 2016-03-29
 */

@Singleton
public class XmlSerializerProvider implements IXmlSerializerProvider {

	private Map<String, IXmlSerializer<?>> mSerializers = new HashMap<>();

	@Override
	public <Type> IXmlSerializer<Type> getSerializer(Class<Type> serializedType) {
		String serializedTypeName = serializedType.getName();
		if (serializedType.isInterface() && !mSerializers.containsKey(serializedTypeName)) {
			throw new RuntimeException("Interface is not registered: " + serializedTypeName);
		}
		if (!serializedType.isInterface() && !mSerializers.containsKey(serializedTypeName)) {
			mSerializers.put(serializedTypeName, new XmlSerializer<Type>(serializedType));
		}
		@SuppressWarnings("unchecked")
		IXmlSerializer<Type> serializer = (IXmlSerializer<Type>) mSerializers.get(serializedTypeName);
		return serializer;
	}

	@Override
	public <IClass, Impl extends IClass> void addSerializer(Class<IClass> typeInterface,
			Class<Impl> typeImplementation) {
		String typeInterfaceName = typeInterface.getName();
		if (typeInterface.isInterface() && !mSerializers.containsKey(typeInterfaceName)) {
			mSerializers.put(typeInterfaceName, new XmlSerializer<IClass>(typeImplementation));
		}
	}

}
