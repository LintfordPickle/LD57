package lintfordpickle.fantac.data;

public class Team {

	public static final int TEAM_NONE_UID = -1;

	public static final int TEAM_1_UID = 0;
	public static final int TEAM_2_UID = 1;
	public static final int TEAM_3_UID = 2;
	public static final int TEAM_4_UID = 3;

	public static final int RACE_HUMANS = 1;
	public static final int RACE_DEMONS = 2;

	public static final Team Team1 = new Team(TEAM_1_UID, RACE_HUMANS);
	public static final Team Team2 = new Team(TEAM_2_UID, RACE_DEMONS);

	public final int teamUid;
	public final int raceUid;

	private Team(int teamUid, int raceUid) {
		this.teamUid = teamUid;
		this.raceUid = raceUid;
	}

	public static Team getTeamByUid(int uid) {
		if (uid == TEAM_1_UID)
			return Team1;
		return Team2;
	}

}
