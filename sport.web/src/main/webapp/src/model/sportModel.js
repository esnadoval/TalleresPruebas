define(['model/_sportModel'], function() {
    App.Model.SportModel = App.Model._SportModel.extend({

    });

    App.Model.SportList = App.Model._SportList.extend({
        model: App.Model.SportModel
    });

    return  App.Model.SportModel;

});