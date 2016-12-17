package BasicCommonClassesTest;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import BasicCommonClasses.SmartCode;;
/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class SmartCodeTest {
	@Test public void SmartCodeTestMethod() {
		LocalDate ld = new LocalDate(2016,12,11);
		SmartCode sc = new SmartCode(123, ld);
		if (sc.getBarcode() != 123 || !sc.getExpirationDate().equals(ld))
			fail();
		sc.setBarcode(111);
		ld.plusDays(1);
		sc.setExpirationDate(ld);
		if (sc.getBarcode() != 111 || !sc.getExpirationDate().equals(ld)) 
			fail();
		LocalDate ld2 = new LocalDate(2015, 12, 12);
		SmartCode sc2 = new SmartCode(111,ld2);
		if (sc2.equals(sc))
			fail();
		sc2.setExpirationDate(sc.getExpirationDate());
		if (!sc2.equals(sc))
			fail();
	}
}
