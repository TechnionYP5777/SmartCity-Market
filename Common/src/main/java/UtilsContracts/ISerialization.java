package UtilsContracts;

/**
 * ISerialization - This interface represents contract of the serialization class that will implement it.
 * 
 * @author Shimon Azulay
 * @since 2016-12-19
 */

public interface ISerialization {

	String serialize(Object toSerialize);

	<T> T deserialize(String toDeserialize, Class<T> classType);
	
	
	
}
