import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions
{
    private final Random rand = new Random();

    private static final int COLOR_MASK = 0xffffff;
    private static final int KEYED_IMAGE_MIN = 5;
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;

    private static final int PROPERTY_KEY = 0;

    private static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    private static final String SAPLING_KEY = "sapling";
    private static final String OBSTACLE_KEY = "obstacle";
    private static final String DUDE_KEY = "dude";
    private static final String HOUSE_KEY = "house";
    private static final String FAIRY_KEY = "fairy";
    private static final String STUMP_KEY = "stump";
    private static final String TREE_KEY = "tree";
    private static final String BGND_KEY = "background";
    public static final String FIRE_KEY = "fire";
    public static final String GHOST_KEY = "ghost";
    public static final String GOOMAB_KEY = "goomab";




    public static void loadImages(
            Scanner in, ImageStore imageStore, PApplet screen)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                processImageLine(imageStore.getImages(), in.nextLine(), screen);
            }
            catch (NumberFormatException e) {
                System.out.println(
                        String.format("Image format error on line %d",
                                      lineNumber));
            }
            lineNumber++;
        }
    }

    public static void processImageLine(
            Map<String, List<PImage>> images, String line, PApplet screen)
    {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    public static List<PImage> getImages(
            Map<String, List<PImage>> images, String key)
    {
        List<PImage> imgs = images.get(key);
        if (imgs == null) {
            imgs = new LinkedList<>();
            images.put(key, imgs);
        }
        return imgs;
    }

    /*    
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    public static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public static void load(
            Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                                                     lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                      e.getMessage()));
            }
            lineNumber++;
        }
    }

    public static boolean processLine(
            String line, WorldModel world, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, world, imageStore);
                case DUDE_KEY:
                    return parseDude(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, world, imageStore);
                case FAIRY_KEY:
                    return parseFairy(properties, world, imageStore);
                case HOUSE_KEY:
                    return parseHouse(properties, world, imageStore);
                case TREE_KEY:
                    return parseTree(properties, world, imageStore);
                case SAPLING_KEY:
                    return parseSapling(properties, world, imageStore);
                case FIRE_KEY:
                    return parseFire(properties, world, imageStore);
                case GHOST_KEY:
                    return parseGhost(properties,world, imageStore);
                case GOOMAB_KEY:
                    return parseGoomab(properties,world,imageStore);
            }
        }

        return false;
    }

    public static boolean parseFire(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == world.getBGND_NUM_PROPERTIES()) {
            String id = properties[world.getFIRE_KEY()];
            List<PImage> is = imageStore.getImageList(FIRE_KEY);
        }
        return properties.length == world.getBGND_NUM_PROPERTIES();
    }

    public static boolean parseGhost(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == world.getBGND_NUM_PROPERTIES()) {
            String id = properties[world.getGHOST_KEY()];
            List<PImage> is = imageStore.getImageList(GHOST_KEY);
        }
        return properties.length == world.getBGND_NUM_PROPERTIES();
    }

    public static boolean parseGoomab(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == world.getBGND_NUM_PROPERTIES()) {
            String id = properties[world.getGOOMAB_KEY()];
            List<PImage> is = imageStore.getImageList(GOOMAB_KEY);
        }
        return properties.length == world.getBGND_NUM_PROPERTIES();
    }

    public static boolean parseBackground(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getBGND_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getBGND_COL()]),
                                 Integer.parseInt(properties[world.getBGND_ROW()]));
            String id = properties[world.getBGND_ID()];
            world.setBackground( pt,
                          new Background(id, imageStore.getImageList( id)));
        }

        return properties.length == world.getBGND_NUM_PROPERTIES();
    }

    public static boolean parseSapling(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getSAPLING_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getSAPLING_COL()]),
                    Integer.parseInt(properties[world.getSAPLING_ROW()]));
            String id = properties[world.getSAPLING_ID()];
            int health = Integer.parseInt(properties[world.getSAPLING_HEALTH()]);
            Sapling entity = new Sapling( id, pt, imageStore.getImageList( SAPLING_KEY),
                    Factory.getSAPLING_ACTION_ANIMATION_PERIOD(), Factory.getSAPLING_ACTION_ANIMATION_PERIOD(), health, Factory.getSAPLING_HEALTH_LIMIT());
            world.tryAddEntity( entity);
        }

        return properties.length == world.getSAPLING_NUM_PROPERTIES();
    }

    public static boolean parseDude(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getDUDE_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getDUDE_COL()]),
                    Integer.parseInt(properties[world.getDUDE_ROW()]));
            DudeNotFull entity = Factory.createDudeNotFull(properties[world.getDUDE_ID()],
                    pt,
                    Integer.parseInt(properties[world.getDUDE_ACTION_PERIOD()]),
                    Integer.parseInt(properties[world.getDUDE_ANIMATION_PERIOD()]),
                    Integer.parseInt(properties[world.getDUDE_LIMIT()]),
                    imageStore.getImageList( DUDE_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == world.getDUDE_NUM_PROPERTIES();
    }

    public static boolean parseFairy(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getFAIRY_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getFAIRY_COL()]),
                    Integer.parseInt(properties[world.getFAIRY_ROW()]));
            Fairy entity = Factory.createFairy(properties[world.getFAIRY_ID()],
                    pt,
                    Integer.parseInt(properties[world.getFAIRY_ACTION_PERIOD()]),
                    Integer.parseInt(properties[world.getFAIRY_ANIMATION_PERIOD()]),
                    imageStore.getImageList( FAIRY_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == world.getFAIRY_NUM_PROPERTIES();
    }

    public static boolean parseTree(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getTREE_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getTREE_COL()]),
                    Integer.parseInt(properties[world.getTREE_ROW()]));
            Tree entity = Factory.createTree(properties[world.getTREE_ID()],
                                        pt,
                                        Integer.parseInt(properties[world.getTREE_ACTION_PERIOD()]),
                                        Integer.parseInt(properties[world.getTREE_ANIMATION_PERIOD()]),
                                         Integer.parseInt(properties[world.getTREE_HEALTH()]),
                                        imageStore.getImageList( TREE_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == world.getTREE_NUM_PROPERTIES();
    }

    public static boolean parseObstacle(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getOBSTACLE_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getOBSTACLE_COL()]),
                                 Integer.parseInt(properties[world.getOBSTACLE_ROW()]));
            Obstacle entity = Factory.createObstacle(properties[world.getOBSTACLE_ID()], pt,
                    Integer.parseInt(properties[world.getOBSTACLE_ANIMATION_PERIOD()]),
                                           imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == world.getOBSTACLE_NUM_PROPERTIES();
    }

    public static boolean parseHouse(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == world.getHOUSE_NUM_PROPERTIES()) {
            Point pt = new Point(Integer.parseInt(properties[world.getHOUSE_COL()]),
                                 Integer.parseInt(properties[world.getHOUSE_ROW()]));
            House entity = Factory.createHouse(properties[world.getHOUSE_ID()], pt,
                                             imageStore.getImageList(HOUSE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == world.getHOUSE_NUM_PROPERTIES();
    }
    public static String getSaplingKey(){
        return SAPLING_KEY;
    }

    public static String getStumpKey(){
        return STUMP_KEY;
    }

    public static String getTreeKey(){
        return TREE_KEY;
    }

    public static String getDudeKey(){return DUDE_KEY;}
}
