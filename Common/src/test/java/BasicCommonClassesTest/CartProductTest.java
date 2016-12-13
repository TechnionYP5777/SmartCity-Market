package BasicCommonClassesTest;

import static org.junit.Assert.fail;
import org.junit.Test;
import java.time.LocalDate;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.CartProduct;
/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class CartProductTest {
	@Test public void CartProductTestMethod() {
		Manufacturer man = new Manufacturer(11,"Osem");
		CatalogProduct catp = new CatalogProduct(11, "Bamba", null, man, "", 12, null);
		LocalDate ld = LocalDate.of(2016,12,11);
		CartProduct crp = new CartProduct(catp, ld, 0);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 0)
			fail();
		catp.setBarcode(12);
		crp.setCatalogProduct(catp);
		ld.plusDays(1);
		crp.setExpirationDate(ld);
		crp.setAmount(2);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 2)
			fail();
		CartProduct crp2 = new CartProduct(catp, ld, 2);
		if (!crp2.equals(crp))
			fail();
		crp2.setAmount(1);
		if (!crp2.equals(crp))
			fail();
		ld.plusDays(1);
		crp2.setExpirationDate(ld);
		//TODO:change to JodaTime due to static implementation of date..
//		if (crp2.equals(crp))
//			fail();
	}
}
