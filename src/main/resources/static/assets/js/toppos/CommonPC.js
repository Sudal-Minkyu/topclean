$(function () {
    $('.gnb').on('mouseenter', function () {
        $('.gnb__layer').stop().slideDown(200);
        $('.gnb__depth2').stop().slideDown(200);
    });

    $('.header').on('mouseleave', function(){
        $('.gnb__layer').stop().slideUp(200);
        $('.gnb__depth2').stop().slideUp(200);
    });

    // 새 헤더
    $('.n-gnb__item').on('mouseenter', function () {
        $(this).children('.n-gnb__depth1').addClass('active');
        $(this).children('.n-gnb__dim').stop().slideDown(400);
    });


    $('.n-gnb__item').on('mouseleave', function () {
        $(this).children('.n-gnb__depth1').removeClass('active');
        $(this).children('.n-gnb__dim').stop().slideUp(100);
    });
});
