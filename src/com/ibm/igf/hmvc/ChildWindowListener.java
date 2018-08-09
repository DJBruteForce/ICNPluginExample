package com.ibm.igf.hmvc;

/**
 * Insert the type's description here. Creation date: (6/14/2001 12:17:48 PM)
 * 
 * @author: Steve Baber
 */

public interface ChildWindowListener {
    public abstract void windowHidden(ChildWindowEvent evt);

    public abstract void windowShown(ChildWindowEvent evt);
}