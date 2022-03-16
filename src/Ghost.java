import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Ghost extends Trackers{

    public Ghost(String id, Point position, List<PImage> images, int act, int animate) {
        super(id, position, images, act, animate);
    }

    @Override
    protected boolean _nextPosition(WorldModel world, Point p) {
        return world.withinBounds(p) &&
                (!world.isOccupied( p) ||   world.isOccupied( p) && ((world.getOccupancyCell(p))).getClass() == Fire.class);
    }

    @Override
    protected boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return true;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Fairy.class)));

        if (target.isPresent()) {
            if (moveTo( world, target.get(), scheduler)) {
                DudeNotFull dude = Factory.createDudeNotFull("dude_" + getId(), this.getPosition(),787,100,world.getDUDE_LIMIT(),
                        imageStore.getImageList(Functions.getDudeKey()));
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                boolean canAdd = world.tryAddEntity(dude);
                if(canAdd){dude.scheduleActions(scheduler, world, imageStore);}
            }
        }

        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }
}
