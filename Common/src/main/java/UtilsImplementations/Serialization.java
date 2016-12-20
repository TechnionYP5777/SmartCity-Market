package UtilsImplementations;

import com.google.gson.Gson;

/**
 * Serialization - This class implement the ISerialization contract interface and use Gson for the 
 * serialization service.
 * 
 * @author Shimon Azulay
 * @since 2016-12-19
 */

import UtilsContracts.ISerialization;

public class Serialization implements ISerialization {
	
	private Gson gson = new Gson();

	@Override
	public String serialize(Object toSerialize) {
		return gson.toJson(toSerialize);
	}

	@Override
	public <T> T deserialize(String toDeserialize, Class<T> classType) {
		return gson.fromJson(toDeserialize, classType);
	}

}
