public class Animation implements Action{

    private int repeatCount;
    private Animates entity;

    public Animation( Animates entity, int repeatCount){
        this.repeatCount = repeatCount;
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler){
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent( entity,
                    Factory.createAnimationAction(entity, repeatCount),
                    entity.getAnimationPeriod());
        }
    }
}
