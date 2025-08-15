package nhn.breakoutt.breakout;

public enum GameState {
    MENU("메뉴"),
    PLAYING("게임 중"),
    PAUSED("일시정지"),
    BALL_LOST("공 잃음"),
    LEVEL_COMPLETE("레벨 완료"),
    GAME_OVER("게임 오버"),
    GAME_WON("게임 클리어");

    private final String description;

    GameState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
