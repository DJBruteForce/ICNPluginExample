package com.ibm.igf.hmvc;

/**
 * Insert the type's description here. Creation date: (10/25/2001 1:17:08 PM)
 * 
 * @author: Steve Baber
 */
import java.util.ArrayList;

import javax.swing.JComponent;

public class ViewMapping extends ArrayList {
    /**
     * PipelineTargetViewMapping constructor comment.
     */
    public ViewMapping()
    {
        super();
    }

    /**
     * Insert the method's description here. Creation date: (8/14/2002 5:20:27
     * PM)
     * 
     * @return javax.swing.JComponent
     * @param idx
     *            int
     */
    public JComponent getComponent(int idx)
    {

        return (JComponent) get(idx);
    }

    /**
     * Insert the method's description here. Creation date: (8/14/2002 5:18:30
     * PM)
     * 
     * @param idx
     *            int
     * @param obj
     *            javax.swing.JComponent
     */
    public void setComponent(int idx, JComponent obj)
    {
        while (idx >= size())
            add(null);

        super.set(idx, obj);
    }
}