define(['component/_CRUDComponent', 'controller/toolbarController', 'model/toolbarModel', 'model/sportModel', 'controller/sportController'], function() {
    App.Component.SportComponent = App.Component._CRUDComponent.extend({
        name: 'sport',
        model: App.Model.SportModel,
        listModel: App.Model.SportList,
        controller : App.Controller.SportController
    });
    return App.Component.SportComponent;
});