package nhn.breakoutt.breakout;

public interface MultiHit extends Breakable {
    enum DamageState {
        PERFECT,    // 완전한 상태
        DAMAGED,    // 손상된 상태
        CRITICAL    // 심각한 손상 상태
    }
    void updateVisualDamage();
}
