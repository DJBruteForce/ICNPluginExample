package com.ibm.igf.hmvc;

import java.awt.event.ActionEvent;

/**
 * Insert the type's description here. Creation date: (10/13/2000 5:56:37 PM)
 * 
 * @author: Steve Baber
 */
public class ActionThread extends Thread {
    private java.awt.event.ActionEvent event = null;

    private ActionThreadListener listener = null;

    /**
     * ActionThread constructor comment.
     */
    public ActionThread()
    {
        super();
    }

    /**
     * ActionThread constructor comment.
     */
    public ActionThread(ActionThreadListener listener, ActionEvent event)
    {
        super();
        setListener(listener);
        setEvent(event);
        if (listener != null)
            setName(listener.toString() + " : " + event.getActionCommand());
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param target
     *            java.lang.Runnable
     */
    public ActionThread(Runnable target)
    {
        super(target);
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param target
     *            java.lang.Runnable
     * @param name
     *            java.lang.String
     */
    public ActionThread(Runnable target, String name)
    {
        super(target, name);
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param name
     *            java.lang.String
     */
    public ActionThread(String name)
    {
        super(name);
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param target
     *            java.lang.Runnable
     */
    public ActionThread(ThreadGroup group, Runnable target)
    {
        super(group, target);
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param target
     *            java.lang.Runnable
     * @param name
     *            java.lang.String
     */
    public ActionThread(ThreadGroup group, Runnable target, String name)
    {
        super(group, target, name);
    }

    /**
     * ActionThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param name
     *            java.lang.String
     */
    public ActionThread(ThreadGroup group, String name)
    {
        super(group, name);
    }

    /**
     * Insert the method's description here. Creation date: (10/13/2000 5:57:56
     * PM)
     * 
     * @return java.awt.event.ActionEvent
     */
    public java.awt.event.ActionEvent getEvent()
    {
        return event;
    }

    /**
     * Insert the method's description here. Creation date: (10/13/2000 6:11:23
     * PM)
     * 
     * @return com.ibm.igf.nacontract.controller.ActionThreadListener
     */
    public ActionThreadListener getListener()
    {
        return listener;
    }

    /**
     * Insert the method's description here. Creation date: (10/13/2000 6:11:43
     * PM)
     */
    public void run()
    {
        // check the listener and event
        if (getListener() == null || getEvent() == null)
        {
            return;
        }

        ActionEvent evt = getEvent();

        // perform the action
        getListener().actionThreadPerformed(evt);
    }

    /**
     * Insert the method's description here. Creation date: (10/13/2000 5:57:56
     * PM)
     * 
     * @param newEvent
     *            java.awt.event.ActionEvent
     */
    public void setEvent(java.awt.event.ActionEvent newEvent)
    {
        event = newEvent;
    }

    /**
     * Insert the method's description here. Creation date: (10/13/2000 6:11:23
     * PM)
     * 
     * @param newListener
     *            com.ibm.igf.nacontract.controller.ActionThreadListener
     */
    public void setListener(ActionThreadListener newListener)
    {
        listener = newListener;
    }
}