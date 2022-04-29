const dtos = {
    send: {

    },
    receive: {

    }
};

const comms = {
};

const trigs = {
    basic() {
    },
}

const wares = {

}

$(function() {
    onPageLoad();
});

function onPageLoad() {
    trigs.basic();
    chkParams();
}

function chkParams() {
    const url = new URL(window.location.href);
    wares.params = url.searchParams;

    if(wares.params.has("fiid")) {
        const fiId = wares.params.get("fiid");

        const target = {
            fiId: parseInt(fiId),
        }

        //
        // comms.getReceiptData(target);
    }
}