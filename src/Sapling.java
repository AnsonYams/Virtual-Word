import processing.core.PImage;
import java.util.*;

public class Sapling extends Plant{

        private int healthLimit;

    public Sapling(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health);
        this.healthLimit = healthLimit;
    }

    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (super.transformPlant(world, scheduler, imageStore) ){
            return true;
        }
        else if (getHealth() >= healthLimit)
        {
            Tree tree = Factory.createTree("tree_" + getId(),
                    getPosition(),
                    getNumFromRange(world.getTREE_ACTION_MAX(), world.getTREE_ACTION_MIN()),
                    getNumFromRange(world.getTREE_ANIMATION_MAX(), world.getTREE_ANIMATION_MIN()),
                    getNumFromRange(world.getTREE_HEALTH_MAX(), world.getTREE_HEALTH_MIN()),
                    imageStore.getImageList( Functions.getTreeKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max - min);
    }

    public void executeActivity(
        WorldModel world,
        ImageStore imageStore,
        EventScheduler scheduler)
    {
        setHealth(1);
        super.executeActivity(world, imageStore, scheduler);
    }

}
