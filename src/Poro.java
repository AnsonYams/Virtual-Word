import processing.core.PImage;

import java.util.List;

public class Poro extends Trackers{
    public Poro(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    @Override
    protected boolean _nextPosition(WorldModel world, Point p) {
        return world.withinBounds(p);
    }

    @Override
    protected boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }
}
