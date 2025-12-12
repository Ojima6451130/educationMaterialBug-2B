/**
 * ファイル名：check.js
 * 共通処理
 *
 * 変更履歴
 * 1.0  2010/09/10 Kazuya.Naraki
 */

/**
 *  ログアウトボタンサブミット
 */
function logout() {
    document.forms[0].action = "/kikin/";
    document.forms[0].submit();
}

// 凡例表示
function openWindow() {
    // 新しいウィンドウを開く
    window.open("/kikin/ShiftPattern","windowBPopup", "menubar=no, toolbar=no, scrollbars=auto, resizable=yes, width=520px, height=650px");
    
}

/**
 *  戻るボタンサブミット
 */
function doSubmit(action) {
    document.forms[0].action = "/kikin/";
    document.forms[0].submit();
}

//ハンバーガーメニューのｊｓ
// ハンバーガーメニュー開閉
document.addEventListener("DOMContentLoaded", () => {

    const hamburger = document.getElementById("hamburger");

    hamburger.addEventListener("click", (e) => {
        e.stopPropagation();
        hamburger.classList.toggle("oppenned");
    });

    // メニュー外をクリックしたら閉じる
    document.body.addEventListener("click", () => {
        hamburger.classList.remove("oppenned");
    });

    // × 部分のクリックも閉じる
    document.querySelectorAll("#hamburger .cls").forEach(c => {
        c.addEventListener("click", (e) => {
            hamburger.classList.remove("oppenned");
            e.stopPropagation();
        });
    });
});

