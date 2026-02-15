# Matule

Matule — Android-приложение на Kotlin + Jetpack Compose.

## Модули

- `:app` — runtime, экраны, навигация, ViewModel, WorkManager-сценарии, auth-flow.
- `:uikit` — UI-компоненты и тема (`components/*`, `ui/theme/*`, `data/Product.kt`).
- `:network` — API/DTO/network-репозитории (`data/network/*`, `data/repository/*`, кроме `MockAuthRepository`).
- `:benchmark` — макробенчмарки.

## Требования окружения

- Gradle JVM: `JDK 17..21` (рекомендуется `JDK 21`).
- Проверено в этом проекте: `JDK 21` (Android Studio `jbr`).
- На `JDK 24` возможна ошибка при создании `AndroidUnitTest`-задач: `Type T not present`.

## Runtime Auth

Текущий runtime закреплён на `MockAuth`:

- Провайдер: `app/src/main/java/com/example/lol/authorization/AuthRepositoryProvider.kt`
- Флаг режима: `AuthRuntimeConfig.useMockAuthInRuntime = true` (объявлен в `AuthRepositoryProvider.kt`)
- `AuthModeBanner` подключён на экранах SignIn/SignUp/CreatePassword, но визуально скрыт (`app/src/main/java/com/example/lol/authorization/AuthModeBanner.kt`)

Network-auth остаётся доступным как вторичный путь (переключение флага в `AuthRuntimeConfig`).

## Локальные уведомления

Реализованы два независимых сценария Sprint-4:

- Сценарий A: через 30 секунд после ухода из приложения и далее каждые 2 минуты до открытия приложения.
- Сценарий B: one-shot через 1 минуту неактивности, не повторяется до следующего входа.

Основные файлы:

- `app/src/main/java/com/example/lol/notifications/InactivityNotificationScheduler.kt`
- `app/src/main/java/com/example/lol/notifications/RepeatingInactivityWorker.kt`
- `app/src/main/java/com/example/lol/notifications/OneShotInactivityWorker.kt`
- `app/src/main/java/com/example/lol/authorization/SessionManager.kt`

## Persistence

Сохранение/восстановление состояния экрана создания профиля после перезапуска:

- `SessionManager.getCreateProfileDraft()/saveCreateProfileDraft()`
- Использование в `app/src/main/java/com/example/lol/authorization/SignUpScreen.kt`

Сохранение последнего маршрута (last route) сохранено и работает параллельно:

- `SessionManager.saveLastRoute()/getLastRoute()`
- `MainContainer` в `app/src/main/java/com/example/lol/MainActivity.kt`

## Storybook и UIKit

- Storybook: `app/src/main/java/com/example/lol/storybook/StorybookScreen.kt`
- `AppSelectField` переведён на `ModalBottomSheet` с emoji-опциями.
- Добавлены `AppChip` и `AppTabBar` в `:uikit`.

## Матрица требований и доказательств

| Требование | Доказательство |
|---|---|
| Структура `:app + :uikit + :network` | `settings.gradle.kts`, `uikit/build.gradle.kts`, `network/build.gradle.kts` |
| Storybook покрывает состояния UIKit | `app/src/main/java/com/example/lol/storybook/StorybookScreen.kt` |
| Select открывает BottomSheet с emoji | `uikit/src/main/java/com/example/lol/components/AppSelectField.kt` |
| UIKit state-тесты (Input error, Select no icon, Chips, TabBar, emoji select) | `uikit/src/androidTest/java/com/example/lol/components/UiKitStateTest.kt` |
| Runtime auth через MockAuth | `app/src/main/java/com/example/lol/authorization/AuthRepositoryProvider.kt` |
| Network-слой покрыт repo-тестами | `network/src/test/java/com/example/lol/data/repository/*.kt` |
| Ошибки сервера показываются в 5s уведомлении с ручным закрытием | `uikit/src/main/java/com/example/lol/components/ErrorNotification.kt`, использование в auth-экранах |
| Восстановление формы и last route после restart | `app/src/main/java/com/example/lol/authorization/SignUpScreen.kt`, `app/src/main/java/com/example/lol/MainActivity.kt`, `app/src/main/java/com/example/lol/authorization/SessionManager.kt` |
| Уведомления соответствуют двум тайминг-сценариям | `app/src/main/java/com/example/lol/notifications/*` |

## Матрица network-запросов и тестов

