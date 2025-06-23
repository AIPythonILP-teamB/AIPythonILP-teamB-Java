// user_manage.js

let users = [];
let editIndex = -1;

// 初期表示：サーバーからユーザー一覧を取得
function fetchUsers() {
  fetch('/api/users')
    .then(res => res.json())
    .then(data => {
      users = data;
      renderUserTable();
    })
    .catch(err => console.error('取得失敗:', err));
}

// テーブル描画
function renderUserTable() {
  const tbody = document.querySelector("#userTable tbody");
  tbody.innerHTML = "";
  users.forEach((u, i) => {
    const statusText = u.isActive ? "休職/退職" : "在籍";
    const adminText = u.role === "ADMIN" ? "あり" : "なし";

    const row = `
      <tr>
        <td>${u.userName}</td>
        <td>${statusText}</td>
        <td>${adminText}</td>
        <td>${u.email}</td>
        <td>
          <a href="#" onclick="openResetModal(${i}); return false;">再設定</a>
        </td>
        <td>
          <a href="#" onclick="openEditModal(${i}); return false;">編集</a>
        </td>
          </tr>
    `;
    tbody.innerHTML += row;
  });
}

// 新規作成モーダルを開く
function openCreateModal() {
  document.getElementById("newName").value = "";
  document.getElementById("newAdmin").value = "なし";
  document.getElementById("newEmail").value = "";
  document.getElementById("newPassword").value = "";
  document.getElementById("addError").textContent = "";
  document.getElementById("createModal").style.display = "flex";
}


//  アカウント追加処理
function addUser() {
  const userName = document.getElementById("newName").value.trim();
  const role = document.getElementById("newAdmin").value === "あり" ? "ADMIN" : "USER";
  const email = document.getElementById("newEmail").value.trim();
  const password = document.getElementById("newPassword").value;
  const errorEl = document.getElementById("addError");

  if (!userName || !email || !password) {
    errorEl.textContent = "名前・メール・パスワードは必須です";
    return;
  }
  fetch('/api/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userName, role, email, password })
  })
    .then(res => {
      if (!res.ok) throw new Error('登録失敗');
      return res.json();
    })
    .then(() => {
      closeModal('createModal');
      fetchUsers();
    })
    .catch(err => { errorEl.textContent = err.message; });
}

//  編集モーダルを開く
function openEditModal(idx) {
  editIndex = idx;
  const u = users[idx];
  document.getElementById("editName").value = u.userName;
  document.getElementById("editAdmin").value = u.role === "ADMIN" ? "あり" : "なし";
  document.getElementById("editEmail").value = u.email;
  document.getElementById("editError").textContent = "";
  document.getElementById("editModal").style.display = "flex";
}

//  更新
function updateUser() {
  const u = users[editIndex];
  const userName = document.getElementById("editName").value.trim();
  const role = document.getElementById("editAdmin").value === "あり" ? "ADMIN" : "USER";
  const email = document.getElementById("editEmail").value.trim();
  const errorEl = document.getElementById("editError");

  if (!userName || !email) {
    errorEl.textContent = "名前とメールは必須です";
    return;
  }

  fetch(`/api/users/${u.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userName, role, emai })
  })
    .then(res => {
      if (!res.ok) throw new Error('更新失敗');
      return res.json();
    })
    .then(() => {
      closeModal('editModal');
      fetchUsers();
    })
    .catch(err => { errorEl.textContent = err.message; });
}

//  論理削除
function deleteUser(idx) {
  if (!confirm('本当に削除しますか？')) return;
  const u = users[idx];
  fetch(`/api/users/${u.id}`, { method: 'DELETE' })
    .then(res => {
      if (!res.ok) throw new Error('削除失敗');
      fetchUsers();
    })
    .catch(err => alert(err.message));
}

//  パスワード再設定モーダルを開く
function openResetModal(idx) {
  editIndex = idx;
  document.getElementById("resetPw1").value = "";
  document.getElementById("resetPw2").value = "";
  document.getElementById("resetModal").style.display = "flex";
}

//　パスワードを再設定する
// パスワード再設定処理
function resetPassword() {
  const pw1 = document.getElementById("resetPw1").value;
  const pw2 = document.getElementById("resetPw2").value;
  const u = users[editIndex]; // openResetModal で設定されたユーザー

  if (!pw1 || !pw2) {
    alert("パスワードを両方入力してください");
    return;
  }

  if (pw1 !== pw2) {
    alert("パスワードが一致しません");
    return;
  }

  fetch(`/api/users/${u.id}/reset-password`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ password: pw1 })
  })
    .then(res => {
      if (!res.ok) throw new Error("再設定失敗");
      closeModal("resetModal");
      openModal("doneModal"); // 成功モーダル表示
    })
    .catch(err => alert(err.message));
}

// モーダルを閉じる
function closeModal(id) {
  document.getElementById(id).style.display = 'none';
}

// ページ読み込み時に一覧を取得
document.addEventListener("DOMContentLoaded", fetchUsers);

//openModal()を定義する
function openModal(id) {
  document.getElementById(id).style.display = 'flex';
}