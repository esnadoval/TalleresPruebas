define([], function() {
    App.Model._SportModel = Backbone.Model.extend({
        defaults: {
 
		 'name' : ''
 ,  
		 'minAge' : ''
 ,  
		 'maxAge' : ''
        },
        initialize: function() {
            this.on('invalid', function(error) {
                Backbone.trigger('sport-model-error', error);
            });
        },               
        validate: function(attrs, options){
           if (attrs.name == '') {
                return "You must set a value in Attribute";
            }
        },
        getDisplay: function(name) {
         return this.get(name);
        }
    });

    App.Model._SportList = Backbone.Collection.extend({
        model: App.Model._SportModel,
        initialize: function() {
        }

    });
    return App.Model._SportModel;
});