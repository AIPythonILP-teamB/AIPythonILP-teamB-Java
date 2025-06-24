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
        })

        .catch(error => {
            console.error('エラー:', error);
            renderNoData(dateStr);  // ネットワークやサーバーエラー時も処理
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


