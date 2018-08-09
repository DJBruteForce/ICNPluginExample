define(["dojo/_base/lang",
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/window",
	"dojo/_base/sniff"], 
	function(lang, declare, array, Window, sniff){
	
	var currentDoc=lang.getObject('dojox.html',true);
	var dynamicStyleMap = {};
	var pageStyleSheets = {};
	var titledSheets = [];
	
	currentDoc.getStyleSheet=function(ssName){
		debugger;
		// try dynamic sheets first
		
		if(!ssName){
			// no arg is nly good for the default style sheet
			// and it has not been created yet.
			return false;
		}
		var allSheets = currentDoc.getStyleSheets();
		// now try document style sheets by name
		if(allSheets[ssName]){
			return currentDoc.getStyleSheets()[styleSheetName];
		}
	};
	currentDoc.getStyleSheets=function(){
		debugger;
		if(pageStyleSheets.collected) {return pageStyleSheets;}
		var sheets = Window.doc.styleSheets;//document.styleSheets;//Window.doc.styleSheets;
		dojo.forEach(sheets, function(n){
			var s = (n.sheet) ? n.sheet : n;
			var name = s.title || s.href;
			pageStyleSheets[name] = s;
			pageStyleSheets[name].id = s.ownerNode.id;
		});
		pageStyleSheets.collected = true;
		return pageStyleSheets; //Object
	};
	currentDoc.disableStyleSheet=function(styleSheetName){
		var ss = currentDoc.getStyleSheet(styleSheetName);
		if(ss){
			if(ss.sheet){
				ss.sheet.disabled = true;
			}else{
				ss.disabled = true;
			}
		}

	};
	currentDoc.enableStyleSheet = function(styleSheetName){

		var ss = currentDoc.getStyleSheet(styleSheetName);
		if(ss){
			if(ss.sheet){
				ss.sheet.disabled = false;
			}else{
				ss.disabled = false;
			}
		}
};
	 return currentDoc;
});