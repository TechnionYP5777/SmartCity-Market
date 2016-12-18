package BasicCommonClassesTest;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;

import org.junit.Test;

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class ProductPackageTest {
	@Test public void ProductPackageTestMethod() {
		LocalDate ld = new LocalDate(2016,12,11);
		SmartCode sc = new SmartCode(123, ld);
		Location lo = new Location(1,1,PlaceInMarket.WAREHOUSE);
		ProductPackage pp = new ProductPackage(sc,2,lo);
		if (!sc.equals(pp.getSmartCode()) || pp.getAmount() != 2 || !lo.equals(pp.getLocation())) 
			fail();
		sc.setBarcode(111);
		lo.setX(2);
		pp.setSmartCode(sc);
		pp.setAmount(1);
		pp.setLocation(lo);
		if (!sc.equals(pp.getSmartCode()) || pp.getAmount() != 1 || !lo.equals(pp.getLocation())) 
			fail();
		LocalDate ld2 = new LocalDate(2015, 12, 12);
		SmartCode sc2 = new SmartCode(111,ld2);
		ProductPackage pp2 = new ProductPackage(sc2, 1, lo);
		if (pp2.equals(pp))
			fail();
		sc2.setExpirationDate(sc.getExpirationDate());
		pp.setSmartCode(sc2);
		if (!pp2.equals(pp))
			fail();
	}
}

