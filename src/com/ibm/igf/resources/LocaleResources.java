package com.ibm.igf.resources;

/**
 * This type was created in VisualAge.
 */
public class LocaleResources extends java.util.ListResourceBundle {
    static final Object[][] contents = { { "DB2DATEFORMAT", "yyyy-MM-dd" }, { "DB2DATEPATTERN", "####-##-###" }, { "DB2BLANKDATETEXT", "0001-01-01" }, { "DB2DEFAULTDATETEXT", "01-01-2001" }, { "DB2INSERTBLANKDATEFORMAT", "''yyyy-MM-dd''" },
            { "DB2INSERTBLANKDATETEXT", "'0001-01-01'" },

            { "DB2INSERTDATEFORMAT", "''MM/dd/yyyy''" }, { "DB2INSERTTIMEFORMAT", "''HH:mm:ss''" }, { "DB2INSERTTIMESTAMPFORMAT", "''yyyy-MM-dd HH:mm:ss.SSSSSS''" },

            { "CMDATEFORMAT", "yyyy-MM-dd" }, { "CMDATEPATTERN", "####-##-##" }, { "CMBLANKDATETEXT", "    -  - " }, { "CMDEFAULTDATETEXT", "2001-01-01" },

            { "GUIDATEFORMAT", "MM/dd/yyyy" }, { "GUIDATEPATTERN", "##/##/####" }, { "GUIBLANKDATETEXT", "  /  /    " }, { "GUIDEFAULTDATETEXT", "01/01/2001" },

            { "CMTIMESTAMPFORMAT", "yyyy-MM-dd-HH.mm.ss.SSSSSS" }, { "DEFAULTTIMESTAMPFORMAT", "yyyy-MM-dd HH:mm:ss.SSSSSS" }, { "DEFAULTTIMESTAMPPATTERN", "####-##-## ##:##:##.######" }, { "DEFAULTBLANKTIMESTAMP", "    -  -     :  :  .      " },
            { "DEFAULTTIMESTAMP", "2000-01-01 00:00:00.000000" },

            { "DEFAULTTIMEFORMAT", "HH:mm:ss" }, { "DEFAULTTIMEPATTERN", "##:##:##" }, { "DEFAULTBLANKTIME", "  :  :  " }, { "DEFAULTTIME", "00:00:00" },

            { "DECIMALSEPERATOR", "." }, { "DECIMALPOINTS", "2" }, { "DEFAULTAMOUNTTEXT", "0.00" }, { "DefaultFrameTitle", "GIL" }, { "Version", "5.001" },

    };

    /**
     * LocaleResources constructor comment.
     */
    public LocaleResources()
    {
        super();
    }

    /**
     * getContents method comment.
     */
    protected java.lang.Object[][] getContents()
    {
        return contents;
    }
}
