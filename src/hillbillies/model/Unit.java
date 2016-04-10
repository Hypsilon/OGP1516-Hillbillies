package hillbillies.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ogp.framework.util.ModelException;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * A Unit class for the game Hillbillies.
 * 
 * @invar	The Unit's position is valid.
 * 			| isValidPosition(position)
 * @invar	The name of the Unit is valid.
 * 			| isValidName(name)
 * @invar	Strength, agility, weight and toughness are in valid boundaries.
 * 			| 1 <= this.getStrength() <= 200
 * 			| 1 <= this.getAgility() <= 200
 * 			| 1 <= this.getWeight() <= 200
 * 			| 1 <= this.getToughness() <= 200
 * @invar	Weight is less than or equal to (strength + agility) / 2
 * 			| this.getWeight() <= (strength + agility) / 2
 * @invar 	This unit's currentHealth and currentStamina are valid.
 * 			| 0 < this.getCurrentHealth() <= this.getHealth()
 * 			| 0 <= this.getCurrentStamina() <= this.getStamina()
 * 
 * @author HF corp.
 * @version 1.0
 */
public class Unit {	
	/**
	 * Enum with the states this unit can have.
	 * 
	 * @author HF corp.
	 */
	public enum State {
		NOTHING, WALKING, ATTACKING, DEFENDING, DANCING, WORKING, RESTING
	}
	
	/**
	 * Valid characters for the name of this unit.
	 */
	private static final String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\'\" ";
	
	private State state;
	/** 
	 * Variable registering the time left during which this unit may not change states.
	 */
	private double statetime;
	
	private Vector position;
	private String name;
	
	private Vector endGoal, start, currentGoal;
	private Vector velocity;
	private boolean hasEndGoal;
	private double speedb, currentSpeed;
	private boolean sprinting;
	
	private int weight, strength, agility, toughness;
	private int health, stamina; 
	private double currentHealth, currentStamina;
	
	private double busytime;
	
	private boolean hasToRest;
	private double timeToRest;
	
	private double  orientation;
	private boolean defaultBehaviorEnabled; 
	
	/**
	 * Constructor for the Unit class.
	 * 
	 * @param x
	 * 			The x-coordinate of this Unit.
	 * @param y
	 * 			The y-coordinate of this Unit.
	 * @param z
	 * 			The z-coordinate of this Unit.
	 * @param name
	 * 			The name of this Unit.
	 * @param weight
	 * 			The weight of this Unit.
	 * @param strength
	 * 			The strength of this Unit.
	 * @param agility
	 * 			The agility of this Unit.
	 * @param toughness
	 * 			The toughness of this Unit.
	 * @param defaultBehaviorEnabled
	 * 			Whether or not this unit should have the default behavior.
	 * @throws ModelException 
	 * 			Throws a ModelException if the given position is invalid.
	 * 			| if !isValidPosition(new Vector(x, y, z))
	 * @throws ModelException
	 * 			Throws a ModelException if the given name is invalid.
	 * 			| if !isValidName(name)
	 * @effect	This constructor creates a new unit with the given parameters.
	 * 			| this(new Vector(x, y, z), name, weight, strength, agility, toughness, defaultBehaviorEnabled)
	 */
	public Unit(double x, double y, double z, String name, int weight, int strength, int agility, 
			int toughness, boolean defaultBehaviorEnabled) throws ModelException {
		this(new Vector(x, y, z), name, weight, strength, agility, toughness, defaultBehaviorEnabled);
	}
	
	/**
	 * Constructor for the Unit class.
	 * 
	 * @param position
	 * 			The position of this Unit.
	 * @param name
	 * 			The name of this Unit.
	 * @param weight
	 * 			The weight of this Unit.
	 * @param strength
	 * 			The strength of this Unit.
	 * @param agility
	 * 			The agility of this Unit.
	 * @param toughness
	 * 			The toughness of this Unit. 
	 * 
	 * @effect	Checks whether weight, strength, agility and toughness are valid and changes them appropriately.
	 * 			| this.weight = weight
	 *			| this.strength = strength
	 *			| this.agility = agility
	 *			| this.toughness = toughness
	 *			| checkValidProperty(true)
	 * @post	Sets health and stamina.
	 * 			| new.getHealth() == Math.ceil(200*(weight / 100) * (toughness / 100))
	 * 			| new.getStamina() == Math.ceil(200*(weight / 100) * (toughness / 100))
	 * 			| new.getCurrentHealth() == this.getHealth()
	 * 			| new.getCurrentStamina() == this.getStamina()
	 * @post	The new position equals the given position.
	 * 			| new.getPosition() == position
	 * @post	The new name equals the given name.
	 * 			| new.getName() == name
	 * @post	The orientation equals the default value PI/2.
	 * 			| new this.getOrientation() == Math.PI/2
	 * @throws ModelException
	 * 			If the given position is invalid.
	 * 			| if !isValidPosition(position)
	 * @throws ModelException
	 * 			If the given name is invalid.
	 * 			| if !isValidName(name)
	 */
	public Unit(Vector position, String name, int weight, int strength, int agility, 
			int toughness, boolean defaultBehaviorEnabled) throws ModelException {
		setWeight(weight);
		setStrength(strength);
		setAgility(agility);
		setToughness(toughness);
		checkValidProperty(true);
		
		setHealth((int)Math.ceil(200.0d*(((double)weight)/100.0d)*(((double)toughness)/100.0d)));
		setStamina((int)Math.ceil(200.0d*(((double)weight)/100.0d)*(((double)toughness)/100.0d)));
		setCurrentHealth(health);
		setCurrentStamina(stamina);
		
		setSpeedb(1.5d*(((double)(strength + agility)) / (200.0d * (((double)weight) / 100.0d))));
		
		setPosition(position.add(new Vector(0.5d, 0.5d, 0.5d)));
		
		setName(name);
		
		setOrientation(Math.PI/2);
		
		setHasEndGoal(false);
		
		setHasToRest(false);
		setTimeToRest(0);
		
		setState(State.NOTHING);
		setStatetime(-1);
		setDefaultBehaviorEnabled(defaultBehaviorEnabled);
	}
	
