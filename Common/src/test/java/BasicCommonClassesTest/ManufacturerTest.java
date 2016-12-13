package BasicCommonClassesTest;

import static org.junit.Assert.fail;
import org.junit.Test;

import BasicCommonClasses.Manufacturer;;;
/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class ManufacturerTest {
	@Test public void ManufacturerTestMethod() {
		Manufacturer ing = new Manufacturer(1,"Milk");

		if (ing.getId() != 1 || !"Milk".equals(ing.getName()) )
			fail();
		ing.setId(2);
		ing.setName("Nuts");
		if (ing.getId() !=2 || !"Nuts".equals(ing.getName()) ) 
			fail();
		//the equals consider only the id field
		Manufacturer ing2 = new Manufacturer(2,"Corn");
		if (!ing2.equals(ing))
			fail();
		ing2.setId(3);
		if (ing2.equals(ing))
			fail();
	}
}

