var han;
var hanend;
han = String.fromCharCode(44032);
han2 = String.fromCharCode(55215);


var a1 = "ㄱ";
var a2 = "ㅏ";
var a3 = "ㄻ";

console.log(han);
console.log(han.charCodeAt(0));

console.log(a1.charCodeAt(0));
console.log(a2.charCodeAt(0));
console.log(a3.charCodeAt(0));

console.log("-----------");

var CHO = [
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
    'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
    'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ',
    'ㅍ', 'ㅎ'
]

CHO.forEach(element => {
    console.log(element.charCodeAt(0));
});

console.log("-----------");

for (let i = 12593; i < 12644; i++) {
    console.log(String.fromCharCode(i));
}