	/**
	 * Advances time by the given value of deltaT.
	 * 
	 * @param deltaT
	 * 			Time since the last time advanceTime was called.
	 * 
	 * @post	timeToRest increments by deltaT.
	 * @post	if timeToRest is greater than 180 set hasToRest to true.
	 * 
	 * @effect	If this unit has to rest, start resting.
	 * @effect 	If the unit's state is WALKING and the unit does not have to rest, walk.
	 * @effect 	If the unit's state is WORKING and the unit does not have to rest, work.
	 * @effect	If the unit's state is RESTING and the unit does not have to rest, rest.
	 * @effect 	When the unit is attacking, lower the statetime by deltaT. If the statetime is less than zero, do nothing.
	 * @effect 	When the unit is doing nothing and has a goal, find a path.	
	 * @effect 	When the unit is doing nothing and does not have a goal, set the behavior to default behavior.	
	 * 
	 * @throws 	ModelException
	 * 			|if (deltaT <= 0 or deltaT > 0.2)
	 */
	public void advanceTime(double deltaT) throws ModelException{
		if (deltaT <= 0 || deltaT > 0.2)
			throw new ModelException();
		setTimeToRest(timeToRest + deltaT);
		if (this.getTimeToRest() >= 180) 
			setHasToRest(true);
		if (this.isHasToRest()) {
			startRest();
		} else {
			if (this.getState() == State.WALKING)
				move(deltaT);
			else if (this.getState() == State.WORKING)
				work(deltaT);
			else if (this.getState() == State.RESTING)
				rest(deltaT);
			else if (this.getState() == State.ATTACKING) {
				setStatetime(statetime - deltaT);
				if (this.getStatetime() <= 0)
					setState(State.NOTHING);
			} else if (this.getState() == State.NOTHING && this.isHasEndGoal()) {
				findPath();
			}
			//else if (this.getState() == State.NOTHING) 
				//defaultBehavior();
		}
	}
	
	/**
	 * Makes this unit execute default behavior. This unit can either move to a random valid position, rest or work.
	 * 
	 * @effect	This unit moves to a random valid position, rests or work.
	 * 			| R = random.nextInt(3)
	 * 			| if R == 0 then moveTo(random.nextInt(50), random.nextInt(50), random.nextInt(50))
	 * 			| else if R == 1 then startWork()
	 * 			| else then startRest()
	 */
	@SuppressWarnings("unused")
	private void defaultBehavior(){
		Random rand = new Random();
		int R = rand.nextInt(3);
		if (R ==0){
			int randX = rand.nextInt(50);
			int randY = rand.nextInt(50);
			int randZ = rand.nextInt(50);
			try {
				moveTo(randX, randY, randZ);
			} catch (ModelException e) {
			}
		}
		else if (R ==1)
			startWork();
		else
			startRest();	
	}

