/*
 * Created on Dec 6, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.hmvc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @author SteveBaber
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ProgressDialog extends JDialog {

    private JProgressBar progressBar;
    private JLabel messageLabel;

    public static void main(String[] args)
    {
        ProgressDialog aProgressDialog = new ProgressDialog();

    }

    public ProgressDialog(JFrame parent, String title, String message, boolean modal)
    {
        super(parent, title, modal);
        setModal(true);
        setMessage(message);
        initialize();

    }

    public ProgressDialog()
    {
        initialize();
    }

    public void initialize()
    {
        setSize(350, 200);
        centerFrame();

        getContentPane().setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        getContentPane().add(getMessageLabel(), gridBagConstraints);
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.weightx = 1;
        gridBagConstraints_1.insets = new Insets(20, 30, 20, 30);
        gridBagConstraints_1.ipady = 10;
        gridBagConstraints_1.ipadx = 10;
        gridBagConstraints_1.fill = GridBagConstraints.BOTH;
        gridBagConstraints_1.gridy = 1;
        gridBagConstraints_1.gridx = 0;
        getContentPane().add(getProgressBar(), gridBagConstraints_1);
        setResizable(false);
    }

    protected JLabel getMessageLabel()
    {
        if (messageLabel == null)
        {
            messageLabel = new JLabel();
            messageLabel.setText("Working...");
        }
        return messageLabel;
    }

    protected JProgressBar getProgressBar()
    {
        if (progressBar == null)
        {
            progressBar = new JProgressBar();
        }
        return progressBar;
    }

    public void setMessage(final String message)
    {
        getMessageLabel().setText(message);
    }

    public void setMinimum(int min)
    {
        getProgressBar().setMinimum(min);
    }

    public void setMaximum(int max)
    {
        getProgressBar().setMaximum(max);
    }

    public void setValue(final int val)
    {
        getProgressBar().setValue(val);
    }

    public void centerFrame()
    {
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public void setIndeterminate(final boolean b)
    {
        getProgressBar().setIndeterminate(b);
    }
}
