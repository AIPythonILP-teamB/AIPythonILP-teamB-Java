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
        // REST API からイベントを取得
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
                img.style.pointerEvents = 'none'; //クリックを通す設定
                info.el.prepend(img); // イベントセルの先頭に追加
            }
            // 親の日付セルにアクセスして背景色を設定
            const cell = info.el.closest(".fc-daygrid-day");
            if (cell) {
                cell.style.backgroundColor = "rgba(135, 206, 250, 0.2)";
            }
            info.el.style.pointerEvents = 'none'; //イベント本体にもクリックを通す設定（販売本数などテキストを含む）
        },

        // セルクリックで詳細モーダルを開く
        dateClick: info => openDetail(info.dateStr)
    },

    );

    calendar.render();
});

// 詳細を開くロジック
// function openDetail(dateStr) {
//     fetch(`/api/calendar/detail?date=${dateStr}`)
//         .then(res => {
//             if (!res.ok) throw new Error('データ取得に失敗しました');
//             return res.json();
//         })
//         .then(data => {
//             // 必須データが存在しない場合は「見つかりません」表示
//             if (!data || !data.weatherMain || data.maxTemp == null || data.minTemp == null || data.windSpeed == null) {
//                 renderNoData(dateStr);
//                 return;
//             }

//             // 正常に取得できた場合の表示処理
//             document.getElementById('modalDate').textContent = `${data.date} の詳細情報`;
//             document.getElementById('weatherIcon').textContent = data.weatherMain;
//             document.getElementById('weatherInfo').textContent = `最高/最低：${data.maxTemp}/${data.minTemp} ℃ \n 風速：${data.windSpeed} m/s`;
//             document.getElementById('totalSales').textContent = `合計販売本数：${data.totalSales} 本`;

//             const tbody = document.getElementById('productSales');
//             tbody.innerHTML = Array.isArray(data.productSales)
//                 ? data.productSales.map(ps => `<tr><td>${ps.name}</td><td>${ps.quantity}</td></tr>`).join('')
//                 : '';
//         })
//         .catch(error => {
//             console.error('エラー:', error);
//             renderNoData(dateStr);  // ネットワークやサーバーエラー時も処理
//         });
// }

function openDetail(dateStr) {
    fetch(`/api/calendar/detail?date=${dateStr}`)
        .then(res => {
            if (!res.ok) throw new Error('データ取得に失敗しました');
            return res.json();
        })
        .then(data => {
            if (!data || !data.weatherMain || data.maxTemp == null || data.minTemp == null || data.windSpeed == null) {
                renderNoData(dateStr);
                return;
            }

            document.getElementById('modalDate').textContent = `${data.date} の詳細情報`;

            // アイコン画像を生成して表示
            const weatherIconDiv = document.getElementById('weatherIcon');
            weatherIconDiv.innerHTML = '';
            if (data.icon) {
                const iconUrl = `https://openweathermap.org/img/wn/${data.icon}@2x.png`;
                const img = document.createElement('img');
                img.src = iconUrl;
                img.alt = data.weatherMain;
                img.style.width = '50px';
                weatherIconDiv.appendChild(img);
            }

            // 補足情報（テキスト）はweatherInfoに表示
            document.getElementById('weatherInfo').innerHTML = `
                <p><strong>天気：</strong>${data.weatherMain}</p>
                <p><strong>気温：</strong>
                    <span class="temp-max">${data.maxTemp}℃</span> /
                    <span class="temp-min">${data.minTemp}℃</span>
                </p>
                <p><strong>風速：</strong>${data.windSpeed} m/s</p>
            `;


            document.getElementById('totalSales').innerHTML = `
                <p><strong>合計販売本数：</strong>${data.totalSales} 本</p>
            `;


            const tbody = document.getElementById('productSales');
            tbody.innerHTML = Array.isArray(data.productSales)
                ? data.productSales.map(ps => `<tr><td>${ps.name}</td><td>${ps.quantity}</td></tr>`).join('')
                : '';
                
            // 編集ボタン追加 or 再利用
            let editBtn = document.getElementById('editButton');
            if (!editBtn) {
                editBtn = document.createElement('button');
                editBtn.id = 'editButton';
                editBtn.className = 'button confirm';
                document.getElementById('detail-area').appendChild(editBtn);
            }
            editBtn.textContent = '編集';
            // イベントリスナーを毎回更新する
            editBtn.onclick = () => openEditForm(dateStr);
            const saveBtn = document.getElementById('saveButton');
            if (saveBtn) saveBtn.remove();
        })

        .catch(error => {
            console.error('エラー:', error);
            renderNoData(dateStr);
        });
}

function openEditForm(dateStr) {
    fetch(`/api/calendar/sales?date=${dateStr}`)
        .then(res => res.json())
        .then(sales => {
            const tbody = document.getElementById('productSales');
            tbody.innerHTML = ''; // クリア

            sales.forEach(sale => {
                const tr = document.createElement('tr');

                const tdName = document.createElement('td');
                tdName.textContent = sale.productName;

                const tdQuantity = document.createElement('td');
                const input = document.createElement('input');
                input.type = 'number';
                input.value = sale.quantity;
                input.dataset.id = sale.id;
                input.style.width = '60px';
                tdQuantity.appendChild(input);

                tr.appendChild(tdName);
                tr.appendChild(tdQuantity);
                tbody.appendChild(tr);
            });

            // 保存ボタン作成（既にあれば無視）
            if (!document.getElementById('saveButton')) {
                const saveBtn = document.createElement('button');
                saveBtn.id = 'saveButton';
                saveBtn.textContent = '保存';
                saveBtn.className = 'button confirm';
                saveBtn.addEventListener('click', () => saveEditedSales(dateStr));
                document.getElementById('detail-area').appendChild(saveBtn);
            }

            let editBtn = document.getElementById('editButton');
            if (editBtn) {
                editBtn.textContent = '戻る';
                editBtn.onclick = () => openDetail(dateStr);
            }
        });
}
function saveEditedSales(dateStr) {
    const inputs = document.querySelectorAll('#productSales input');
    const updates = Array.from(inputs).map(input => ({
        id: input.dataset.id,
        quantity: input.value
    }));

    Promise.all(updates.map(u =>
        fetch(`/api/calendar/sale/${u.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ quantity: u.quantity })
        })
    )).then(() => {
        alert('更新しました');
        openDetail(dateStr); // 再描画
    }).catch(err => {
        console.error('保存エラー', err);
        alert('保存に失敗しました');
    });
}


function renderNoData(dateStr) {
    document.getElementById('modalDate').textContent = `${dateStr} の詳細情報`;
    document.getElementById('weatherIcon').innerHTML = '';
    document.getElementById('weatherInfo').textContent = 'データが見つかりません。';
    document.getElementById('totalSales').textContent = '';
    const tbody = document.getElementById('productSales');
    if (tbody) tbody.innerHTML = '';
}