	/**
	 * Makes this unit start moving to the given adjacent cube.
	 * 
	 * @param xdiff
	 * 			The difference in x-coordinate, newX - currentX.
	 * @param ydiff
	 * 			The difference in y-coordinate, newY - currentY.
	 * @param zdiff
	 * 			The difference in z-coordinate, newZ - currentZ.
	 * 
	 * @post	Sets the currentGoal of this unit to the center of the given cube.
	 * 			| new.currentGoal = new Vector(floor(position.getX()) + 0.5, floor(position.getY()) + 0.5,
	 * 			|															floor(position.getZ()) + 0.5)
	 * @post	Sets the currentSpeed and velocity of this unit.
	 * 			| if zdiff == 1 then new.currentSpeed = 0.5 * speedb
	 * 			| if zdiff == -1 then new.currentSpeed = 1.2 * speedb
	 * 			| if zdiff == 0 then new.currentSpeed = speedb
	 * 			| new.velocity = currentGoal.subtract(position).normalize()
	 * @post	Sets the orientation of this unit to face the walking direction.
	 * 			| new.orientation = Math.atan2(velocity.getY(), velocity.getX());
	 * @throws 	ModelException
	 * 			Throws a ModelException if the given cube is not adjacent or if the given cube is not a valid position.
	 * 			| if (xdiff < -1 || xdiff > 1)
	 * 			| if (ydiff < -1 || ydiff > 1)
	 * 			| if (zdiff < -1 || zdiff > 1) 
	 * 			| if isValidPosition(currentGoal)
	 */
	public void moveToAdjacent(int xdiff, int ydiff, int zdiff) throws ModelException {
		if (this.getStatetime() > 0)
			return;
		if (this.getState() != State.NOTHING && this.getState() != State.WORKING)
			return;
		
		if (xdiff < -1 || xdiff > 1)
			throw new ModelException();
		if (ydiff < -1 || ydiff > 1)
			throw new ModelException();
		if (zdiff < -1 || zdiff > 1)
			throw new ModelException();
		
		Vector diff = new Vector(xdiff, ydiff, zdiff);
		setStart(new Vector(this.getPosition()));
		setCurrentGoal(new Vector(Math.floor(this.getPosition().getX()) + 0.5d, 
				Math.floor(this.getPosition().getY()) + 0.5d,
				Math.floor(this.getPosition().getZ()) + 0.5d).add(diff));
		if (!isValidPosition(this.getCurrentGoal()))
			throw new ModelException("Trying to move to an invalid position.");
		
		setVelocity(this.getCurrentGoal().subtract(this.getPosition()));
		if (this.getVelocity().isAlmostEqual(new Vector())) 
			return;
		setVelocity(this.getVelocity().normalize());
		
		if (zdiff == 1)
			setCurrentSpeed(0.5d * this.getSpeedb());
		else if (zdiff == -1)
			setCurrentSpeed(1.2d * this.getSpeedb());
		else
			setCurrentSpeed(this.getSpeedb());
		
		setState(State.WALKING);
		setOrientation(Math.atan2(this.getVelocity().getY(), this.getVelocity().getX()));
	}
	
	/**
	 * Makes this unit start moving towards the given goal.
	 * 
	 * @param endGoalx
	 * 			The x-coordinate of the endgoal.
	 * @param endGoaly
	 * 			The y-coordinate of the endgoal.
	 * @param endGoalz
	 * 			The z-coordinate of the endgoal. 
	 * @pre		The given endgoal must have valid coordinates.
	 * 			| isValidPosition(new Vector(endGoalx, endGoaly, endGoalz)
	 * @post	Sets the endGoal of this unit equal to the given endGoal.
	 * 			| endGoal = new Vector(endGoalx, endGoaly, endGoalz)
	 * @post	Sets hasEndGoal to true.
	 * 			| hasEndGoal = true
	 * @throws ModelException
	 * 			Throws a ModelException if the given goal has an invalid position.
	 * 			| if (!isValidPosition(new Vector(endGoalx, endGoaly, endGoalz)))
	 */
	public void moveTo(int endGoalx, int endGoaly, int endGoalz) throws ModelException{
		if (this.getStatetime() > 0)
			return;
		
		setEndGoal(new Vector(endGoalx, endGoaly, endGoalz));
		if (!isValidPosition(this.getEndGoal()))
			throw new ModelException("Endgoal is an invalid cube");
		setHasEndGoal(true);	
		findPath();
	}

	/**
	 * This unit starts sprinting.
	 * 
	 * @pre		The current stamina of this unit is greater than 0.
	 * 			| currentStamina > 0
	 * @pre 	This unit is walking.
	 * 			| state == State.WALKING
	 * @post	This unit starts sprinting.
	 * 			| new.isSprinting = true
	 */
	public void startSprint(){
		assert(this.getCurrentStamina() > 0);
		assert(this.getState() == State.WALKING);
		setSprinting(true);
	}

	/**
	 * This unit stops sprinting.
	 * 
	 * @pre		This unit is sprinting.
	 * 			| this.isSprinting == true
	 * @post	This unit stops sprinting.
	 * 			| new.isSprinting = false
	 */
	public void stopSprint(){
		assert(this.isSprinting());
		setSprinting(false);
	}

