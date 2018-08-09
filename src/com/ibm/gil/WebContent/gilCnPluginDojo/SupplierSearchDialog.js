define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
        "dojo/text!./templates/SupplierSearchDialog.html",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dojo/io-query",
	    "gilCnPluginDojo/GILDialog",
	    ],	
		function(lang, Action, Request,declare, BaseDialog, template,DataGrid,ItemFileWriteStore,dom,
				on,array,NonModalConfirmDialog,DialogMessages,registry,ioquery,GILDialog) {

	var SupplierSearchDialog =  declare("gilCnPluginDojo.SupplierSearch", [ BaseDialog,GILDialog ], {

		contentString: template,
		widgetsInTemplate: true,
		supplierToSearchStr:null,
		searchType:null,
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
			this.setTitle("Supplier Search Results");
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("Select", "select", false, true);
			this.showActionBar(true);
			this.setSize(1050, 350);
			this.setResizable(true);
			this.setMaximized(false);
			//dojo.style(this.contentArea, 'overflow', 'scroll');
    		registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', false);
    		registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', false);
    		registry.byId(this.callerForm.id+'_accountNumber').set('_onChangeActive', false);
		},
		
 
	
	cancel: function() {
		
 		registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', true);
 		registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', true);
 		registry.byId(this.callerForm.id+'_accountNumber').set('_onChangeActive', true);
		this.destroyRecursive();
	},
		
	select : function(message) {
		
			var dialogMessages = new DialogMessages();
	        var items = this.grid.selection.getSelected();
	        
	        if(items.length) {

	    	     this.callerForm.supplierName.set("value",items[0]['supplierName'][0]);
	    	     this.callerForm.supplierNumber.set("value",items[0]['supplierNumber'][0]);
	    	     
	    	     if(this.callerForm.supplierNumber.value!='XXXXXX') {
	    	     
		    	     this.callerForm.accountNumber.set("value",items[0]['accountNumber'][0]);
		    	     this.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = (items[0]['commision'][0]);  	     
		    	     this.callerForm.jsonInvoiceObject['SRNUMBER'] = (items[0]['srnumber'][0]);
		    	     if (this.callerForm.jsonInvoiceObject.VENDORCOMMISSION !=null && this.callerForm.jsonInvoiceObject.VENDORCOMMISSION!="") {
		    	    	 
		    	    	 this.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = this.callerForm.jsonInvoiceObject.VENDORCOMMISSION.replace(/,/g, '.');
		    	     }
	    	     
	    	     }
	     		registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', true);
	    		registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', true);
	    		registry.byId(this.callerForm.id+'_accountNumber').set('_onChangeActive', true);
	    	    this.destroyRecursive();
	    	     	
	        } else {
	            	dialogMessages.addErrorMessages( "You must first select a Supplier from the list.");
	            	dialogMessages.setHasErrorMessages(true);
		        	this.showMessage("Error",dialogMessages.getAllMessages());	
	        }
		},
		
		showSupplierSearchDataGrid: function() {
			
			var thisForm = this;
			
			if(this.searchType == 'supplierName') {
				
				 thisForm.serviceParameters['frontEndAction'] = 'getAllByVendorName';
				 thisForm.serviceParameters['queryItem'] = this.callerForm.supplierName.get('value');
				
			} else if(this.searchType == 'accountNumber') {
				
						thisForm.serviceParameters['frontEndAction'] = 'getAllByAccount';
						thisForm.serviceParameters['queryItem'] = this.callerForm.accountNumber.get('value');
			} else {
						thisForm.serviceParameters['frontEndAction'] = 'getAllByVendorNumber';
						thisForm.serviceParameters['queryItem'] = this.callerForm.supplierNumber.get('value');
			}
			
			Request.postPluginService("GilCnPlugin", "VendorSearchService",'application/x-www-form-urlencoded', {
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
		    			
	    			jsonVendorSearchGridValues	= JSON.parse(response.jsonVendorSearchGridValues);
	    			
	    			if(jsonVendorSearchGridValues.length == 2) {
	    				
	   		    	     thisForm.callerForm.supplierName.set("value",	jsonVendorSearchGridValues[0].supplierName );
			    	     thisForm.callerForm.supplierNumber.set("value",	jsonVendorSearchGridValues[0].supplierNumber );
			    	     thisForm.callerForm.accountNumber.set("value", 	jsonVendorSearchGridValues[0].accountNumber );
			    	     thisForm.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = jsonVendorSearchGridValues[0].commision;
			    	     thisForm.callerForm.jsonInvoiceObject['SRNUMBER'] =jsonVendorSearchGridValues[0].srnumber;
		    	     
		    	     if (thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION !=null && thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION!="") {
		    	    	 
		    	    	 thisForm.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION.replace(/,/g, '.');
		    	     }
		    	     
		    	     thisForm.destroyRecursive();
		    	     
	    			} else {
	    			
		    		    var data = {
		    		    	      identifier: "id",
		    		    	      items: []
		    		    	    };

		    		    	    for(var i = 0, l = jsonVendorSearchGridValues.length; i < l; i++){
		    		    	    	data.items.push(lang.mixin({ id: i+1 }, jsonVendorSearchGridValues[i%l]));
		    		    	    }
		    		    	    var store = new ItemFileWriteStore({data: data});
		    		    	    store.data = data;
		    		    	    var layout = [[
		    			    		    	      {'name': 'Account#', 			'field': 'accountNumber', 	'width': '50px'},
		    			    		    	      {'name': 'Supplier Name', 	'field': 'supplierName', 	'width': '200px'},
		    			    		    	      {'name': 'Supplier#', 		'field': 'supplierNumber', 	'width': '70px'},
		    			    		    	      {'name': 'Block Ind.', 		'field': 'blockInd', 		'width': '80px'},
		    			    		    	      {'name': 'Legal ID Number', 	'field': 'legalIdNumber', 	'width': '100px'},
		    			    		    	      {'name': 'Addr1', 			'field': 'addr1', 			'width': '150px'},
		    			    		    	      {'name': 'Addr2', 			'field': 'addr2', 			'width': '115px'},
		    			    		    	      {'name': 'Addr3', 			'field': 'addr3', 			'width': '115px'},
		    			    		    	      {'name': 'Commission', 		'field': 'commision', 		'width': '150px', hidden: true} ,
		    			    		    	      {'name': 'SRNumber', 			'field': 'srnumber', 		'width': '150px', hidden: true} 
		    			    		    	    ]];

		    		    	    var grid1 = new DataGrid({
		    		    	        store: store,
		    		    	        selectionMode:'single',
		    		    	        structure: layout,
		    		    	        autoHeight: true,
		    		    	        rowSelector: '1px',});
		    		    	    
		    		    	    grid1.on("RowDblClick", function(evt){
		    		    	    	
			    		    	     var idx = evt.rowIndex,
			    		    	     rowData = grid1.getItem(idx);
			    		    	        
			    		    	     thisForm.callerForm.supplierName.set("value",		rowData.supplierName[0] );
			    		    	     thisForm.callerForm.supplierNumber.set("value",	rowData.supplierNumber[0]);
			    		    	     
			    		    	     if(thisForm.callerForm.supplierNumber.value!='XXXXXX') {
			    		    	    	 
				    		    	     thisForm.callerForm.accountNumber.set("value", 	rowData.accountNumber[0]);
				    		    	     thisForm.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = rowData.commision[0];
				    		    	     thisForm.callerForm.jsonInvoiceObject['SRNUMBER'] = rowData.srnumber[0];
				    		    	     
				    		    	     if (thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION !=null && thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION!="") {
				    		    	    	 
				    		    	    	 thisForm.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = thisForm.callerForm.jsonInvoiceObject.VENDORCOMMISSION.replace(/,/g, '.');
				    		    	     }
			    		    	    	 
			    		    	     }			    		    	     			    		    	     
			    			     	 registry.byId(thisForm.callerForm.id+'_supplierName').set('_onChangeActive', true);
			    			     	 registry.byId(thisForm.callerForm.id+'_supplierNumber').set('_onChangeActive', true);
			    			     	 registry.byId(thisForm.callerForm.id+'_accountNumber').set('_onChangeActive', true);
			    		    	     thisForm.destroyRecursive();

			    		    	    }, true);

		    		    	    grid1.placeAt("idSearchVendor");    
		    		    	    grid1.startup();
		    		    	    this.grid = grid1;
		    		    	    this.show();
	    			}
	    		
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
	return SupplierSearchDialog;		
});
