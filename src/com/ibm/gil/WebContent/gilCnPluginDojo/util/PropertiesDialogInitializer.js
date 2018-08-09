define(["dojo/_base/lang",
        "dojo/_base/declare",
        "dojo/aspect" ,	
        "dojo/dom-style",
        "ecm/model/Desktop",
        "dojo/query",
        "dijit/registry",
        "dojo/dom-attr",
        "gilCnPluginDojo/ConfirmationDialog",
        "dojo/on",
        "ecm/widget/dialog/ConfirmationDialog",
        "dijit/Tooltip",
        "gilCnPluginDojo/ConfirmDialog",
        "gilCnPluginDojo/NonModalConfirmDialog",
        "dojo/dom-class",
        "dojo/_base/connect", 
        "dojo/on",
        "gilCnPluginDojo/DialogMessages"     
        ],  
        
        function(lang, declare, aspect, domstyle, Desktop, query, 
        		registry, domAttr,ConfirmationDialog, on, ECMConfirmationDialog,Tooltip,
        		ConfirmDialog,NonModalConfirmDialog ,domClass,connect,on, DialogMessages){
	
	/**
	 * Class that extends ecm native library behaviour for GIL  
	 */
		return declare("gilCnPluginDojo.util.PropertiesDialogInitializer",[] , {
			
			
			hasGilGui: function (itemType) {
				
				return	((itemType.startsWith("Invoice") && itemType.length == 10)
					|| (itemType.startsWith("Misc Invoice") && itemType.length == itemType.length)
					|| (itemType.startsWith("Contract") && itemType.length == 11)
					|| (itemType.startsWith("Offering Letter") && itemType.length == 18)
					|| (itemType.endsWith("SignCOA") && itemType.length == 9)
					|| (itemType.endsWith("StampdutyOL") && itemType.length == 13)
					|| (itemType.endsWith("CountersignOL") && itemType.length == 15)
					|| (itemType.endsWith("SignOL") && itemType.length == 8)
					|| (itemType.valueOf() === "ROF Signed Offer Letter")
					|| (itemType.endsWith("Invoice") && itemType.length == 9)
					|| (itemType.endsWith("Misc") && itemType.length == 6))
			},
			
			validateMiscELS: function(){
		   		var ok = this._addedButtons[3];
		   		var olWidget = query('input[id^="' + this._itemEditPane._itemPropertiesPane._commonProperties.id + '_GCPS_OL_Number"]', 
		   				this._itemEditPane._itemPropertiesPane._commonProperties.domNode)[0];
		   		var coaWidget = query('input[id^="' + this._itemEditPane._itemPropertiesPane._commonProperties.id + '_GCPS_COA_Number"]', 
		   				this._itemEditPane._itemPropertiesPane._commonProperties.domNode)[0];
		   		var invWidget = query('input[id^="' + this._itemEditPane._itemPropertiesPane._commonProperties.id + '_Invoice_Number"]', 
		   				this._itemEditPane._itemPropertiesPane._commonProperties.domNode)[0];
		   		if(olWidget != undefined && coaWidget != undefined && invWidget != undefined){
			   		if(olWidget.value.trim() == "" && coaWidget.value.trim() == "" && invWidget.value.trim() == ""){ //if Misc is invalid
			   			ok.set('disabled', true);
			   			domClass.add('widget_' + olWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			domClass.add('widget_' + coaWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			domClass.add('widget_' + invWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			
			   			var message = "A Misc ELS document must be indexed against either a GCPS OL, GCPS COA or Invoice Number";
			   			var position = ['above'];
				   		var tol = new Tooltip({
				   	        connectId: 'widget_' + olWidget.id,
				   	        label: message,
				   	        position: position
				   	    });

				   	    var tcoa = new Tooltip({
				   	        connectId: 'widget_' + coaWidget.id,
				   	        label: message,
				   	        position: position
				   	    });

				   	    var tinv = new Tooltip({
				   	        connectId: 'widget_' + invWidget.id,
				   	        label: message,
				   	        position: position
				   	    });
				   	    
			   		} else {
		   				ok.set('disabled', false);
		   				domClass.remove('widget_' + olWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			domClass.remove('widget_' + coaWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			domClass.remove('widget_' + invWidget.id, 'dijitTextBoxError dijitValidationTextBoxError dijitError');
			   			
			   		}
		   		}
			},
			
			
			validateCountryCode: function(country){
				
				
				thisForm=this;
				var countryList=['AT','AU','BE','BR','CA','CH','CL','CN','CO','CZ','DE','DK','ES','FI','FR','GB','HK','HU','IE','IL','IT','KR','MX','MY','NL','NO','NZ','PE','PH','PL','PT','SE','SG','SI','SK','TH','ZA','XX'];
				var i ;
				var y= parseInt(1); 
				
				for (i in countryList) {
					
					if (countryList[i]== country){
						
						y++;
						break;
						//return true;
					} 	
					//return true;
				}
			 
			  var x=1 ;
			  var j=parseInt(i)+2;
			  
			  if(j<countryList.length)
				  {
				    return true; 
				  }
			   else if(j>countryList.length||country==''){  		 
	          
			   this.showMessage ("<img src='./plugin/GilCnPlugin/getResource/error.png'  style='width:15px;height:15px;'> &nbsp;"+"Error","Invalid Country Code -"+country,"onlyOk",null,null);   			   
			   return false;		
			  }
				
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
			        
			        new dijit.form.Button({"label": "No ",
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
				            	
				            	console.log("ok was clicked:");
				            	document.getElementById(d1.id).remove();
				            	d1.destroyRecursive();
				            	
				            } }).placeAt(actionBar);
	            	
	            	
	            }
	            
	            
		        d1.startup();
	            d1.show();
				
			},
			
			
			capLock:function (e, there){
				
				
				 kc = e.keyCode?e.keyCode:e.which;	
				 sk = e.shiftKey?e.shiftKey:((kc == 16)?true:false);
				 if(((kc >= 65 && kc <= 90) && !sk)||((kc >= 97 && kc <= 122) && sk))
					 if (dojo.byId(there.id+"_capLock") != null){
//					 dojo.place("<div id='"+there.id+"_capLock '>"+"Cap Locks activated!"+"</div><br>", dijit.byId(there.id+"_password").domNode, "before");
						 dojo.byId(there.id+"_capLock").style.visibility = 'visible';
					 }else{
						 //<div class="dijitTooltipContainer dijitTooltipContents" data-dojo-attach-point="containerNode" role="alert" align="left"></div>
//						 dojo.place("<div id='"+there.id+"_capLock'>"+"Cap Locks activated!"+"</div>", dijit.byId(there.id).domNode, "after");
//						 dojo.place("<div class='"+"dijitTooltipContainer dijitTooltipContents"+"data-dojo-attach-point='"+"'containerNode'"+" role='"+"'alert'"+" id='"+there.id+"_capLock'>"+"Cap Locks activated!"+"</div>", dijit.byId(there.id).domNode, "after");
						 dojo.place("<div class='"+"'dijitTooltipContainer dijitTooltipContents capsContainer' "+"data-dojo-attach-point='"+"'containerNode'"+" role='"+"'alert'"+" id='"+there.id+"_capLock'>"+"<img src='./plugin/GilCnPlugin/getResource/capsLock.png'  style='width:220px;height:34px;'>"+"</div>", dijit.byId(there.id).domNode, "after");
						 dojo.byId(there.id+"_capLock").style.visibility = 'visible';
					 }
				 else{
					 if (dojo.byId(there.id+"_capLock") != null){
						 dojo.byId(there.id+"_capLock").style.visibility = 'hidden';
					 //ecm_widget_layout_NavigatorMainLayout_0_LoginPane_LoginButton
					 var iD=there.id;
					 	dijit.byId(iD.substring(0,iD.length-8)+"LoginButton").style.margin='0px';
					 }
				 }
				},
		    run: function () {
		    	
		    	var that= this;
		    	
		    	aspect.after(ecm.widget.LoginPane.prototype,"_initializeForDesktop",function(){
		    		
//		    		dojo.connect(dojo.byId(this.id+"_password"),"keypress", function(evt){
//		    			
//		    				that.capLock(evt)
//		    		});
		    		on(dijit.byId(this.id+"_password"), "keypress", function(evt) {
					
						that.capLock(evt, this)
			    	

			     });
		    		
		    			  
		    			//this.id "ecm_widget_layout_NavigatorMainLayout_0_LoginPane"
		    			//widget_ecm_widget_layout_NavigatorMainLayout_0_LoginPane_password
		    		console.log("***************************great");
		    	});
		    	//_enableLogin
		    	//Uppercasing all field values after user clicks ok in attributes window before values are saved
			   	aspect.before(ecm.widget.dialog.EditPropertiesDialog.prototype, "onSave", function() {
					var itemType = this._itemEditPane._itemPropertiesPane._contentClass.name;
					if (that.hasGilGui(itemType)){
						var props = query('input[id^="'+ this._itemEditPane._itemPropertiesPane._commonProperties.id +'_"]');
						for(var i=0; i<props.length; i++){
							props[i].value = props[i].value.toUpperCase();
					  	};
					}
			   		
			   	});
			   	
			  
			   	
			   	
				
			   	
			   	// for misc invoice - validate the country code on the attributes screen and display the error
				aspect.around(ecm.widget.ItemEditPane.prototype, "save",  lang.hitch(this,function(originalSave){
   					return lang.hitch(function(arg1){
   						
   	   					var contentClass=this._itemPropertiesPane._contentClass.name;
   	   				

   	   				if(contentClass.startsWith("Misc Invoice")|| contentClass.endsWith("MISC Invoice Backup")){
   				   			var ccWidget=query('input[id^="'+this._itemPropertiesPane._commonProperties.id+'_Country_Code"]',
   				   					this._itemPropertiesPane._commonProperties.domNode)[0];
   				   			var validationCCode=that.validateCountryCode(ccWidget.value);
   				   			if(validationCCode==false){
   				   				console.log("inside validation code");
   				   				ccWidget.focus();   				   				
   				   				return;
   				   			}else{   				   				
   				   			    return (originalSave.apply(this,arguments));   				   			   
   				   			}
   				   		}else{ 
   				   			return originalSave.apply(this,arguments);			   			
   			   			}
   						
   					});

   				}));
			   	
			 
 	
			   	//Removing button Save and adding button OK and also enabling OK button
			   	// action even when no data was changed	
			   	aspect.after(ecm.widget.dialog.EditPropertiesDialog.prototype, "showProperties", function() {
			   		//if you want manipulate item data access this object this._item 
			   		if (!this._item.locked || (this._item.locked && Desktop.userId.toUpperCase()==this._item.lockedUser.toUpperCase()) ) {
			   			this._itemEditPane._isClassChanged = true; 
			   		}

			   		domstyle.set(this._saveButton.domNode, "display", "none");
			   		this.addButton("OK", "onSave", false, false, "SAVE");      
			   	});
			   	
			   	//disable OK button for Misc ELS when missing Invoice, COA and OL
			   	aspect.after(ecm.widget.dialog.EditPropertiesDialog.prototype, "_enableDisableSaveButton", function() {
			   		var buttons = this._addedButtons;
			   		var contentClass = this._itemEditPane._itemPropertiesPane._contentClass.name;
			   		if(buttons != undefined && buttons.length > 3){
				   		var ok = buttons[3];
				   		if(contentClass.length == 6 && contentClass.endsWith("Misc")){ // if it's Misc ELS
				   			lang.hitch(this, that.validateMiscELS)();
				   		} 
				   		// TODO Add Misc Invoice validation
//				   		else if(contentClass.startsWith("Misc Invoice") && contentClass.length == contentClass.length){ // if it's Misc Invoice
//				   			
//				   			
//				   		}
				   		else if (ok.disabled == true) { //  enable button again
				   			ok.set('disabled', false);
				   				
				   		}				   		

				   		// Hides security tab
		                this._itemEditPane._itemSecurityPane.controlButton.domNode.style.display = "none";
		                //Hides "edit notelog" button
		                if(!domClass.contains(this._itemEditPane._itemNotelogsPane._notelogEditButton.domNode, "displayNone"))
		                	domClass.add(this._itemEditPane._itemNotelogsPane._notelogEditButton.domNode, "displayNone")
				   	}
			   	});

			   	aspect.after(ecm.widget.ItemPropertiesPane.prototype, "onCompleteRendering", function() {						   	
			   		//Disable typing on date fields
			   		var timestamp = query('input[title=\"Timestamp\"]', this.domNode);
			   		var dates = query('input[title$=\"Date\"]', this.domNode);
			   		var tsList = query('div[widgetid*="_TIMESTAMP"]', this.domNode);
			   		var tsWidget = null;
			   		var source = query('div[widgetid*="SOURCE"]', this.domNode);
			   		var userId = query('div[widgetid*="USER_ID"]', this.domNode);

			   		if(tsList.length != 0)
			   			tsWidget = registry.byNode(tsList[0]);
			   		if(tsWidget != undefined){
			   			domstyle.set(tsWidget._buttonNode, "pointerEvents", "none");
			   			tsWidget.constraints.fullYear = true;
			   			tsWidget.set("disabled", true);
			   		}

			   		if(timestamp.length != 0){
			   			domAttr.set(timestamp[0], "disabled", "disabled");
			   		}

			   		if(dates.length != 0){
			   			dates.forEach((item, index) => {
			   				domAttr.set(item, "disabled", "disabled");
			   			});
			   		}

				   	// prevent user from editing SOURCE and USER ID
			   		if(source.length != 0){
			   			source = registry.byNode(source[0]);
			   			source.set('disabled', true);
			   		}
			   		if(userId.length != 0){
			   			userId = registry.byNode(userId[0]);
			   			userId.set('disabled', true);
			   		}
			   	});
			   	
			   	//fills TIMSTAMP, SOURCE and USER_ID automatically on "Add Document"
			   	aspect.after(ecm.widget.dialog.AddContentItemDialog.prototype, 
			   			"_onContentClassSelected", function() {
			   		var date = new Date();
			   		var propertiesPane = this.addContentItemPropertiesPane._commonProperties;
			   		var timestamp = query("div[widgetid^=" + propertiesPane.id +"_TIMESTAMP" + "]", 
			   				propertiesPane.domNode);
			   		var tsWidget;
			   		var source = query('div[widgetid*="SOURCE"]', propertiesPane.domNode);
			   		var userId = query('div[widgetid*="USER_ID"]', propertiesPane.domNode);
			   		var timestampText = query("input[id^=\"" + propertiesPane.id + "_TIMESTAMP\"]", 
			   				propertiesPane.domNode);

			   		if(timestamp.length != 0){
			   			tsWidget = registry.byNode(timestamp[0]);
			   			var dateString = date.toLocaleString();
			   			var sIndex = dateString.lastIndexOf(':');
			   			if(sIndex == -1)
			   				sIndex = dateString.lastIndexOf('.');
			   			tsWidget.textbox.value = dateString.substring(0, sIndex) + 
			   			dateString.substring(sIndex+3);
			   		}
			   		// TIMESTAMP
				   	if(timestampText.length != 0){
				   		domAttr.set(timestampText[0], "disabled", "disabled");
				   	}
				   	if(tsWidget != undefined){
				   		domstyle.set(tsWidget._buttonNode, "pointerEvents", "none");
				   		tsWidget.set("disabled", true);
				   	}

				   	// SOURCE and USER ID
			   		if(source.length != 0){
			   			source = registry.byNode(source[0]);
			   			source.set("value", "IMPORT");
			   			source.set("disabled", true);
			   		}
			   		if(userId.length != 0){
			   			userId = registry.byNode(userId[0]);
			   			userId.set("value", Desktop.userId.toUpperCase());
			   			userId.set("disabled", true);
			   		}



			   	});


			   	// Removes item types that was GIL GUI's from 'Add Document' dropdown
			   	// Sets default class if an item was added already
			   	ecm.widget.dialog.AddContentItemDialog.prototype.removeGilClassesAspectHandler = aspect.after(ecm.widget.AddContentItemPropertiesPane.prototype, 
			   			"_renderClassSelector", function(){
			   		var classSelector = this._contentClassSelector;
			   		
			   		if(this._classSelectorLoaded){
			   		
			   			classSelector.repository.retrieveContentClasses(classes => {
			   			
			   				var gilClasses = classes.filter(c => {
			   					return	that.hasGilGui(c.name);
			   				});
			   				classSelector.setExcludedItems(gilClasses.map(c => {return c.id;}));
					   		
			   			}, "createDocument");
			   			
			   		}
			   	});
			   	
			   	lang.extend(ecm.widget.dialog.AddContentItemDialog, {
			   		setDefaultContentClass: function(defaultContentClass){
			   			if(window.gilLastAddedClass){
				   			this._defaultContentClass = gilLastAddedClass;			   				
			   			} else {
			   				this._defaultContentClass = defaultContentClass;
			   			}
			   		}
			   	});
			   	
			   	lang.extend(ecm.widget.dialog.EmailDialog, {
			   		loadDefaults:function (callback) {
			            var repository = Desktop.getDefaultRepository();
			            if (repository) {
			                repository.getUserEmail(repository.userId, lang.hitch(this, function (email) {
			                    if (email) {
			                        this.fromInput.set("value", email);
			                    } else{
			                    	this.fromInput.set("value", "igfgiadm@us.ibm.com");
			                    }
			                    callback();
			                }));
			            } else {
			                callback();
			            }
			   		}
			   	});
			   	
			   	// Removes server dropdown and label from search screen
			   	aspect.before(ecm.widget.search.BasicSearchDefinition.prototype, 
			   			"startup", function(){
			   		var a = query('div[class="searchScopeFields"]', this.domNode)[0];
			   		a = a.children[0].children[0];
			   		domstyle.set(a.children[0].children[0], 'display', 'none');
			   		domstyle.set(a.children[0].children[1].children[1], 'display', 'none');
			   		domAttr.set(a.children[0].children[1], 'colspan', '2');
			   		domstyle.set(a.children[1].children[0], 'width', '83px');
			   		
			   	});
			   	
			   	// Hides "Search in" from saved searches.
			   	aspect.after(ecm.widget.search.SearchTab.prototype, 
			   			"_isSearchInCriteriaHidden", function(){
			   		domstyle.set(this._searchIn.domNode, 'display', 'none');   		
			   		
			   	});
			   	
			   	// Remove repository name from Add Document.
			   	aspect.after(ecm.widget.AddContentItemGeneralPane.prototype, 
			   			"createRendering", function(){
			   		var a = this.domNode.children[0].children[0].children[0].children[1];
			   		domstyle.set(a, 'display', 'none');   		
			   		
			   	});
			   	
			   	// Stores last added class by (Add Document)
			   	aspect.after(ecm.widget.dialog.AddContentItemDialog.prototype, 
			   			"onAddFinished", function() {
			   		gilLastAddedClass = this.addContentItemPropertiesPane._contentClass;

			   	});
			   	
			   	// Change "New Search" title
			   	aspect.after(ecm.widget.search.SearchBuilder.prototype, 'postCreate', function(){
			   		aspect.after(this.searchDefinition, 'onSearchCompleted', () => {
			   			if(this._gilBreadcrumbHandle)
			   				this._gilBreadcrumbHandle.remove();
				   		var bsd = this.searchDefinition;
				   		// if more than 2 content classes are selected, display only the first 2
				   		if(bsd.contentClass.contentClasses && bsd.contentClass.contentClasses.length > 2){
				   			var title = bsd.contentClass.contentClasses[0].name + ', ';
				   			title += bsd.contentClass.contentClasses[1].name + '...';
				   			this.set('title', title);
				   		} else {
				   			this.set('title', bsd.contentClass.name);
				   		}
				   		
				   		var bc = query('div[widgetid^="ecm_widget_Breadcrumb_', 
				   				this.searchResults.topContainer.domNode)[0];
				   		if(bc){
					   		var first = query('span[id^="ecm_widget_Breadcrumb_', 
					   				bc)[0];
					   		if(first)
					   			first.innerHTML = this.title;
				   			bc = registry.byNode(bc);
				   			var that = this;
				   			this._gilBreadcrumbHandle = aspect.after(this.searchResults, 'setResultSet', function(){
						   		var first = query('span[id^="ecm_widget_Breadcrumb_', 
						   				this.topContainer.domNode)[0];
						   		if(first){
						   			first.children[0].innerHTML = that.title;
						   		} else {
						   			first = query('div[widgetid^="ecm_widget_Breadcrumb_', 
							   			this.topContainer.domNode)[0];
							   		if(first.children[0].children.length != 0)
							   			first.children[0].children[0].children[1].children[0]
							   				.innerHTML = that.title;
						   		}
				   			});
				   		}
			   		});
			   	});
			   	
			   	// Opening a new dialog when user clicks on "Add" on Add Document function
			   	aspect.after(ecm.widget.layout.CommonActionsHandler.prototype, 'actionImport', function(){
			   		var dialog = this.addContentItemDialog;
			   		aspect.after(dialog, 'onAddFinished', function(){
			   			if(this._gilRepeatImportAction){
			   				//ecm.model.desktop.getActionsHandler().actionImport(this.repository);
			   				ecm.model.desktop.getLayout(function (layout) {
			   					layout.globalToolbar.toolbarButtons[0].onClick();
			   				});
			   			}
			   		});
			   		aspect.before(this.addContentItemDialog._addedButtons[0], 'onClick', function(){
			   			dialog._gilRepeatImportAction = true;
				   		
			   		})
			   		aspect.before(this.addContentItemDialog.cancelButton, 'onClick', function(){
			   			dialog._gilRepeatImportAction = false;
				   		
			   		})
			   	});
			   	
			   	// Removing confirmation when user clicks "Close All"
			   	lang.extend(ecm.widget.search.SearchTabContainer, {_showCloseConfirmationDialog: function (onExecute, text) {
			   		onExecute();
			   	}});

		    }

		});
});


