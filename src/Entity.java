import processing.core.PImage;

import java.util.*;

public abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */

    public Entity(String id, Point position, List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
        imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return this.getImages().get(imageIndex);
    }

    public String getId(){return id;};

    public Point getPosition(){return position;};

    public void setPosition(Point pos){position = pos;};

    public List<PImage> getImages(){return images;};

    public void setImageIndex(int i){imageIndex = i;}

    public int getImageIndex(){return imageIndex;}

}


