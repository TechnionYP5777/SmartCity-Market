package UtilsImplementations;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;


/**
 * Serialization - This class gives serialization service
 * 
 * @author Shimon Azulay
 * @since 2016-12-19
 */

public class Serialization {
	
	public static String serialize(Object toSerialize) {
		return new Gson().toJson(toSerialize);
	}


	public static <T> T deserialize(String toDeserialize, Class<T> classType) {
		return new Gson().fromJson(toDeserialize, classType);
	}
	
	public static HashSet<Ingredient> deserializeIngredientHashSet(String hashsetToDeserialize){
		Gson gson = new Gson();

		Type hashsetType = new TypeToken<HashSet<Ingredient>>(){}.getType();

		return gson.fromJson(hashsetToDeserialize, hashsetType);
	}
	
	public static HashSet<Manufacturer> deserializeManufacturersHashSet(String hashsetToDeserialize){
		Gson gson = new Gson();

		Type hashsetType = new TypeToken<HashSet<Manufacturer>>(){}.getType();

		return gson.fromJson(hashsetToDeserialize, hashsetType);
	}
	
	public static HashMap<String,Boolean> deserializeWorkersHashMap(String hashmapToDeserialize){
		Gson gson = new Gson();

		Type hashsetType = new TypeToken<HashMap<String,Boolean>>(){}.getType();

		return gson.fromJson(hashmapToDeserialize, hashsetType);
	}

}
