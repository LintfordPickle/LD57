package lintfordpickle.fantac.data.teams;

import net.lintfordlib.core.graphics.Color;
import net.lintfordlib.core.graphics.ColorConstants;

public class Team {

	private static final Color team0Color = new Color(ColorConstants.RED);
	private static final Color team1Color = new Color(ColorConstants.BLUE_SKY);
	private static final Color team2Color = new Color(ColorConstants.YELLOW);
	private static final Color team3Color = new Color(ColorConstants.GREEN);

	public static Color getTeamColor(int teamUid) {
		switch (teamUid) {
		default:
		case 0:
			return team0Color;
		case 1:
			return team1Color;
		case 2:
			return team2Color;
		case 3:
			return team3Color;
		}
	}

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
