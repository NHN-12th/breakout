package nhn.breakoutt.cannongame;

public enum ProjectTileType {
    NORMAL, // 일반 포탄 (검정, 1.0x 질량, 50 폭발반경)
    HEAVY, // 무거운 포탄 (회색, 2.0x 질량, 70 폭발반경)
    EXPLOSIVE, // 폭발 포탄 (빨강, 1.0x 질량, 100 폭발반경)
    SCATTER, // 산탄 (파랑, 0.8x 질량, 30 폭발반경)
    BOUNCY // 탄성 포탄 (초록, 1.0x 질량, 40 폭발반경)
}
