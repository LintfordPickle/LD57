package lintfordpickle.fantac.data.teams;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int CONTROLLED_NONE = 0;
	public static final int CONTROLLED_PLAYER = 1;
	public static final int CONTROLLED_AI = 2;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private int mTeamUidCounter;
	public final List<Team> teams = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public Team getTeamByUid(int uid) {
		final var lNumTeams = teams.size();
		for (int i = 0; i < lNumTeams; i++) {
			if (teams.get(i).teamUid == uid)
				return teams.get(i);
		}
		return null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public TeamManager() {
		startNewGame();

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void startNewGame() {
		teams.clear();

		final var lNewTeam = new Team(CONTROLLED_NONE, TeamRace.RACE_UNOCCUPIED, false);
		teams.add(lNewTeam);

		mTeamUidCounter = 1;
	}

	public Team addTeam(boolean playerControlled, int raceUid) {
		final var lNewTeam = new Team(mTeamUidCounter++, raceUid, playerControlled);
		teams.add(lNewTeam);
		return lNewTeam;
	}

}
