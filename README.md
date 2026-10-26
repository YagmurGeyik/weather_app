# Hava - Modern Android Hava Durumu Uygulaması 🌤️

**Hava**, Jetpack Compose ve Material 3 kullanılarak geliştirilmiş, modern ve kullanıcı dostu bir Android hava durumu uygulamasıdır. [Open-Meteo API](https://open-meteo.com/) altyapısını kullanarak güncel hava durumu tahminleri ve şehir listesi verileri sunar.

---

## 🚀 Başlıca Özellikler

- **Jetpack Compose UI:** Tamamen deklaratif arayüz bileşenleri ve modern Android UI standartları.
- **Material 3 Tasarım:** Dinamik renkler, şık kartlar, karanlık (Dark) ve aydınlık (Light) tema desteği.
- **Detaylı Hava Durumu Bilgileri:** Sıcaklık, hissedilen sıcaklık, rüzgar hızı, nem, yağış olasılığı, günlük ve saatlik tahminler.
- **Şehir Arama ve Listeleme:** Open-Meteo Geocoding API ile hızlı şehir arama ve keşfetme.
- **Favoriler Yönetimi:** Beğenilen şehirleri favorilere ekleme ve kolayca erişme.
- **Modern Mimari:** MVVM (Model-View-ViewModel), Kotlin Coroutines ve Flow tabanlı reaktif veri akışı.
- **Edge-to-Edge Desteği:** Modern sistem çubuğu entegrasyonu.

---

## 🛠️ Kullanılan Teknolojiler ve Kütüphaneler

- **Dil:** [Kotlin](https://kotlinlang.org/)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
- **Mimari:** MVVM Pattern, StateFlow, ViewModel
- **Ağ İletişimi (Networking):** [Retrofit](https://square.github.io/retrofit/) & Gson Converter
- **Loglama:** OkHttp Logging Interceptor
- **Asenkron Programlama:** Kotlin Coroutines

---

## 📁 Proje Mimarisi ve Paket Yapısı

```tree
com.example.hava/
├── data/
│   ├── model/         # API veri modelleri (CityDto, WeatherResponse vb.)
│   ├── remote/        # Retrofit servis arayüzleri (CityApi, WeatherApi, ApiClient)
│   └── repository/    # Veri kaynakları ve repository implementasyonları
├── ui/
│   ├── components/    # Compose ekranları ve bileşenleri (CityListScreen, WeatherDetailScreen, FavoritesScreen, CityCard)
│   ├── state/         # Yükleme ve durum yönetim sınıfları (LoadState)
│   ├── theme/         # Material 3 tema, renkler ve tipografi
│   └── viewmodel/     # UI state ve iş mantığı yöneticileri (CityViewModel)
└── MainActivity.kt    # Ana aktivite ve Navigasyon / Ekran yönlendirmesi
```

---

## ⚙️ Kurulum ve Çalıştırma

1. Projeyi [Android Studio](https://developer.android.com/studio) (Koala, Ladybug veya daha yeni bir sürüm) ile açın.
2. Gradle senkronizasyonunun tamamlanmasını bekleyin (`Sync Project with Gradle Files`).
3. Uygulamayı çalıştırmak için bir Android Emülatör (API 24+) veya gerçek bir cihaz seçin.
4. **Run** ( ▶️ ) butonuna basarak uygulamayı derleyin ve çalıştırın.

*(Not: Open-Meteo API ücretsizdir ve herhangi bir API anahtarı (API Key) gerektirmez.)*

---

## 📄 Lisans

Bu proje eğitim ve geliştirme amaçlı oluşturulmuştur.