	/**
	 * Moves this unit towards its current goal. 
	 * (This method is only called from advanceTime so no formal documentation is given)
	 * 
	 * @param deltaT
	 * 			The time passed since the last update
	 * @post 	The unit moves towards its current goal. When sprinting this unit moves twice as fast.
	 * @post 	If this unit has reached its current goal, the position will be set equal to the currentGoal. If the 
	 * 			unit has an end goal, look for the next cube to walk to.
	 */
	private void move(double deltaT){
		if (!this.isSprinting())
			try {
				setPosition(this.getPosition().add(this.getVelocity().multiply(deltaT*this.getCurrentSpeed())));
			} catch (ModelException e) {
				e.printStackTrace();
			}
		else {
			try {
				setPosition(this.getPosition().add(this.getVelocity().multiply(deltaT*2*this.getCurrentSpeed())));
			} catch (ModelException e) {
			}
			setCurrentStamina(this.getCurrentStamina() - 0.1 * deltaT);
		}
		double length = this.getCurrentGoal().subtract(this.getStart()).length();
		double lengthOnTheRoad = this.getPosition().subtract(this.getStart()).length();
		if (lengthOnTheRoad >= length){
			try {
				setPosition(this.getCurrentGoal());
			} catch (ModelException e) {
			}
			this.setState(State.NOTHING);
			if (this.isHasEndGoal()) {
				if (	Math.floor(this.getCurrentGoal().getX()) == Math.floor(this.getEndGoal().getX()) &&
						Math.floor(this.getCurrentGoal().getY()) == Math.floor(this.getEndGoal().getY()) &&
						Math.floor(this.getCurrentGoal().getZ()) == Math.floor(this.getEndGoal().getZ())) {
					setHasEndGoal(false);
					setSprinting(false);
				} else {
					findPath();
				}
			}
		}
	}

	/**
	 * Finds the next cube to walk to to progress towards the endgoal.
	 * 
	 * @effect Calls the moveToAdjacent(xdiff, ydiff, zdiff) to move this unit towards the endgoal.
	 * 			| xdiff = ydiff = zdiff = 0
	 * 			| if floor(position.getX()) == floor(endGoal.getX()) then xdiff = 0
	 * 			| else if floor(position.getX()) < floor(endGoal.getX()) then xdiff = 1
	 * 			| else xdiff = -1
	 * 			| if floor(position.getY()) == floor(endGoal.getY()) then ydiff = 0
	 * 			| else if floor(position.getY()) < floor(endGoal.getY()) then ydiff = 1
	 * 			| else ydiff = -1
	 * 			| if floor(position.getZ()) == floor(endGoal.getZ()) then zdiff = 0
	 * 			| else if floor(position.getZ()) < floor(endGoal.getZ()) then zdiff = 1
	 * 			| else zdiff = -1
	 * 			| moveToAdjacent(xdiff, ydiff, zdiff)
	 */
	private void findPath(){
		int cubeX = (int)Math.floor(this.getPosition().getX());
		int cubeY = (int)Math.floor(this.getPosition().getY());
		int cubeZ = (int)Math.floor(this.getPosition().getZ());
		int goalX = (int)Math.floor(this.getEndGoal().getX());
		int goalY = (int)Math.floor(this.getEndGoal().getY());
		int goalZ = (int)Math.floor(this.getEndGoal().getZ());
		int xdiff,ydiff,zdiff = 0;
		if (cubeX == goalX)
			xdiff = 0;
		else if (cubeX < goalX)
			xdiff = 1;
		else
			xdiff = -1;
		if (cubeY == goalY)
			ydiff = 0;
		else if (cubeY < goalY)
			ydiff = 1;
		else
			ydiff = -1;
		if (cubeZ == goalZ)
			zdiff = 0;
		else if (cubeZ < goalZ)
			zdiff = 1;
		else
			zdiff = -1;
		try {
			moveToAdjacent(xdiff, ydiff, zdiff);
		} catch (ModelException e) {
		}
	}

	/**
	 * Makes this unit attack the given unit.
	 * 
	 * @param victim
	 * 			The victim to attack.
	 * @pre		Statetime is less than or equal to 0.
	 * 			| statetime <= 0
	 * @pre 	The attacker and the victim are in adjacent cubes.
	 * 			|(Math.abs(victim.getPosition().getX() - position.getX())< 2 
				|&& Math.abs(victim.getPosition().getY() - position.getY())< 2 
				|&& Math.abs(victim.getPosition().getZ() - position.getZ())< 2)
	 * @post 	The attacker and the victim face each other. 
	 * 			|new.orientation = Math.atan2((victim.getPosition().getY() - position.getY()),
										(victim.getPosition().getX() - position.getX()))
	 * @post	State of the attacker is ATTACKING. Statetime equals one.
	 * 			|new.state = State.ATTACKING
	 * 			|new.statetime = 1
	 * @effect	The attacker attacks the victim and the victim defends itself.
	 * 			|victim.defend(this)
	 * 	
	 */
	public void attack(Unit victim){
		if (this.getStatetime() > 0)
			return;
		if (Math.abs(victim.getPosition().getX() - this.getPosition().getX())< 2 
				&& Math.abs(victim.getPosition().getY() - this.getPosition().getY())< 2 
				&& Math.abs(victim.getPosition().getZ() - this.getPosition().getZ())< 2){
			
			setOrientation(Math.atan2((victim.getPosition().getY() - this.getPosition().getY()),
										(victim.getPosition().getX() - this.getPosition().getX())));
			victim.setOrientation(Math.atan2((this.getPosition().getY() - victim.getPosition().getY()),
												(this.getPosition().getX() - victim.getPosition().getX())));
			setState(State.ATTACKING);
			setStatetime(1);
			victim.defend(this);
		}
	}

