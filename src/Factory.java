import processing.core.PImage;

import java.util.List;

public class Factory {
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_HEALTH_LIMIT = 5;

    public static  House createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House(id, position, images);
    }

    public static Obstacle createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images,animationPeriod);
    }

    public static Tree createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, actionPeriod, animationPeriod, health, images);
    }

    public static Stump createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Sapling createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public static Fairy createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images, actionPeriod, animationPeriod);
    }

    // need resource count, though it always starts at 0
    public static DudeNotFull createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull( id, position, images, 0, resourceLimit, actionPeriod, animationPeriod);
    }

    // don't technically need resource count ... full
    public static DudeFull createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Fire createFire(String id, Point position, List<PImage> images,int actionPeriod,
                                  int animationPeriod)
    {
        return new Fire(id, position, images,actionPeriod,animationPeriod);
    }

    public static Ghost createGhost(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Ghost(id, position, images, actionPeriod, animationPeriod);
    }

    public static Goomab createGoomab(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Goomab(id, position, images, actionPeriod, animationPeriod);
    }

    public static Poro createFluffy(String id, Point point, List<PImage> images, int actionPeriod, int animationPeriod) {
        return new Poro(id, point, images,actionPeriod,animationPeriod);
    }

    public static Animation createAnimationAction(Animates entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public static Activity createActivityAction(
            Acts entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world,imageStore);
    }



    public static int getSAPLING_ACTION_ANIMATION_PERIOD(){return SAPLING_ACTION_ANIMATION_PERIOD;}
    public static int getSAPLING_HEALTH_LIMIT(){return SAPLING_ACTION_ANIMATION_PERIOD;}

}
