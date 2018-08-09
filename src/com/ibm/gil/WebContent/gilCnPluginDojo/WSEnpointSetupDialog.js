/**
 * @author ferandra
 */
define([
    'dojo/_base/declare',
	'dojo/_base/lang',
	'ecm/model/Action',
	'ecm/model/Request',
	'ecm/model/Desktop',
	'ecm/widget/dialog/BaseDialog',
	'gilCnPluginDojo/GILDialog',
	'dojo/io-query',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojo/dom-class',
	'dojo/text!./templates/WSEndpointSetupDialog.html',
	'dijit/registry',
	'dojo/dom-construct',
	'dijit/form/Button',
	'dojo/aspect',
    "dojo/store/Memory",
    "dojo/store/Observable",
    "dojo/data/ObjectStore",
    "dijit/form/Select",
    "dijit/registry",
    "gilCnPluginDojo/util/JsonUtils"
	],	
function(declare, lang, Action, Request, Desktop, BaseDialog, GILDialog,
		 ioquery, NonModalConfirmDialog, domClass, template, 
		registry, domConstruct, Button, aspect, Memory, Observable,
		ObjectStore,Select,registry,JsonUtils){


	return declare('gilCnPluginDojo.WSEnpointSetupDialog', [ BaseDialog, GILDialog ], {

		contentString: template,
		widgetsInTemplate: true,
		serviceParameters: null,
		helpDialog: null,
		    
		constructor: function(args){	
			this.serviceParameters = args;
			
		},
	
		postCreate: function() {
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,'display','none');
			this.setTitle('Choose GCMS Endpoint');
			this.addButton('Close', 'cancel', false, true);
			this.addButton('Save', 'save', false, true);
			this.showActionBar(true);
			this.setResizable(false);
			this.setMaximized(false);
			this.setSize(400, 200);
			this.initializeSelects();
			
    	},
		
		cancel: function() {
			this.destroyRecursive();
		},
		
		
		buildSelect: function( jsonEndpoints) {
			
			var jsonUtils = new JsonUtils();
			var jsonGcmsOption = JSON.parse(jsonEndpoints);
			   var selectGCMS= new dijit.form.Select({
			        name: this.id + "_gcmsSelect",
			        id:this.id + "_gcmsSelect",   
			       options:jsonGcmsOption,
			      });

			   selectGCMS.placeAt("gcmsDiv");
			   selectGCMS.startup();             
			   selectGCMS.set("value", jsonUtils.getDefaultOrSelected(jsonGcmsOption,"value", true));
		},
		
		initializeSelects: function() {
			
	    	this.serviceParameters['frontEndAction'] = 'getEndpoints';
	    	Request.postPluginService("GilCnPlugin", "WSEndpointSetupService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
			    	this.buildSelect(response.listOfGcmsEndPoints);
	    		})
			});
			
		},
		
		save: function(){
			
	    	this.serviceParameters['frontEndAction'] = 'setEndpoint';
	    	this.serviceParameters['gcmsEndpoint'] = registry.byId(this.id+"_gcmsSelect").get('value');
	    	Request.postPluginService("GilCnPlugin", "WSEndpointSetupService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
			    	
			    	this.cancel();
	    		})
			});
		},
		
	});
});