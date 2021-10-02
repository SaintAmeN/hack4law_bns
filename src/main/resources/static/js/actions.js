function fire_request(identifier, event) {
    event.preventDefault();

    console.log(identifier);

    var dataimageRequestParams = {};
    dataimageRequestParams["dataimageId"] = identifier;

    console.log(dataimageRequestParams);

    return false;
};


// $(document).ready(function () {
//
//     $("#update-dataimage-validation").submit(function (event) {
//
//         //stop submit the form, we will post it manually.

//
//         fire_ajax_request();
//
//     });
//
// });
