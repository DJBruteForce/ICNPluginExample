/*
 * Created on Aug 6, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.hmvc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Cryptography {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Cryptography.class);
	
    public class myObject implements Serializable {
        byte[] data = null;

        public myObject(byte[] data)
        {
            super();
            this.data = data;
        }
    }

    private static boolean cipherLoaded = false;

    /**
     * Cryptography constructor comment.
     */
    public Cryptography()
    {
        super();
    }

    /**
     * Insert the method's description here. Creation date: (4/13/2001 2:19:30
     * PM)
     * 
     * @param is
     *            java.io.InputStream
     * @param os
     *            java.io.OutputStream
     */
    public static void dcryptStream(ObjectInputStream is, OutputStream os, char[] passwordData)
    {
        String hint = null;
        SealedObject encryptedObject = null;
        Object obj = null;
        byte[] theData = null;
        try
        {
            // read the hint
            hint = (String) is.readObject();

            while (true)
            {
                try
                {
                    obj = is.readObject();
                } catch (EOFException exc)
                {
                    break;
                }
                if (obj == null)
                {
                    break;
                } else
                {
                    // decrypt the data
                    encryptedObject = (SealedObject) obj;
                    theData = (byte[]) encryptedObject.getObject(getKey(passwordData));

                    // write the data
                    os.write(theData);
                }
            }

        } catch (InvalidKeyException exc)
        {
            error("Password doesn't match.  Hint is [" + hint + "]");
        } catch (Exception exc)
        {
            error(exc.toString());
        }
    }

    /**
     * Insert the method's description here. Creation date: (4/13/2001 2:24:44
     * PM)
     * 
     * @param is
     *            java.io.InputStream
     * @param os
     *            java.io.ObjectOutputStream
     * @param passwordData
     *            char[]
     */
    public static void ecryptStream(InputStream is, ObjectOutputStream os, char[] passwordData, String hint)
    {
        int readsize;
        int blocksize = 102400;
        byte[] filedata = new byte[blocksize];
        byte[] tmpdata = null;
        SealedObject encryptedObject = null;

        try
        {
            // write the hint
            os.writeObject(hint);
            while (true)
            {
                // read a block of data
                readsize = is.read(filedata);

                // adjust the block data
                if (readsize <= 0)
                {
                    break;
                } else if (readsize < blocksize)
                {
                    tmpdata = new byte[readsize];
                    System.arraycopy(filedata, 0, tmpdata, 0, readsize);
                } else
                {
                    tmpdata = filedata;
                }
                // encrypt the data
                encryptedObject = encryption(passwordData, tmpdata);

                // write the data
                os.writeObject(encryptedObject);
            }
        } catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /**
     * Testcrypt constructor comment.
     */
    private static SealedObject encryption(char[] password, byte[] theData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException,
            InvalidAlgorithmParameterException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        if (!cipherLoaded)
        {
            try
            {
                Security.addProvider((Provider) (Class.forName("com.ibm.crypto.provider.IBMJCE").newInstance()));
                cipherLoaded = true;
            } catch (ClassNotFoundException exc)
            {
            }
            try
            {
                Security.addProvider((Provider) (Class.forName("com.sun.crypto.provider.SunJCE").newInstance()));
                cipherLoaded = true;
            } catch (ClassNotFoundException exc)
            {
            }
            if (!cipherLoaded)
            {
                throw (new NoSuchAlgorithmException("Could not load JCE providers"));
            }
        }

        /*
         * Create the cipher
         */
        Cipher desCipher;
        desCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, getKey(password));

        // Encrypt the cleartext
        SealedObject sealedObject = new SealedObject(theData, desCipher);

        return (sealedObject);
    }

    public static void error(String errorMsg)
    {
        try
        {
            javax.swing.JOptionPane.showMessageDialog(null, errorMsg, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc)
        {
        	logger.debug(errorMsg);
        }
    }

    /**
     * Insert the method's description here. Creation date: (2/28/00 1:34:57 PM)
     */
    private static SecretKey getKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        if (!cipherLoaded)
        {
            try
            {
                Security.addProvider((Provider) (Class.forName("com.ibm.crypto.provider.IBMJCE").newInstance()));
                cipherLoaded = true;
            } catch (ClassNotFoundException exc)
            {
            }
            try
            {
                Security.addProvider((Provider) (Class.forName("com.sun.crypto.provider.SunJCE").newInstance()));
                cipherLoaded = true;
            } catch (ClassNotFoundException exc)
            {
            }
            if (!cipherLoaded)
            {
                throw (new NoSuchAlgorithmException("Could not load JCE providers"));
            }
        }

        byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
        int count = 20;
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        return (pbeKey);
    }
}