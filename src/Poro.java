import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Poro extends Trackers{
    public Poro(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        double treePercent = Math.random();
        Optional<Entity> goom =
                world.findNearest(getPosition(), new ArrayList<>(List.of(Goomab.class)));

        if (goom.isPresent()) {
            if (treePercent < 0.01) {
                Sapling sapling = Factory.createSapling("sapling_" + getId(), getPosition(),
                        imageStore.getImageList(Functions.getSaplingKey()));
                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
            if (moveTo(world, goom.get(), scheduler)) {
                world.removeEntity(goom.get());
                scheduler.unscheduleAllEvents(goom.get());
                System.out.println("e");
            }
        } else {
            Optional<Entity> home =
                    world.findNearest(getPosition(), new ArrayList<>(List.of(House.class)));

            moveTo(world, home.get(), scheduler);
        }

        scheduler.scheduleEvent( this,
                Factory.createActivityAction(this,world, imageStore),
                getActionPeriod());
    }

    @Override
    protected boolean _nextPosition(WorldModel world, Point p) {
        return world.withinBounds(p) && !world.isOccupied( p);
    }

    @Override
    protected boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return true;
    }
}