	/**
	 * Makes the attacked unit defend itself by dodging, blocking, or taking damage.
	 * 
	 * @param attacker
	 * @effect	The unit has 0.2d * (double)agility/(double)attacker.getAgility() chance to dodge this attack.
	 * 			If the unit dodges he loses no health and moves to an adjacent valid cube in the xy-plane.
	 * 			| pDodge = 0.2d * (double)agility/(double)attacker.getAgility()
	 * 			| if (random.nextDouble() < pDodge)
	 * 			| 	dodge()
	 * @post 	If dodging fails this unit has a 0.25d *((double)strength + (double)agility)/((double)attacker.getStrength() + (double)attacker.getAgility())
	 * 			chance to block this attack. If the unit blocks he loses no health.
	 * 			| pBlock = 0.25d *((double)strength + (double)agility)/((double)attacker.getStrength() + (double)attacker.getAgility())
	 * 			| else if (random.nextDouble() < pBlock)
	 * 			|	return
	 * @post	If the unit fails to dodge or block, this unit loses a portion of its currentHealth.
	 * 			| else
	 * 			| 	new.currentHealth = currentHealth - attacker.getStrength()/10
	 * @post	This unit's state is set to NOTHING, this overrides the statetime. Statetime is set to -1.
	 * 			| new.state = State.NOTHING
	 * 			| new.statetime = -1
	 */
	private void defend(Unit attacker){
		double pDodge = 0.2d * (double)this.getAgility()/(double)attacker.getAgility();
		double pBlock = 0.25d *((double)this.getStrength() + (double)this.getAgility())/
				((double)attacker.getStrength() + (double)attacker.getAgility());
		Random rand = new Random();
		if (rand.nextDouble() < pDodge) {
			dodge(rand);
		}
		else if(rand.nextDouble() < pBlock)
			return;
		else {
			setCurrentHealth(this.getCurrentHealth() - attacker.getStrength()/10);
			//TODO: unit is dood!
			assert (this.getCurrentHealth() > 0);
		}
		setState(State.NOTHING);
		setStatetime(-1);
	}	

	/**
	 * Moves this unit to an adjacent valid cube in its xy-plane.
	 * 
	 * @post	This unit has moved to an adjacent valid cube in its xy-plane.
	 *			| List<Vector> validPos = new LinkedList<Vector>()
	 *			| for int xd from -1 to 1 do
	 *			|	for int yd from -1 to 1 do
	 *			|		if xd == 0 and yd == 0 then continue
	 *			|		Vector diff = new Vector(xd, yd, 0)
	 *			|		if isValidPosition(diff) then validPos.add(diff)
	 *			| int index  = random.nextInt(validPos.size())
	 *			| position = position.add(validPos.get(index))
	 *
	 */
	private void dodge(Random rand) {
		List<Vector> validPos = new LinkedList<Vector>();
		for(int xd = -1; xd <= 1; xd++) {
			for(int yd = -1; yd <= 1; yd++) {
				if (xd == 0 && yd == 0)
					continue;
				Vector diff = new Vector(xd, yd, 0);
				if (isValidPosition(diff))
					validPos.add(diff);
			}
		}
		int index = rand.nextInt(validPos.size());
		try {
			setPosition(this.getPosition().add(validPos.get(index)));
		} catch (ModelException e) {
		}
	}
	
	/**
	 * This unit starts working.
	 * 
	 * @pre 	This unit's statetime is greater than 0.
	 * 			| statetime > 0
	 * @pre		This unit is doing nothing or is working already.
	 * 			| state == State.NOTHING or state == State.WORKING
	 * @post	This unit's state is set to working and its busytime is set to 500 / strength.
	 * 			| new.state = State.WORKING
	 * 			| new.busytime = 500/(double)strength
	 */
	public void startWork(){
		if (this.getStatetime() > 0)
			return;
		if (this.getState() == State.NOTHING || this.getState() == State.WORKING){
			setState(State.WORKING);
			setBusytime(500.0d/(double)strength);
		}
	}

	/**
	 * Makes this unit work.
	 * 
	 * @param deltaT
	 * 			The time passed since the last update.
	 * @post	busytime decreases and if this unit is done working (busytime <= 0) the state is set to State.NOTHING .
	 */
	private void work(double deltaT){
		setBusytime(this.getBusytime() - deltaT);
		if (this.getBusytime() <= 0)
			setState(State.NOTHING);
	}

	/**
	 * This unit starts resting.
	 * 
	 * @pre		This unit's statetime is greater than 0.
	 * 			| statetime > 0
	 * @post	This unit starts resting.
	 * 			| new.state = State.RESTING
	 * @post	This unit's statetime is set to 40/toughness.
	 * 			| new.statetime = 40/(double)toughness
	 * @post	This unit no longer has to rest.
	 * 			| hasToRest = false
	 * @post	This unit's timeToRest is set to 0.
	 * 			| timeToRest = 0
	 */
	public void startRest(){
		if (this.getStatetime() > 0)
			return;
		setState(State.RESTING);
		setStatetime(40/(double)toughness);
		setHasToRest(false);
		setTimeToRest(0);
	}

