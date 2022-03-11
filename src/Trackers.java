import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Trackers extends Acts{

    public Trackers(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        //PathingStrategy strategy = new SingleStepPathingStrategy();
        PathingStrategy strategy = new AStarPathingStrategy();
        Point start = this.getPosition();
        Point end = destPos;
        Predicate<Point> canPassThrough = p -> _nextPosition(world,p);
        BiPredicate<Point, Point> withinReach = (p1,p2) -> adjacent(p1,p2);
        Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;
        List<Point> path = strategy.computePath(start, end, canPassThrough, withinReach, potentialNeighbors);
        Point newPos;
        if(path.isEmpty() || path.get(0) == null){
            newPos = this.getPosition();
        }
        else{
            newPos = path.get(0);
        }
        return newPos;
    }

    protected abstract boolean _nextPosition(WorldModel world, Point p);

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(getPosition(), target.getPosition())) {
            return _moveTo(world, target, scheduler);
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract boolean _moveTo(WorldModel world,
                                       Entity target,
                                       EventScheduler scheduler);
}
