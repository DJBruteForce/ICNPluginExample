define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
        "dojo/text!./templates/InvoiceSearchDialog.html",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "gilCnPluginDojo/GILDialog",],	
		function(lang, Action, Request,declare, BaseDialog, template,DataGrid,ItemFileWriteStore,dom,on,
				 array,NonModalConfirmDialog,DialogMessages,registry,GILDialog) {


	var InvoiceSearchDialog =   declare("gilCnPluginDojo.InvoiceSearch", [ BaseDialog,GILDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
		gridSupplier:null,
		callerForm: null,
		serviceParameters:null,
		grid:null,
		    
		constructor: function(args){
			
			lang.mixin(this, args);
		      
		},
	
		postCreate: function() {
			
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("GAPTS Invoice Search Results");
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("Select", "select", false, true);
			this.showActionBar(true);
			this.setSize(630, 360);
			this.setResizable(true);
			this.setMaximized(false);

		},
		
 
	
	cancel: function() {
		
		this.destroyRecursive();

	},
		
	select : function(message) {
		
		 	thisFormInvoiceSearch = this;
			var dialogMessages = new DialogMessages();
			var invoiceNumber;
			var invoiceDate;
			var customername;
	        var items = thisFormInvoiceSearch.grid.selection.getSelected();
	       
	       
	
	        dojo.forEach(items, function(selectedItem){
	        	
                if(selectedItem !== null) {
                	
                	dojo.forEach(thisFormInvoiceSearch.grid.store.getAttributes(selectedItem), function(attribute){

                        var value = thisFormInvoiceSearch.grid.store.getValues(selectedItem, attribute);
                        
                        if(attribute == 'invoiceNumber') invoiceNumber = value
                        if(attribute == 'invoiceDate')  invoiceDate = value
                        if(attribute == 'customerName') customername = value

                	})
                	
					var newInvoiceListItem = {  invoiceNumber:  invoiceNumber[0], 
						                		invoiceDate:invoiceDate[0], 
						                		fromDatabase:	false 
						                	};

                	thisFormInvoiceSearch.callerForm.invoiceListDataStore.newItem(newInvoiceListItem);

                }
	        })
              
	        
	        
	        thisFormInvoiceSearch.cancel();

		},
		
		showInvoiceSearchDataGrid: function() {
			
			var thisFormInvoiceSearch = this;
			
			var store = registry.byId(thisFormInvoiceSearch.callerForm.id+"_invoiceListGrid").store	
			var arrInvoicesList = [];
			
			store.fetch( {
			               onItem: function(item) {

			            		   
			            		   arrInvoicesList.push({
				            		   INVOICENUMBER: 	store.getValue( item, 'invoiceNumber' ),
				            		   INVOICEDATE: 	store.getValue( item, 'invoiceDate' ),
				            		   OBJECTID: 		store.getValue( item, 'objectId' ),
				            		   CONTRACTNUMBER: 	store.getValue( item, 'contractNumber' ),
				            		   COUNTRY: 		store.getValue( item, 'country' ),
				            	   });
			            	 

			               }
			});
			
			
			
			thisFormInvoiceSearch.serviceParameters['currentInvoicesList'] = JSON.stringify(arrInvoicesList);
			
			thisFormInvoiceSearch.serviceParameters['frontEndAction'] = 'searchByInvoice';
			thisFormInvoiceSearch.serviceParameters['offeringLetter'] = thisFormInvoiceSearch.callerForm.contractOfferingLetterNumber.get('value');
			
			
			Request.invokePluginService("GilCnPlugin", "InvoiceSearchService", {
	    		requestParams : thisFormInvoiceSearch.serviceParameters,
	    		requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			
	    			jsonInvoiceSearchGridValues	= JSON.parse(response.jsonInvoiceSearchGridValues);
	    			
	    		    var data = {
	    		    	      identifier: "id",
	    		    	      items: []
	    		    	    };
	    		    
				    		if (jsonInvoiceSearchGridValues.length<=0){
				    			
				    			var dialogMessages = new DialogMessages();
				    			dialogMessages.addWarningMessages("No Invoices Found");
				    			this.showMessage("Warning",dialogMessages.warningMessages());
				    			this.callerForm.enableClick(registry.byId(dojo.query('[id^="addGaptsInvoice"]')[0].id));
				    			  this.cancel();
				    			return;
				    		}
			    		    		

	    		    	    for(var i = 0, l = jsonInvoiceSearchGridValues.length; i < l; i++){
	    		    	    	data.items.push(lang.mixin({ id: i+1 }, jsonInvoiceSearchGridValues[i%l]));
	    		    	    }
	    		    	    var store = new ItemFileWriteStore({data: data});
	    		    	   
	    		    	    var layout = [[
	    			    		    	      {'name': 'Invoice Number#', 	'field': 'invoiceNumber', 	'width': 'auto'},
	    			    		    	      {'name': 'Invoice Date', 		'field': 'invoiceDate', 	'width': 'auto'},
	    			    		    	      {'name': 'Customer Name', 	'field': 'customerName', 	'width': '280px'},
	    		    	    ]];


	    		    	    var gridAux = new DataGrid({
	    		    	        store: store,
	    		    	        selectionMode:'multiple',
	    		    	        structure: layout,
	    		    	        autoHeight: true,
	    		    	        rowSelector: '1px',});
	    			
	    		    	    gridAux.placeAt("idSearchInvoice");
	    		    	    gridAux.startup();
	    			
	    		    	    this.show();
	    		    	    
	    		    	    this.grid = gridAux;
	    		    	    
	    		    	    this.callerForm.enableClick(registry.byId(dojo.query('[id^="addGaptsInvoice"]')[0].id));
	    		})
			});
			

		},
		
		
		showMessage: function(titl, msg) {	
			
			
            var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
            })
            

            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
			            	d1.destroyRecursive(); 
			            	
			            } }).placeAt(actionBar);
			        
			        new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            	} }).placeAt(actionBar);
       
            
	        d1.startup();
            d1.show();
			
		},
		
		

	});
	
	return InvoiceSearchDialog;
});
