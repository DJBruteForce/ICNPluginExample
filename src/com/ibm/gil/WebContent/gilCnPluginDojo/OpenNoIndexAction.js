define([
	'dojo/_base/declare', 
	'dojo/_base/lang', 
	'ecm/model/Action', 
	'ecm/model/Request', 
	'gilCnPluginDojo/MessagesDialog', 
	'gilCnPluginDojo/PopupDialog',
	'ecm/model/Desktop',
	'gilCnPluginDojo/GILInitializerForm',
	'gilCnPluginDojo/GILInitializerFields',
	'gilCnPluginDojo/util/StringFormatterUtils',
	'dojo/_base/json' ,
	'gilCnPluginDojo/OfferingLetterDialog',
	'gilCnPluginDojo/InvoiceELSDialog',
	'gilCnPluginDojo/constants/ValidationConstants',
	'dojo/date',
	'dojo/date/locale',
	'dojo/date/stamp',
	'ecm/widget/dialog/EditPropertiesDialog',
	'dojo/aspect',
	'dojo/dom-style',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojo/dom-class',
	'dijit/form/Button',
	'gilCnPluginDojo/DialogMessages',
	'dojo/query',
	'dijit/registry',
	'ecm/model/SearchTemplate',
	'ecm/model/SearchCriterion',
	'dojo/io-query'
], 
         
   function (declare, lang, Action, Request, MessagesDialog, PopupDialog, Desktop,GILInitializerForm, GILInitializerFields,
		   StringFormatterUtils,json, OfferingLetterDialog, InvoiceELSDialog,ValidationConstants,date,
		   locale,stamp,EditPropertiesDialog,aspect,domstyle,NonModalConfirmDialog,domclass,Button,DialogMessages,
		   query, registry, SearchTemplate, SearchCriterion, ioquery) {
	
	/**
	 * the first class that is called when user clicks 'Attributes'.
	 */  
	return declare(Action, {

		/**
		 * parameters that will be passed to the service.
		 */ 
		serviceParams :null,
		counter:0,

		/**
		 * Overriding method performAction
		 */
		performAction : function (repository, itemList, callback, teamspace, resultSet, parameterMap) {
			this.serviceParams = {
  				repository: repository,
  				desktopUserId: Desktop.userId.trim().toUpperCase(),			
		   		repositoryId: itemList[0].repository.id,
		   		userId: Desktop.userId.trim().toUpperCase()
		   	};
			this.tabCreationParams = {};

			// get timestamp to create tab
			this.tabCreationParams.timestamp = new Date(Date.now())
			this.serviceParams.timestamp = this.tabCreationParams.timestamp.toISOString().replace('Z', '');
			/**
			 * this code gets the selected documents attributes values and use a callback 
			 * function(a function that is passed as a parameter to another function) to
			 * to create a JSON object with all theses attributes and then call the 
			 * correspondent form dialog.
			 * 
			 */
			itemList[0].retrieveAttributes(lang.hitch(this, function(item) {
				
				thisOpenNoIndexDlg = this;
				thisOpenNoIndexDlg.counter = 0;
				this.tabCreationParams.oldClass = item.getContentClass();
				
				if(item.locked && thisOpenNoIndexDlg.serviceParams['userId'].trim().toUpperCase()!=item.lockedUser.trim().toUpperCase() ) {
					var dialogMessages = new DialogMessages();
					dialogMessages.addConfirmMessages("The document is checked out to user "+ item.lockedUser +". Do you want to open the item in browse mode instead?");
					thisOpenNoIndexDlg.showMessage("Confirm",dialogMessages.confirmMessages(),true ,thisOpenNoIndexDlg.openProperties.bind(null,item,itemList[0],teamspace,resultSet), thisOpenNoIndexDlg.cancel);
				} else {
					thisOpenNoIndexDlg.openProperties(item,itemList[0],teamspace,resultSet);
				}

			}), false);
			
		},
	
		 openProperties: function(itemObj, itemListObj, teamSpaceObj, resultSet) {
			var prop = new EditPropertiesDialog();
			
			
			thisOpenNoIndexDlg.serviceParams['oldItemAttributes'] = itemListObj.attributes;
			//if the action is called via work view, an ecm.model.WorkItem is retrieved instead an ecm.model.ContentItem
			if(itemObj.declaredClass == "ecm.model.WorkItem")
				itemObj = itemObj.contentItem;
			prop.showProperties(itemObj, lang.hitch(this, thisOpenNoIndexDlg.callGilGui.bind(this,itemListObj,itemObj,resultSet)), teamSpaceObj);	
		 },
	 
		 cancel: function() {
	
		 },
	 
		 callGilGui: function(itemListObj, itemObj, resultSet){

			this.tabCreationParams.newClass = itemObj.getContentClass();
		 	thisOpenNoIndexDlg.serviceParams['newItemAttributes'] = itemObj.attributes;
			thisOpenNoIndexDlg.counter++;
			
			//stop function from being called twice, must be a dojo issue and also check if the user 
			if(thisOpenNoIndexDlg.counter>1 ){
				return;
			}
			
			// Open index tab if item class has changed
			var newClass = itemObj.getContentClass();
			if(this.tabCreationParams.oldClass.id != newClass.id){
				this.tabCreationParams.timestamp = new Date(itemObj.attributes.modifiedTimestamp);
				this.serviceParams.timestamp = this.tabCreationParams.timestamp.toISOString().replace('Z', '');
				this.serviceParams.repository.findUsers((users) => {
					this.serviceParams.frontEndAction = "getServerOffset";
					Request.postPluginService('GilCnPlugin', 'CommonService','application/x-www-form-urlencoded', {
						requestBody : ioquery.objectToQuery(this.serviceParams),
						requestCompleteCallback : lang.hitch(this, (response) => {
							this.tabCreationParams.serverOffset = response.offset;
							this.tabCreationParams.user = users[0];
							this.openIndexedTabOnDesktop(newClass);
							
						})
					});
				}, null, this.serviceParams.desktopUserId);
			}
			
			//stops gil gui from opening if item is locked by a different user then the one who is logged in ICN
			// it seems redundant but its to fix a bug in ICN window
			if(itemObj.locked && thisOpenNoIndexDlg.serviceParams['userId'].trim().toUpperCase()!=itemObj.lockedUser.trim().toUpperCase()){
				return;
			}
			
			if(resultSet!=null) {
				resultSet.refresh();
			}
	
			var businessAttributesJson = {};
			var stringFormatter = new StringFormatterUtils();
			var fields = new GILInitializerFields(itemObj.getContentClass().name);
		    var now = new Date();
			
			var fieldsArray = fields.get("doc");
			var formFieldsArray = fields.get("form");
		
	   		 for (var attr in itemObj.attributes) {
				
	   			var indexOfField = fieldsArray.indexOf( attr.toString().trim() );
	   			 
					if ( indexOfField > -1 )	{
						
						var field = stringFormatter.removeSpecialCharacters(attr);
						var fieldValue = itemObj.attributes[attr];
						
						if(field == 'createdTimestamp') {
							
							var parsedCreatedTimestamp = stamp.fromISOString(fieldValue).toGMTString();
							fieldValue = locale.format(new Date(parsedCreatedTimestamp),{ selector: "date", datePattern : ValidationConstants("DefaultTimeStampPattern")  });								 
						}
						
						businessAttributesJson[formFieldsArray[indexOfField]] = fieldValue;
	
					}
					
				}
	
	   		thisOpenNoIndexDlg.serviceParams['businessAttributes'] = dojo.toJson(businessAttributesJson);
	   		thisOpenNoIndexDlg.serviceParams['className'] = itemObj.getContentClass().name;
	   		thisOpenNoIndexDlg.serviceParams['itemId'] 	= itemObj.itemId;
	   		thisOpenNoIndexDlg.serviceParams['userTimeZone'] 	= dojo.date.getTimezoneName(now);	
	
			var gilForm = new GILInitializerForm(thisOpenNoIndexDlg.serviceParams);
			dialog =  gilForm.buildIndexingFormDialog();
				
		 },
		
		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	
			
		
	        var d1 = new NonModalConfirmDialog({
	            title: titl, 
	            style: "min-width:350px;",
	            content:msg,
	            name:"dialogMessage"
	        })
	        
	        if(needConfirmation){
	        	
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);
	
		       var buttonYes = new dijit.form.Button({ "label": "Yes", 
		            onClick: function(){
		            	callbackYes();
		            	d1.destroyRecursive(); 
		            }});
		        
		        buttonYes.placeAt(actionBar);
		        domclass.add(buttonYes.focusNode, "yesNoOkButton");
		        												
		        var buttonNo = new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	d1.destroyRecursive(); 
		            	}});
		        
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton");
	        	
	        } else {
	        
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk = new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	d1.destroyRecursive();
			            } });
			        
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			      var buttonCancel = new dijit.form.Button({"label": "Cancel", 
			            onClick: function(){
			            	d1.destroyRecursive();
			            	} });
			        
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton");
	        }
	        
	        d1.startup();
	        d1.show();
			
		},
		
		// Open tab for indexed document
		openIndexedTabOnDesktop: function(contentClass){
			var searchPane = query('div[widgetid^="ecm_widget_layout_SearchPane_"]');
			searchPane = registry.byNode(searchPane[0]);
	
			if (searchPane.searchSelector) {
				searchPane.searchSelector.clearSelection();
			}
			if(!searchPane.gilTabs)
				searchPane.gilTabs = {};
			
			if(searchPane.gilTabs.hasOwnProperty(contentClass.id)){
				searchPane.gilTabs[contentClass.id].hasBeenUpdated = true;
				console.log(contentClass.id + ' already exists.');
				var currentTab = searchPane.searchTabContainer.selectedChildWidget;
				var currentTabResultSet = currentTab.searchResults.getResultSet();
				if(currentTabResultSet.items.length == 0 && currentTab._gilIsAutoGenerated){
					var nextTab = searchPane.gilTabs[contentClass.id];
					nextTab.selected = true;
					searchPane.searchTabContainer.openTab(nextTab);
					nextTab.controlButton.onClick();
					searchPane.searchTabContainer.closeTab(currentTab);
					if(searchPane.tabToBeRemoved)
						delete searchPane.tabToBeRemoved;
				}
				
			} else {
				var searchTemplate = this.generateSearchTemplate(searchPane.repository, contentClass);
				
				var tab = searchPane.openTab({title: contentClass.name, tabType:"searchbuilder", repository: searchPane.repository, selected:false, closable:true});
				tab._gilIsAutoGenerated = true;
				aspect.after(tab.searchDefinition, 'onLoad', function(){
					tab.searchDefinition.contentClassSelector.setInitialSelection(contentClass);
					tab.searchCriteriaPane.domNode.style.display = 'none';
					lang.mixin(tab.searchDefinition, {_prepareSearchTemplateForExecution: function(){
						return searchTemplate;
					}});
					tab.hasBeenUpdated = true;
					if(searchPane.tabToBeRemoved){
						tab.selected = true;
						searchPane.searchTabContainer.openTab(tab);
						tab.controlButton.onClick();
						searchPane.searchTabContainer.closeTab(searchPane.tabToBeRemoved);
						delete searchPane.tabToBeRemoved;
					}
				});
				aspect.after(tab, 'destroy', function(){
					delete searchPane.gilTabs[contentClass.id];
				});
				aspect.after(tab.controlButton, 'onClick', function(){
					if(tab.hasBeenUpdated){
						tab.searchDefinition._search();
						tab.hasBeenUpdated = false;
					}
				});
				
				aspect.after(tab.searchResults, 'onEmpty', function(){
					searchPane.tabToBeRemoved = searchPane.gilTabs[contentClass.id];
				});
				
				searchPane.gilTabs[contentClass.id] = tab;
			}
	   		
		},
		
		generateSearchTemplate: function(repository, contentClass){
			var st = new SearchTemplate({id: ''/*contentClass.name + '_' + Date.now()*/, repository: repository});
			st.name = contentClass.name;
			st.description = "";
			st.autoRun = true;
			st.showInTree = false;
			st.searchCriteria = this.generateSearchCriteria();
			
			st.objectType = "all";
			st.includeSubclasses = null;
			st.setSearchContentClass(contentClass);
			st.objectStores =  [{
				id: repository.objectStoreName, 
				symbolicName: repository.objectStoreName, 
				displayName: repository.objectStoreDisplayName, 
				repositoryId: repository.id
			}];
			
			st.folders = [];
			st.andSearch = true;
			st.moreOption = {objectType: st.objectType, versionOption: 'currentversion'};
			st.textSearchType = repository.textSearchType;
			st.textSearchCriteria = null;
			st.resultsDisplay = {
				sortBy: '{NAME}',
				sortAsc: true,
				columns: repository.searchDefaultColumns
			};
			st.editable = false;
			
			return st;
				

		},
		
		generateSearchCriteria: function(){
			var user = this.tabCreationParams.user;
			var timestamp = this.formatTimestamp(this.tabCreationParams.timestamp, this.tabCreationParams.serverOffset);
			var modifiedBy = new SearchCriterion({
				id: 'modifiedBy',
				name: 'Modified By',
				defaultOperator: "EQUAL",
				availableOperators: ["EQUAL"],
				hasSelectedOperators: true,
				dataType: "xs:string",
				format: null,
				defaultValue: null, // should be USERID?
				valueRequired: true, // true
				readOnly: true, // true
				hasDependentAttributes: false,
				hidden: true, // true
				allowedValues: undefined, // should be USERID?
				minLength: undefined,
				maxLength: undefined,
				minValue: undefined,
				maxValue: undefined,
				cardinality: "SINGLE",
				choiceList: null,
				usesLongColumn: false,
				repository: this.serviceParams.repository,
				_isPseudo: undefined,
			});
	
			modifiedBy.setValues([ user ]);
			modifiedBy.defaultValue = [ user ];
			modifiedBy.selectedOperator = "EQUAL";
	
	
	
			// TIMESTAMP
			var modifiedOn = new SearchCriterion({
				id: 'modifiedTimestamp',
				name: 'Modified On',
				defaultOperator: "GREATEROREQUAL",
				availableOperators: ["GREATEROREQUAL"],
				hasSelectedOperators: true,
				dataType: "xs:timestamp",
				format: ecm.model.desktop.valueFormatter.getDefaultFormat("xs:date"), // 'M/d/yyyy'
				defaultValue: null, // should be USERID?
				valueRequired: false, // true
				readOnly: false, // true
				hasDependentAttributes: false,
				hidden: false, // true
				allowedValues: undefined, // should be USERID?
				minLength: undefined,
				maxLength: undefined,
				minValue: undefined,
				maxValue: undefined,
				cardinality: "SINGLE",
				choiceList: null,
				usesLongColumn: false,
				repository: this.serviceParams.repository,
				_isPseudo: undefined
			});


			modifiedOn.setValues([ timestamp ]); // "2018-04-30T00:00:00.000-03:00" like format
			modifiedOn.defaultValue = [ timestamp ];
			modifiedOn.selectedOperator = "GREATEROREQUAL";
	
			return [modifiedBy, modifiedOn];
		},
		
		formatTimestamp: function(date, offset){
			var timestamp = date.toISOString();
			var offsetStr = '';
			if(offset > 0)
				offsetStr += '+';
			else
				offsetStr += '-';
			if (offset/60 < 10)
				offsetStr += '0';
			offsetStr += Math.abs(offset/60);
			offsetStr += ':';
			if(offset%60 < 10)
				offsetStr += '0';
			offsetStr += Math.abs(offset % 60);
			return timestamp.replace('Z', offsetStr);
		}
	
	  });
	});
