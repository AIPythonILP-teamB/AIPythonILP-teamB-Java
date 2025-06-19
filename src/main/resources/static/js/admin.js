function navigate(target) {
    switch (target) {
        case 'forecast':
            window.location.href = '/forecast';
            break;
        case 'user':
            window.location.href = '/users/list';
            break;
        case 'product':
            window.location.href = '/products';
            break;
        case 'salesInput':
            window.location.href = '/sales/input';
            break;
        case 'salesList':
            window.location.href = '/sales/list';
            break;
    }
}


document.addEventListener('DOMContentLoaded', () => {
    const calendarEl = document.getElementById('calendar-area');
    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ja',
        headerToolbar: {
            left: 'prev,next',
            center: 'title',
            right: ''
        },
        // ① REST API からイベントを取得
        events: (fetchInfo, successCallback, failureCallback) => {
            const start = fetchInfo.startStr;
            const end = fetchInfo.endStr;
            fetch(`/api/calendar/events?start=${start}&end=${end}`)
                .then(res => res.json())
                .then(events => successCallback(events))
                .catch(err => failureCallback(err));
        },
        eventDidMount: function (info) {
            const iconCode = info.event.extendedProps.icon;
            if (iconCode) {
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}@2x.png`;
                const img = document.createElement('img');
                img.src = iconUrl;
                img.style.width = '25px';
                img.style.display = 'block';
                info.el.prepend(img); // イベントセルの先頭に追加
            }
            // 親の日付セルにアクセスして背景色を設定
            const cell = info.el.closest(".fc-daygrid-day");
            if (cell) {
                cell.style.backgroundColor = "rgba(135, 206, 250, 0.2)";  // 例: 水色
            }
        },

        // ② セルクリックで詳細モーダルを開く
        dateClick: info => openDetail(info.dateStr)
    },

    );

    calendar.render();
});

// 詳細を開くロジック
function openDetail(dateStr) {
    fetch(`/api/calendar/detail?date=${dateStr}`)
        .then(res => {
            if (!res.ok) throw new Error('データ取得に失敗しました');
            return res.json();
        })
        .then(data => {
            // 必須データが存在しない場合は「見つかりません」表示
            if (!data || !data.weatherMain || data.maxTemp == null || data.minTemp == null || data.windSpeed == null) {
                renderNoData(dateStr);
                return;
            }

            // 正常に取得できた場合の表示処理
            document.getElementById('modalDate').textContent = `${data.date} の詳細情報`;
            document.getElementById('weatherIcon').textContent = data.weatherMain;
            document.getElementById('weatherInfo').textContent = `最高/最低：${data.maxTemp}/${data.minTemp} ℃ 風速：${data.windSpeed} m/s`;
            document.getElementById('totalSales').textContent = `合計販売本数：${data.totalSales} 本`;

            const tbody = document.getElementById('productSales');
            tbody.innerHTML = Array.isArray(data.productSales)
                ? data.productSales.map(ps => `<tr><td>${ps.name}</td><td>${ps.quantity}</td></tr>`).join('')
                : '';
        })
        .catch(error => {
            console.error('エラー:', error);
            renderNoData(dateStr);  // ネットワークやサーバーエラー時も処理
        });
}

function renderNoData(dateStr) {
    document.getElementById('modalDate').textContent = `${dateStr} の詳細情報`;
    document.getElementById('weatherIcon').textContent = '';
    document.getElementById('weatherInfo').textContent = 'データが見つかりません。';
    document.getElementById('totalSales').textContent = '';
    const tbody = document.getElementById('productSales');
    if (tbody) tbody.innerHTML = '';
}

document.getElementById("userName").textContent = loginUserName;

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
    document.getElementById("addError").textContent = ""; // エラーリセット
    document.getElementById("addModal").style.display = "flex";
}

function openEditModal(index) {
    editIndex = index;
    const p = products[index];
    document.getElementById("editName").value = p.productName;
    document.getElementById("editPrice").value = p.price;
    document.getElementById("editJan").value = p.janCode;
    document.getElementById("editError").textContent = ""; // エラーリセット
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

