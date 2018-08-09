/**
 * 
 */
define([
	'dojo/_base/declare',
	'ecm/widget/layout/WorkPane',
	'dojo/_base/lang',
	'dijit/layout/ContentPane',
	'dojo/dom-style',
	'ecm/model/SearchQuery',
	'dojo/aspect',
	'dojo/query',
	'dijit/registry',
	'dojo/text!./templates/ExtendedWorkPane.html',
	'ecm/widget/listView/modules/Bar',
	'ecm/widget/listView/modules/Breadcrumb',
	'ecm/widget/listView/modules/Toolbar2',
	'ecm/widget/listView/modules/P8InbasketToolbar',
	'ecm/widget/listView/modules/DocInfo',
	'ecm/model/WorkItem',
	'ecm/model/Worklist'
	
], function(declare, WorkPane, lang, ContentPane, domStyle, SearchQuery, aspect, query, registry, template,
		Bar, Breadcrumb, Toolbar, P8InbasketToolbar, DocInfo, WorkItem, Worklist){
	
	return declare("gilCnPluginDojo.ExtendedWorkPane", [ WorkPane ], {
		constructor: function(){
			lang.mixin(this, {templateString: template});
			this.cd = {};
			this.isRetrievingWorklist = false; //needed to control double click when selecting a worklist.
		},
		
        postCreate: function(){
			this.inherited(arguments);
			
			// hides toolbar
			aspect.after(this.inbasketArea.worklistContents, 'setResultSet', () => {
				domStyle.set(this.inbasketArea.worklistContentsPane.domNode.children[0].children[0].children[1].children[0], 'display', 'none');
				this.onSetBreadcrumb();
			
			});
			
			// Needed to remove the dependency of loading configuration from workPane
			lang.mixin(this.inbasketArea, {_getContentListModules: lang.hitch(this.inbasketArea, this._getContentListModules)});
			
			aspect.after(this, 'searchCallback', lang.hitch(this, this.unlockTabSelection));
			aspect.before(this, 'loadContent', lang.hitch(this, () => {
				domStyle.set(this.inbasketTabs.domNode, "visibility", "hidden");
			}));
		},
		
		setWorklist:function (worklist, repository) {

			if(this.isRetrievingWorklist == true){
				return;
			}
			this.isRetrievingWorklist = true;
            this.worklist = worklist;
            this.repository = repository;
            this.inbasketArea.setRepository(this.repository);
            if (this.repository.type == "p8") {
                this.openInbaskets(this.worklist);
            } else {
                if (this.repository.type == "cm") {
                    this.openWorklists(this.worklist);
                }
            }
        },
		
		openWorklists: function(worklist){
            this._worklist = worklist; //lang.mixin(worklist, {_retrieveWorkItemsCompleted: this._retrieveWorkItemsCompleted});
            if (!this.isLoaded) {
                this.loadContent();
            } else {
        		if(this.cd.loadTabHandler)
        			this.cd.loadTabHandler.remove();
        		var tabs = this.inbasketTabs.getChildren();
    			for(i = 0; i < tabs.length; i++){
        			this.inbasketTabs.removeChild(tabs[i]);
        		}
    			this.cd.worklistName = worklist.worklist_name;
                worklist.retrieveWorkItems(lang.hitch(this, this.retrieveWorkItemsCallback));
                this.connect(worklist, "onRefresh", function () {
                    worklist.retrieveWorkItems(lang.hitch(this, function (resultSet) {
                    	this.retrieveWorkItemsCallback(resultSet);
                        this.inbasketArea.worklistContents.setResultSet(resultSet);
                    }));
                });
                this.connect(worklist, "onRefreshWorklist", function () {
                    this.inbasketArea.refresh();
                });
                this.inbasketArea.setSelectedWorklist(worklist);
                this.inbasketArea.setSelectedContentList(this.inbasketArea.worklistContents);
            }
			
		},
		

		onlyUnique: function(value, index, self) { 
			    return self.indexOf(value) === index;
		},
			
		compare: function(a, b) {
			  if (a.name < b.name)
			    return -1;
			  if (a.name > b.name)
			    return 1;
			  return 0;
		},
		
		loadTab: function(tab){
			if(tab == undefined || tab == null){ // fixes error when worklist is empty.
            	this.isRetrievingWorklist = false;
				return;
			}
			domStyle.set(this.inbasketArea.domNode, "visibility", "hidden");
        	if(this.cd.itemTypes.length > 1 && this.cd.lastLoadedTabId != tab.id){ // for some unknown reason the line below makes this.inbasketArea.worklistContents undefined when executed and there's only 1 tab.
            	tab.set("content", this.inbasketArea);
            	this.cd.lastLoadedTabId = tab.id;
        	}
        	var resultSet = this.cd.wiResultSet;
        	resultSet.items = this.filterResultSet(tab.itemTypeId, this.cd.items);
        	this.searchCallback(resultSet);
		},
		
		searchCallback: function(resultSet) {
			//var wResultSet = this.mergeResultSet(this.cd.wiResultSet, resultSet);
        	this.inbasketArea.worklistContents.setResultSet(resultSet);
        	domStyle.set(this.inbasketArea.domNode, "visibility", "visible");
			
		},
		
		unlockTabSelection: function(){
        	this.isRetrievingWorklist = false;
   			domStyle.set(this.inbasketTabs.domNode, "pointerEvents", "");
        	
        },
        
        onSetBreadcrumb: function(){
        	var list = query('div[widgetid^="ecm_widget_Breadcrumb_"]');
			var bc = registry.byNode(list[0]);
        	var bcs = bc.breadcrumbs;
            if(bcs[0].class == "simpleBreadCrumb"){
            	domStyle.set(bc.containerNode.children[0], 'visibility', 'hidden');
            	if(bcs.length > 1){
                	domStyle.set(bc.containerNode.children[0], 'visibility', '');
                	domStyle.set(bc.containerNode.children[0], 'display', 'none');
            		domStyle.set(bcs[1].domNode, 'display', 'none');
            	}
            }
        },
        
        _getContentListModules: function (repository, inbasket) {
            var array = [];
            if (repository.type == "cm") {
                array.push({moduleClass:Bar, top:[[[{moduleClass:Toolbar}]], [[{moduleClass:Breadcrumb}]]]});
                var config = {
                		defaultRepository: repository.id,
                		documentInfoPaneDefaultOpen: false,
                		documentInfoPaneOpenOnSelection: true,
                		showDocumentInfoPane: true,
                		showMyCheckouts: false,
                		showRepositories: [repository.id],
                		showTreeView: false,
                		showViews: undefined
                };
                if (config && config.showDocumentInfoPane) {
                    array.push({moduleClass:DocInfo, selectAutoOpen:config.documentInfoPaneOpenOnSelection, showSystemProps:false});
                }
            } else {
                if (repository.type == "p8") {
                    array.push({moduleClass:Bar, top:[[[{moduleClass:P8InbasketToolbar, queueType:inbasket ? inbasket.queueType : "", queueName:inbasket ? inbasket.queueName : ""}]], [[{moduleClass:Breadcrumb, disabled:true}]]]});
                    array.push({moduleClass:DocInfo, showSystemProps:true, showPreview:false, selectAutoOpen:false});
                }
            }
            return array;
        },
        
        setRepository: function (repository){
        	if(repository == null)
	        	domStyle.set(this.inbasketTabs.domNode, "visibility", "hidden");
			this.inherited(arguments);
        },
        
        
        // ecm.model.Worklist methods override
//		_retrieveWorkItemsCompleted:function (response, callback) {
//            response.repository = this.repository;
//            response.parentFolder = this;
//            response.setType = "workItems";
//            var results = Worklist.createResultSet(response);
//            if(response.num_results < response.pageSize) {
//            	var searchQuery = new SearchQuery();
//                searchQuery.query = '/* [@VERSIONID = latest-version(.) AND REFERENCEDBY/@REFERENCER => WORKPACKAGE[@PROCESSITEMID =  /ROUTINGPROCESS[@PROCESSNAME = "' + 
//                response.worklist_name + '"]/@ITEMID  AND ../@WORKNODENAME = "' + response.worklist_name + '"]]';
//                searchQuery.repository = response.repository;
//                searchQuery.search(lang.hitch(this, this.searchCallback), '{NAME}', false, null, null,
//                		lang.hitch(this, this.rebuildWorkItems));
//            } else {
//            	console.log('More results than actual page size for the worklist.');
//                callback(results);
//            }
//            var sq = new SearchQuery();
//            sq.repository = this.repository;
//            sq.query = '/Offering_LF_AR';
//            sq.search((resultSet) => {
//				console.log('Number of results for Offering_LF_AR: ' + resultSet.num_results);
//            }, '{NAME}', false, null, null);
//
//            sq = new SearchQuery();
//            sq.repository = this.repository;
//            sq.query = '/Offering_L_0008';
//            sq.search((resultSet) => {
//				console.log('Number of results for Offering_L_0008: ' + resultSet.num_results);
//            }, '{NAME}', false, null, null);
//        },
	
        filterResultSet: function(itemType, items){
        	return items.filter(item => item.template == itemType);
        },
        
        retrieveWorkItemsCallback: function (resultSet) {
        	resultSet.refreshHandler = lang.hitch(this, this.refreshResultSetCallback)
	        this.cd.itemTypes = resultSet.items.map(item => {return item.contentItem.getContentClass()})
	      	.filter(this.onlyUnique).sort(this.compare);
	        this.cd.wiResultSet = resultSet;
	        this.cd.items = resultSet.items;
	        domStyle.set(this.inbasketTabs.domNode, "visibility", "visible");
			for(i = 0; i < this.cd.itemTypes.length; i++){
	        	var tab = new ContentPane({
	        		title: this.cd.itemTypes[i].name,
	        		content: this.inbasketArea,
	        		itemTypeId: this.cd.itemTypes[i].id,
	        		worklistName: resultSet.worklist_name,
	        		searchRepository: resultSet.repository
	        		
	        	});
	        	this.inbasketTabs.addChild(tab);
	        }
			this.loadTab(this.inbasketTabs.selectedChildWidget);
	        this.cd.loadTabHandler = aspect.after(this.inbasketTabs, 'selectChild', () => {
	   			domStyle.set(this.inbasketTabs.domNode, "pointerEvents", "none");
	        	this.loadTab(this.inbasketTabs.selectedChildWidget);
	        });
	        this.inbasketArea.resize();
	        if(this.cd.currentItemTypeId)
	        	delete(this.cd.currentItemTypeId);
	        
	    },
	    
	    refreshResultSetCallback: function(resultSet, callback){
        	this.worklist.retrieveWorkItems((resultSet) => {
        		this.cd.itemTypes = resultSet.items.map(item => {return item.contentItem.getContentClass()})
    	      	.filter(this.onlyUnique).sort(this.compare);
    	        this.cd.wiResultSet = resultSet;
    	        this.cd.items = resultSet.items;
        		this.loadTab(this.inbasketTabs.selectedChildWidget);
        	});
	    }
        
	});
});