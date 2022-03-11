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

}