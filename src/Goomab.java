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
                !world.isOccupied( p) || (world.withinBounds(p) && world.isOccupied( p) && ((Entity)(world.getOccupancyCell(p))).getClass() == Fire.class);
    }

    @Override
    protected boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return true;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFull =
                world.findNearest(getPosition(), new ArrayList<>(List.of(DudeNotFull.class)));
        Optional<Entity> full =
                world.findNearest(getPosition(), new ArrayList<>(List.of(DudeFull.class)));
        if(notFull.isPresent() && full.isPresent()) {
            if (world.distanceSquared(getPosition(), notFull.get().getPosition()) > world.distanceSquared(getPosition(), full.get().getPosition())) {
                if (moveTo(world, full.get(), scheduler)) {
                    ((Dude) full.get()).transformGhost(world, scheduler, imageStore);
                    world.removeEntity(full.get());
                    scheduler.unscheduleAllEvents(full.get());
                }
            } else {
                Point tgtPos = notFull.get().getPosition();
                if (moveTo(world, notFull.get(), scheduler)) {

                    ((Dude) notFull.get()).transformGhost(world, scheduler, imageStore);
                    world.removeEntity(notFull.get());
                    scheduler.unscheduleAllEvents(notFull.get());
                }
            }
        }
        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }
}

