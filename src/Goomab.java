import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Goomab extends Trackers{
    public Goomab(String id, Point position, List<PImage> images, int act, int animate) {
        super(id, position, images, act, animate);
    }

    @Override
    protected  boolean _nextPosition(WorldModel world, Point p){
        return world.withinBounds(p) &&
                (!world.isOccupied( p) ||  world.isOccupied( p) && ((Entity)(world.getOccupancyCell(p))).getClass() == Fire.class);
    }

    @Override
    protected boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return true;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> full =
                world.findNearest(getPosition(), new ArrayList<>(List.of(DudeFull.class)));
        if(full.isPresent())
        {
            DudeFull df = (DudeFull) full.get();
            if (moveTo(world, df, scheduler)) {
                df.transformGhost(world, scheduler, imageStore);
                world.removeEntity(df);
                scheduler.unscheduleAllEvents(df);
            }
        }

        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }
}

