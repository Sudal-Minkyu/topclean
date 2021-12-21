$(function () {
    /*
    CommonUI.ajax("/api/head/addCostInfo", "GET", {}, function (req) {
        console.log(req);
    });
    */
});

function onSave() {
    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/addCostUpdate";

    console.log(Object.fromEntries(formData));
    /*
    CommonUI.ajax(url, "POST", formData, function (req){
        console.log(req);
        alertSuccess("가격 기초정보 저장 완료");
    });
    */
}