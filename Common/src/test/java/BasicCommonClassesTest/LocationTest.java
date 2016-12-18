package BasicCommonClassesTest;


import static org.junit.Assert.fail;

import org.junit.Test;

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;;
/**@author Lior Ben Ami
 * @since 2016-12-11 */

public class LocationTest {
	@Test public void LocationTestMethod() {
		Location lo = new Location(1,1,PlaceInMarket.WAREHOUSE);
		if (lo.getX() != 1 || lo.getY() != 1 || lo.getPlaceInMarket() != PlaceInMarket.WAREHOUSE)
			fail();
		lo.setX(2);
		lo.setY(2);
		lo.setPlaceInMarket(PlaceInMarket.STORE);
		//TODO: add null check?
		if (lo.getX() != 2 || lo.getY() != 2 || lo.getPlaceInMarket() != PlaceInMarket.STORE)
			fail();
		Location lo2 = new Location(1,2,PlaceInMarket.STORE);
		if (lo2.equals(lo))
			fail();
		lo2.setX(2);
		if (!lo2.equals(lo))
			fail();
	}
}
