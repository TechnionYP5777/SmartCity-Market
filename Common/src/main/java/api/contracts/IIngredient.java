package api.contracts;

public interface IIngredient {

	String getName();
	
	boolean isEqualTo(IIngredient other);
}
