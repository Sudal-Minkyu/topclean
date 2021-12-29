
// 비밀번호 수정 실행 함수
function passwordUpdate(){
    const $oldpassword = $("#oldpassword");
    if(!$oldpassword.val().length) {
        alertCaution("현재비밀번호를 입력해 주세요", 1);
        return false;
    }

    const $newpassword = $("#newpassword");
    if(!$newpassword.val().length) {
        alertCaution("신규비밀번호를 입력해 주세요", 1);
        return false;
    }

    const $passwordconfirm = $("#passwordconfirm");
    if(!$passwordconfirm.val().length) {
        alertCaution("신규번호확인을 입력해 주세요", 1);
        return false;
    }

    const formData = new FormData();
    formData.append("oldpassword", $oldpassword.val());
    formData.append("newpassword", $newpassword.val());
    formData.append("passwordconfirm", $passwordconfirm.val());

    CommonUI.ajax("/api/user/franchisePassword", "POST", formData, function (req) {
        $oldpassword.val('');
        $newpassword.val('');
        $passwordconfirm.val('');
        alertSuccess("비밀번호 변경 완료");
    });

}