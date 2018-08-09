/**
 * 
 */
define([
	'dojo/_base/declare',
    'ecm/model/Request',
    'dojo/_base/lang',
    'dojo/io-query',
    'gilCnPluginDojo/DialogMessages',
    'gilCnPluginDojo/NonModalConfirmDialog',
    'dojo/dom-class'
	], 
	function(declare, request, lang, ioquery, DialogMessages, NonModalConfirmDialog, domClass){
		return declare('gilCnPluginDojo.MiscELS', null, {
			
			constructor: function(serviceParams){
				this.serviceParams = serviceParams;
			},
			
			index: function(){
		    	this.serviceParams['frontEndAction'] = 'index';
		    	 request.postPluginService('GilCnPlugin', 'MiscELSService','application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(this.serviceParams),
					    requestCompleteCallback : lang.hitch(this, function (response) {
				    		var rDialog = new DialogMessages();
					    	if(response.wasSuccessful){
					    		rDialog.addInfoMessages('Response received from GCMS');
					    		this.showMessage('', rDialog.infoMessages(), false, null, null);
					    		
					    	} else{
					    		rDialog.addErrorMessages('Misc Service failed');
					    		this.showMessage('', rDialog.errorMessages(), false, null, null);
					    		
					    	}
			    		})
				});
		    },
			
			showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {
				thisForm = this;
				var d1 = new NonModalConfirmDialog({
					title: titl,
					style: "min-width:350px;",
					content: msg,
					name:"dialogMessage"
				});
				var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
				var buttonOk= new dijit.form.Button({"label": "OK",
					onClick: function(){
						d1.destroyRecursive(); 
					}
				});
				buttonOk.placeAt(actionBar);
				domClass.add(buttonOk.focusNode, "yesNoOkButton");
				
				d1.startup();
				d1.show();
				
			}
	});
});