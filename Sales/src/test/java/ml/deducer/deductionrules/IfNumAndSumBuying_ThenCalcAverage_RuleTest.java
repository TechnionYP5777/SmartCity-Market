package ml.deducer.deductionrules;

import static ml.utils.CollectionFunctions.findInCollection;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.NumOfBuyersPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfPurchasesPerMonthProperty;
import ml.common.property.deducedproperties.AverageCartPricePerMonthProperty;
import testmocks.DBMock;

public class IfNumAndSumBuying_ThenCalcAverage_RuleTest {

private static final double DELTA = 1e-15;
	
	@Test
	public void testDeduceProperties() {
		NumOfBuyersPerMonthProperty Numproperty = new NumOfBuyersPerMonthProperty(3,3);
		SumOfPurchasesPerMonthProperty Sumproperty = new SumOfPurchasesPerMonthProperty(3,7);
		
		Set<AProperty> propertySet = new HashSet<>();
		propertySet.add(Numproperty);
		propertySet.add(Sumproperty);
		
		Set<? extends AProperty> resultProperty = 
				new IfNumAndSumBuying_ThenCalcAverage_Rule().deduceProperties(DBMock.getSalePref(), propertySet);
		
		assertEquals(AverageCartPricePerMonthProperty.goMonthesBackLimit, resultProperty.size());
		assertTrue(resultProperty.contains(
				new AverageCartPricePerMonthProperty(Numproperty.getMonthAgo(), 0)));

		AverageCartPricePerMonthProperty itemFound = findInCollection(resultProperty, new AverageCartPricePerMonthProperty(Numproperty.getMonthAgo(), 0));

		assertNotNull(itemFound);
		assertEquals(itemFound.getAverage(), Sumproperty.getSumOfPurchases() / Numproperty.getNumOfBuyers(), DELTA);
			
	}
	

}