	/**
	 * The unit rests.
	 * 
	 * @param deltaT
	 * 			The time since the last update.
	 * @post	The statetime gets lowered by deltaT.
	 * @post	currentHealth increases until it reaches its maximum health.
	 * @post	If currentHealth has reached its maximum, currentStamina increases 
	 * 			until it reaches its maximum stamina.
	 * @post 	When currentHealth and currentStamina have reached their maxima,
	 * 			the unit rests during the rest of the statetime. 
	 * 			Afterwards, its state returns to NOTHING.
	 */
	private void rest(double deltaT){
		setStatetime(this.getStatetime() - deltaT);
		if (this.getCurrentHealth() + this.getCurrentHealth() + (double)this.getToughness()/40 *deltaT< this.getHealth())
			setCurrentHealth(this.getCurrentHealth() + (double)this.getToughness()/40 *deltaT);
		else if (this.getCurrentHealth() < this.getHealth())
			setCurrentHealth(this.getHealth());
		else if (this.getCurrentStamina() + (double)this.getToughness()/20 * deltaT < this.getStamina())
			setCurrentStamina(this.getCurrentStamina() + (double)this.getToughness()/20 * deltaT);
		else if (this.getCurrentStamina()< this.getStamina())
			setCurrentStamina(this.getStamina());
		else if (this.getStatetime() < 0)
			setState(State.NOTHING);
	}
	
	/**
	 * Checks whether this position is valid. Every coordinate must be in the interval [0, 50).
	 * 
	 * @param position
	 * 			The position to check.
	 * 
	 * @return	Returns true if this position is valid.
	 * 			| if (position.getX()<0 || position.getX() >= 50) then return false
	 * 			| if (position.getY()<0 || position.getY() >= 50) then return false
	 * 			| if (position.getZ()<0 || position.getZ() >= 50) then return false
	 * 			| else return true
	 */
	@Raw
	public static boolean isValidPosition(Vector position) {
		if (position.getX()<0 || position.getX() >= 50)
			return false;
		if (position.getY()<0 || position.getY() >= 50)
			return false;
		if (position.getZ()<0 || position.getZ() >= 50)
			return false;
		return true;
	}

	/**
	 * Checks whether this name is valid. The first letter should be a capital letter. The length of the name should be at least 2.
	 * Every character in the name must be a valid char, found in validChars.
	 * 
	 * @param name
	 * 			The name to check.
	 * 
	 * @return  If the given name is less than two characters long, returns false.
	 * 			| if name.length() < 2 then return false
	 * 			| if not Character.isUpperCase(name.charAt(0)) then return false
	 * @return  If the name has invalid characters, returns false.
	 * 			| for elementName in name: 
	 * 			|	boolean same = false
	 * 			|	for elementValid in validChars:
	 * 			|		if elementName == elementValid then
	 * 			|			same = true
	 * 			|			break
	 * 			|	if not same then
	 * 			|		return false
	 * @return 	Else return true.
	 * 			| else return true
	 */
	@Raw
	public static boolean isValidName(String name) {
		if (name.length()<2)
			return false;
		if (! Character.isUpperCase(name.charAt(0)))
			return false;
		for (char elementName:name.toCharArray()){
			boolean same = false;
			for (char elementValid:validChars.toCharArray()){
				if (elementName == elementValid) {
					same = true;
					break;
				}
			}
			if (! same)
				return false;
		}				
		return true;
	}

