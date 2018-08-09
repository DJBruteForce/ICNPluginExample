define(["dojo/_base/lang",
        "ecm/model/Action", 
        "dojo/dom-class",
        "dojo/dom-style",
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
        "dojo/text!./templates/SupplierSearchELSDialog.html",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dojo/date/locale",
	    "dojo/date",
	    "gilCnPluginDojo/GILDialog",
	    "gilCnPluginDojo/constants/ValidationConstants",
		 "dojo/aspect",
		 "dojo/io-query",],	
		function(lang, Action,domClass,domStyle, Request,declare, BaseDialog, template,DataGrid,ItemFileWriteStore,dom,on,array,NonModalConfirmDialog,DialogMessages,
				registry,locale,date,GILDialog,ValidationConstants, aspect,ioquery) {


	return declare("gilCnPluginDojo.SupplierSearchELS", [ BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
		supplierToSearchStr:null,
		searchType:null,
		gridSupplier:null,
		callerForm: null,
		jsonObject:null,
		serviceParameters:null,
		grid1:null,
		gilDatePattern:ValidationConstants("datePattern"),
		thisForm:null,
		    
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
			//this.setSize(1020, 350);
			//this.setSize(1050, 400);
			this.setSize(1150,500)
			this.setResizable(true);
			this.setMaximized(false);
			dojo.style(this.contentArea, 'overflow', 'scroll');
			this.initializeDateFields();
			this.upperCaseFields();
//			
			registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', false);
    		registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', false);
    		registry.byId(this.callerForm.id+'_legalId').set('_onChangeActive', false);
    		thisForm=this;
		},
		
 
	
		cancel: function() {
			debugger;
	 		registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', true);
	 		registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', true);
	 		registry.byId(this.callerForm.id+'_legalId').set('_onChangeActive', true);
//	 		
			thisForm.destroyRecursive();

		},
		
	select : function(message) {
		
			var dialogMessages = new DialogMessages();
	        var items = grid.selection.getSelected();
	        var validDate = new GILDialog();
	        if(items.length) {

	    	     this.callerForm.suppName.set("value",		 items[0]['supplierName'][0]	 );
	    	     this.callerForm.suppNumber.set("value",	 items[0]['supplierNumber'][0]	);
	    	     this.callerForm.legalId.set("value", 	 items[0]['legalIdNumber'][0]	);
	    	     this.callerForm.suppAddress.set("value", 	 items[0]['addr1'][0]	);
	    	     this.callerForm.suppAddress2.set("value", 	 items[0]['addr2'][0]	);
	    	     this.callerForm.suppAddress3.set("value", 	 items[0]['addr3'][0]	);
	    	     
	    	    /* var validate = new GILDialog();*/
	    	    // var sadate= this.parseDate(items[0]['sadate'][0],false);
	    	     var sadate = this.parseDateforSupplier(items[0]['sadate'][0],false);
	    	     console.log("sa date inside select before parse is :"+items[0]['sadate'][0]);
	    	     console.log("Gil pattern from ValidationConstants is :"+this.gilDatePattern);
	    	     console.log("sadate after parse inside select is : "+sadate);
	    	     //this.callerForm.saDate.set("value", sadate);
	    	     this.callerForm.saDate.set("value", items[0]['sadate'][0]);
	    	     this.callerForm.saNumber.set("value", 	 items[0]['sanumber'][0]	);
	    	     
	    	     
	    	     registry.byId(this.callerForm.id+'_supplierName').set('_onChangeActive', false);
		    	 registry.byId(this.callerForm.id+'_supplierNumber').set('_onChangeActive', false);
		    	 registry.byId(this.callerForm.id+'_legalId').set('_onChangeActive', false);
		    	 jsonInvoiceEls['vendorCommission'] = items[0]['commission'][0];  
		    	 console.log("Supplier Search - VendorCommission value: "+ jsonInvoiceEls.vendorCommission);
		     	 if (jsonInvoiceEls.vendorCommission !=null && jsonInvoiceEls.vendorCommission!="") {
	    	    	 
	    	    	 jsonInvoiceEls['vendorCommission'] = jsonInvoiceEls.vendorCommission.replace(/,/g, '.');
	    	     }
	    	      	     
	    	    /* if (this.callerForm.jsonInvoiceObject.vendorCommission !=null && this.callerForm.jsonInvoiceObject.vendorCommission!="")
	    	    	 this.callerForm.jsonInvoiceObject['vendorCommission'] = this.callerForm.jsonInvoiceObject.vendorCommission.replace(/,/g, '.');*/
	    	     
	    	     	thisForm.cancel();
	    	     	
	        } else {
	        	
	            	dialogMessages.addErrorMessages( "You must first select a Supplier from the list.");
	            	dialogMessages.setHasErrorMessages(true);
		        	thisForm.showMessage("Error",dialogMessages.getAllMessages());	
	        	
	        }
	        
	        

		},
		
		showSupplierSearchDataGrid: function() {
			
			var thisForm = this;
			
			
			if(this.searchType == 'supplierName') {
				 console.log("inside searchtype if : "+ this.searchType) ;
				 thisForm.serviceParameters['frontEndAction'] = 'getAllByVendorName';
				 thisForm.serviceParameters['queryItem'] = this.callerForm.suppName.get('value');
				
			} else if(this.searchType == 'legalId') {
				          console.log("inside searchtype else if: "+ this.searchType) ;
						thisForm.serviceParameters['frontEndAction'] = 'getAllByVat';
						thisForm.serviceParameters['queryItem'] = this.callerForm.legalId.get('value');
				
			}else {
				        console.log("inside searchtype else  "+ this.searchType) ;
						thisForm.serviceParameters['frontEndAction'] = 'getAllByVendorNumber';
						thisForm.serviceParameters['queryItem'] = this.callerForm.suppNumber.get('value');
				
			}
			console.log("Before invoke plugin call");
			
			Request.postPluginService("GilCnPlugin","VendorSearchELSService",'application/x-www-form-urlencoded',{
				
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			var dialogMessages = new DialogMessages();
	    			jsonVendorSearchGridValues	= JSON.parse(response.jsonVendorSearchGridValues);
	    			
	    			if (jsonVendorSearchGridValues.length== 0)
	    				{
	    				
	    				dialogMessages.addErrorMessages( "Supplier not found.");
		            	dialogMessages.setHasErrorMessages(true);
			        	thisForm.showMessage("Error",dialogMessages.getAllMessages());	
	    				thisForm.destroy();
	    				}
	    			
	    			console.log("jsonVendorSearchGridValues"+jsonVendorSearchGridValues);
	    			if(jsonVendorSearchGridValues.length== 1){
	    				 thisForm.callerForm.suppName.set("value",		jsonVendorSearchGridValues[0].supplierName );
    		    	     thisForm.callerForm.suppNumber.set("value",	jsonVendorSearchGridValues[0].supplierNumber);
    		    	     thisForm.callerForm.legalId.set("value", 	jsonVendorSearchGridValues[0].legalIdNumber);
    		    	     thisForm.callerForm.suppAddress.set("value", 	jsonVendorSearchGridValues[0].addr1);
    		    	     thisForm.callerForm.suppAddress2.set("value", 	jsonVendorSearchGridValues[0].addr2);
    		    	     thisForm.callerForm.suppAddress3.set("value", 	jsonVendorSearchGridValues[0].addr3);
    		    	     
    		    	     var sadate = this.parseDateforSupplier(jsonVendorSearchGridValues[0].sadate,false);
    		    	     console.log("sadate after parse is : "+sadate);
    		    	     
    		    	     
    		    	     console.log("sa date inside select before parse is :"+jsonVendorSearchGridValues[0].sadate);
    		    	    // thisForm.callerForm.saDate.set("value", sadate	);
    		    	     thisForm.callerForm.saDate.set("value", jsonVendorSearchGridValues[0].sadate);
    		    	     thisForm.callerForm.saNumber.set("value", 	jsonVendorSearchGridValues[0].sanumber);
    		    	     
    		    	     registry.byId(thisForm.callerForm.id+'_supplierName').set('_onChangeActive', false);
    			     	 registry.byId(thisForm.callerForm.id+'_supplierNumber').set('_onChangeActive', false);
    			     	 registry.byId(thisForm.callerForm.id+'_legalId').set('_onChangeActive', false);
    			     	 
    			     	 jsonInvoiceEls['vendorCommission'] = jsonVendorSearchGridValues[0].commission;
    			     	console.log("Supplier Search - VendorCommission value: "+ jsonInvoiceEls.vendorCommission);
    			     	 if (jsonInvoiceEls.vendorCommission !=null && jsonInvoiceEls.vendorCommission!="") {
    		    	    	 
    		    	    	 jsonInvoiceEls['vendorCommission'] =jsonInvoiceEls.vendorCommission.replace(/,/g, '.');
    		    	     }
    		    	     thisForm.destroy();
    		    	    

	    			}else{
	    				var data = {
		    		    	      identifier: "id",
		    		    	      items: []
		    		    	    };

		    		    	    for(var i = 0, l = jsonVendorSearchGridValues.length; i < l; i++){
		    		    	        data.items.push(lang.mixin({ id: i+1 }, jsonVendorSearchGridValues[i%l]));
		    		    	    }
		    		    	    var store = new ItemFileWriteStore({data: data});

		    		    	   
		    		    	    var layout = [[
		    		    	      {'name': 'Supplier Name', 	'field': 'supplierName', 	'width': '250px'},
		    		    	      {'name': 'Supplier#', 		'field': 'supplierNumber', 	'width': '70px'},
		    		    	      {'name': 'Block Ind.', 		'field': 'blockInd', 	    'width': '80px'},
		    		     	      {'name': 'Legal ID Number', 	'field': 'legalIdNumber', 	'width': '100px'},
		    		    	      {'name': 'Addr1', 			'field': 'addr1', 			'width': '100px'},
		    		    	      {'name': 'Addr2', 			'field': 'addr2', 			'width': '150px'},
		    		    	      {'name': 'Addr3', 			'field': 'addr3', 			'width': '300px'},
		    		    	      {'name': 'SADate', 		    'field': 'sadate', 		'width': '150px', hidden: true} ,
		    		    	      {'name': 'SRNumber', 			'field': 'sanumber', 		'width': '150px', hidden: true} 
		    		    	    ]];


		    		    	    grid = new DataGrid({
		    		    	        store: store,
		    		    	        selectionMode:'single',
//		    		    	        autoHeight: true,
		    		    	        structure: layout,
		    		    	        rowSelector: '1px',});
		    		    	 
		    		    	   
		    		    	    domStyle.set(this.id + '_idSearchVendorELS', 'height', '350px');
		    		    	    
		    		    	    grid.placeAt(thisForm.id+"_idSearchVendorELS");
		    		    	    thisForm.grid1=grid;
		    		    	    grid.on("RowDblClick", function(evt){
		    		    	    	
		    		    	     var idx = evt.rowIndex,
		    		    	     rowData = grid.getItem(idx);
		    		    	        
		    		    	     thisForm.callerForm.suppName.set("value",		rowData.supplierName[0] );
		    		    	     thisForm.callerForm.suppNumber.set("value",	rowData.supplierNumber[0]);
		    		    	     thisForm.callerForm.legalId.set("value", 	rowData.legalIdNumber[0]);
		    		    	     thisForm.callerForm.suppAddress.set("value", 	rowData.addr1[0]);
		    		    	     thisForm.callerForm.suppAddress2.set("value", 	rowData.addr2[0]);
		    		    	     thisForm.callerForm.suppAddress3.set("value", 	rowData.addr3[0]);
		    		    	    
		    		    	     var sadate = thisForm.parseDateforSupplier(rowData.sadate[0],false);
		    		    	     console.log("sadate after parse is : "+sadate);
		    		    	     
		    		    	     console.log("sa date inside select before parse is :"+rowData.sadate[0]);
		    		    	    
		    		    	     thisForm.callerForm.saDate.set("value", rowData.sadate[0]);
		    		    	     thisForm.callerForm.saNumber.set("value", 	rowData.sanumber[0]);
		    		    	    
		    		    	     jsonInvoiceEls['vendorCommission'] = rowData.commission[0];
		    		    	     console.log("Supplier Search - VendorCommission value: "+ jsonInvoiceEls.vendorCommission);
		    			     	 if (jsonInvoiceEls.vendorCommission !=null && jsonInvoiceEls.vendorCommission!="") {
		    		    	    	 
		    		    	    	 jsonInvoiceEls['vendorCommission'] = jsonInvoiceEls.vendorCommission.replace(/,/g, '.');
		    		    	     }
		    		    	     
		    		    	    
		    		    	     
		    		    	     thisForm.cancel();

		    		    	    }, true);
		    		    	    
	 		    		    	    
		    		    	    grid.startup();
		    		    	    domClass.add(dijit.byId(thisForm.grid1).domNode, 'suppliersGridR');
		    			
		    			thisForm.show();
		    			
		    			thisForm.setLayout();	
	    			}
	    		    	    		
	    		})
			});
			

		},
		
		setLayout: function(){
			var restoreGrid=(restore)=>{
				if(restore!=null){
					thisForm.grid1.resize();
					var th = thisForm.grid1.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'supplierTableHeader'))
						domClass.add(th, 'supplierTableHeader');
					var dojoGridScrollBoxx=thisForm.grid1.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					
					if(!domClass.contains(dijit.byId(thisForm.grid1).domNode,'suppliersGridR')){
						domClass.add(dijit.byId(thisForm.grid1).domNode, 'suppliersGridR');
						domClass.remove(dijit.byId(thisForm.grid1).domNode, 'suppliersGridM');
						}
					var lIG= dojo.byId(thisForm.id+"_idSearchVendorELS");
					if(!domClass.contains(lIG,'vendorContentR') && domClass.contains(lIG,'vendorContent') ){
						domClass.add(lIG, 'vendorContentR');
						domClass.remove(lIG, 'vendorContent');
					}else if(!domClass.contains(lIG,'vendorContentR') && domClass.contains(lIG,'vendorContentM') ){
						domClass.add(lIG, 'vendorContentR');
						domClass.remove(lIG, 'vendorContentM');
						
					}
					domStyle.set(this.id + '_idSearchVendorELS', 'height', '350px');//205
					thisForm.grid1.resize();
					thisForm.grid1.render();
				
				}
			};
			var maximizeGrid=(maximize)=>{
				if(maximize!=null){
					thisForm.grid1.resize();
					var th = thisForm.grid1.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'supplierTableHeader'))
						domClass.add(th, 'supplierTableHeader');
					var dojoGridScrollBoxx=thisForm.grid1.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					
					if(!domClass.contains(dijit.byId(thisForm.grid1).domNode,'suppliersGridM')){
						domClass.add(dijit.byId(thisForm.grid1).domNode, 'suppliersGridM');
						domClass.remove(dijit.byId(thisForm.grid1).domNode, 'suppliersGridR');
					}
					var lIG= dojo.byId(thisForm.id+"_idSearchVendorELS");
					if(!domClass.contains(lIG,'vendorContentM') && domClass.contains(lIG,'vendorContent') ){
						domClass.add(lIG, 'vendorContentM');
						domClass.remove(lIG, 'vendorContent');
					}else if(!domClass.contains(lIG,'vendorContentM') && domClass.contains(lIG,'vendorContentR') ){
						domClass.add(lIG, 'vendorContentM');
						domClass.remove(lIG, 'vendorContentR');
						
					}
					domStyle.set(this.id + '_idSearchVendorELS', 'height', '95%');//320
					thisForm.grid1.resize();
					
				}
			};
			
			aspect.after(thisForm._maximizeButton, 'onClick', maximizeGrid, this._maximizeButton);
			aspect.after(thisForm._restoreButton, 'onClick', restoreGrid, this._restoreButton);
			
		},
		 parseDateforSupplier: function(dateAux, isISO8601){
		    	
		    	if(isISO8601){
		    		
		    		return stamp.fromISOString(dateAux.toString()).toGMTString();
		    		
		    	} else {
		    	
		    		debugger;
		    		
		    		console.log("Date from back end:"+dateAux);
		    		
		    		var date= new Date(dateAux);
		    		
			    		var parsedDate = locale.format(date, {
			    			datePattern : "MM/dd/yyyy",
			    			selector: "date"
					});
				    
			    		console.log("Parsed Date is :"+ parsedDate);
			    		return parsedDate;
		    	}
		    },	
		
		    
			upperCaseFields: function() {
				var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
				var validate = new GILDialog();
				for(var i=0; i<allValTextbox.length; i++){
					validate.toUpperCase(allValTextbox[i]);
				}
			},
			
			

			initializeDateFields: function() {
						var allDtInputWid = dojo.query('input#[id*="Date"][id^="'+ this.id +'_"]');
						var validate = new GILDialog();
						for(var i=0; i<allDtInputWid.length; i++){
							validate.setDatePattern(registry.byId(allDtInputWid[i].id));
						}
			},
			    
			 /* parseDate: function(dateAux, isISO8601){
			    	
			    	if(isISO8601){
			    		
			    		return stamp.fromISOString(dateAux.toString()).toGMTString();
			    		
			    	} else {
			    	       console.log("inside parseDate else part:"+ dateAux);
			    	       var date= new Date(dateAux);
				    		var parsedDate = locale.parse(date.toString(), {
				    			formatLength: "short",
				    			selector: "date",
				    			 locale: "en-US",
				    			datePattern : "MM/dd/yyyy",
				    			//datePattern : this.gilDatePattern,
				    			
						});
					        console.log("ParsedDate inside ParseDate function:"+parsedDate);
				    		return parsedDate;
			    	}
			    },*/
			
			
		
		showMessage: function(titl, msg) {	
			
			
            var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:260px;",
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
});
