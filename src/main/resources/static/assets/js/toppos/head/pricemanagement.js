$(function () {
    CommonUI.ajax("/api/head/addCostInfo", "GET", {}, function (req) {
        Object.keys(req.sendData.addCostDto).forEach(id => {
           $("#" + id).val(req.sendData.addCostDto[id].toLocaleString());
        });
    });
    $("#priceManagementForm input").on("keyup", function (e) {
        this.value = this.value.toInt().toLocaleString();
    });
});

function onSave() {
    const inputs = $("#priceManagementForm input");
    inputs.each(function (index, item) {
        item.value = item.value.toInt();
    });
    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/addCostUpdate";

    CommonUI.ajax(url, "POST", formData, function (req){
        alertSuccess("가격 기초정보 저장 완료");
    });

    inputs.each(function (index, item) {
        item.value = item.value.toInt().toLocaleString();
    });

}