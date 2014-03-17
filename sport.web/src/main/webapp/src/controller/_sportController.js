define(['model/sportModel'], function(sportModel) {
    App.Controller._SportController = Backbone.View.extend({
        initialize: function(options) {
            this.modelClass = options.modelClass;
            this.listModelClass = options.listModelClass;
            this.showEdit = true;
            this.showDelete = true;
            this.editTemplate = _.template($('#sport').html());
            this.listTemplate = _.template($('#sportList').html());
            if (!options || !options.componentId) {
                this.componentId = _.random(0, 100) + "";
            }else{
				this.componentId = options.componentId;
		    }
            var self = this;
            Backbone.on(this.componentId + '-' + 'sport-create', function(params) {
                self.create(params);
            });
            Backbone.on(this.componentId + '-' + 'sport-list', function(params) {
                self.list(params);
            });
            Backbone.on(this.componentId + '-' + 'sport-edit', function(params) {
                self.edit(params);
            });
            Backbone.on(this.componentId + '-' + 'sport-delete', function(params) {
                self.destroy(params);
            });
            Backbone.on(this.componentId + '-' + 'post-sport-delete', function(params) {
                self.list(params);
            });
            Backbone.on(this.componentId + '-' + 'sport-save', function(params) {
                self.save(params);
            });
            if(self.postInit){
            	self.postInit();
            }
        },
        create: function() {
            if (App.Utils.eventExists(this.componentId + '-' +'instead-sport-create')) {
                Backbone.trigger(this.componentId + '-' + 'instead-sport-create', {view: this});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-sport-create', {view: this});
                this.currentSportModel = new this.modelClass();
                this._renderEdit();
                Backbone.trigger(this.componentId + '-' + 'post-sport-create', {view: this});
            }
        },
        list: function(params) {
            if (params) {
                var data = params.data;
            }
            if (App.Utils.eventExists(this.componentId + '-' +'instead-sport-list')) {
                Backbone.trigger(this.componentId + '-' + 'instead-sport-list', {view: this, data: data});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-sport-list', {view: this, data: data});
                var self = this;
				if(!this.sportModelList){
                 this.sportModelList = new this.listModelClass();
				}
                this.sportModelList.fetch({
                    data: data,
                    success: function() {
                        self._renderList();
                        Backbone.trigger(self.componentId + '-' + 'post-sport-list', {view: self});
                    },
                    error: function(mode, error) {
                        Backbone.trigger(self.componentId + '-' + 'error', {event: 'sport-list', view: self, error: error});
                    }
                });
            }
        },
        edit: function(params) {
            var id = params.id;
            var data = params.data;
            if (App.Utils.eventExists(this.componentId + '-' +'instead-sport-edit')) {
                Backbone.trigger(this.componentId + '-' + 'instead-sport-edit', {view: this, id: id, data: data});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-sport-edit', {view: this, id: id, data: data});
                if (this.sportModelList) {
                    this.currentSportModel = this.sportModelList.get(id);
                    this._renderEdit();
                    Backbone.trigger(this.componentId + '-' + 'post-sport-edit', {view: this, id: id, data: data});
                } else {
                    var self = this;
                    this.currentSportModel = new this.modelClass({id: id});
                    this.currentSportModel.fetch({
                        data: data,
                        success: function() {
                            self._renderEdit();
                            Backbone.trigger(self.componentId + '-' + 'post-sport-edit', {view: this, id: id, data: data});
                        },
                        error: function() {
                            Backbone.trigger(self.componentId + '-' + 'error', {event: 'sport-edit', view: self, id: id, data: data, error: error});
                        }
                    });
                }
            }
        },
        destroy: function(params) {
            var id = params.id;
            var self = this;
            if (App.Utils.eventExists(this.componentId + '-' +'instead-sport-delete')) {
                Backbone.trigger(this.componentId + '-' + 'instead-sport-delete', {view: this, id: id});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-sport-delete', {view: this, id: id});
                var deleteModel;
                if (this.sportModelList) {
                    deleteModel = this.sportModelList.get(id);
                } else {
                    deleteModel = new this.modelClass({id: id});
                }
                deleteModel.destroy({
                    success: function() {
                        Backbone.trigger(self.componentId + '-' + 'post-sport-delete', {view: self, model: deleteModel});
                    },
                    error: function() {
                        Backbone.trigger(self.componentId + '-' + 'error', {event: 'sport-delete', view: self, error: error});
                    }
                });
            }
        },
        save: function() {
            var self = this;
            var model = $('#' + this.componentId + '-sportForm').serializeObject();
            if (App.Utils.eventExists(this.componentId + '-' +'instead-sport-save')) {
                Backbone.trigger(this.componentId + '-' + 'instead-sport-save', {view: this, model : model});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-sport-save', {view: this, model : model});
                this.currentSportModel.set(model);
                this.currentSportModel.save({},
                        {
                            success: function(model) {
                                Backbone.trigger(self.componentId + '-' + 'post-sport-save', {model: self.currentSportModel});
                            },
                            error: function(error) {
                                Backbone.trigger(self.componentId + '-' + 'error', {event: 'sport-save', view: self, error: error});
                            }
                        });
            }
        },
        _renderList: function() {
            var self = this;
            this.$el.slideUp("fast", function() {
                self.$el.html(self.listTemplate({sports: self.sportModelList.models, componentId: self.componentId, showEdit : self.showEdit , showDelete : self.showDelete}));
                self.$el.slideDown("fast");
            });
        },
        _renderEdit: function() {
            var self = this;
            this.$el.slideUp("fast", function() {
                self.$el.html(self.editTemplate({sport: self.currentSportModel, componentId: self.componentId , showEdit : self.showEdit , showDelete : self.showDelete
 
				}));
                self.$el.slideDown("fast");
            });
        }
    });
    return App.Controller._SportController;
});