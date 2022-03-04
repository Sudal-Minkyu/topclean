const dtos = {
    send: {

    },
    receive: {

    }
};

const urls = {
    
}

const comms = {
    
};

const trigs = {
    basic() {
        $("#pickUpBtn").on("click", function() {

        });
    },
}

const wares = {
    
}

$(function() {
    onPageLoad();
});

function onPageLoad() {
    trigs.basic();
    updateTime();
    setInterval(updateTime, 1000);
}

function updateTime() {
    const now = new Date().toLocaleString();
    $("#timeNow").html(now);
}