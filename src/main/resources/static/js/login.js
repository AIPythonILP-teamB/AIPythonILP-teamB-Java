document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("modal");
  const openLink = document.getElementById("forgotPasswordLink");
  const closeBtn = document.getElementById("closeModal");

// linkがクリックされた時にモーダルを表示
  openLink.onclick = function () {
    modal.style.display = "block";
  };

//   // 確認ボタンがクリックされた時にモーダルを非表示
  closeBtn.onclick = function () {
    modal.style.display = "none";
  };

 // モーダルの外側がクリックされた時にモーダルを非表示
  window.onclick = function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  };
});
