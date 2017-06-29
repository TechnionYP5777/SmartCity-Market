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
	
	public static boolean isProductRatedHealthy(IProduct product){
		Set<String> productIngredientsNames = 
				product.getIngredients()
				.stream()
				.map(i -> i.getName())
				.collect(Collectors.toSet());

		Set<String> intersection = new HashSet<String>(healthyRatedIngredientsNames);
		intersection.retainAll(productIngredientsNames);
		return intersection.size() >= intersectionSizeToBeRatedHealthy;
	}

	public IProduct getProduct(){
		return product;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HealthyRatedProductProperty other = (HealthyRatedProductProperty) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
}
