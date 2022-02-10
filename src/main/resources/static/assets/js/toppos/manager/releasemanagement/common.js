$(function () {
    $('.gnb').on('mouseenter', function () {
        $('.gnb__layer').stop().slideDown(200);
        $('.gnb__depth2').stop().slideDown(200);
    });

    $('.header').on('mouseleave', function(){
        $('.gnb__layer').stop().slideUp(200);
        $('.gnb__depth2').stop().slideUp(200);
    });
});