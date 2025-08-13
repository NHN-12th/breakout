package nhn.breakoutt.breakout;

import java.util.List;
import nhn.breakoutt.*;

public interface Exploding extends Breakable {
    int getExplosionDamage();
    List<Breakable> explode(List<? extends Boundable> allObjects);
    ExplosionEffect createExplosionEffect();
}
