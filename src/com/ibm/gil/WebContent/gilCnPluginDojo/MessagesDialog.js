define(["dojo/_base/declare", "ecm/widget/dialog/BaseDialog", "dojo/text!./templates/MessagesDialog.html"],	function(declare, BaseDialog, template) {

	/**
	 * @name samplePluginDojo.MessagesDialog
	 * @class Provides a dialog to display a messages.  Invoking addMessage will append a new message line
	 * to those already appearing in the dialog.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	return declare("gilCnPluginDojo.MessagesDialog", [ BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
	
		postCreate: function() {
			
			this.inherited(arguments);
			this.setResizable(false);
			this.setMaximized(false);
			this.setTitle("Error");
			this.addButton("Ok", "ok", false, true);
			this.showActionBar(true);
		},
		
		addMessage: function(message) {
			
			this.clearMessages();
			
			if (typeof message == "string") {
				
				this.messagesTextarea.value += message + "\n";
				
			} else {
				
				this.messagesTextarea.value += message.text + "\n";
			}
			
			this.show();
		},
	
		ok: function(evt) {
			
			this.messagesTextarea.value = "";
		}
		

	});
});
