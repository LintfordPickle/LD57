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
	private Team mPlayerTeam;

	public final List<Team> teams = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public Team playerTeam() {
		return mPlayerTeam;
	}

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
		setupNewGame();

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setupNewGame() {
		teams.clear();

		mPlayerTeam = null;

		final var lNewTeam = new Team(CONTROLLED_NONE, TeamRace.RACE_UNOCCUPIED, false);
		teams.add(lNewTeam);

		mTeamUidCounter = 1;
	}

	public Team addPlayerTeam(int raceUid) {
		if (mPlayerTeam != null)
			return mPlayerTeam; // only add one team

		mPlayerTeam = new Team(mTeamUidCounter++, raceUid, true);
		teams.add(mPlayerTeam);
		return mPlayerTeam;
	}

	public Team addComputerTeam(int raceUid) {
		final var lNewTeam = new Team(mTeamUidCounter++, raceUid, false);
		teams.add(lNewTeam);
		return lNewTeam;
	}

}
