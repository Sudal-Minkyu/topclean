function onSave() {
    const formData = new FormData(document.getElementById('priceManagementForm'));

    let url = "/api/head/";

    console.log(Object.fromEntries(formData));
    /*
    CommonUI.ajax(url, "POST", formData, function (req){
        alertSuccess("가격 기초정보 저장 완료");
    });
    */
}