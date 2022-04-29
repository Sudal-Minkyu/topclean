$(function () {
    CommonUI.ajax("/api/head/addCostInfo", "GET", {}, function (req) {
        console.log(req.sendData);
        Object.keys(req.sendData.addCostDto).forEach(id => {
            if($("#" + id).attr('type') === "number") {
                $("#" + id).val(req.sendData.addCostDto[id]);
            } else {
                $("#" + id).val(req.sendData.addCostDto[id].toLocaleString());
            }

        });
    });

    const inputs = $("#priceManagementForm input");
    // for(let i = 0; i < 9; i++) {
    //     inputs.eq(i).on("keyup", function () {
    //         const v = this.value.replace(/[^0-9.]/g, "");
    //         if(v !== "" && v !== ".") {
    //             console.log(v.split('.'));
    //             this.value = v;
    //         }else {
    //             this.value = "0"
    //         }
    //     });
    // }
    const separator = function(i) {
        inputs.eq(i).on("keyup", function () {
            this.value = this.value.toInt().toLocaleString();
        });
    }
    for(let i = 9; i < inputs.length; i++) {
        separator(i)
    }
    separator(5);
    separator(7);
});

function onSave() {
    const inputs = $("#priceManagementForm input");
    // 빈 값 체크
    for (let i = 0; i < inputs.length; i++) {
        if(inputs[i].value === "") {
            alertCaution('값을 모두 입력해주세요', 1);
            return false;
        }
    }
    // 0 - 100 사이 입력
    if($('.discountRate').val() < 0 || $('.discountRate').val() > 100) {
        alertCaution('할인율은 0 ~ 100 사이로 입력해주세요.', 1);
        return false
    }
    // 값을 숫자로 변환
    for(let i = 9; i < inputs.length; i++) {
        inputs[i].value = inputs[i].value.numString();
    }
    inputs[5].value = inputs[5].value.numString();
    inputs[7].value = inputs[7].value.numString();

    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/addCostUpdate";

    CommonUI.ajax(url, "POST", formData, function (req){
        console.log(Object.fromEntries(formData));
        console.log(req);
        alertSuccess("가격 기초정보 저장 완료");
    });
    // 금액에 쉽표 표시
    for(let i = 9; i < inputs.length; i++) {
        inputs[i].value = inputs[i].value.toInt().toLocaleString();
    }
    inputs[5].value = inputs[5].value.toInt().toLocaleString();
    inputs[7].value = inputs[7].value.toInt().toLocaleString();
}