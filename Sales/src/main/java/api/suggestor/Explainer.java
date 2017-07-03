package api.suggestor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ml.common.property.AProperty;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.saleproperty.ASaleProperty;
import static ml.utils.CollectionFunctions.findInCollection;

/**
 * This class generate a String that's explain the {@link Suggestor} decisions
 * 
 * @author noam
 *
 */
public class Explainer {

	public static String explainSale(ASaleProperty p, Set<AProperty> ps){
		
		StringBuilder explanation = new StringBuilder();
		Set<AProperty> temp = new HashSet<>();
		temp.add(p);
		
		explainSetPropertiesRec(temp, ps, 0, explanation);
		return explanation.toString();
	}
	
	private static void explainSetPropertiesRec(Set<AProperty> propertiesToExplain,  Set<AProperty> allDeducedProperties,
			int indentation, StringBuilder explaination){
		
		for (AProperty property : propertiesToExplain){
			
			if (property instanceof ABasicProperty)
				continue;
			
			Set<AProperty> dependIn = property.getDeductionRule().whatNeedToDeduceProperty(property).stream()
					.map(pl -> findInCollection(allDeducedProperties, pl)).collect(Collectors.toSet());
			
			explaination.append(indentationToTabs(indentation));
			explaination.append("I deduced ").append(property.getDescription())
				.append(" Because i know: ")
				.append(dependIn.stream().map(p -> p.getDescription()).collect(Collectors.joining(", ")))
				.append("\n");
			
			explainSetPropertiesRec(dependIn, allDeducedProperties, indentation + 1, explaination);
		}
	}
	
	public static String indentationToTabs(int numOfTabs){
		StringBuilder tabs = new StringBuilder();
		
		for (int i = 0; i < numOfTabs; ++i)
			tabs.append("\t");
		
		return tabs.toString();
	}
}
