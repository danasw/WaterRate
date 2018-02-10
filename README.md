# WaterRate


WateRATE adalah aplikasi android yang berfungsi untuk mengatur dan memantau penggunaan air.


# FITUR

-	Menampilkan informasi penggunaan air
-	Menampilkan informasi kualitas air
-	Mengontrol status kran air (ON/OFF)
-	Melaporkan gangguan
-	Notifikasi kualitas air buruk
-	Menampilkan informasi perkiraan biaya penggunaan air


# Bagaimana WateRATE Bekerja ?

Aplikasi WateRATE dihubungkan dengan perangkat yang didesain dan dibuat sesuai kebutuhan aplikasi.
Perangkat tersebut mimiliki sensor flow meter, sensor pH, sensor turbiditas(kekeruhan), solenoid water valve dan servo.
Semua komponen tersebut dihubungkan pada NodeMCU yang bertugas mengolah data dan mengirimnya ke Firebase.
Data di Firebase akan diambil oleh aplikasi WateRATE, selain itu untuk mengontrol kran air aplikasi mengirimkan command ke Firebase.


# Tutorial Penggunaan

1.	Hubungkan perangkat android dengan koneksi internet.
2.	Daftarkan diri anda pada halaman awal atau masukkan username dan password jika sudah mendaftar.
3.	Pada halaman utama akan muncul informasi penggunaan air pada hari ini, penggunaan air dalam sebulan, biaya yang dikeluarkan, kualitas air dan tombol untuk mengatur kran.
4.	Untuk mengatur kran, pengguna hanya perlu menekan tombol yang disediakan.
5.	Pada tab history (riwayat), pengguna dapat melihat riwayat penggunaan air selama seminggu terakhir juga mengetahui informasi pengguna air hari ini secara detail per kran yang ada.
6.	Untuk melaporkan gangguan, pengguna hanya perlu menekan tombol menu pada kanan atas lalu pilih Lapor Gangguan.
	Pengguna hanya perlu mengisi nomer yang dapat dihubungi serta keluhan atau masalah pada alat.
