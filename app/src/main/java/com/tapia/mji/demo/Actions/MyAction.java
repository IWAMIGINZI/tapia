package com.tapia.mji.demo.Actions;

import com.tapia.mji.tapialib.Actions.Action;

/**
 * Abstract class MyAction encapsulates data and methods mostly to facilitate
 * the NLU's handling of user commands.
 *
 * To add actions to Tapia, extend abstract class MyAction.
 *
 * Note: before to add your MyActionType to the enum class here when adding an action to Tapia.
 *
 * Created by Sami on 07-Jul-16.
 */
public abstract class MyAction extends Action {
    /**
     * Constructor
     *
     * @param onActionListener the listener object listening for calls to perform the action.
     */
    protected MyAction(OnActionListener onActionListener){
        super(onActionListener);
    }

    /**
     * Enum for entity type.
     */
    enum MyEntity{
        LOCATION,
        AGE,
        DURATION,
        DATETIME,
        CONTACT,
        ORDINAL,//first , second third etc
        NUMBER,
        DISTANCE,
        TEMPERATURE,
    }

    /**
     * Enum for action type. All new actions must be added to this enum class.
     */
    public enum MyActionType implements ActionType {
        CONVERSATION,

        ROTATE,
        GIVE_TIME,
        GIVE_DATE,
        WEATHER,
        WEEK,
        NEWS,
        CAMERA,
        NICETOMEETYOU,
        GOODMORNING,
        HELLO,
        GOODEVENING,
        IMHOME,
        GOODNIGHT,
        SUPPORT,
        DIVISION1,
        DIVISION2,
        DIVISION3,
        DIVISIONTECH,
        KIBAN,
        TOKATU,
        ICT,
        EIGYO1,
        EIGYO2,
        EIGYO3,
        UNYO1,
        UNYO2,
        UNYO3,
        KAIHATU1,
        KAIHATU2,
        KAIHATU3,
        KANRI,
        FORTUNE,
    }
}
