define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request",
        "ecm/model/Desktop",
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog",
        "gilCnPluginDojo/GILDialog",
        "dojo/text!./templates/CommentsDialog.html",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dijit/form/SimpleTextarea",
	    "dijit/form/Button",
	     "dojo/dom",
	     "dojo/on",
	     "dojo/_base/connect",
	     "dijit/form/Form",
	     "dojo/dom-style",
	     "dojo/dom-class",
	     "dojo/aspect",
	     "dojo/query",
	     "gilCnPluginDojo/DialogMessages"
	     ],	
		function(lang, Action, Request, Desktop, declare, BaseDialog, GILDialog, template,
				DataGrid, ItemFileWriteStore, dom, array, NonModalConfirmDialog,
				DialogMessages, registry, SimpleTextarea, Button, dom, on, connect, Form,
				domStyle, domClass, aspect, query, DialogMessages){


	return declare("gilCnPluginDojo.Comments", [ BaseDialog, GILDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
		supplierToSearchStr: null,
		searchType: null,
		gridSupplier: null,
		callerForm: null,
		serviceParameters: null,
		grid: null,
		numComments: 0,
		cancelConfirm: null,
		confirmMessages: null,
		changed: false,
		    
		constructor: function(args){	
			lang.mixin(this, args);
			numComments = jsonInvoiceEls.comments.length;
		},
	
		postCreate: function() {
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("Invoice Comments");
			this.addButton("Cancel", "showConfirmation", false, true);
			this.addButton("Ok", "save", false, true);
			this.showActionBar(true);
			this.setSize(1020, 500);
			this.setResizable(true);
			this.setMaximized(false);
			this.initGrid();
			this.showCommentsTextArea();
			this.setLayout();
			
    	},
    	
    	showConfirmation: function() {
			if(this.changed){
				if(this.cancelConfirm == null){
					this.confirmMessages = new DialogMessages();
					this.confirmMessages
						.addConfirmMessages("Your changes will be lost. Are you sure you want to cancel?");
					this.showCancelConfirmation("Confirm", this.confirmMessages.confirmMessages());
				
				} else {
					this.cancelConfirm.show();
				}
			} else{
				this.cancel();
			}
    		
    	},
	 
		
		cancel: function() {
			this.destroyRecursive();
		},
		
		save: function(){
			var comments = [];
			var i = 0;
			for(i = 0; i < numComments; i++){
				var item = this.grid.getItem(i);
				comments.push({
					'COMMENTNUMBER': item.COMMENTNUMBER[0],
					'COMMENTS': item.COMMENTS[0],
					'COUNTRY': item.COUNTRY[0],
					'CREATED': item.CREATED[0],
					'DIRTYFLAG': item.DIRTYFLAG[0],
					'INDEXCLASS': item.INDEXCLASS[0],
					'OBJECTID': item.OBJECTID[0],
					'REQUIREDFIELDS': item.REQUIREDFIELDS[0],
					'SOURCE': item.SOURCE[0],
					'TIMESTAMP': item.TIMESTAMP[0],
					'TOBEINDEXEDWORKFLOW': item.TOBEINDEXEDWORKFLOW[0],
					'UNIQUEREQUESTNUMBER': item.UNIQUEREQUESTNUMBER[0],
					'USERID': item.USERID[0],
				});
			}
			jsonInvoiceEls.comments = comments;
			if(this.cancelConfirm != null)
				this.cancelConfirm.destroyRecursive();
			this.destroyRecursive();
			
		},
		
		initGrid: function(){
			var data = { identifier: 'COMMENTNUMBER', items: []};
			
			for(var i = 0; i < jsonInvoiceEls.comments.length; i++) {
		    	data.items.push(jsonInvoiceEls.comments[i]);
		    }
			var store = ItemFileWriteStore({data: data});
			var layout = [[
						    {'name': 'Comments', 'field': 'COMMENTS', 'width': '100%' },
			]];
			this.grid = new DataGrid({
				store: store, 
				selectionMode:'single', 
				autoHeight: true,
				rowSelector: '1px', 
				structure: layout, 
				columnReordering: false,
				sortDesc: false, //present comments from newest to the oldest?

				//prevents sort by clicking on column header
				canSort: function(col){
					return false;
				},
				//fills text area with the selected comment
				onRowClick: (e) => {
					this.commentTextarea.set("value", e.grid.getItem(e.rowIndex).COMMENTS[0]);
				}
			});
			
			this.grid.placeAt(this.id + "_commentsDialogTable");
			this.grid.startup();
			
			dojo.connect(dojo.byId(this.id+"_addComment"), "onclick", () => {
				if(this.commentTextarea.value != "" && this.commentTextarea.value != null){
					var comment = this.commentTextarea.value;
					numComments++;
					this.grid.store.newItem({
						'COMMENTNUMBER': numComments,
						'COMMENTS': comment,
						'CREATED': "",
						'COUNTRY': "",
						'DIRTYFLAG': false,
						'INDEXCLASS': "",
						'OBJECTID': "",
						'REQUIREDFIELDS': 0,
						'SOURCE': "",
						'TIMESTAMP': new Date(Date.now()).toISOString().replace("T", " ").replace("Z", ""),
						'TOBEINDEXEDWORKFLOW': "",
						'UNIQUEREQUESTNUMBER': "",
						'USERID': Desktop.userId,
					});
					this.grid.store.save();
					this.commentTextarea.set("value", "");
				}
			});
			on.once(this.commentTextarea, "change", () => this.changed = true);
		},
	
		showCommentsTextArea: function(){
			this.show();
		},
		
		setSize:function (width, height) {
			if (!this._sizeToViewportRatio) {
				domStyle.set(this.domNode, {width:width + "px", height:height + "px",});
			}
			this.resize();
		},
		
		setLayout: function(){			
			//DataGrid
			var resizeGrid = (foo) => {
				if(foo != null){
					this.grid.resize();
					var th = this.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'commentsTableHeader'))
						domClass.add(th, 'commentsTableHeader');
					
				}
			};

			aspect.after(this.resizeHandle, 'onResize', resizeGrid, this.grid);
			aspect.after(this._maximizeButton, 'onClick', resizeGrid, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', resizeGrid, this._restoreButton);
			
			domStyle.set(this.id + '_commentsDialogTable', 'overflow-y', 'auto');
			domStyle.set(this.id + '_commentsDialogTable', 'width', '100%');
			domStyle.set(this.id + '_commentsDialogTable', 'height', '45%');

			var th = this.grid.viewsHeaderNode.children[1].children[0].children[0]
				.children[0].children[0].children[0].children[0];
			domClass.add(th, 'commentsTableHeader');
			
			//Button
			domStyle.set(this.id + '_addButton', 'text-align', 'center');
			domStyle.set(this.id + '_addButton', 'margin-top', '10px');
			domStyle.set(this.id + '_addButton', 'margin-bottom', '10px');
			
		},
		
		showCancelConfirmation: function(titl, msg) {
			this.cancelConfirm = new NonModalConfirmDialog({
				title: titl,
				style: "min-width:350px;",
				content: msg,
				name:"dialogMessage"
			});
			var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.cancelConfirm.containerNode);
			var buttonOk = new dijit.form.Button({"label": "Yes",
				onClick: () => {
					this.cancelConfirm.destroyRecursive();
					this.cancel();
				}
			});
			var buttonNo = new dijit.form.Button({"label": "No",
				onClick: () => {
					this.cancelConfirm.hide();
				}
			});
			
			buttonOk.placeAt(actionBar);
			buttonNo.placeAt(actionBar);
			domClass.add(buttonOk.focusNode, "yesNoOkButton");
			domClass.add(buttonNo.focusNode, "yesNoOkButton");
			
			this.cancelConfirm.startup();
			this.cancelConfirm.show();
		}
		
	});
});