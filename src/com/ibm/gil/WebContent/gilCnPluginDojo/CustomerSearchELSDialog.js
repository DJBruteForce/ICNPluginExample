define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request",
        "dojo/dom-class",
        "dojo/dom-style",
        "dojo/dom",
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
        "dojo/text!./templates/CustomerSearchELSDialog.html",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dojo/aspect",
	    "dojo/io-query",],	
		function(lang, Action, Request,domClass,domStyle,dom,declare, BaseDialog, template,DataGrid,ItemFileWriteStore,
				dom,on,array,NonModalConfirmDialog,DialogMessages,registry,aspect,ioquery) {


	return declare("gilCnPluginDojo.CustomerSearchELS", [ BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
		supplierToSearchStr:null,
		searchType:null,
		gridSupplier:null,
		callerForm: null,
		serviceParameters:null,
		grid:null,
		thisForm:null,
		    
			constructor: function(args){
			
				lang.mixin(this, args);
		      
		    },
	
		postCreate: function() {
			
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("Customer Search Results");
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("Select", "select", false, true);
			this.showActionBar(true);
			//this.setSize(1020, 350);
			this.setSize(1150,550)
			this.setResizable(true);
			this.setMaximized(false);
			dojo.style(this.contentArea, 'overflow', 'scroll'); 
			
			registry.byId(this.callerForm.id+'_instCustNum').set('_onChangeActive', false);
    		//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', false);
    		registry.byId(this.callerForm.id+'_cityS').set('_onChangeActive', false);
    		thisForm=this;

		},
		
 
	
    cancel: function() {
			
	 		registry.byId(this.callerForm.id+'_instCustNum').set('_onChangeActive', true);
	 		//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', true);
	 		registry.byId(this.callerForm.id+'_cityS').set('_onChangeActive', true);
	 		 		
			this.destroyRecursive();

		},
		
	select : function(message) {
		
			var dialogMessages = new DialogMessages();
	        var items = thisForm.grid.selection.getSelected();
	        
	        if(items.length) {

	    	     this.callerForm.instCustNum.set("value",		 items[0]['CUSTOMERNUMBER'][0]	 );
	    	     this.callerForm.custNameSearch.set("value",	 items[0]['CUSTOMERNAME'][0]	);
	    	     this.callerForm.custAdd.set("value", 	 items[0]['CUSTOMERADDRESS1'][0]	);
	    	     this.callerForm.custAdd2.set("value", 	 items[0]['CUSTOMERADDRESS2'][0]	);
	    	     this.callerForm.cityS.set("value", 	 items[0]['CUSTOMERADDRESS3'][0]	);
	    	     this.callerForm.postalCS.set("value", 	 items[0]['CUSTOMERADDRESS4'][0]	);
	    	     this.callerForm.statea6.set("value", 	 items[0]['CUSTOMERADDRESS5'][0]	);
	    	  
	    	      	     
	    	    /* if (this.callerForm.jsonInvoiceObject.VENDORCOMMISSION !=null && this.callerForm.jsonInvoiceObject.VENDORCOMMISSION!="")
	    	    	 this.callerForm.jsonInvoiceObject['VENDORCOMMISSION'] = this.callerForm.jsonInvoiceObject.VENDORCOMMISSION.replace(/,/g, '.');*/
	    	     
	    	    registry.byId(this.callerForm.id+'_instCustNum').set('_onChangeActive', true);
	 	 		//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', true);
	 	 		registry.byId(this.callerForm.id+'_cityS').set('_onChangeActive', true);
	 	 		
	 	 		thisForm.setFieldsNotEditable(true);
	    	     
	    	     	this.cancel();
	    	     	
	        } else {
	        	
	            	dialogMessages.addErrorMessages( "You must first select a Customer from the list.");
	            	dialogMessages.setHasErrorMessages(true);
		        	this.showMessage("Error",dialogMessages.getAllMessages());	
	        	
	        }
	        
	        

		},
		
		showCustomerSearchDataGrid: function() {
			
			var thisForm = this;
			
			debugger;
			if(this.searchType == 'OLCustomerNum') {
				 thisForm.serviceParameters['frontEndAction'] = 'getAllByCustomerNumber';
				 thisForm.serviceParameters['queryItem'] = this.callerForm.olCustNum.get('value');
				
			} else if(this.searchType == 'InstCustomerNum') {
				 thisForm.serviceParameters['frontEndAction'] = 'getAllByCustomerNumber';
				 thisForm.serviceParameters['queryItem'] = this.callerForm.instCustNum.get('value');
				
			} else if(this.searchType == 'CustName') {
				        thisForm.serviceParameters['frontEndAction'] = 'getAllByCustomerName';
						thisForm.serviceParameters['queryItem'] = this.callerForm.custNameSearch.get('value');
						thisForm.serviceParameters['city'] = this.callerForm.cityS.get('value');
				
			}
			
			
						
			
				Request.postPluginService("GilCnPlugin","CustomerSearchELSService",'application/x-www-form-urlencoded',{
					
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
	    			   			
	    			
	    			var dialogMessages = new DialogMessages();
	    			jsonCustomerSearchGridValues = JSON.parse(response.jsonCustomerSearchGridValues);
	    			
	    			if (jsonCustomerSearchGridValues.length== 0 && this.searchType == 'OLCustomerNum' ){
	    				 thisForm.callerForm.cleanOLCustomerValues();
	    				dialogMessages.addErrorMessages( "Customer not found.");
	    				dialogMessages.setHasErrorMessages(true);
	    				this.showMessage("Error",dialogMessages.getAllMessages());	
			        	this.destroy();
	    				return;
	    				}else if (jsonCustomerSearchGridValues.length== 0 ){
		    				this.resetCustomerFields();
		    				dialogMessages.addErrorMessages( "Customer not found.");
		    				dialogMessages.setHasErrorMessages(true);
		    				this.showMessage("Error",dialogMessages.getAllMessages());	
				        	this.destroy();
		    				return;
		    			}
	    			
	    			
	    			if (jsonCustomerSearchGridValues.length== 1 && this.searchType == 'OLCustomerNum' ){
	    				 
	    				
	    				 thisForm.callerForm.olCustNum.set("value",		jsonCustomerSearchGridValues[0].CUSTOMERNUMBER );
    		    	     thisForm.callerForm.custName.set("value",	jsonCustomerSearchGridValues[0].CUSTOMERNAME);
    		    	     thisForm.callerForm.olcustAdd.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS1);
    		    	     thisForm.callerForm.custAdd2.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS2);
    		    	     thisForm.callerForm.city.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS3);
    		    	     thisForm.callerForm.postalC.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS4);
    		    	     thisForm.callerForm.statea5.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS5);
    		    	     thisForm.callerForm.province.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS5);
    		    	     
//    		    	     registry.byId(this.callerForm.id+'_instCustNum').set('_onChangeActive', true);
    			 	 	//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', true);
//    			 	 	 registry.byId(this.callerForm.id+'_cityS').set('_onChangeActive', true);
    		    	      			  		
//    		    	     thisForm.setFieldsNotEditable(true);
    		    	     thisForm.callerForm.enableCustomerFieldsBasedOnOfferingNumber();
    		    	     
	    			}else if (jsonCustomerSearchGridValues.length== 1 )	{
	    				 
	    				
	    				 thisForm.callerForm.instCustNum.set("value",		jsonCustomerSearchGridValues[0].CUSTOMERNUMBER );
   		    	     thisForm.callerForm.custNameSearch.set("value",	jsonCustomerSearchGridValues[0].CUSTOMERNAME);
   		    	     thisForm.callerForm.custAdd.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS1);
   		    	     thisForm.callerForm.custAdd2.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS2);
   		    	     thisForm.callerForm.cityS.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS3);
   		    	     thisForm.callerForm.postalCS.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS4);
   		    	     thisForm.callerForm.statea6.set("value", 	jsonCustomerSearchGridValues[0].CUSTOMERADDRESS5);
   		    	     
   		    	     registry.byId(this.callerForm.id+'_instCustNum').set('_onChangeActive', true);
   			 	 	//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', true);
   			 	 	 registry.byId(this.callerForm.id+'_cityS').set('_onChangeActive', true);
   		    	      			  		
   		    	     thisForm.setFieldsNotEditable(true);
   		    	     
	    			}
	    			else if(this.searchType != 'OLCustomerNum'){
	    			
	    			
	    		    var data = {
	    		    	      identifier: "id",
	    		    	      items: []
	    		    	    };
                           
	    		    	    for(var i = 0, l = jsonCustomerSearchGridValues.length; i < l; i++){
	    		    	        data.items.push(lang.mixin({ id: i+1 }, jsonCustomerSearchGridValues[i%l]));
	    		    	    }
	    		    	    
	    		    	   
	    		    	    var store = new ItemFileWriteStore({data: data});
//	    		    	    store.data=data;
	    		    	   
	    		    	    var layout = [[	    		    	      
	    		    	      {'name': 'Customer Name', 	'field': 'CUSTOMERNAME', 		'width': '250px'},
	    		    	      {'name': 'Customer Number', 	'field': 'CUSTOMERNUMBER', 		'width': '100px'},
	    		     	      {'name': 'Addr1', 			'field': 'CUSTOMERADDRESS1', 	'width': '100px'},
	    		    	      {'name': 'Addr2', 			'field': 'CUSTOMERADDRESS2', 	'width': '250px'},
	    		    	      {'name': 'Addr3', 			'field': 'CUSTOMERADDRESS3', 	'width': '250px'},
	    		    	      {'name': 'Postal code', 		'field': 'CUSTOMERADDRESS4', 	'width': '150px',hidden: true},
	    		    	      {'name': 'State', 		    'field': 'CUSTOMERADDRESS5', 	'width': '150px',hidden: true},
	    		    	       
	    		    	    ]];
	    		    	    
	    		    	    
	    		    	     var grid1 = new DataGrid({
	    		    	        store: store,
	    		    	        selectionMode:'single',
	    		    	        structure: layout,
//	    		    	        autoHeight: true,	    		    	        
	    		    	        rowSelector: '1px',
	    		    	        });
	    		    	    
	    		    	    domStyle.set(this.id + '_idSearchCustomerELS', 'height', '350px');
		    		    	    
	    		    	    grid1.placeAt(thisForm.id+"_idSearchCustomerELS");
	    		    	    thisForm.grid = grid1;
	    		    	    grid1.on("RowDblClick", function(evt){
	    		    	    	
	    		    	     var idx = evt.rowIndex,
	    		    	     rowData = grid1.getItem(idx);
	    		    	     
	    		    	  
	    		    	     thisForm.callerForm.instCustNum.set("value",		rowData.CUSTOMERNUMBER[0] );
	    		    	     thisForm.callerForm.custNameSearch.set("value",	rowData.CUSTOMERNAME[0]);
	    		    	     thisForm.callerForm.custAdd.set("value", 	rowData.CUSTOMERADDRESS1[0]);
	    		    	     thisForm.callerForm.custAdd2.set("value", 	rowData.CUSTOMERADDRESS2[0]);
	    		    	     thisForm.callerForm.cityS.set("value", 	rowData.CUSTOMERADDRESS3[0]);
	    		    	     thisForm.callerForm.postalCS.set("value", 	rowData.CUSTOMERADDRESS4[0]);
	    		    	     thisForm.callerForm.statea6.set("value", 	rowData.CUSTOMERADDRESS5[0]);

	    		    	     
	    		    	    registry.byId(thisForm.callerForm.id+'_instCustNum').set('_onChangeActive', true);
	    		 	 		//registry.byId(this.callerForm.id+'_custNameSearch').set('_onChangeActive', true);
	    		 	 		registry.byId(thisForm.callerForm.id+'_cityS').set('_onChangeActive', true);
	    		    	    
	    		 	 		 thisForm.setFieldsNotEditable(true);
	    		 	 		
	    		    	     thisForm.cancel();

	    		    	    }, true);
	    		    	    
	    		    	    grid1.startup();
	    		    	    domClass.add(dijit.byId(thisForm.grid).domNode, 'customersGridR');
	    		    	    
	    			
	    			thisForm.show();
	    			thisForm.setLayout();	
	    			}
	    		
	    		})
			});
			
			
		},
		setLayout: function(){
			var restoreGrid=(restore)=>{
				if(restore!=null){
					thisForm.grid.resize();
					var th = thisForm.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'customerTableHeader'))
						domClass.add(th, 'customerTableHeader');
					var dojoGridScrollBoxx=thisForm.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					
					if(!domClass.contains(dijit.byId(thisForm.grid).domNode,'customersGridR')){
						domClass.add(dijit.byId(thisForm.grid).domNode, 'customersGridR');
						domClass.remove(dijit.byId(thisForm.grid).domNode, 'customersGridM');
						}
					var lIG= dojo.byId(thisForm.id+"_idSearchCustomerELS");
					if(!domClass.contains(lIG,'customerContentR') && domClass.contains(lIG,'customerContent') ){
						domClass.add(lIG, 'customerContentR');
						domClass.remove(lIG, 'customerContent');
					}else if(!domClass.contains(lIG,'customerContentR') && domClass.contains(lIG,'customerContentM') ){
						domClass.add(lIG, 'customerContentR');
						domClass.remove(lIG, 'customerContentM');
						
					}
					domStyle.set(this.id + '_idSearchCustomerELS', 'height', '350px');//205
					thisForm.grid.resize();
					thisForm.grid.render();
				
				}
			};
			var maximizeGrid=(maximize)=>{
				if(maximize!=null){
					thisForm.grid.resize();
					var th = thisForm.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'customerTableHeader'))
						domClass.add(th, 'customerrTableHeader');
					var dojoGridScrollBoxx=thisForm.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					
					if(!domClass.contains(dijit.byId(thisForm.grid).domNode,'customersGridM')){
						domClass.add(dijit.byId(thisForm.grid).domNode, 'customersGridM');
						domClass.remove(dijit.byId(thisForm.grid).domNode, 'customersGridR');
					}
					var lIG= dojo.byId(thisForm.id+"_idSearchCustomerELS");
					if(!domClass.contains(lIG,'customerContentM') && domClass.contains(lIG,'customerContent') ){
						domClass.add(lIG, 'customerContentM');
						domClass.remove(lIG, 'customerContent');
					}else if(!domClass.contains(lIG,'customerContentM') && domClass.contains(lIG,'customerContentR') ){
						domClass.add(lIG, 'customerContentM');
						domClass.remove(lIG, 'customerContentR');
						
					}
					domStyle.set(this.id + '_idSearchCustomerELS', 'height', '95%');//320
					thisForm.grid.resize();
					
				}
			};
			
			aspect.after(thisForm._maximizeButton, 'onClick', maximizeGrid, this._maximizeButton);
			aspect.after(thisForm._restoreButton, 'onClick', restoreGrid, this._restoreButton);
			
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
		
		setFieldsNotEditable :  function(editable){
			
						
			
			thisForm.callerForm.custAdd.set('disabled',editable);
			thisForm.callerForm.custAdd2.set('disabled',editable);
			thisForm.callerForm.cityS.set('disabled',editable);
			thisForm.callerForm.postalCS.set('disabled',editable);
			thisForm.callerForm.statea6.set('disabled',editable);
			
			
		},
		
		resetCustomerFields:function(){
		var thisForm = this;	
		thisForm.callerForm.cityS.set("value","");
		
		}
		
		

	});
});
