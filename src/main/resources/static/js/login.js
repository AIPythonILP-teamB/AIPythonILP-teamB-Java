document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("modal");
  const openLink = document.getElementById("forgotPasswordLink");
  const closeBtn = document.getElementById("closeModal");

  // リンクがクリックされたときモーダル表示
  openLink.addEventListener("click", function (event) {
    event.preventDefault(); // リンクのジャンプを防止
    modal.style.display = "block";
  });

  // 閉じるボタンがクリックされたとき非表示
  closeBtn.addEventListener("click", function () {
    modal.style.display = "none";
  });

  // モーダルの外側をクリックしたら閉じる
  window.addEventListener("click", function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
});
