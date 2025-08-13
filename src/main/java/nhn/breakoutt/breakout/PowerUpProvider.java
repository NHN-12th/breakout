package nhn.breakoutt.breakout;

public interface PowerUpProvider extends Breakable {
    enum PowerUpType {
        WIDER_PADDLE("W", 10.0, "패들 확장"),
        MULTI_BALL("M", 0, "멀티볼"),
        EXTRA_LIFE("+1", 0, "생명 추가"),
        LASER("L", 10.0, "레이저 발사"),
        SLOW_BALL("S", 15.0, "공 감속"),
        STICKY_PADDLE("G", 10.0, "끈끈한 패들");

        private final String symbol;
        private final double duration;
        private final String description;

        PowerUpType(String symbol, double duration, String description) {
            this.symbol = symbol;
            this.duration = duration;
            this.description = description;
        }

        public String getSymbol() { return symbol; }
        public double getDuration() { return duration; }
        public String getDescription() { return description; }
    }

    boolean shouldDropPowerUp();
    PowerUpType getPowerUpType();
}
