package com.ibm.igf.hmvc;

/**
 * Insert the type's description here. Creation date: (8/14/2001 2:38:39 PM)
 * 
 * @author: Steve Baber
 */
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ProgressMonitor;

public class StatementProgressThread extends Thread {
    private boolean statementExecuting = true;

    private int timeout = DB2ConnectionPool.DEFAULTTIMEOUT;

    private Statement stmt = null;

    private boolean isSleeping = false;

    /**
     * StatementProgressThread constructor comment.
     */
    public StatementProgressThread()
    {
        super("StatementProgressThread");
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param target
     *            java.lang.Runnable
     */
    public StatementProgressThread(Runnable target)
    {
        super(target);
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param target
     *            java.lang.Runnable
     * @param name
     *            java.lang.String
     */
    public StatementProgressThread(Runnable target, String name)
    {
        super(target, name);
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param name
     *            java.lang.String
     */
    public StatementProgressThread(String name)
    {
        super(name);
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param target
     *            java.lang.Runnable
     */
    public StatementProgressThread(ThreadGroup group, Runnable target)
    {
        super(group, target);
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param target
     *            java.lang.Runnable
     * @param name
     *            java.lang.String
     */
    public StatementProgressThread(ThreadGroup group, Runnable target, String name)
    {
        super(group, target, name);
    }

    /**
     * StatementProgressThread constructor comment.
     * 
     * @param group
     *            java.lang.ThreadGroup
     * @param name
     *            java.lang.String
     */
    public StatementProgressThread(ThreadGroup group, String name)
    {
        super(group, name);
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void monitor()
    {
        if (isAlive())
        {
            if (isSleeping)
            {
                statementExecuting = true;
                isSleeping = false;
                synchronized (this)
                {
                    notifyAll();
                }
            }
        } else
        {
            start();
        }
    }

    public void run()
    {
        try
        {
            while (true)
            {
                int timeout = getTimeout();
                ProgressMonitor pm = new ProgressMonitor(null, "Accessing Database", "Please wait...", 0, timeout/60);
                pm.setMillisToDecideToPopup(5000);
                pm.setMillisToPopup(5000);
                int i = 1;
                while (statementExecuting)
                {
                    try
                    {
                        pm.setProgress(i++);
                        if (pm.isCanceled())
                        {
                            stmt.cancel();
                            statementExecuting = false;
                            break;
                        }
                        sleep(1000);
                    } catch (InterruptedException exc)
                    {
                        break;
                    }
                }
                pm.close();
                synchronized (this)
                {
                    try
                    {
                        isSleeping = true;
                        wait();
                    } catch (Exception exc)
                    {
                    }
                }
            }
        } catch (SQLException exc)
        {
        }
    }

    public void setStatement(Statement aStmt)
    {
        stmt = aStmt;
    }

    public void setTimeout(int aTimeout)
    {
        timeout = aTimeout - 2;
    }

    public void statementCompleted()
    {
        statementExecuting = false;
        this.interrupt();
    }
}