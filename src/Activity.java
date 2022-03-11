public class Activity implements Action{

    private WorldModel world;
    private ImageStore imageStore;
    private Acts entity;

    public Activity(Acts entity, WorldModel world, ImageStore imageStore){
        this.world = world;
        this.imageStore = imageStore;
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler){
        entity.executeActivity(world, imageStore, scheduler);
    }
}
