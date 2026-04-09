/**
 * File: notification.js
 * Xử lý logic cho trang Thông báo - Dự án Nông Nghiệp IoT
 */

// 1. Hàm tạo thông báo mới (Admin gửi cho User)
function createNotification() {
    const accountId = document.getElementById('targetAccountId').value.trim();
    const message = document.getElementById('targetMessage').value.trim();

    if (!accountId || !message) {
        showToast('Vui lòng nhập đủ ID và nội dung!');
        return;
    }

    // Đóng gói dữ liệu gửi lên Java
    const formData = new URLSearchParams();
    formData.append('accountId', accountId);
    formData.append('message', message);

    fetch('/user/notification/api/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData
    })
    .then(res => {
        if (res.ok) {
            showToast('🚀 Gửi thông báo thành công!');
            document.getElementById('targetMessage').value = ''; // Reset ô nhập
            // Đợi 1 giây để người dùng kịp thấy Toast rồi mới load lại trang
            setTimeout(() => location.reload(), 1000);
        } else {
            showToast('Lỗi server: Không thể gửi!');
        }
    })
    .catch(err => showToast('Lỗi kết nối mạng!'));
}

// 2. Hàm đánh dấu 1 thông báo là Đã đọc
function handleMarkAsRead(btn) {
    const id = btn.getAttribute('data-id');

    // Lưu ý: Java Controller cũ của bạn dùng @PutMapping nên ở đây dùng method: 'PUT'
    fetch('/user/notification/api/' + id + '/read', { method: 'PUT' })
        .then(res => {
            if (res.ok) {
                // Hiệu ứng giao diện: Tìm thẻ card chứa nút bấm này
                const card = btn.closest('.noti-card');
                card.classList.remove('unread');
                card.classList.add('read');

                // Thay thế chữ "MỚI" thành "Đã xem"
                const badge = card.querySelector('.badge-unread');
                if (badge) {
                    badge.outerHTML = '<span style="font-size:11px;color:#9cb89c;">✓ Đã xem</span>';
                }

                // Xóa nút bấm "Đã đọc" vì đã đọc xong rồi
                btn.remove();

                // Cập nhật con số đếm ở trên Stats bar
                updateUnreadCount(-1);
                showToast('Đã xem thông báo!');
            } else {
                showToast('Lỗi server!');
            }
        })
        .catch(() => showToast('Lỗi kết nối!'));
}

// 3. Hàm đánh dấu Tất cả đã đọc
function markAllAsRead() {
    const idAccount = document.getElementById('accIdInput').value.trim();
    if (!idAccount) {
        showToast('Vui lòng nhập ID Account!');
        return;
    }

    fetch('/user/notification/api/read-all/' + idAccount, { method: 'PUT' })
        .then(res => {
            if (res.ok) {
                // Chuyển toàn bộ card unread sang trạng thái đã đọc trên màn hình
                document.querySelectorAll('.noti-card.unread').forEach(card => {
                    card.classList.remove('unread');
                    card.classList.add('read');

                    const badge = card.querySelector('.badge-unread');
                    if (badge) {
                        badge.outerHTML = '<span style="font-size:11px;color:#9cb89c;">✓ Đã xem</span>';
                    }

                    const btn = card.querySelector('.btn-read');
                    if (btn) btn.remove();
                });

                // Reset con số đếm về 0
                const counter = document.getElementById('unreadCount');
                if (counter) counter.textContent = '0';

                showToast('Đã đọc tất cả thông báo!');
            } else {
                showToast('Lỗi server!');
            }
        })
        .catch(() => showToast('Lỗi kết nối!'));
}

// 4. Hàm phụ trợ: Cập nhật số lượng thông báo chưa đọc trên giao diện
function updateUnreadCount(delta) {
    const counter = document.getElementById('unreadCount');
    if (counter) {
        const current = parseInt(counter.textContent) || 0;
        counter.textContent = Math.max(0, current + delta);
    }
}

// 5. Hàm phụ trợ: Hiển thị thông báo Toast
function showToast(msg) {
    const toast = document.getElementById('toast');
    if (toast) {
        toast.textContent = msg;
        toast.style.display = 'block';
        setTimeout(() => {
            toast.style.display = 'none';
        }, 2500);
    }
}