	/**
	 * Checks whether weight, strength, agility and toughness are valid and changes them appropriately.
	 * 
	 * @param init
	 * 			true iff this is the initialization of this Unit.
	 * 
	 * @post	If strength is less than minValue, sets strength to minValue and if strength is greater than
	 * 			maxValue sets strength to maxValue. minValue and maxValue are equal to 25, 100 if init is true
	 * 			and 1, 200 if init is false.
	 * 			| if init then minValue = 25 and maxValue = 100
	 * 			| else then minValue = 1 and maxValue = 200
	 * 			| if strength < minValue then new.strength = minValue
	 * 			| else if strength > maxValue then new.strength = maxValue
	 * @post	If agility is less than minValue, sets agility to minValue and if agility is greater than
	 * 			maxValue sets agility to maxValue. minValue and maxValue are equal to 25, 100 if init is true
	 * 			and 1, 200 if init is false.
	 * 			| if init then minValue = 25 and maxValue = 100
	 * 			| else then minValue = 1 and maxValue = 200
	 * 			| if agility < minValue then new.agility = minValue
	 * 			| else if agility > maxValue then new.agility = maxValue
	 * @post	If weight is less than minValue, sets weight to minValue and if weight is greater than
	 * 			maxValue sets weight to maxValue. minValue and maxValue are equal to 25, 100 if init is true
	 * 			and 1, 200 if init is false. If weight is less than (strength + agility) / 2, then weight
	 * 			is set equal to (strength + agility) / 2.
	 * 			| if init then minValue = 25 and maxValue = 100
	 * 			| else then minValue = 1 and maxValue = 200
	 * 			| if weight < (strength + agility) / 2 then new.weight = (strength + agility) / 2
	 * 			| if weight < minValue then new.weight = minValue
	 * 			| else if weight > maxValue then new.weight = maxValue
	 * @post	If toughness is less than minValue, sets toughness to minValue and if toughness is greater than
	 * 			maxValue sets toughness to maxValue. minValue and maxValue are equal to 25, 100 if init is true
	 * 			and 1, 200 if init is false.
	 * 			| if init then minValue = 25 and maxValue = 100
	 * 			| else then minValue = 1 and maxValue = 200
	 * 			| if toughness < minValue then new.toughness = minValue
	 * 			| else if toughness > maxValue then new.toughness = maxValue
	 */
	@Raw
	private void checkValidProperty(boolean init){
		int minValue = -1;
		int maxValue = -1;
		if (init) {
			minValue = 25;
			maxValue = 100;
		} else {
			minValue = 1;
			maxValue = 200;
		}
			
		if (this.getStrength() < minValue)
			setStrength(minValue);
		else if (this.getStrength() > maxValue)
			setStrength(maxValue);
		
		if (this.getAgility() < minValue)
			this.setAgility(minValue);
		else if (this.getAgility() > maxValue)
			setAgility(maxValue);
		
		if (this.getWeight() < (this.getStrength() + this.getAgility())/2)
			setWeight((this.getStrength() + this.getAgility())/2);
		else if (this.getWeight() < minValue)
			setWeight(minValue);
		else if (this.getWeight() > maxValue)
			setWeight(maxValue);
		
		if (this.getToughness() < minValue)
			setToughness(minValue);
		else if (this.getToughness() > maxValue)
			setToughness(maxValue);	
	}
	
	/**
	 * @return the position
	 */
	@Basic @Raw
	public Vector getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 * @throws ModelException 
	 * 			| if !isValidPosition(position)
	 */
	@Basic @Raw
	public void setPosition(Vector position) throws ModelException {
		if(!isValidPosition(position))
			throw new ModelException("Trying to set an invalid position.");	
		this.position = position;
	}

	/**
	 * @return the name
	 */
	@Basic @Raw
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 * @throws ModelException 
	 * 			| if !isValidName(name)
	 */
	@Basic @Raw
	public void setName(String name) throws ModelException {
		if (!isValidName(name))
			throw new ModelException("Trying to set an invalid name.");	
		this.name = name;
	}

	/**
	 * @return the defaultBehaviourEnabled
	 */
	@Basic @Raw
	public boolean isDefaultBehaviorEnabled() {
		return defaultBehaviorEnabled;
	}

	/**
	 * @param defaultBehaviourEnabled the defaultBehaviourEnabled to set
	 */
	@Basic @Raw
	public void setDefaultBehaviorEnabled(boolean defaultBehaviourEnabled) {
		this.defaultBehaviorEnabled = defaultBehaviourEnabled;
	}

	/**
	 * @return the state
	 */
	@Basic @Raw
	public State getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	@Basic @Raw
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return the orientation
	 */
	@Basic @Raw
	public double getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	@Basic @Raw
	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the weight
	 */
	@Basic @Raw
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	@Basic @Raw
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the strength
	 */
	@Basic @Raw
	public int getStrength() {
		return strength;
	}

	/**
	 * @param strength the strength to set
	 */
	@Basic @Raw
	public void setStrength(int strength) {
		this.strength = strength;
	}

	/**
	 * @return the agility
	 */
	@Basic @Raw
	public int getAgility() {
		return agility;
	}

	/**
	 * @param agility the agility to set
	 */
	@Basic @Raw
	public void setAgility(int agility) {
		this.agility = agility;
	}

	/**
	 * @return the toughness
	 */
	@Basic @Raw
	public int getToughness() {
		return toughness;
	}

	/**
	 * @param toughness the toughness to set
	 */
	@Basic @Raw
	public void setToughness(int toughness) {
		this.toughness = toughness;
	}

	/**
	 * @return the health
	 */
	@Basic @Raw
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	@Basic @Raw
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the stamina
	 */
	@Basic @Raw
	public int getStamina() {
		return stamina;
	}

	/**
	 * @param stamina the stamina to set
	 */
	@Basic @Raw
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	/**
	 * @return the currentHealth
	 */
	@Basic @Raw
	public double getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * @param currentHealth the currentHealth to set
	 * 
	 * @pre		The given currentHealth should be between 0 and this.getHealth()
	 * 			| currentHealth > 0 && currentHealth <= this.getHealth()
	 */
	@Basic @Raw
	public void setCurrentHealth(double currentHealth) {
		assert (currentHealth > 0 && currentHealth <= this.getHealth());
		this.currentHealth = currentHealth;
	}

