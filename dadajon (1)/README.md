# Dadajon — Oilaviy Sovg'a Ilovasi ❤️

Bu ilova dadajon va onajonimiz uchun maxsus sovg'a sifatida yaratilgan. Ilova butunlay oflayn rejimda ishlaydi va internet talab qilmaydi.

## Asosiy Imkoniyatlar:
1. **Yillik Bildirishnomalar:** Har yili avtomatik ravishda bildirishnomalar yuboradi (internet, Telegram yoki SMSlarsiz).
   - **22 Noyabr** — Dadajonning tug'ilgan kunlari.
   - **26 Iyun** — Dadajon va Onajonning nikoh yilliklari.
2. **Oilaviy Galereya:** Chiroyli dizayn, yaqinlashtirish (pinch-to-zoom) va silliq animatsiyalar bilan oilaviy rasmlarni tomosha qilish.
3. **Musiqa:** Tinchlantiruvchi, oflayn rejimda ishlaydigan va batareyani tejaydigan chiroyli fon musiqasi.
4. **Taimer / Hisoblagich:** Keyingi bayramgacha necha kun, soat, daqiqa va soniya qolganini real vaqt rejimida ko'rsatuvchi hisoblagich.
5. **Sozlamalar:** Tug'ilgan kun va nikoh yilligi sanalarini o'zgartirish, musiqani va bildirishnomalarni yoqish/o'chirish imkoniyati.

---

## Yo'riqnoma va O'rnatish (Сборка APK)

Loyiha to'liq tayyor holatda bo'lib, quyidagi buyruqlar yordamida osongina APK shaklida yig'iladi:

### 1. Debug APK yig'ish (Yaxshi tavsiya):
```bash
gradle assembleDebug
```
Ushbu buyruq ishga tushgach, tayyor o'rnatiluvchi APK fayli quyidagi manzilda paydo bo'ladi:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Release APK yig'ish:
```bash
gradle assembleRelease
```
Tayyor APK fayli quyidagi manzilda paydo bo'ladi:
`app/build/outputs/apk/release/app-release-unsigned.apk`

---

## Texnologiyalar:
- **Kotlin & Jetpack Compose:** Eng zamonaviy Android UI kutubxonasi.
- **Material 3 & Glassmorphism:** Zamonaviy va jozibali premium dizayn uslubi.
- **Room Database:** Sozlamalarni qurilmaning o'zida saqlash.
- **AlarmManager & BroadcastReceiver:** Telefon o'chib yonganidan so'ng ham ishlaydigan va har yili takrorlanadigan oflayn bildirishnomalar.
- **Local Audio Synthesis:** Hech qanday og'ir fayllarsiz, 100% oflayn ishlaydigan ohang generatori.
