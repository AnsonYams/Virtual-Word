import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Acts{

    private int health;

    public Plant(String n, Point p, List<PImage> i, int act, int animate, int health){
        super(n,p,i,act,animate);
        this.health = health;
    }

    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (getHealth() <= 0) {
            Stump stump = Factory.createStump(getId(),
                    getPosition(),
                    imageStore.getImageList( Functions.getStumpKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this,
                    Factory.createActivityAction( this, world, imageStore),
                    getActionPeriod());
        }
    }

    protected int getHealth() {
        return health;
    }

    protected void setHealth(int d){health += d;};
}
