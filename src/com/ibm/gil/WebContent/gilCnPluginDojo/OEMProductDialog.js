 define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request",
        "ecm/model/Desktop",
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog",
        "gilCnPluginDojo/GILDialog",
        "dojo/text!./templates/OEMProductDialog.html",
        "dojox/grid/DataGrid",
        "dojo/data/ItemFileWriteStore",
        "dojo/dom",
        "dojo/_base/array",
        "gilCnPluginDojo/util/ManageStyles",
        "gilCnPluginDojo/NonModalConfirmDialog",
        "gilCnPluginDojo/DialogMessages",
        "dijit/registry",
        "dijit/form/Button",
        "dojo/dom",
        "dojo/on",
        "dojo/_base/connect",
        "dijit/form/Form",
        "dojo/dom-style",
        "dojo/dom-class",
        "dojo/aspect",
        "dojo/query",
        "gilCnPluginDojo/DialogMessages",
        "gilCnPluginDojo/ELSDialog",
        "gilCnPluginDojo/util/JsonUtils",
        "dojo/json",
        "dijit/_WidgetsInTemplateMixin",
        "dojo/parser",
        "dijit/form/Button",
        "dojo/_base/connect",  
        "dojo/dom-class",
        "dojo/io-query",
        "dojox/grid/EnhancedGrid",
        "dojox/grid/enhanced/plugins/Pagination",        

        ],	
        function(lang, Action, Request, Desktop, declare, BaseDialog, GILDialog, template,
        		DataGrid, ItemFileWriteStore, dom, array,MStyles, NonModalConfirmDialog,
        		DialogMessages, registry, Button, dom, on, connect, Form,
        		domStyle, domClass, aspect, query, DialogMessages,ELSDialog,JsonUtils,json,_WidgetsInTemplateMixin,parser,Button,connect,domclass,ioquery,EnhancedGrid,Pagination){


	var OEMProductDialog= declare("gilCnPluginDojo.OEMProductDialog", [ BaseDialog, GILDialog,ELSDialog], {


		contentString: template,
		widgetsInTemplate: true,		
		callerForm: null,
		serviceParameters: null,
		grid: null,
		numComments: 0,
		cancelConfirm: null,
		confirmMessages: null,
		changed: false,
		jsonInvoiceObject : null,
		oemProductString  : null,
		store             :null,		
		dataCounter   : 0,
		counterCancel : 0,

		constructor: function(args){	
			lang.mixin(this, args);

		},

		postCreate: function() {
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("OEM Product Guide");
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("Select", "select", false, true);			
			this.showActionBar(true);
			this.setSize(1150, 700);
			//this.setSize(1500, 800);
			this.setResizable(false);
			//this.setMaximized(false);			
			this.index();
			this.initializeOEMProductGrid();
			//this.setLayout();

/***Commenting this, since it is not working properly for now. Need to fix.*/
			
			dojo.create("link", {href:'./plugin/GilCnPlugin/getResource/EnhancedGrid_rtl.css', type:'text/css', rel:'stylesheet'}, document.getElementsByTagName('head')[0]);
			dojo.create("link", {href:'./plugin/GilCnPlugin/getResource/EnhancedGrid.css', type:'text/css', rel:'stylesheet'}, document.getElementsByTagName('head')[0]);	
			dojo.create("link", {href:'./plugin/GilCnPlugin/getResource/dojo.css', type:'text/css', rel:'stylesheet'}, document.getElementsByTagName('head')[0]);
		
		},    	




		select : function(message) {

			var dialogMessages = new DialogMessages();
			var items = this.grid.selection.getSelected();
			var validDate = new GILDialog();
			
			this.changed = false;
			if(items.length) {

				this.callerForm.type.set("value",		 items[0]['Type'][0]	 );
				this.callerForm.model.set("value",	 items[0]['Model'][0]	);
				this.callerForm.desc.set("value", 	 items[0]['ProductDescription'][0]	);    	    
				this.cancel();

			} else {

				dialogMessages.addErrorMessages("You must first select a row from the list");
				dialogMessages.setHasErrorMessages(true);
				this.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);	

			}
	
		}, 	



		index: function() {

			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			thisForm = this;
			var jsonUtils = new JsonUtils();	
			var country = thisForm.serviceParameters['country'];
			this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
			
			thisForm.userId.set("value",""); 

			thisForm.show();			
			Request.postPluginService("GilCnPlugin", "OEMProductService", 'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
				requestCompleteCallback : lang.hitch(this, function (response) {

					this.oemProductString	= JSON.parse(response.oemProductJsonString);
					thisForm.show();
					thisForm.userId.set("value",""); 
					
					thisForm.equipmentType.set("options", this.oemProductString.equipmentTypeList);
					thisForm.equipmentType.set("value", jsonUtils.getDefaultOrSelected(this.oemProductString.equipmentTypeList,"value", true));

					thisForm.manufacturerName.set("options", this.oemProductString.manufacturersNameList);
					thisForm.manufacturerName.set("value", jsonUtils.getDefaultOrSelected(this.oemProductString.manufacturersNameList,"value", true));	    	
				})
			});	

		},


		initializeOEMProductGrid: function() {

			this.serviceParameters['frontEndAction'] = arguments.callee.nom;

			thisForm = this;
			var data = { identifier: 'OEMProduct', items: []};	

			var storeLength =0;			
			this.store = ItemFileWriteStore({data: data});
			this.store.clearOnClose=true;
			var layout = [[
			               {'name': 'Type', 	            'field': 'Type', 	            'width': '250px' },
			               {'name': 'Model', 	            'field': 'Model', 	            'width': '250px' },
			               {'name': 'Product Description', 	'field': 'ProductDescription', 	'width': '250px' },
			               {'name': 'Manufacturer Name', 	'field': 'ManufacturerName', 	'width': '270px' },
			               ]];	
			
			
			 this.grid = new dojox.grid.EnhancedGrid({
			        id: 'grid',
			        store: this.store,
			        structure: layout,
			        rowSelector: '20px',
			        plugins: {
			          pagination: {
			              pageSizes: ["25", "50", "100", "All"],
			              description: true,
			              sizeSwitch: true,
			              pageStepper: true,
			              gotoButton: true,
			                      /*page step to be displayed*/
			              maxPageStep: 4,
			                      /*position of the pagination bar*/
			              position: "bottom"
			          }
			        }
			    });

			this.grid.placeAt(thisForm.id+"_oemProductTable");			
			this.grid.startup();
			//domClass.add(dijit.byId(thisDialog.grid).domNode, 'oemGridR');
			
			
			
			dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
			thisForm.grid.update();
			this.setLayout();

          
			dojo.connect(dojo.byId(this.id+"_queryInfo"),"onclick", function(){				


				var checkedValue =   thisForm.ibmLenovoEquipment.get('value'); 			

				thisForm.oemProductString['IBMorLenovoEquip']   = checkedValue; 
				thisForm.oemProductString['ManufacturerName']   = thisForm.manufacturerName.get('value');     
				thisForm.oemProductString['GHZMHZ']   = thisForm.hertz.get('value');
				thisForm.oemProductString['INCH']   = thisForm.inch.get('value');  
				thisForm.oemProductString['GBMB']   = thisForm.memory.get('value');  
				thisForm.oemProductString['Processor']   = thisForm.processor.get('value');  
				thisForm.oemProductString['EquipmentType']   = thisForm.equipmentType.get('value');  



				thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.oemProductString);

				Request.postPluginService("GilCnPlugin", "OEMProductService",'application/x-www-form-urlencoded', {
					requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
					requestCompleteCallback : lang.hitch(this, function (response) {
						

						var dialogMessages = new DialogMessages();
						jsonOEMGridValues	= JSON.parse(response.jsonOEMGridValues);  				
						


						if (jsonOEMGridValues.length== 0)
						{

							dialogMessages.addErrorMessages( "Type/Model not found.");
							dialogMessages.setHasErrorMessages(true);
							thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);	
							thisForm.grid._refresh();

						}else {

							thisForm.changed = true;			
							thisForm.dataCounter++;				
							
							

							// the below piece of code is called when the user changes the options and clicks the query info button again
							thisForm.storeLength= thisForm.grid.store._arrayOfAllItems.length;

							if(thisForm.storeLength>0){
								
								thisForm.grid.store.fetch({
									onItem: function(it){										
										thisForm.grid.store.deleteItem(it);
									}
								});
								
								thisForm.grid.store.save();
								thisForm.grid.store.close();
								
							}

							thisForm.grid.store = new ItemFileWriteStore({data: data});


							for(var i = 0, l = jsonOEMGridValues.length; i < l; i++){			

								thisForm.grid.store.newItem({'OEMProduct': i+1 ,
									'Type': jsonOEMGridValues[i%l].Type,
									'Model': jsonOEMGridValues[i%l].Model,
									'ManufacturerName':  jsonOEMGridValues[i%l].ManufacturerName,
									'ProductDescription':  jsonOEMGridValues[i%l].ProductDescription,}); 

							}      	    

							thisForm.grid.store.save(); 
							thisForm.grid._refresh();							

							

							thisForm.grid.on("RowDblClick", function(evt){
								thisForm.changed = false;
								var idx = evt.rowIndex,
								rowData = thisForm.grid.getItem(idx);
								

								thisForm.callerForm.type.set("value",		rowData.Type[0] );
								thisForm.callerForm.model.set("value",	rowData.Model[0]);
								thisForm.callerForm.desc.set("value", 	rowData.ProductDescription[0]);				
															
								thisForm.cancel();

							}, true);


						}
					})
				});


			});


		},

		


		setSize:function (width, height) {
			if (!this._sizeToViewportRatio) {
				domStyle.set(this.domNode, {width:width + "px", height:height + "px",});
			}
			this.resize();
		},
		
		
		
		
		
		
		setLayout: function(){
			thisForm=this;
			var dojoxGridScrollbox=this.grid.viewsNode.children[0].children[2];
			domClass.add(dojoxGridScrollbox,"gridScrollBoxOEM")	;
		
			
			debugger;
			var restoreGrid=(restore)=>{
				if(restore!=null){
					thisForm.grid.resize();
				
					var th = thisForm.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'oemTableHeader'))
						domClass.add(th, 'oemTableHeader');
					var dojoGridScrollBox=thisForm.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBox,"gridScrollBoxOEM"))				
						domClass.add(dojoGridScrollBox,"gridScrollBoxOEM");					
					
					
					if(!domClass.contains(dijit.byId(thisForm.grid).domNode,'oemGridR')){
						domClass.add(dijit.byId(thisForm.grid).domNode, 'oemGridR');
						domClass.remove(dijit.byId(thisForm.grid).domNode, 'oemGridM');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();
						}
					var lIG= dojo.byId(thisForm.id+"_oemProductTable");
					if(!domClass.contains(lIG,'oemProductGridR') && domClass.contains(lIG,'oemProductGrid') ){
						domClass.add(lIG, 'oemProductGridR');
						domClass.remove(lIG, 'oemProductGrid');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();						
					}else if(!domClass.contains(lIG,'oemProductGridR') && domClass.contains(lIG,'oemProductGridM') ){
						domClass.add(lIG, 'oemProductGridR');
						domClass.remove(lIG, 'oemProductGridM');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();			
						
					}					
				
					thisForm.grid.resize();
					thisForm.grid.render();
					
					if(!domclass.contains(this.actionBar,"oemProductDialogPaneActionBar")){			
						domclass.add(this.actionBar,"oemProductDialogPaneActionBarR");
						domclass.remove(this.actionBar, 'oemProductDialogPaneActionBarM');
						domStyle.set(this.actionBar, "padding-right", "450px");
					}
					
				
				
				}
			};
			var maximizeGrid=(maximize)=>{
				if(maximize!=null){
					thisForm.grid.resize();
					
					var th = thisForm.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'oemTableHeader'))
						domClass.add(th, 'oemTableHeader');
					var dojoGridScrollBox=thisForm.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBox,"gridScrollBoxOEMM")){				
						domClass.add(dojoGridScrollBox,"gridScrollBoxOEMM");
					    domClass.remove(dojoGridScrollBox,"gridScrollBoxOEM");
					}
					
					
					
					if(!domClass.contains(dijit.byId(thisForm.grid).domNode,'oemGridM')){
						domClass.add(dijit.byId(thisForm.grid).domNode, 'oemGridM');
						domClass.remove(dijit.byId(thisForm.grid).domNode, 'oemGridR');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();
						
					}
					 var lIG= dojo.byId(thisForm.id+"_oemProductTable");
					if(!domClass.contains(lIG,'oemProductGridM') && domClass.contains(lIG,'oemProductGrid') ){
						domClass.add(lIG, 'oemProductGridM');
						domClass.remove(lIG, 'oemProductGrid');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();						
					}else if(!domClass.contains(lIG,'oemProductGridM') && domClass.contains(lIG,'oemProductGridR') ){
					
						domClass.add(lIG, 'oemProductGridM');						
						domClass.remove(lIG, 'oemProductGridR');
						dojo.contentBox(thisForm.grid.domNode, {w:1000, h:350});
						thisForm.grid.update();
					}
					
					
					if(!domclass.contains(this.actionBar,"oemProductDialogPaneActionBarM")){
						domclass.add(this.actionBar,"oemProductDialogPaneActionBarM");
						domclass.remove(this.actionBar, 'oemProductDialogPaneActionBarR');
						domStyle.set(this.actionBar, "padding-right", "15px");
					}
					
					
				}
			};
			
			aspect.after(thisForm._maximizeButton, 'onClick', maximizeGrid, this._maximizeButton);
			aspect.after(thisForm._restoreButton, 'onClick', restoreGrid, this._restoreButton);
			domStyle.set(this.actionBar, "padding-right", "450px");
			
			
		},
		
		

		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	

			thisForm = this;
			var d1 = new NonModalConfirmDialog({
				title: titl, 
				style: "min-width:350px;",
				content:msg,
				name:"dialogMessage"
			})

			if(needConfirmation == true){

				var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
				var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);

				new dijit.form.Button({ "label": "Yes",
					onClick: function(){

						callbackYes();
						d1.destroyRecursive(); 

					}}).placeAt(actionBar);

				new dijit.form.Button({"label": "No",
					onClick: function(){

						callbackNo();
						d1.destroyRecursive(); 

					}}).placeAt(actionBar);

			} else if(needConfirmation == false) {

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
			} else if(needConfirmation == "onlyOk"){

				console.log("inside only ok block :");
				var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
				var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);

				new dijit.form.Button({"label": "Ok",
					onClick: function(){

						callbackYes();
						console.log("ok was clicked:");
						document.getElementById(d1.id).remove();
						d1.destroyRecursive();

					} }).placeAt(actionBar);


			}


			d1.startup();
			d1.show();

		},


		cancel : function() {
			
			if(this.changed){
				dialogMsgs=new DialogMessages();
				dialogMsgs.addConfirmMessages("You have not selected a Type/Model. Are you sure you want to quit?");
				this.changed = false ;
				this.showMessage("Confirm",dialogMsgs.confirmMessages(), true, this.choiceYCancel,this.choiceNCancel);

			}else {
				thisForm.destroyRecursive();
			}

		},
		choiceYCancel:function(){

			try { 
				
				thisForm.destroyRecursive();
			
			}catch(e){
				console.log(e);
			}
		},
		choiceNCancel:function(){
			//nothing to do
			this.counterCancel=0;
		},





	});
	return OEMProductDialog;
});