| Запрос | Тесты |
|---|---|
| `POST /collections/users/auth-with-password` | `network/src/test/java/com/example/lol/data/repository/AuthRepositoryTest.kt` (`login success/failure`) |
| `POST /collections/users/records` | `network/src/test/java/com/example/lol/data/repository/AuthRepositoryTest.kt` (`register success/failure`) |
| `GET /collections/users/records/{id}` | `network/src/test/java/com/example/lol/data/repository/AuthRepositoryTest.kt` (`getUser success/failure`) |
| `PATCH /collections/users/records/{id}` | `network/src/test/java/com/example/lol/data/repository/AuthRepositoryTest.kt` (`updateUser success`) |
| `DELETE /collections/_authOrigins/records/{id}` + auth list | `network/src/test/java/com/example/lol/data/repository/AuthRepositoryTest.kt` (`logout success`, `logout when token missing still clears local auth`) |
| `GET /collections/products/records` | `network/src/test/java/com/example/lol/data/repository/ProductRepositoryApiTest.kt` (`getProducts success/failure`, `search filter`) |
| `GET /collections/products/records/{id}` | `network/src/test/java/com/example/lol/data/repository/ProductRepositoryApiTest.kt` (`getProductById success`) |
| `GET /collections/news/records` | `network/src/test/java/com/example/lol/data/repository/ProductRepositoryApiTest.kt` (`getNews success/failure`) |
| `POST/PATCH /collections/cart/records` | `network/src/test/java/com/example/lol/data/repository/CartRepositoryTest.kt` (`add`, `update`, network error`) |
| `POST /collections/orders/records` | `network/src/test/java/com/example/lol/data/repository/OrderRepositoryTest.kt` (`create success/failure`) |
| `GET/POST /collections/project/records` | `network/src/test/java/com/example/lol/data/repository/ProjectRepositoryTest.kt` (`get/create success`, `create failure`) |

## Сборка

```bash
# Windows (PowerShell)
.\gradlew.bat :network:compileDebugKotlin :uikit:compileDebugKotlin :app:assembleDebug

# macOS/Linux
./gradlew :network:compileDebugKotlin :uikit:compileDebugKotlin :app:assembleDebug
```

## Тесты

```bash
# Компиляция androidTest для UIKit
# Windows (PowerShell)
.\gradlew.bat :uikit:compileDebugAndroidTestKotlin

# macOS/Linux
./gradlew :uikit:compileDebugAndroidTestKotlin

# Unit-тесты network
# Windows (PowerShell)
.\gradlew.bat :network:testDebugUnitTest

# macOS/Linux
./gradlew :network:testDebugUnitTest
```

## Бенчмарки

В проекте есть два benchmark-сценария:

- `ExampleStartupBenchmark` — macrobenchmark cold startup.
- `CreateProjectFlowBenchmark` — сценарий экрана создания проекта (загрузка, image-picker proxy, валидация, submit UI-путь).

### Что добавлено для сценария Create Project

- Benchmark-only Activity: `app/src/benchmark/java/com/example/lol/benchmark/CreateProjectBenchmarkActivity.kt`
- Benchmark repository (контролируемый submit-path): `app/src/benchmark/java/com/example/lol/benchmark/BenchmarkProjectRepository.kt`
- UI benchmark test: `benchmark/src/main/java/com/example/lol/benchmark/CreateProjectFlowBenchmark.kt`
- Семантические маркеры в UI для стабильного поиска элементов:
  - `benchmark_project_name_input`
  - `benchmark_photo_picker_area`
  - `benchmark_submit_button`

### Запуск (Windows / PowerShell)

```powershell
# Важно: JDK 21 (Android Studio jbr)
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
$env:Path="$env:JAVA_HOME\bin;$env:LOCALAPPDATA\Android\Sdk\platform-tools;$env:Path"

# Полный набор benchmark-тестов модуля
.\gradlew.bat :benchmark:connectedBenchmarkAndroidTest

# Только CreateProjectFlowBenchmark
.\gradlew.bat :benchmark:connectedBenchmarkAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.example.lol.benchmark.CreateProjectFlowBenchmark"

# Только startup benchmark
.\gradlew.bat :benchmark:connectedBenchmarkAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.example.lol.benchmark.ExampleStartupBenchmark"
```

### Где смотреть результаты

- Общие instrumented результаты:
  - `benchmark/build/outputs/androidTest-results/connected/benchmark/`
- Логи `CreateProjectFlowBenchmark` с метриками:
  - `.../logcat-com.example.lol.benchmark.CreateProjectFlowBenchmark-benchmarkCreateProjectFlow.txt`
  - Внутри логов ищите строки:
    - `BENCH_UI_RAW,...`
    - `BENCH_UI_SUMMARY,...`
    - `BENCH_UI_PROFILE,...`
- Macrobenchmark JSON (startup):
  - `benchmark/build/outputs/connected_android_test_additional_output/benchmark/connected/<device>/com.example.lol.benchmark-benchmarkData.json`

### Примечание про эмулятор

Для локального запуска benchmark в эмуляторе включено подавление ошибки `EMULATOR`:

- `benchmark/build.gradle.kts` -> `testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"`

Это удобно для разработки и CI без физического девайса, но финальные performance-оценки лучше перепроверять на реальном устройстве.

## Troubleshooting

Симптом: `Type T not present` при запуске unit-тестов.

Причина: Gradle запущен на неподдерживаемой JVM (например, `JDK 24`).

Быстрый фикс для PowerShell (текущая сессия):

```powershell
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :network:testDebugUnitTest
```
