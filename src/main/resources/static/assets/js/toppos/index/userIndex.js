function franchiseInfo(){
    const url = "/api/user/franchiseInfo";
    CommonUI.ajax(url, "GET", null, function (req) {
        const userIndexDto = req.sendData.userIndexDto;
        // console.log(userIndexDto);
        if(userIndexDto.brName===null){
            $("#brName").text("무소속");
        }else{
            $("#brName").text(userIndexDto.brName);
        }
        $("#frName").text(userIndexDto.frName+" 점");
        $("#userName").text(userIndexDto.username);
        $("#userTel").text(userIndexDto.usertel);
    });
}





