$(function() {
    const $tabsBtn = $('.c-tabs__btn');
    const $tabsContent = $('.c-tabs__content');

    $tabsBtn.on('click', function () {
        const idx = $(this).index();

        $tabsBtn.removeClass('active');
        $tabsBtn.eq(idx).addClass('active');
        $tabsContent.removeClass('active');
        $tabsContent.eq(idx).addClass('active');
    });
})