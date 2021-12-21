$(function () {
    CommonUI.ajax("/api/head/addCostInfo", "GET", {}, function (req) {
        Object.keys(req.sendData.addCostDto).forEach(id => {
           $("#" + id).val(req.sendData.addCostDto[id]);
        });
    });
});

function onSave() {
    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/addCostUpdate";

    CommonUI.ajax(url, "POST", formData, function (req){
        alertSuccess("가격 기초정보 저장 완료");
    });
}