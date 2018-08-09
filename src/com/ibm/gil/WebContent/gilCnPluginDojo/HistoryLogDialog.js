/**
 * @author mdevino
 */
define([
    'dojo/_base/declare',
	'dojo/_base/lang',
	'ecm/model/Action',
	'ecm/model/Request',
	'ecm/model/Desktop',
	'ecm/widget/dialog/BaseDialog',
	'gilCnPluginDojo/GILDialog',
	'dojo/text!./templates/HistoryLogDialog.html',
	'dojo/io-query',
	'dojo/dom-class',
	'dojo/dom-style',
	'dijit/Dialog',
	'gilCnPluginDojo/NonModalConfirmDialog'
	],	
function(declare, lang, Action, Request, Desktop, BaseDialog, GILDialog, template, ioquery, domClass, domStyle, 
		NonModalConfirmDialog){


	return declare('gilCnPluginDojo.HistoryLogDialog', [ BaseDialog, GILDialog ], {

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
			this.setTitle('History Log for ' + this.serviceParameters.name);
			this.addButton('Close', 'cancel', false, true);
			this.addButton('Help', 'help', false, true);
			this.showActionBar(true);
			this.setSize(600, 300);
			this.setResizable(true);
			this.setMaximized(false);
			
			//Disables textare but doesn't make it look grayed out
			this.textarea.set('disabled', true);
			domClass.remove(this.textarea.domNode, 'dijitTextBoxDisabled dijitTextAreaDisabled dijitDisabled');
			domStyle.set(this.textarea.domNode, "resize", "none");
			
			this.retrieveEvents();
			
    	},
		
		cancel: function() {
			this.destroyRecursive();
			
		},
		
		retrieveEvents: function(){
			this.serviceParameters['frontEndAction'] = 'getHistoryLog';
			this.serviceParameters['userTimezone'] = Intl.DateTimeFormat().resolvedOptions().timeZone;
			
			try{
				Request.postPluginService('GilCnPlugin', 'CommonService','application/x-www-form-urlencoded', {
					requestBody : ioquery.objectToQuery(this.serviceParameters),
					requestCompleteCallback : lang.hitch(this, function (response) {
						var events = JSON.parse(response.events);
						var log = "";
						var n = 0;
						for(var i = 0; i < events.length; i++){
							log += events[i].timestamp + ': ';
							log += events[i].userId + ', ';
							log += events[i].code;
							for(var j = 0; j < events[i].dataCount; j++){
								log += ', ' + events[i].data[j];
							}
							log += '\n';
						}
						this.textarea.set('value', log);
					})
				});
		    } catch(e){
				console.log(e.name);
				console.log(e.message);
		    }
		},
		
		help: function(){
			var dateString = this.getUserOffset();
			
			var msg = '<p>The system logs some actions taken on items in an automatically-generated log file, ' +
			'called the history log. Your system administrator specifies what logging is enabled for specific' +
			' Item Types.</p>' +
			'<p>To view the history log:</p>' +
			'<ol><li>Select <b>Right click an item -> History Log</b>. The history log window is displayed.' + 
			' You can view multiple log files concurrently.</li>' +
			'<li>Scroll down to view each history log entry in its entirety.</li></ol>' + 
			'<p>The entries are read-only so you cannot modify them, but you can copy the text to the clipboard.<p/>' +
			'<p><b>The timestamp displayed on History log is always in GMT time. Your current timezone is ' +
			dateString + '.</b></p>'

			if(this.helpDialog == null){
				this.helpDialog = new NonModalConfirmDialog({
	            	id: this.id + '_help_dialog',
	            	title: 'Viewing the history log', 
	                style: "width:600px;height:350px;",
	                content: msg,
	            });
				var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.helpDialog.containerNode);
				
		        var buttonOk = new dijit.form.Button({"label": "OK",
		            onClick: () => {
		            	this.helpDialog.hide(); 
		            	
		            } });
		        buttonOk.placeAt(actionBar);
		        domClass.add(buttonOk.focusNode, "yesNoOkButton");
				
			}
			this.helpDialog.startup();
			this.helpDialog.show();
			
		},
		
		hideHelp: function(){
			this.helpDialog.hide();
		},
		
		getUserOffset: function(){
			var d = new Date();
			var dateString = "GMT";
			var offset = d.getTimezoneOffset();
			if(offset < 0)
				dateString += "+";
			dateString += offset.toString() / 60 * -1;
			if((offset % 60) != 0){
				dateString += ":30";
			}
			
			return dateString;
			
		}
		
	});
});