import processing.core.PImage;

import java.util.List;

public abstract class Animates extends Entity{

    private int animationPeriod;

    public Animates(String n, Point p, List<PImage> i, int a){
        super(n,p,i);
        animationPeriod = a;
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }

    public void nextImage() {
        setImageIndex( (getImageIndex() + 1) % getImages().size());
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this,0),
                getAnimationPeriod());
    }
}
