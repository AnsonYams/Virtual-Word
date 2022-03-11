import processing.core.PImage;

import java.util.List;

public abstract class Acts extends Animates{

        private int actionPeriod;

        public Acts(String n, Point p, List<PImage> i, int act, int animate){
                super(n,p,i,animate);
                actionPeriod = act;
        }

        @Override
        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this,world, imageStore),
                        getActionPeriod());
                super.scheduleActions(scheduler, world, imageStore);
        }

        public abstract void  executeActivity(WorldModel world, ImageStore imageStore,EventScheduler scheduler);

        protected int getActionPeriod(){return actionPeriod;}
}

