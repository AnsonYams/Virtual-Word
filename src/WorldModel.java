import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private final int numRows;
    private final int numCols;
    private final Background[][] background;
    private final Entity[][] occupancy;
    private final Set<Entity> entities;
    private final int SAPLING_NUM_PROPERTIES = 4;
    private final int SAPLING_ID = 1;
    private final int SAPLING_COL = 2;
    private final int SAPLING_ROW = 3;
    private final int SAPLING_HEALTH = 4;
    private final int TREE_ANIMATION_MAX = 600;
    private final int TREE_ANIMATION_MIN = 50;
    private final int TREE_ACTION_MAX = 1400;
    private final int TREE_ACTION_MIN = 1000;
    private final int TREE_HEALTH_MAX = 3;
    private final int TREE_HEALTH_MIN = 1;
    private final int OBSTACLE_NUM_PROPERTIES = 5;
    private final int OBSTACLE_ID = 1;
    private final int OBSTACLE_COL = 2;
    private final int OBSTACLE_ROW = 3;
    private final int OBSTACLE_ANIMATION_PERIOD = 4;
    private final int DUDE_NUM_PROPERTIES = 7;
    private final int DUDE_ID = 1;
    private final int DUDE_COL = 2;
    private final int DUDE_ROW = 3;
    private final int DUDE_LIMIT = 4;
    private final int DUDE_ACTION_PERIOD = 5;
    private final int DUDE_ANIMATION_PERIOD = 6;
    private final int HOUSE_NUM_PROPERTIES = 4;
    private final int HOUSE_ID = 1;
    private final int HOUSE_COL = 2;
    private final int HOUSE_ROW = 3;
    private final int FAIRY_NUM_PROPERTIES = 6;
    private final int FAIRY_ID = 1;
    private final int FAIRY_COL = 2;
    private final int FAIRY_ROW = 3;
    private final int FAIRY_ANIMATION_PERIOD = 4;
    private final int FAIRY_ACTION_PERIOD = 5;
    private final int TREE_NUM_PROPERTIES = 7;
    private final int TREE_ID = 1;
    private final int TREE_COL = 2;
    private final int TREE_ROW = 3;
    private final int TREE_ANIMATION_PERIOD = 4;
    private final int TREE_ACTION_PERIOD = 5;
    private final int TREE_HEALTH = 6;
    private final int BGND_NUM_PROPERTIES = 4;
    private final int BGND_ID = 1;
    private final int BGND_COL = 2;
    private final int BGND_ROW = 3;
    private final int FIRE_KEY = 1;
    private final int GHOST_KEY = 1;
    private final int GOOMAB_KEY =1;


    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0
                && pos.x < numCols;
    }

    /*
   Assumes that there is no entity currently occupying the
   intended destination cell.
*/
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public  Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public  void setOccupancyCell(
            Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }

    public  Background getBackgroundCell( Point pos) {
        return background[pos.y][pos.x];
    }

    public  void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell( pos));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public Optional<PImage> getBackgroundImage(
            Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(
            Point pos, Background background)
    {
        if (withinBounds(pos)) {
            setBackgroundCell( pos, background);
        }
    }

    public Optional<Entity> findNearest(
            Point pos, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }

    private Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }



    public void moveEntity( Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt( pos);
            setOccupancyCell( pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity( Entity entity) {
        removeEntityAt( entity.getPosition());
    }



    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell( pos) != null) {
            Entity entity = getOccupancyCell( pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell( pos, null);
        }
    }
    public boolean tryAddEntity(Entity entity) {
        if(entity.getClass() == Fire.class) {
            if (isOccupied(entity.getPosition())) {
                Entity e = getOccupancyCell(entity.getPosition());
                if (e.getClass() == Tree.class || e.getClass() == Sapling.class || e.getClass() == Stump.class) {
                    removeEntity(e);
                    addEntity(entity);
                    return true;
                }
                else{return false;}
            }
        }
        if(entity.getClass() == Goomab.class)
        {
            if (!isOccupied(entity.getPosition()))
            {
                addEntity(entity);
                return true;
            }
            else{return false;}
        }
        else {
            addEntity(entity);
            return true;
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public int getSAPLING_NUM_PROPERTIES() {
        return SAPLING_NUM_PROPERTIES;
    }

    public int getSAPLING_ID() {
        return SAPLING_ID;
    }

    public int getSAPLING_COL() {
        return SAPLING_COL;
    }

    public int getSAPLING_ROW() {
        return SAPLING_ROW;
    }

    public int getSAPLING_HEALTH() {
        return SAPLING_HEALTH;
    }

    public int getTREE_ANIMATION_MAX() {
        return TREE_ANIMATION_MAX;
    }

    public int getTREE_ANIMATION_MIN() {
        return TREE_ANIMATION_MIN;
    }

    public int getTREE_ACTION_MAX() {
        return TREE_ACTION_MAX;
    }

    public int getTREE_ACTION_MIN() {
        return TREE_ACTION_MIN;
    }

    public int getTREE_HEALTH_MAX() {
        return TREE_HEALTH_MAX;
    }

    public int getTREE_HEALTH_MIN() {
        return TREE_HEALTH_MIN;
    }

    public int getTREE_NUM_PROPERTIES() {
        return TREE_NUM_PROPERTIES;
    }

    public int getTREE_ID() {
        return TREE_ID;
    }

    public int getTREE_COL() {
        return TREE_COL;
    }

    public int getTREE_ROW() {
        return TREE_ROW;
    }

    public int getTREE_ANIMATION_PERIOD() {
        return TREE_ANIMATION_PERIOD;
    }

    public int getTREE_ACTION_PERIOD() {
        return TREE_ACTION_PERIOD;
    }

    public int getTREE_HEALTH() {
        return TREE_HEALTH;
    }

    public int getOBSTACLE_NUM_PROPERTIES() {
        return OBSTACLE_NUM_PROPERTIES;
    }

    public int getOBSTACLE_ID() {
        return OBSTACLE_ID;
    }

    public int getOBSTACLE_COL() {
        return OBSTACLE_COL;
    }

    public int getOBSTACLE_ROW() {
        return OBSTACLE_ROW;
    }

    public int getOBSTACLE_ANIMATION_PERIOD() {
        return OBSTACLE_ANIMATION_PERIOD;
    }

    public int getDUDE_NUM_PROPERTIES() {
        return DUDE_NUM_PROPERTIES;
    }

    public int getDUDE_ID() {
        return DUDE_ID;
    }

    public int getDUDE_COL() {
        return DUDE_COL;
    }

    public int getDUDE_ROW() {
        return DUDE_ROW;
    }

    public int getDUDE_LIMIT() {
        return DUDE_LIMIT;
    }

    public int getDUDE_ACTION_PERIOD() {
        return DUDE_ACTION_PERIOD;
    }

    public int getDUDE_ANIMATION_PERIOD() {
        return DUDE_ANIMATION_PERIOD;
    }

    public int getHOUSE_NUM_PROPERTIES() {
        return HOUSE_NUM_PROPERTIES;
    }

    public int getHOUSE_ID() {
        return HOUSE_ID;
    }

    public int getHOUSE_COL() {
        return HOUSE_COL;
    }

    public int getHOUSE_ROW() {
        return HOUSE_ROW;
    }

    public int getFAIRY_NUM_PROPERTIES() {
        return FAIRY_NUM_PROPERTIES;
    }

    public int getFAIRY_ID() {
        return FAIRY_ID;
    }

    public int getFAIRY_COL() {
        return FAIRY_COL;
    }

    public int getFAIRY_ROW() {
        return FAIRY_ROW;
    }

    public int getFAIRY_ANIMATION_PERIOD() {
        return FAIRY_ANIMATION_PERIOD;
    }

    public int getFAIRY_ACTION_PERIOD() {
        return FAIRY_ACTION_PERIOD;
    }

    public int getBGND_NUM_PROPERTIES() {
        return BGND_NUM_PROPERTIES;
    }

    public int getBGND_ID() {
        return BGND_ID;
    }

    public int getBGND_COL() {
        return BGND_COL;
    }

    public int getBGND_ROW() {
        return BGND_ROW;
    }

    public int getFIRE_KEY(){return FIRE_KEY;}

    public int getGHOST_KEY(){ return GHOST_KEY;}

    public int getGOOMAB_KEY(){return GOOMAB_KEY;}

}
