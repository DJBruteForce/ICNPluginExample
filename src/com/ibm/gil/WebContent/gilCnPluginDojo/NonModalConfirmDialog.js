define(["dojo/_base/declare",  "dojo/parser", "dijit/Dialog", "ecm/widget/dialog/BaseDialog", "dojo/text!./templates/ConfirmationDialog.html", "dojo/dom", "dojo/html"],	function(declare,parser, Dialog, BaseDialog, template,dom, html) {

	/**
	 * @name samplePluginDojo.MessagesDialog
	 * @class Provides a dialog to display a messages.  Invoking addMessage will append a new message line
	 * to those already appearing in the dialog.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	return declare( [ Dialog ], {

		
	
		
        addWarningMessage: function(msg) {
        	
        	this.content = msg;
        },

        
     
        
		
        show: function() {
           // this.inherited(arguments);
        	this.inherited("show", []);
            
            //var ds = Dialog._dialogStack;
           // var zIndex = ds[ds.length - 1].zIndex;
            //Dialog._DialogLevelManager._beginZIndex = zIndex + 2;
            
            Dialog._DialogLevelManager.hide(this);                      
        },

		

	});
});
