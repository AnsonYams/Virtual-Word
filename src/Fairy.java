import processing.core.PImage;
import java.util.*;

public class Fairy extends Trackers{


    public Fairy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected  boolean _nextPosition(WorldModel world, Point p){return world.withinBounds(p) && !world.isOccupied( p) || world.withinBounds(p) && world.isOccupied( p) && ((Entity)(world.getOccupancyCell(p))).getClass() == Fire.class;}


    protected boolean _moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents( target);
            return true;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo( world, fairyTarget.get(), scheduler)) {
                Sapling sapling = Factory.createSapling("sapling_" + getId(), tgtPos,
                        imageStore.getImageList(Functions.getSaplingKey()));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }
}
