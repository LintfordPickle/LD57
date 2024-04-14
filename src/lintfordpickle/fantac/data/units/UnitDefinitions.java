package lintfordpickle.fantac.data.units;

public class UnitDefinitions {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int UNIT_WORKER_UID = 1;
	public static final int UNIT_SOLDIER_UID = 2;

	// @formatter:off
	public static UnitDefinitions worker 	= new UnitDefinitions(UNIT_WORKER_UID, 		"Worker", 	1, 	1, 	1.f);
	public static UnitDefinitions soldier 	= new UnitDefinitions(UNIT_SOLDIER_UID, 	"Soldier", 	2, 	2, 	.5f);
	// @formatter:on

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final int uid;
	public final String name;
	public final int attack;
	public final int defense;
	public final float speed;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitDefinitions(int uid, String name, int attack, int defense, float speed) {
		this.uid = uid;
		this.name = name;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public static UnitDefinitions getUnitDefByUid(int uid) {
		switch (uid) {
		default:
		case UNIT_WORKER_UID:
			return worker;
		case UNIT_SOLDIER_UID:
			return soldier;
		}
	}

}
