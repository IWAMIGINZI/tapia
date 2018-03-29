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

    public static class Nicetomeetyou extends MySimpleAction{
        public Nicetomeetyou(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.NICETOMEETYOU);
        }
    }

    public static class Goodmorning extends MySimpleAction{
        public Goodmorning(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.GOODMORNING);
        }
    }

    public static class Hello extends MySimpleAction{
        public Hello(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.HELLO);
        }
    }

    public static class Goodevening extends MySimpleAction{
        public Goodevening(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.GOODEVENING);
        }
    }

    public static class Imhome extends MySimpleAction{
        public Imhome(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.IMHOME);
        }
    }

    public static class Goodnight extends MySimpleAction{
        public Goodnight(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.GOODNIGHT);
        }
    }

    public static class Support extends MySimpleAction{
        public Support(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.SUPPORT);
        }
    }

    public static class Division1 extends MySimpleAction{
        public Division1(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.DIVISION1);
        }
    }

    public static class Division2 extends MySimpleAction{
        public Division2(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.DIVISION2);
        }
    }

    public static class Division3 extends MySimpleAction{
        public Division3(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.DIVISION3);
        }
    }

    public static class Divisiontech extends MySimpleAction{
        public Divisiontech(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.DIVISIONTECH);
        }
    }

    public static class Kiban extends MySimpleAction{
        public Kiban(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KIBAN);
        }
    }

    public static class Tokatu extends MySimpleAction{
        public Tokatu(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.TOKATU);
        }
    }

    public static class Ict extends MySimpleAction{
        public Ict(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.ICT);
        }
    }

    public static class Eigyo1 extends MySimpleAction{
        public Eigyo1(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.EIGYO1);
        }
    }

    public static class Eigyo2 extends MySimpleAction{
        public Eigyo2(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.EIGYO2);
        }
    }

    public static class Eigyo3 extends MySimpleAction{
        public Eigyo3(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.EIGYO3);
        }
    }

    public static class Unyo1 extends MySimpleAction{
        public Unyo1(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.UNYO1);
        }
    }

    public static class Unyo2 extends MySimpleAction{
        public Unyo2(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.UNYO2);
        }
    }

    public static class Unyo3 extends MySimpleAction{
        public Unyo3(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.UNYO3);
        }
    }

    public static class Kaihatu1 extends MySimpleAction{
        public Kaihatu1(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KAIHATU1);
        }
    }

    public static class Kaihatu2 extends MySimpleAction{
        public Kaihatu2(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KAIHATU2);
        }
    }

    public static class Kaihatu3 extends MySimpleAction{
        public Kaihatu3(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KAIHATU3);
        }
    }

    public static class Kanri extends MySimpleAction{
        public Kanri(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.KANRI);
        }
    }

    public static class EnterRoom extends MySimpleAction{
        public EnterRoom(SimpleAction.OnSimpleActionListener simpleActionListener){
            super(simpleActionListener,MyActionType.ENTER_ROOM);
        }
    }

}




