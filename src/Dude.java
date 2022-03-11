import processing.core.PImage;
import java.util.List;
public abstract class Dude extends Trackers {
    private int resourceLimit;

    public Dude(String n, Point p, List<PImage> i, int act, int animate, int r) {
        super(n, p, i, act, animate);
        resourceLimit = r;
    }

    protected boolean _nextPosition(WorldModel world, Point p) {
        return world.withinBounds(p) &&
                (!world.isOccupied(p) || world.getOccupancyCell(p).getClass() == Stump.class || world.getOccupancyCell(p).getClass() == Fire.class);
    }

    protected boolean transformGhost(WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore) {
        Ghost ghost = Factory.createGhost(Functions.GHOST_KEY, this.getPosition(), imageStore.getImageList(Functions.GHOST_KEY), getActionPeriod(), getAnimationPeriod());
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(ghost);
        ghost.scheduleActions(scheduler, world, imageStore);
        return true;
    }

    public void transformGhost_helper(WorldModel world, boolean shouldRun) {

    }
    protected int getResourceLimit() {
        return resourceLimit;
    }
}