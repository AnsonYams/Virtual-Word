import processing.core.PImage;

import java.sql.Array;
import java.util.*;

public class DudeFull extends Dude{

    public DudeFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        DudeNotFull miner = Factory.createDudeNotFull(getId(),
                getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getResourceLimit(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents( this);
        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean _moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
            return true;
    }


    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList((House.class))));

        if (fullTarget.isPresent() && moveTo( world,
                fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent( this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

}