	/**
	 * @return the currentStamina
	 */
	@Basic @Raw
	public double getCurrentStamina() {
		return currentStamina;
	}

	/**
	 * @param currentStamina the currentStamina to set
	 *
	 * @pre		The given currentStamina should be between 0 and this.getStamina()
	 * 			| currentStamina > 0 && currentStamina <= this.getStamina()
	 */
	@Basic @Raw
	public void setCurrentStamina(double currentStamina) {
		assert (currentStamina > 0 && currentStamina <= this.getStamina());
		this.currentStamina = currentStamina;
	}

	/**
	 * @return the currentSpeed
	 */
	@Basic @Raw
	public double getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @param currentSpeed the currentSpeed to set
	 */
	@Basic @Raw
	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	/**
	 * @return the isSprinting
	 * 			| return sprinting && this.getState() == State.WALKING
	 */
	@Basic @Raw
	public boolean isSprinting() {
		return sprinting && this.getState() == State.WALKING;
	}

	/**
	 * @param isSprinting the isSprinting to set
	 */
	@Basic @Raw
	public void setSprinting(boolean isSprinting) {
		this.sprinting = isSprinting;
	}

	
	/**
	 * 
	 * @return
	 */
	@Basic @Raw
	public double getSpeedb() {
		return speedb;
	}
	
	/**
	 * 
	 * @param speedb
	 */
	@Basic @Raw
	public void setSpeedb(double speedb) {
		this.speedb = speedb;
	}

	/**
	 * @return the statetime
	 */
	@Basic @Raw
	public double getStatetime() {
		return statetime;
	}

	/**
	 * @param statetime the statetime to set
	 */
	@Basic @Raw
	public void setStatetime(double statetime) {
		this.statetime = statetime;
	}

	/**
	 * @return the endGoal
	 */
	@Basic @Raw
	public Vector getEndGoal() {
		return new Vector(endGoal);
	}

	/**
	 * @param endGoal the endGoal to set
	 * @throws ModelException 
	 * 			If the given endgoal is invalid.
	 * 			| if !isValidPosition(endGoal)
	 */
	@Basic @Raw
	public void setEndGoal(Vector endGoal) throws ModelException {
		if (!isValidPosition(endGoal))
			throw new ModelException("Endgoal is an invalid position.");
		this.endGoal = endGoal;
	}

	/**
	 * @return the start
	 */
	@Basic @Raw
	public Vector getStart() {
		return new Vector(start);
	}

	/**
	 * @param start the start to set
	 * @throws ModelException 
	 *			If the given start position is invalid.
	 *			| if !isValidPosition(start)
	 */
	@Basic @Raw
	public void setStart(Vector start) throws ModelException {
		if (!isValidPosition(start))
			throw new ModelException("Invalid start position.");
		this.start = start;
	}

	/**
	 * @return the currentGoal
	 */
	@Basic @Raw
	public Vector getCurrentGoal() {
		return new Vector(currentGoal);
	}

	/**
	 * @param currentGoal the currentGoal to set
	 * @throws ModelException 
	 *			If the currentGoal is invalid.
	 *			| if !isValidPosition(currentGoal)
	 */
	@Basic @Raw
	public void setCurrentGoal(Vector currentGoal) throws ModelException {
		if (!isValidPosition(currentGoal))
			throw new ModelException("The given currentGoal is invalid.");
		this.currentGoal = currentGoal;
	}

	/**
	 * @return the hasEndGoal
	 */
	@Basic @Raw
	public boolean isHasEndGoal() {
		return hasEndGoal;
	}

	/**
	 * @param hasEndGoal the hasEndGoal to set
	 */
	@Basic @Raw
	public void setHasEndGoal(boolean hasEndGoal) {
		this.hasEndGoal = hasEndGoal;
	}

	/**
	 * @return the busytime
	 */
	@Basic @Raw
	public double getBusytime() {
		return busytime;
	}

	/**
	 * @param busytime the busytime to set
	 */
	@Basic @Raw
	public void setBusytime(double busytime) {
		this.busytime = busytime;
	}

	/**
	 * @return the hasToRest
	 */
	@Basic @Raw
	public boolean isHasToRest() {
		return hasToRest;
	}

	/**
	 * @param hasToRest the hasToRest to set
	 */
	@Basic @Raw
	public void setHasToRest(boolean hasToRest) {
		this.hasToRest = hasToRest;
	}

	/**
	 * @return the timeToRest
	 */
	@Basic @Raw
	public double getTimeToRest() {
		return timeToRest;
	}

	/**
	 * @param timeToRest the timeToRest to set
	 */
	@Basic @Raw
	public void setTimeToRest(double timeToRest) {
		this.timeToRest = timeToRest;
	}

	/**
	 * @return the velocity
	 */
	@Basic @Raw
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	@Basic @Raw
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
}