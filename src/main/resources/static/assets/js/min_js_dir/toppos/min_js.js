function logout(){
    localStorage.setItem("Authorization",null);
    location.href ="/login";
}

// function logreg(num,menuName1,menuName2,data) {
//     JWT_Get();
//
//     if (accessToken == null && refreshToken == null && insert_id == null) {
//         // console.log("callinfo(userid)함수 : 토큰&리플레시&로그인한아이디 Null");
//         alertCaution("토큰이 만료되었습니다.<BR>다시 로그인해주세요.", 2);
//     } else {
//
//         let typeName;
//
//         if (num === 1) {
//             typeName = "open";
//         } else {
//             typeName = "search";
//         }
//
//         const params = {
//             menuName1: menuName1,
//             menuName2: menuName2,
//             useType: typeName,
//             data: data
//         };
//
//         let url;
//         url = $("#backend_protocol").val() + "://" + $("#backend_url").val() + "/api/userLog/logreg"; // 호출할 백엔드 API
//         console.log("url : " + url);
//         $.ajax({
//             url: url,
//             type: 'Post',
//             data: params,
//             cache: false,
//             beforeSend: function (xhr) {
//                 xhr.setRequestHeader("JWT_AccessToken", accessToken);
//                 xhr.setRequestHeader("insert_id", insert_id);
//             },
//             error: function (request) {
//                 if (request.status === 500) {
//                     // console.log("500에러 재로그인 해주세요.");
//                     alertCaution("500에러 재로그인 해주세요.", 2);
//                 } else {
//                     // console.log("404에러 재로그인 해주세요.");
//                     alertCaution("404에러 재로그인 해주세요.", 2);
//                 }
//             },
//             success: function (request) {
//                 let status = request.status;
//                 if (status === 200) {
//                     console.log(menuName1+" / "+menuName2+" 로그기록 완료");
//                 }
//             }
//         });
//     }
// }
//
// //한글을 지우는 함수
// function delHangle(evt){
//     var objTarget = evt.srcElement || evt.target;
//     var _value = event.srcElement.value;
//     if(/[ㄱ-ㅎㅏ-ㅡ가-핳]/g.test(_value)) {
//         objTarget.value = null;
//     }
// }
// // 소수점한개로 제한한 실수값입력할수있게하는 함수
// function isNumberKey(evt,num) {
//     const charCode = (evt.which) ? evt.which : event.keyCode;
//     const _value = event.srcElement.value;
//     if (event.keyCode < 48 || event.keyCode > 57) {
//         if (event.keyCode !== 46) {
//             return false;
//         }
//     }
//     // 소수점(.)이 두번 이상 나오지 못하게
//     const _pattern0 = /^\d*[.]\d*$/;
//     if (_pattern0.test(_value)) {
//         if (charCode === 46) {
//             return false;
//         }
//     }
//
//     if(num===1){
//         // 소수점 넷째자리까지만 입력가능
//         const _pattern2 = /^\d*[.]\d{4}$/;
//         if (_pattern2.test(_value)) {
//             return false;
//         }
//     }
// }
//
// // 숫자만 입력하게하고, 3번째 숫자마다 콤마(,)를 찍어주는 함수(input용)
// function inputNumberFormat(obj) {
//     obj.value = comma(uncomma(obj.value));
// }
// function comma(str) {
//     str = String(str);
//     return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
// }
// function uncomma(str) {
//     str = String(str);
//     return str.replace(/[^\d]+/g, '');
// }
//
// // 숫자콤마찍는 함수(output용)
// function pushComma(num){
//     let len, point, str;
//     num = num + "";
//     point = num.length % 3 ;
//     len = num.length;
//     str = num.substring(0, point);
//     while (point < len) {
//         if (str !== "") str += ",";
//         str += num.substring(point, point + 3);
//         point += 3;
//     } return str;
// }