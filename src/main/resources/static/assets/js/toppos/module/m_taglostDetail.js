function getTaglostDetail(getCondition) {
    CommonUI.ajax("/api/user/tagGalleryDetail", "GET", getCondition, function (res) {
        const data = res.sendData;

        const refinedData = {
            frCompleteCheck: data.frCompleteCheck,
            btId: data.tagGallery.btId,
            btBrandName: data.tagGallery.btBrandName,
            btInputDate: data.tagGallery.btInputDate,
            btMaterial: data.tagGallery.btMaterial,
            btRemark: data.tagGallery.btRemark,
            brCloseYn: data.tagGallery.brCloseYn,
            tagGalleryFileList: data.tagGalleryFileList,
            tagGalleryCheckList: data.tagGalleryCheckList,
        }

        wares.currentRequest = refinedData;
        resetTaglostPop();

        for(const photo of wares.currentRequest.tagGalleryFileList) {
            const photoHtml = `<li class="tag-imgs__item">
                <a href="${photo.bfPath + photo.bfFilename}" data-lightbox="images" data-title="이미지 확대">
                    <img src="${photo.bfPath + "s_" + photo.bfFilename}" class="tag-imgs__img" alt=""/>
                </a>
            </li>`
            $("#photoList").append(photoHtml);
        }

        /* 가맹점 응답의 텍스트 구성 */
        let responseList = ""
        for(const fr of wares.currentRequest.tagGalleryCheckList) {
            responseList += fr.frName + " ";
            responseList += fr.brCompleteYn === "Y" ? "(완료)" : "(확인요청)";
            responseList += " ,"
        }
        $("#frResponse").val(responseList.substring(0, responseList.length - 2));

        $("#btBrandName").val(wares.currentRequest.btBrandName);
        $("#btMaterial").val(wares.currentRequest.btMaterial);
        $("#btRemark").val(wares.currentRequest.btRemark);
        $("#btInputDate").val(wares.currentRequest.btInputDate);


        const $frCheck = $("#frCheck");
        const $frCheckLabel = $("#frCheck").siblings("label");
        const $frComplete = $("#frComplete");

        switch(wares.currentRequest.frCompleteCheck) {
            case 0 :
                $frCheck.prop("checked", false);
                $frCheckLabel.show();
                $frCheck.prop("disabled", false);
                $frComplete.hide();
                $frComplete.prop("disabled", false);
                break;
            case 1 :
                $frCheck.prop("checked", true);
                $frCheckLabel.show();
                $frCheck.prop("disabled", false);
                $frComplete.show();
                $frComplete.prop("disabled", false);
                break;
            case 2 :
                $frCheck.prop("checked", true);
                $frCheckLabel.hide();
                $frComplete.hide();
                break;
        }

        if(wares.currentRequest.brCloseYn === "Y") {
            $frCheckLabel.hide();
            $frComplete.hide();
        }

        openTaglostPop();
    });
}
function resetTaglostPop() {
    $("#btBrandName").val("");
    $("#btMaterial").val("");
    $("#btRemark").val("");
    $("#frResponse").val("");
    $("#photoList").html("");
}

function taglostCheck(answer) {
    const tagGalleryCheck = {
        btId: "nr",
        type: "sr", // 체크 혹은 체크해제 1, 최종확인 2
    }
    dv.chk(answer, tagGalleryCheck, "체크나 최종완료 신호 보내기");
    CommonUI.ajax("/api/user/tagGalleryCheck", "PARAM", answer, function (res) {
        const getCondition = {
            btId: wares.currentRequest.btId,
        }
        getTaglostDetail(getCondition);
    });
}

export {getTaglostDetail, resetTaglostPop, taglostCheck};