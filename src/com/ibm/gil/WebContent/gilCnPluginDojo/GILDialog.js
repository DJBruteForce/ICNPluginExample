define(["dojo/_base/declare", 
        "dojo/ready", 
        "dojo/parser", 
        "dojo/on", 
        "dojo/keys",
        "dijit/registry",
        "dojo/dom-class",
        "dojo/dom-style",
        "gilCnPluginDojo/constants/ValidationConstants",
        "dojo/date/locale",
        "dojo/date/stamp",
        'dojo/_base/lang',
        'dojo/io-query', 
        'ecm/model/Request',
        "gilCnPluginDojo/util/MathJs",
        "dojo/dom-form",
        "dojo/_base/array",
        "dojo/dom-construct",
        ], 
   function(declare, ready, parser, on, keys, registry, domclass, domStyle, ValidationConstants, locale, 
		   stamp, lang, ioquery, request,MathJs,domForm,array,construct){
	
	var GILDialog = declare(null,{
	  
		serialFormClean:"",
		serialFormDirty:"",
		noop : function() {},
		
	    constructor: function(args){
	        declare.safeMixin(this,args);
	    },
	    
	    isFormDirty: function(formid) {
			this.serialFormDirty = this.serializeForm(formid);
			return this.serialFormClean.localeCompare(this.serialFormDirty)!=0
	    },
	    
	    serializeForm: function(formid) {
	    	return	JSON.stringify(domForm.toObject(formid));
	    },
	    
	    
	    
	    gilDatePattern:ValidationConstants("datePattern"),
	   
	    isNumberKey: function(fieldId) {
	     on(dijit.byId(fieldId), "keypress", function(event) {
	    	
	        var charCode = (event.which) ? event.which : event.keyCode;	
	        if (event.charOrCode=="'" ||event.charOrCode=="%"){
	        	dojo.stopEvent(event);
	        	return true;
	        }
	        if (charCode !=keys.LEFT_ARROW && charCode !=keys.RIGHT_ARROW && charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
	          dojo.stopEvent(event);

	        if (event.target.value.indexOf('.') >= 0 && charCode == 46)
	          dojo.stopEvent(event);
	        
	        return true;

	      });
	    },
	    isAlphaNumeric: function(fieldId) {
		     on(dijit.byId(fieldId), "keypress", function(event) {
		    	
		        var charCode = (event.which) ? event.which : event.keyCode;	
		        if (event.charOrCode=="'" ||event.charOrCode=="%"){
		        	dojo.stopEvent(event);
		        	return true;
		        }
		        if (charCode !=keys.LEFT_ARROW && charCode !=keys.RIGHT_ARROW && charCode > 31 && (charCode < 48 || charCode > 57) && (charCode < 65 || charCode > 90) && (charCode < 97 || charCode > 122)){
		          dojo.stopEvent(event);
		          return true;
		        }
		       

		      });
		    },
	    isInteger: function(fieldId) {
		     on(dijit.byId(fieldId), "keypress", function(event) {
		    	
		        var charCode = (event.which) ? event.which : event.keyCode;	
		        charCode=Number(charCode);
		        if (charCode == 46 || event.charOrCode=="'" ||event.charOrCode=="%"){
		        	dojo.stopEvent(event);
		        	return true;
		        }
		        
		        if( charCode > 47 && charCode < 58  || 
		        		charCode==keys.LEFT_ARROW || 
		        		charCode== keys.RIGHT_ARROW ||
		        		charCode==keys.BACKSPACE || 
		        		charCode== keys.TAB ||
		        		charCode== keys.DELETE  ){
		        	
		        	console.log("is >47 && <58 -->"+charCode);
		        		
		        	console.log("is part of an integer")
		        		
		    	 //do nothing
		     	}else{
		     		dojo.stopEvent(event);
		     	}

		      
		        return true;

		      });
		    },

	    isDateKey: function(fieldId) {
	    	//do not allow more than 2 "//"slashes...
	    on(dijit.byId(fieldId), "keypress", function(event) {
	    
	        var charCode = (event.which) ? event.which : event.keyCode;
	        if (charCode != 47 && charCode > 31 && (charCode < 48 || charCode > 57))
	          dojo.stopEvent(event);
	       
	        if ( charCode == 47 ){
	        	var a = event.target.value.match(/\//g);
	 	        var countSlash=(a || []).length;
	 	        if(countSlash == 2 )dojo.stopEvent(event);
	        }
	          

	        return true;

	      });
	    },
	    
	    setDatePattern: function(dateWidget){
	    	
			var constr = dateWidget.get("constraints");
			constr.datePattern = this.gilDatePattern;
			//dateWidget.set('constraints', constr);
			dateWidget.set("placeholder", this.gilDatePattern );	
	    },
	    
	    parseDate: function(dateAux, isISO8601){
	    	
	    	if(isISO8601){
	    		
	    		return stamp.fromISOString(dateAux.toString()).toGMTString();
	    		
	    	} else {
	    	
		    		var parsedDate = locale.parse(dateAux, {
		    			formatLength: "short",
		    			selector: "date",
		    			datePattern : this.gilDatePattern,
		    			
				});
			    
		    		return parsedDate;
	    	}
	    },
	    
	    toNumber: function(num) {

	    	var res = MathJs.eval(num); 
	    	return res;
	    },
	    
	    formatNumber: function(num, precis) {

	    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
	    	return res;
	    },
	    
	    parseTimestamp: function(tsValue){
	    	
		    return  locale.parse(tsValue, {
		        datePattern: ValidationConstants("DefaultTimeStampPattern"), 
		        selector: 'date'
		    });
	    },
	    
	    setSize:function (width, height) {
			if (!this._sizeToViewportRatio) {
				domStyle.set(this.domNode, {width:width + "px", height:height + "px",});
			}
			this.resize();
		},
	    
	    toUpperCase: function(widget){
	    	
	    	domclass.add(registry.byId(widget.id).focusNode, "uppercaseField");
	    	registry.byId(widget.id).set('uppercase','true');
	    },
	    
	    finalizeGUI: function(){
	    	this.serviceParameters['frontEndAction'] = 'unlockItem';
	    	request.postPluginService("GilCnPlugin", "CommonService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			
	    			   array.forEach(registry.findWidgets(this.domNode), function (widget) {
	         				 widget.destroyRecursive();
	         				 delete window[widget.id]
	         				
	    			   	});
	    			   construct.empty(this.domNode);
	    			   this.destroyRecursive();
	    		})
			});
	    },
	    
	    lockItem: function(){
	    	try{
		   		if (!this.item.locked || (this.item.locked && Desktop.userId.toUpperCase()==this.item.lockedUser.toUpperCase()) ) {
					this.item.repository.lockItems([this.item], lang.hitch(this, function(arg1, arg2) {
						console.log('lockItem()');
						console.log('arg1: ' + arg1);
						console.log('arg2: ' + arg2);
					}));
		   		}
	    	} catch(e){
	    		console.log(e.name)
	    		console.log(e.message)
	    	}
	    },
	    
	    unlockItem: function(){
	    	try{
		   		if (this.item.locked) {
					this.item.repository.unlockItems([this.item], lang.hitch(this, function(arg1, arg2) {
						console.log('unlockItem()');
						console.log('arg1: ' + arg1);
						console.log('arg2: ' + arg2);
					}));
		   		}
	    	} catch(e){
	    		console.log(e.name)
	    		console.log(e.message)
	    	}
	    },
	    /** 
         * Method : disableEnterForInputs
         * Defect : 1837903
         * Usage : Used to diasble enter key press action for input text , Raiobutton & checkbox
         * 
         * **/
        disableEnterForInputs:function(){
            debugger;
            dojo.query("input[type=text],[type=radio],[type=checkbox]").on("keydown", function(event) {
            if(event.keyCode==keys.ENTER)
                    dojo.stopEvent(event);
              });
        },
	    _onKey: function(/*Event*/ evt){
			// summary:
			//		Handles the keyboard events for accessibility reasons
			// tags:
			//		private

			if(evt.keyCode == keys.TAB){
				this._getFocusItems();
				var node = evt.target;
				if(this._firstFocusItem == this._lastFocusItem){
					// don't move focus anywhere, but don't allow browser to move focus off of dialog either
					evt.stopPropagation();
					evt.preventDefault();
				}else if(node == this._firstFocusItem && evt.shiftKey){
					// if we are shift-tabbing from first focusable item in dialog, send focus to last item
					focus.focus(this._lastFocusItem);
					evt.stopPropagation();
					evt.preventDefault();
				}else if(node == this._lastFocusItem && !evt.shiftKey){
					// if we are tabbing from last focusable item in dialog, send focus to first item
					focus.focus(this._firstFocusItem);
					evt.stopPropagation();
					evt.preventDefault();
				}
			}else if(this.closable && evt.keyCode == keys.ESCAPE){
				this.cancel();
				evt.stopPropagation();
				evt.preventDefault();
			}
		}

  });
  
  return GILDialog;
});
