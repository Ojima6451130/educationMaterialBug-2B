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
document.addEventListener("DOMContentLoaded", () =>{
	const hamburger = document.getElementById("hamburger");
	const navMenu = document.getElementById("navMenu");
		
	if(!hamburger || !navMenu){
		return;
	}
	
	hamburger.addEventListener("click",()=>{
	  		navMenu.classList.toggle("active");
	  		hamburger.classList.toggle("active");
	  	});
	
});
