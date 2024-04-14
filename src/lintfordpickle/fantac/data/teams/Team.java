package lintfordpickle.fantac.data.teams;

public class Team {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final int teamUid;
	public final int raceUid;

	public boolean isPlaying;

	public final boolean playerControlled;

	public float expandAgression = .5f;
	public float attackAgression = .5f;

	public boolean isExpanding;
	public boolean isAttacking;

	public int requestPopExpandSettlementUid;
	public int requestPopAttackSettlementUid;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	Team(int teamUid, int raceUid, boolean playerControlled) {
		this.teamUid = teamUid;
		this.raceUid = raceUid;
		this.playerControlled = playerControlled;

		isPlaying = true;

		requestPopExpandSettlementUid = -1;
		requestPopAttackSettlementUid = -1;
	}
}
