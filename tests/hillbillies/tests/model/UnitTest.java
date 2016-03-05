package hillbillies.tests.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hillbillies.model.Unit;
import hillbillies.model.Unit.State;
import hillbillies.model.Vector;
import ogp.framework.util.ModelException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A Test class to test the Unit class.
 * 
 * @author HF corp.
 * @version 1.0
 */
public class UnitTest {
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void startSprint_Valid() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Jezus", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setState(State.WALKING);
		testUnit.startSprint();
		assertTrue(testUnit.isSprinting());
	}
	
	/*@Test
	public void startSprint_InvalidNoStamina() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Jezus", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setCurrentStamina(0);
		testUnit.startSprint();
		assertFalse(testUnit.isSprinting());
	}*/
	
	@Test
	public void stopSprint_Valid() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Job", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setState(State.WALKING);
		testUnit.startSprint();
		testUnit.stopSprint();
		assertFalse(testUnit.isSprinting());
	}
	
	@Test
	public void attack_Valid() {
		Unit testAttacker = null;
		Unit testDefender = null;
		try {
			testAttacker = new Unit(10, 10, 10, "Magdalena", 50, 50, 50, 50, false);
			testDefender = new Unit(11, 9, 10, "Lucas", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen
		}
		testAttacker.attack(testDefender);
		assertTrue(testAttacker.getState() == State.ATTACKING);
	}
	
	@Test
	public void attack_InvalidStatetime() {
		Unit testAttacker = null;
		Unit testDefender = null;
		try {
			testAttacker = new Unit(10, 10, 10, "Mattheus", 50, 50, 50, 50, false);
			testDefender = new Unit(11, 9, 10, "Judas", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen
		}
		testAttacker.setStatetime(10);
		testAttacker.attack(testDefender);
		assertFalse(testAttacker.getState() == State.ATTACKING);
	}
	
	@Test
	public void attack_InvalidTooFarAway() {
		Unit testAttacker = null;
		Unit testDefender = null;
		try {
			testAttacker = new Unit(10, 10, 10, "Noah", 50, 50, 50, 50, false);
			testDefender = new Unit(20, 25, 17, "Jona", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen
		}
		testAttacker.attack(testDefender);
		assertFalse(testAttacker.getState() == State.ATTACKING);
	}
	
	@Test
	public void startWork_Valid() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Ezechiel", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.startWork();
		assertTrue(testUnit.getState() == State.WORKING);
	}
	
	@Test
	public void startWork_InvalidStatetime() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Johannes", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setStatetime(3.14d);
		testUnit.startWork();
		assertFalse(testUnit.getState() == State.WORKING);
	}
	
	@Test
	public void startWork_InvalidState() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Jozef", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setState(State.DANCING);
		testUnit.startWork();
		assertFalse(testUnit.getState() == State.WORKING);
	}
	
	@Test
	public void startRest_Valid() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Abraham", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.startRest();
		assertTrue(testUnit.getState() == State.RESTING);
	}
	
	@Test
	public void startRest_Invalid() {
		Unit testUnit = null;
		try {
			testUnit = new Unit(10, 10, 10, "Isaak", 50, 50, 50, 50, false);
		} catch (ModelException e) {
			//not supposed to happen.
		}
		testUnit.setStatetime(42);
		testUnit.startRest();
		assertFalse(testUnit.getState() == State.RESTING);
	}
	
	@Test
	public void isValidPositon_Valid() {
		assertTrue(Unit.isValidPosition(new Vector(13, 14, 15)));
	}
	
	@Test
	public void isValidPositon_Invalid() {
		assertFalse(Unit.isValidPosition(new Vector(60, 14, 15)));
	}
	
	@Test
	public void isValidName_Valid() {
		assertTrue(Unit.isValidName("Lucifer"));
	}
	
	@Test
	public void isValidName_InvalidCharacter() {
		assertFalse(Unit.isValidName("Daniël"));
	}
	
	@Test
	public void isValidName_InvalidUppercase() {
		assertFalse(Unit.isValidName("schaap"));
	}
	
	@Test
	public void isValidName_InvalidLength() {
		assertFalse(Unit.isValidName("H"));
	}
}
