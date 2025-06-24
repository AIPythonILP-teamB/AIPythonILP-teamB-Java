
// 商品管理ページの処理
let products = [];
let editIndex = -1;

// 初期表示：サーバーから商品一覧を取得
function fetchProducts() {
    fetch('/api/products')
        .then(res => res.json())
        .then(data => {
            products = data;
            renderProductTable();
        })
        .catch(err => console.error('取得失敗:', err));
}

function renderProductTable() {
    const tbody = document.querySelector("#productTable tbody");
    tbody.innerHTML = "";
    products.forEach((p, i) => {
        const row = `
      <tr>
        <td>${p.productName}</td>
        <td>${p.price}</td>
        <td>${p.janCode}</td>
        <td>
          <a href="#" onclick="openEditModal(${i})">編集</a>
        </td>
      </tr>
    `;
        tbody.innerHTML += row;
    });
}


function addProduct() {
    const name = document.getElementById("addName").value.trim();
    const price = document.getElementById("addPrice").value.trim();
    const jan = document.getElementById("addJan").value.trim();
    const errorEl = document.getElementById("addError");

    // バリデーションチェック
    if (!name || !price || !jan) {
        errorEl.textContent = "全ての項目を入力してください。";
        return;
    }

    if (!validateProductInput(name, price, jan, errorEl)) return;

    const product = {
        productName: name,
        price: parseInt(price),
        janCode: jan
    };

    fetch('/api/products', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product)
    })
        .then(res => {
            if (!res.ok) throw new Error("登録に失敗しました");
            closeModal("addModal");
            fetchProducts();
        })
        .catch(err => {
            errorEl.textContent = "サーバーエラーが発生しました。";
            console.error(err);
        });
}

function updateProduct() {
    const name = document.getElementById("editName").value.trim();
    const price = document.getElementById("editPrice").value.trim();
    const jan = document.getElementById("editJan").value.trim();
    const errorEl = document.getElementById("editError");

    // バリデーションチェック
    if (!name || !price || !jan) {
        errorEl.textContent = "全ての項目を入力してください。";
        return;
    }

    if (!validateProductInput(name, price, jan, errorEl)) return;

    const updated = {
        id: products[editIndex].id,
        productName: name,
        price: parseInt(price),
        janCode: jan
    };

    fetch(`/api/products/${updated.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updated)
    })
        .then(res => {
            if (!res.ok) throw new Error("更新に失敗しました");
            closeModal("editModal");
            fetchProducts();
        })
        .catch(err => {
            errorEl.textContent = "サーバーエラーが発生しました。";
            console.error(err);
        });
}

// 共通バリデーション関数
function validateProductInput(name, price, jan, errorEl) {
    if (!name || !price || !jan) {
        errorEl.textContent = "全ての項目を入力してください。";
        return false;
    }
    if (!/^\d+$/.test(price) || parseInt(price) < 0) {
        errorEl.textContent = "価格は0以上の数値で入力してください。";
        return false;
    }
    if (!/^\d{13}$/.test(jan)) {
        errorEl.textContent = "JANコードは13桁の数字で入力してください。";
        return false;
    }
    return true;
}

function openAddModal() {
    document.getElementById("addName").value = "";
    document.getElementById("addPrice").value = "";
    document.getElementById("addJan").value = "";
    document.getElementById("addError").textContent = "";
    document.getElementById("addModal").style.display = "flex";
}

function openEditModal(index) {
    editIndex = index;
    const p = products[index];
    document.getElementById("editName").value = p.productName;
    document.getElementById("editPrice").value = p.price;
    document.getElementById("editJan").value = p.janCode;
    document.getElementById("editError").textContent = "";
    document.getElementById("editModal").style.display = "flex";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}




function deleteProductFromModal() {
    const id = products[editIndex].id;
    if (!confirm('本当に削除しますか？')) return;
    fetch(`/api/products/${id}`, {
        method: 'DELETE'
    })
        .then(() => {
            closeModal("editModal");
            fetchProducts();
        })
        .catch(err => console.error('削除に失敗:', err));
}
function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

document.addEventListener('DOMContentLoaded', fetchProducts);