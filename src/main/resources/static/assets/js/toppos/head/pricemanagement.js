$(function () {
    CommonUI.ajax("/api/head/addCostInfo", "GET", {}, function (req) {
        Object.keys(req.sendData.addCostDto).forEach(id => {
           $("#" + id).val(req.sendData.addCostDto[id].toLocaleString());
        });
    });


    const inputs = $("#priceManagementForm input");
    for(let i = 0; i < 5; i++) {
        inputs.eq(i).on("keyup", function () {
            const v = this.value.replace(/[^0-9.]/g, "");
            if(v.substr(-1, 1) !== ".") {
                this.value = Number(v.replace(/^(\d+.?\d{0,2})\d*$/,"$1")).toLocaleString();
            }else{
                this.value = Number(v).toLocaleString() + ".";
            }
        });
    }
    for(let i = 5; i < inputs.length; i++) {
        inputs.eq(i).on("keyup", function () {
            this.value = this.value.toInt().toLocaleString();
        });
    }
});

function onSave() {
    const inputs = $("#priceManagementForm input");
    for(let i = 5; i < inputs.length; i++) {
        inputs[i].value = inputs[i].value.toInt();
    }

    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/addCostUpdate";

    CommonUI.ajax(url, "POST", formData, function (req){
        alertSuccess("가격 기초정보 저장 완료");
    });

    for(let i = 5; i < inputs.length; i++) {
        inputs[i].value = inputs[i].value.toInt().toLocaleString();
    }

}