package com.ibm.igf.hmvc;

/**
 * Insert the type's description here. Creation date: (6/14/2001 1:03:06 PM)
 * 
 * @author: Steve Baber
 */
import java.awt.Window;
import java.awt.event.WindowEvent;

public class ChildWindowEvent extends WindowEvent {
    public static final int WINDOW_SHOWN = 7 + WINDOW_FIRST;

    public static final int WINDOW_HIDDEN = 8 + WINDOW_FIRST;

    public ChildWindowEvent(Window source, int id)
    {
        super(source, id);
    }
}