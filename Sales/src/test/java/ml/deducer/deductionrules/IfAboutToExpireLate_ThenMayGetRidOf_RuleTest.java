package ml.deducer.deductionrules;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import BasicCommonClasses.PlaceInMarket;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import ml.common.property.deducedproperties.MayGetRidOfPackageProperty;
import testmocks.DBMock;
import testmocks.StorePackageMock;

public class IfAboutToExpireLate_ThenMayGetRidOf_RuleTest {

	@Test
	public void testDeduceProperties() {
		AboutToExpireLateStorePackageProperty property =
				new AboutToExpireLateStorePackageProperty(new StorePackageMock(1, 1, LocalDate.now().plusDays(20), PlaceInMarket.STORE));
		
		Set<AProperty> propertySet = new HashSet<>();
		propertySet.add(property);
		
		Set<? extends AProperty> resultProperty = 
				new IfAboutToExpireLate_ThenMayGetRidOf_Rule().deduceProperties(DBMock.getSalePref(), propertySet);
		
		assertEquals(1, resultProperty.size());
		assert resultProperty.contains(new MayGetRidOfPackageProperty(property.getStorePackage(),
				1 - 20.0 / AboutToExpireLateStorePackageProperty.maxDaysThreshold));
	}

}
