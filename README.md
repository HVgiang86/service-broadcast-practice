
# Project: Ứng dụng thực hành phần Service và Broadcast Receiver (VCS)



## Sinh viên thực hiện

- [@Hoàng Văn Giang](https://github.com/HVgiang86/service-broadcast-practice.git)


## Cấu trúc ứng dụng

### Service:

**LogWritingService:** Thực hiện việc ghi log vào Logcat. Là foreground service với một Notification có 2 button

**BackgroundRunningService:** Giữ các thành phần broadcast thực hiện chức năng Ghi Log, Mở khoá màn hình, Cài đặt ứng dụng mới, chạy nền khi ứng dụng đã tắt

### Broadcast Receiver:

**BootCompleteBroadcastReceiver:** Nhận system broadcast khi hệ thống khởi động xong. Receiver này sẽ gọi phương thức startActivity để mở Main Activity

**LogNotificationBroadcastReceiver:** Nhận local broadcast tương ứng với các button của notification điều khiển việc khi Logcat. Bao gồm 2 Action là START_LOG và STOP_LOG

**NewPackageInstalledBroadcastReceiver:** Nhận system broadcast khi ứng dụng mới được cài đặt vào thiết bị. Receiver này sẽ hiển thị một thông báo trên notification bar đê thông báo

**ScreenOnBroadcastReceiver:** Nhận system broadcast khi màn hình được bật sáng. Receiver này sẽ gọi mở một Alert Activity chứa Alert Dialog thông báo cho user

### Activity
**Main Activity:** Chứa giao diện chính của chương trình, gồm một một list các chức năng
**MessageBoxActivity:** Sử dụng theme AlertDialog, chứa duy nhất một AlertDialog để thông báo

### Permission

**android.permission.FOREGROUND_SERVICE**
**android.permission.RECEIVE_BOOT_COMPLETED**
**android.permission.SYSTEM_ALERT_WINDOW**

## Tính năng

- Màn hình bật tắt từng chức năng
- Hoạt động dưới nền sau khi tắt ứng dụng
- Tự động khởi động khi bật máy (startup app)
- Hiển thị message box khi bật sáng màn hình
- Hiển thị Notification với 2 nút Start và Stop, điều khiển việc ghi log vào logcat
- Hiển thị thông báo khi một úng dụng mới được cài đặt


## Screenshots

![App Screenshot](https://i.imgur.com/PXHOZCF.png)

![App Screenshot](https://i.imgur.com/PXbddsz.png)

![App Screenshot](https://i.imgur.com/HHbP1pg.png)

![App Screenshot](https://i.imgur.com/dwoBYqt.png)

![App Screenshot](https://i.imgur.com/xCL2inY.png)
