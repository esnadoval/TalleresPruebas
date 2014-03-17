define(['controller/_sportController','delegate/sportDelegate'], function() {
    App.Controller.SportController = App.Controller._SportController.extend({
         postInit: function(options){
            var self = this;
            Backbone.on('sport-model-error', function(params) {
                var error = params.error;
                Backbone.trigger(self.componentId + '-' + 'error', 
                         {event: 'user-model', view: self, error:error});
            });
        } 
    });
    return App.Controller.SportController;
}); 