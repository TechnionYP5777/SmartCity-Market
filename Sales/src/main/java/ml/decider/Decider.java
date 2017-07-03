package ml.decider;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import api.contracts.ISale;
import api.preferences.SalesPreferences;
import api.types.sales.ProductSale;
import ml.common.property.saleproperty.ASaleProperty;

/**
 * This class decide one sale from the collaction that deduced
 * 
 * @author noam
 * 
 */
public class Decider {

	public static ASaleProperty decideBestSale(SalesPreferences p, Set<ASaleProperty> ps){
		
		int n = new Random().nextInt(ps.size()), i = 0;
		for (ASaleProperty aSaleProperty : ps) {
			if (i == n)
				return aSaleProperty;
			
			++i;
		}
		
		
		return null;
	}
	
	public static ASaleProperty decideSaleSimilar(SalesPreferences salesPreferences, Set<ASaleProperty> ps
			,ISale saleToCompare){
		
		List<ASaleProperty> sales = ps.stream().filter(p -> p.getOffer().getProduct().equals(saleToCompare.getProduct()))
			.sorted((ASaleProperty p1, ASaleProperty p2) -> {
				ProductSale p1Sale = p1.getOffer(), p2Sale = p2.getOffer();
				int p1AmountDiff = Math.abs(saleToCompare.getTotalAmount() - p1Sale.getTotalAmount()),
						p2AmountDiff = Math.abs(saleToCompare.getTotalAmount() - p2Sale.getTotalAmount());
				if (p1AmountDiff != p2AmountDiff)
					return p1AmountDiff - p2AmountDiff;
				double p1PriceDiff = Math.abs(saleToCompare.getTotalPrice() - p1Sale.getTotalPrice()),
						p2PriceDiff = Math.abs(saleToCompare.getTotalPrice() - p2Sale.getTotalPrice());
				return p1PriceDiff < p2PriceDiff ? -1 : p2PriceDiff < p1PriceDiff ? 1 : 0;
			}).collect(Collectors.toList());
		
		return sales.isEmpty() ? null : sales.get(0);

	}
}
