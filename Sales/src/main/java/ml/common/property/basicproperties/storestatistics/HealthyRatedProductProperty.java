package ml.common.property.basicproperties.storestatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 24, 2017
 * 
 * this class represents healthy rated product property
 */
public class HealthyRatedProductProperty extends ABasicProperty {

	private static List<String> healthyRatedIngredientsNames = 
			new ArrayList<>
				(Arrays.asList(
						String.valueOf("rice"),
						String.valueOf("corn"),
						String.valueOf("beans")));

	private static int intersectionSizeToBeRatedHealthy = 1;
	
	private IProduct product;
	
	public HealthyRatedProductProperty(IProduct product) {
		this.product = product;
	}
	
	public HealthyRatedProductProperty(IProduct product, ADeductionRule rule) {
		super(rule);
		this.product = product;
	}
	
	public static boolean isProductRatedHealthy(IProduct p){
		Set<String> intersection = new HashSet<String>(healthyRatedIngredientsNames);

		intersection.retainAll(p.getIngredients().stream().map(i -> i.getName()).collect(Collectors.toSet()));
		return intersection.size() >= intersectionSizeToBeRatedHealthy;
	}

	public IProduct getProduct(){
		return product;
	}
	
	@Override
	public int hashCode() {
		return 31 * super.hashCode() + ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		HealthyRatedProductProperty other = (HealthyRatedProductProperty) o;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The product: " + product.getName() + " (bracode: " + product.getBarcode() + ")" +
				" is healthy";
	}
}
