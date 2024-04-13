package lintfordpickle.fantac.data;

public interface IGameStateListener {

	void onGameWon(int teamUid);

	void onGameLost(int teamUid);

}
