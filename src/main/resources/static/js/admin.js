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
        // ② セルクリックで詳細モーダルを開く
        dateClick: info => openDetail(info.dateStr)
    });

    calendar.render();
});

// モーダルを開くロジック
function openDetail(dateStr) {
    fetch(`/api/calendar/detail?date=${dateStr}`)
        .then(res => res.json())
        .then(data => {
            // data = { date, weatherMain, maxTemp, minTemp, windSpeed, totalSales, productSales: [{name, quantity},…] }
            document.getElementById('modalDate').textContent = `${data.date} の詳細情報`;
            document.getElementById('weatherIcon').textContent = data.weatherMain;
            document.getElementById('weatherInfo').textContent = `最高/最低：${data.maxTemp}/${data.minTemp} ℃ 風速：${data.windSpeed} m/s`;
            document.getElementById('totalSales').textContent = `合計販売本数：${data.totalSales} 本`;
            const tbody = document.getElementById('productSales');
            tbody.innerHTML = data.productSales
                .map(ps => `<tr><td>${ps.name}</td><td>${ps.quantity}</td></tr>`)
                .join('');
            document.getElementById('detailModal').style.display = 'block';
        });
}

// モーダルを閉じる
function closeModal() {
    document.getElementById('detailModal').style.display = 'none';
}
