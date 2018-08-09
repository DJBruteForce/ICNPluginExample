define([
		"dojo/_base/declare",
		"dijit/_TemplatedMixin",
		"dijit/_WidgetsInTemplateMixin",
		
		"ecm/widget/admin/PluginConfigurationPane",
		"dojo/text!./templates/ConfigurationPane.html",
		"dojo/_base/lang",
		"dojo/date/locale",
		"dojo/dom-class"
	],
	function(declare, _TemplatedMixin, _WidgetsInTemplateMixin, PluginConfigurationPane, template,lang,locale,domclass) {

		return declare("gilCnPluginDojo.ConfigurationPane", [  PluginConfigurationPane, _TemplatedMixin, _WidgetsInTemplateMixin], {
		
		templateString: template,
		widgetsInTemplate: true,
	
		
			load: function(callback){
				debugger;
				if(this.configurationString){
					var jsonConfig = JSON.parse(this.configurationString);
										
					for(var i=0; i<jsonConfig.configuration.length; i++){
						
						var name = jsonConfig.configuration[i].name;
						var value = jsonConfig.configuration[i].value;
						
						if(value!=null && value!=""){
							if(domclass.contains(lang.getObject(name, false, this).domNode, "dijitDateTextBox")){
					    		var parsedDate = locale.parse(value, {formatLength: "short",selector: "date",datePattern : "MM/dd/yyyy",});
					    		lang.getObject(name, false, this).set("value", parsedDate);
							}else{
								lang.getObject(name, false, this).set("value", value);
							}
						}else {
							lang.getObject(name, false, this).set("value", "");
						}


					}
//					
//					
//					dojo.byId(this.id+"_gilSchema").placeholder = "Ex: GILSCHEMA.";
//					this.toUpperCase(dijit.byId("gilSchema"));
					
//					dojo.byId(this.id+"_icfsDevSchema").placeholder = "Ex: ICFSDEVCHEMA.";
//					this.toUpperCase(dijit.byId("icfsDevSchema"));
/*				for(var i=0; i<jsonConfig.configuration.length; i++){
					    	switch(i) {
					        case 0:
					        	if(jsonConfig.configuration[0].value!=null && jsonConfig.configuration[0].value!=""){
									this.gcmsServer.set('value',jsonConfig.configuration[0].value);
								}else{
									this.gcmsServer.set('value',"");
								}
								console.log("length 0: "+jsonConfig.configuration[0].value.length);
					            break;
					        case 1:

								if(jsonConfig.configuration[1].value!=null && jsonConfig.configuration[1].value!=""){
									this.rdcServer.set('value',jsonConfig.configuration[1].value);
								}else{
									this.rdcServer.set('value',"");
								}
								console.log("length 1: "+jsonConfig.configuration[1].value.length);
					            break;
					        case 2:
					        	if(jsonConfig.configuration[2].value!=null && jsonConfig.configuration[2].value!=""){
									this.accessLevelPrivileges.set('value',jsonConfig.configuration[2].value);
								}else{
									this.accessLevelPrivileges.set('value',"");
								}
								console.log("length 2: "+jsonConfig.configuration[2].value.length);
					            break;
					        case 3:
					        	if(jsonConfig.configuration[3].value!=null && jsonConfig.configuration[3].value!=""){
									this.icfsDevSchema.set('value',jsonConfig.configuration[3].value);
								}else{
									this.icfsDevSchema.set('value',"");
								}
								console.log("length 3: "+jsonConfig.configuration[3].value.length);
								
					            break;
					        case 4:
					        	if(jsonConfig.configuration[4].value!=null && jsonConfig.configuration[4].value!=""){
									this.gilSchema.set('value',jsonConfig.configuration[4].value);
								}else{
									this.gilSchema.set('value',"");
								}
								console.log("length 4: "+jsonConfig.configuration[4].value.length);
					            break;
					            
					        case 5:
					        	if(jsonConfig.configuration[i].value!=null && jsonConfig.configuration[i].value!=""){
									this.gilGcmsUser.set('value',jsonConfig.configuration[i].value);
								}else{
									this.gilGcmsUser.set('value',"");
								}
								console.log("length 5: "+jsonConfig.configuration[i].value.length);
					            break;
					        case 6:
					        	if(jsonConfig.configuration[4].value!=null && jsonConfig.configuration[i].value!=""){
									this.gilGcmsPw.set('value',jsonConfig.configuration[i].value);
								}else{
									this.gilGcmsPw.set('value',"");
								}
								console.log("length 6: "+jsonConfig.configuration[i].value.length);
					            break;
					    }
					}	*/
				}
			},
			_onParamChange: function() {
				debugger;
				var configArray = new Array();
				
				var configString = {
						name: "inGSTEffDate",
						value: this.inGSTEffDate.get('displayedValue')
				};
				 configArray.push(configString);
				 
				var configString = {
						name: "gcmsServer",
						value: this.gcmsServer.get('value')
				};
				 configArray.push(configString);
				 
				 configString = {
						name: "rdcServer",
						value: this.rdcServer.get('value')
				};
				configArray.push(configString);
				
				var configString = {
						name: "accessLevelPrivileges",
						value: this.accessLevelPrivileges.get('value')
				};
				configArray.push(configString);
				
				configString = {
						name: "icfsDevSchema",
						value: this.icfsDevSchema.get('value')
				};
				configArray.push(configString);
				
				configString = {
						name: "gilSchema",
						value: this.gilSchema.get('value')
				};
				configArray.push(configString);
				
				configString = {
						name: "gilGcmsUser",
						value: this.gilGcmsUser.get('value')
				};
				configArray.push(configString);
				
				configString = {
						name: "gilGcmsPw",
						value: this.gilGcmsPw.get('value')
				};
				configArray.push(configString);
				
				configString = {
						name: "region",
						value: this.region.get('value')
				};
				configArray.push(configString);
				
				var configJson = {
						"configuration" : configArray
				};
				this.configurationString = JSON.stringify(configJson);
				this.onSaveNeeded(true);
			},
			

		validate: function() {
			
			//Need to add logic to check servers urls?
			return true;
		}
	});
});
