package com.ibm.gil.business;


/*
 * CommentsDataModel -- Data model for the invoice comments
 */
public class InvoiceCommentsELS extends Indexing {
//    private static transient short FIELDCOUNT = IndexingDataModel.fieldCount;
//    private static final transient short COMMENTNUMBERidx = FIELDCOUNT++;
//    private static final transient short USERIDidx = FIELDCOUNT++;
//    private static final transient short CREATEDidx = FIELDCOUNT++;
//    private static final transient short COMMENTSidx = FIELDCOUNT++;

    public InvoiceCommentsELS()
    {
        
    }


    
    public String getCOMMENTNUMBER()
    {
        return this.COMMENTNUMBER;
    }
    private String COMMENTNUMBER="";
    public void setCOMMENTNUMBER(String COMMENTNUMBER)
    {
        this.COMMENTNUMBER=COMMENTNUMBER;
    }

    


    private String CREATED="";
    public String getCREATED()
    {
        return this.CREATED;
    }

    public void setCREATED(String CREATED)
    {
       this.CREATED=CREATED;
    }

    private String COMMENTS="";
    public String getCOMMENTS()
    {
        return this.COMMENTS;
    }

    public void setCOMMENTS(String COMMENTS)
    {
        this.COMMENTS=COMMENTS;
    }
}
