package ml.deducer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;
import ml.deducer.deductionrules.IfAboutToExpireSoon_ThenMustGetRidOfPackage_Rule;

public class Deducer {

	
	
	private static List<ADeductionRule> createDeductionRules(){
		List<ADeductionRule> deductionRules = new ArrayList<>();
		
		deductionRules.add(new IfAboutToExpireSoon_ThenMustGetRidOfPackage_Rule());
		
		return deductionRules;
	}
	
	public static Set<AProperty> deduceProperties(SalesPreferences salesPreferences, Set<ABasicProperty> properties){
		
		List<ADeductionRule> deductionRules = createDeductionRules();
		
		Set<AProperty> result = new HashSet<>();
		
		int oldSize = result.size();
		result.addAll(properties);
		
		
		while (oldSize != result.size()){
			oldSize = result.size();
			
			for (ADeductionRule rule : deductionRules) {
				result.addAll(rule.deduceProperties(salesPreferences, result));
			}
			
		}
		
		return result;
		
	}
}
