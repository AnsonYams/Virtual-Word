import processing.core.PImage;
import java.util.*;

public class DudeNotFull extends Dude{
    private int resourceCount;

    public DudeNotFull(String id, Point position, List<PImage> images, int resourceCount, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images ,actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount >= getResourceLimit()) {
            DudeFull miner = Factory.createDudeFull(getId(),
                    getPosition(), getActionPeriod(),
                    getAnimationPeriod(),
                    getResourceLimit(),
                    getImages());
            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( miner);
            miner.scheduleActions(scheduler,world, imageStore);

            return true;
        }

        return false;
    }

    public boolean _moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
            this.resourceCount += 1;
            ((Plant)target).setHealth(-1);
            return true;
    }


    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));


        if (!target.isPresent() || !moveTo( world,
                (Plant) target.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
                    Factory.createActivityAction( this,world, imageStore),
                    getActionPeriod());
        }
    }
}

