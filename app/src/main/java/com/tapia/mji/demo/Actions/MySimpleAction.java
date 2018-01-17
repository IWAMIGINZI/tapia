package com.tapia.mji.demo.Actions;

import com.tapia.mji.tapialib.Actions.SimpleAction;

/**
 * MySimpleAction class encapsulates a variety of simple actions as public inner classes.
 *
 * Many new simple actions can be handled by Tpaia by creating new inner classes here.
 *
 * Note: do not forget to add new actions to the enum class in MyAction.java
 *
 * Created by Sami on 21-Jul-16.
 */
public abstract class MySimpleAction extends MyAction {

    public SimpleAction.OnSimpleActionListener onSimpleActionListener;
    public MySimpleAction(SimpleAction.OnSimpleActionListener simpleActionListener, ActionType actionType){
        super(simpleActionListener);
        onSimpleActionListener = simpleActionListener;
        type = actionType;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////    All simple MyAction definition
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Sami on 12-Jul-16.
     */
    public static class Bye extends MySimpleAction {
        public Bye(SimpleAction.OnSimpleActionListener simpleActionListener) {
            super(simpleActionListener, MyActionType.BYE);
        }
    }

    public static class Kanda extends MySimpleAction{
        public Kanda(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KANDA);
        }
    }

    public static class Weather extends MySimpleAction{
        public Weather(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.WEATHER);
        }
    }

    public static class Day extends MySimpleAction{
        public Day(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.GIVE_DATE);
        }
    }

    public static class Time extends MySimpleAction{
        public Time(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.GIVE_TIME);
        }
    }

    public static class Move extends MySimpleAction{
        public Move(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.MOVE);
        }
    }

    public static class Week extends MySimpleAction{
        public Week(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.WEEK);
        }
    }

    public static class News extends MySimpleAction{
        public News(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.NEWS);
        }
    }

    public static class Camera extends MySimpleAction{
        public Camera(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.CAMERA);
        }
    }




}




