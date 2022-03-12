import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fire extends Acts{
    public Fire(String id, Point position, List<PImage> images, int act, int animate) {
        super(id, position, images, act, animate);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }

    public static void spreadFre(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Point p) {
        Fire f0= Factory.createFire(Functions.FIRE_KEY,p,imageStore.getImageList(Functions.FIRE_KEY),100,100);
        boolean canAdd = world.tryAddEntity(f0);
        if(canAdd)f0.scheduleActions(scheduler,world,imageStore);
    }
}