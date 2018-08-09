define(["dojo/_base/declare", "dojo/parser", "dijit/Dialog", 
        "ecm/widget/dialog/BaseDialog", "dojo/text!./templates/ConfirmationDialog.html", 
        "dojo/dom", "dojo/html", "dojo/dom-construct"],	function(declare, parser, Dialog, BaseDialog, template,dom, html,construct) {

	/**
	 * @name samplePluginDojo.MessagesDialog
	 * @class Provides a dialog to display a messages.  Invoking addMessage will append a new message line
	 * to those already appearing in the dialog.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	return declare("gilCnPluginDojo.ConfirmDialog", [ Dialog, BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
	
		postCreate: function() {
			
			this.inherited(arguments);
			this.setResizable(false);
			this.setMaximized(false);
			this.setTitle("Error");
			//this.addButton("Cancel", "cancel", false, true);
			this.addButton("Ok", "okError", false, true);
			this.showActionBar(true);
			//dojo.style(this.cancelButton.domNode,"display","none"); 
	
			
		},
		
/*		cancel: function() {
			this.destroy();
			
		},*/
		
        show: function() {
            this.inherited(arguments);
            
            var ds = Dialog._dialogStack;
            var zIndex = ds[ds.length - 1].zIndex;
            Dialog._DialogLevelManager._beginZIndex = zIndex + 2;
            
            Dialog._DialogLevelManager.hide(this);                      
        },
		
		addErrorMessage: function(message) {
			
			
			this.setTitle("Error");
			this.addButton("Ok", "okError", false, true);
			
			if (typeof message == "string") {
				
				html.set(dom.byId("msgContent"), message);
				
			} else {
				
				html.set(dom.byId("msgContent"), message.text);
			}
			
			this.show();
		},
		
		addWarningMessage: function(message) {
			
			
			this.setTitle("Warning");
			
			if (typeof message == "string") {
				
				html.set(dom.byId("msgContent"), message);
				
			} else {
				
				html.set(dom.byId("msgContent"), message.text);
			}
			parser.parse();
			this.show();
		},
		
		addMessage: function(message) {
			html.set(dom.byId("msgContent"), message);
			//domMsg = construct.toDom("<h4>"+msg+"</h4>");
			//construct.place(domMsg, "msg");
			//html.set(dom.byId("msgContent"), message);
			
			this.show();
		},
	
		okError: function(evt) {
			
			this.hide();
		}
		

	});
});
