package UtilsContracts;

/**
 * 
 * This service provides XML serializers of a generic type @IXmlSerializer.
 * In order to use XML serialization, the user should inject this service @IXmlSerializerProvider
 * and get a serializer of a desirable type.
 * 
 * If the user works with @IXmlSerializer with a type which is an interface then prior to using
 * the serializer getter, the user should first add the serializer (in order to bind the interface type
 * with the implementation).
 * The best practice is for a user to add a serializer in the Activator of the plug-in where the
 * type (for the serialization) is declared.
 * @author Shimon Azulay
 * @since 2016-03-29 
 */
public interface IXmlSerializerProvider {
	
	/**
	 * returns an instance of @IXmlSerializer<Type> according to a requied type
	 * @param typeInterface the type of object for @IXmlSerializer
	 * 
	 * @return an instance of @IXmlSerializer<typeInterface>
	 */
	<IClass> IXmlSerializer<IClass> getSerializer(Class<IClass> typeInterface);
	
	/**
	 * adds a serializer @IXmlSerializer to an internal list.
	 * If the user works with @IXmlSerializer with a type which is an interface then prior to using
	 * the serializer getter, the user should first add the serializer (in order to bind the interface type
	 * with the implementation).
	 * The best practice is for a user to add a serializer in the Activator of the plug-in where the
	 * type (for the serialization) is declared.
	 * 
	 * @param typeInterface the type the interface for @IXmlSerializer
	 * @param typeImplementation the type of object that implements the interface typeInterface
	 */
	<IClass, Impl extends IClass> void addSerializer(Class<IClass> typeInterface, Class<Impl> typeImplementation);
}
