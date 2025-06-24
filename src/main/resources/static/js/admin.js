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