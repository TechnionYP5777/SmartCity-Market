package BasicCommonClassesTest;

import static org.junit.Assert.fail;

import org.junit.Test;

import BasicCommonClasses.Ingredient;

/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class IngredientTest {
	@Test public void IngredientTestMethod() {
		Ingredient man = new Ingredient(1,"Telma");

		if (man.getId() != 1 || !"Telma".equals(man.getName()) )
			fail();
		man.setId(2);
		man.setName("Osem");
		if (man.getId() !=2 || !"Osem".equals(man.getName()) ) 
			fail();
		//the equals consider only the id field
		Ingredient man2 = new Ingredient(2,"Elite");
		if (!man2.equals(man))
			fail();
		man2.setId(3);
		if (man2.equals(man))
			fail();
	}
}