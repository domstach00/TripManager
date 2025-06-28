# TripManager

TripManager to pełnostackowa aplikacja zaprojektowana, aby pomóc użytkownikom w zarządzaniu podróżami, budżetami i powiązanymi działaniami. Backend jest zbudowany w oparciu o Spring Boot, dostarczając solidne API REST, natomiast frontend jest rozwijany przy użyciu Angulara, oferując dynamiczny i responsywny interfejs użytkownika. Aplikacja wykorzystuje MongoDB do przechowywania danych oraz RabbitMQ do kolejkowania wiadomości.

## Funkcjonalności

*   **Uwierzytelnianie i Autoryzacja Użytkowników:** Bezpieczna rejestracja, logowanie i kontrola dostępu.
*   **Zarządzanie Podróżami:** Tworzenie, przeglądanie, aktualizowanie i usuwanie podróży.
*   **Budżetowanie:** Zarządzanie budżetami dla podróży, w tym dochodami i wydatkami.
*   **Integracja z Google Maps:** Wizualizacja lokalizacji podróży i planowanie tras.
*   **Powiadomienia E-mail:** Automatyczne powiadomienia e-mail o różnych zdarzeniach (np. reset hasła, zaproszenia do budżetu).
*   **Kolejkowanie Wiadomości:** Asynchroniczne przetwarzanie zadań za pomocą RabbitMQ.
*   **API RESTful:** Dobrze udokumentowane API do płynnej komunikacji między frontendem a backendem.

## Wykorzystane Technologie

### Backend (Spring Boot)

*   **Java 17**
*   **Spring Boot 3.1.3**
*   **Spring Data MongoDB:** Do interakcji z bazą danych.
*   **Spring Security:** Do uwierzytelniania i autoryzacji.
*   **Spring Web:** Budowanie API RESTful.
*   **Spring Mail:** Wysyłanie powiadomień e-mail.
*   **Thymeleaf:** Do renderowania szablonów e-mail po stronie serwera.
*   **Spring AMQP:** Integracja z RabbitMQ.
*   **Log4j2:** Do logowania.
*   **java-jwt:** Implementacja JSON Web Token.
*   **Springdoc OpenAPI UI:** Dokumentacja API (Swagger UI).

### Frontend (Angular)

*   **Angular 18.2.3**
*   **Angular Material:** Biblioteka komponentów UI.
*   **Angular Flex Layout:** Do responsywnego projektowania UI.
*   **Angular Google Maps:** Integracja z API Google Maps.
*   **ngx-toastr:** Do powiadomień typu "toast".
*   **ngx-translate:** Do internacjonalizacji (i18n).

### Baza Danych i Komunikacja

*   **MongoDB:** Baza danych NoSQL do przechowywania danych.
*   **RabbitMQ:** Broker wiadomości do asynchronicznej komunikacji.

### Rozwój i Wdrożenie

*   **Maven:** Narzędzie do automatyzacji budowania backendu.
*   **npm/Yarn:** Menedżer pakietów dla frontendu.
*   **Docker Compose:** Do orkiestracji aplikacji Dockerowych składających się z wielu kontenerów (MongoDB, RabbitMQ).

## Rozpoczęcie Pracy

Poniższe instrukcje pomogą Ci uruchomić projekt na Twojej lokalnej maszynie w celach rozwojowych i testowych.

### Wymagania Wstępne

*   Java 17 Development Kit (JDK)
*   Maven
*   Node.js i npm (lub Yarn)
*   Docker i Docker Compose

### Instalacja

1.  **Sklonuj repozytorium:**
    ```bash
    git clone <adres_repozytorium>
    cd TripManager
    ```

2.  **Uruchom usługi Docker (MongoDB i RabbitMQ):**
    ```bash
    cd docker
    docker-compose up -d
    cd ..
    ```

3.  **Zbuduj i uruchom Backend:**
    ```bash
    ./mvnw spring-boot:run
    ```
    Backend zazwyczaj będzie działał pod adresem `http://localhost:8080`.

4.  **Zainstaluj zależności Frontendu i uruchom Frontend:**
    ```bash
    cd webapp
    npm install # lub yarn install
    ng serve
    ```
    Frontend zazwyczaj będzie działał pod adresem `http://localhost:4200`. Domyślnie `ng serve` używa środowiska deweloperskiego (`environment.development.ts`).

## Konfiguracja

### Backend (`src/main/resources/application.properties`)

Ten plik zawiera konfigurację dla backendu Spring Boot. Może być konieczne dostosowanie tych ustawień w zależności od środowiska.

```properties
# Port serwera
server.port=8080

# Konfiguracja MongoDB
# Te wartości powinny odpowiadać zmiennym środowiskowym ustawionym w docker/docker-compose.yml dla usługi 'mongodb'.
# Dla lokalnego środowiska deweloperskiego zazwyczaj są to:
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=tripmanager
spring.data.mongodb.username=rootuser
spring.data.mongodb.password=rootpass

# Konfiguracja RabbitMQ
# Te wartości powinny odpowiadać zmiennym środowiskowym ustawionym w docker/docker-compose.yml dla usługi 'rabbitmq'.
# Dla lokalnego środowiska deweloperskiego zazwyczaj są to:
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Konfiguracja JWT
jwt.secret=YourSuperSecretKeyThatIsAtLeast256BitsLongAndShouldBeStoredSecurely
jwt.expiration=3600000 # 1 godzina w milisekundach

# Konfiguracja poczty e-mail (Przykładowe wartości - skonfiguruj dla swojego serwera SMTP)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.example.com
```
**Ważna uwaga dotycząca bezpieczeństwa:**
W środowiskach produkcyjnych **kluczowe** jest, aby zmienić następujące wartości na silne, unikalne i bezpiecznie zarządzane dane uwierzytelniające:
*   `spring.data.mongodb.username` i `spring.data.mongodb.password`: Użyj silnych, unikalnych danych uwierzytelniających dla swojej produkcyjnej instancji MongoDB.
*   `spring.rabbitmq.username` i `spring.rabbitmq.password`: Użyj silnych, unikalnych danych uwierzytelniających dla swojej produkcyjnej instancji RabbitMQ.
*   `jwt.secret`: Wygeneruj bardzo długi, losowy i unikalny klucz tajny. **Nigdy nie umieszczaj go na stałe w kodzie w środowisku produkcyjnym.** Użyj zmiennych środowiskowych lub dedykowanego systemu zarządzania sekretami.
*   `spring.mail.username` i `spring.mail.password`: Użyj bezpiecznych danych uwierzytelniających dla swojej produkcyjnej usługi e-mail. **Nigdy nie umieszczaj ich na stałe w kodzie w środowisku produkcyjnym.**

### Frontend (`webapp/src/environments/environment.ts` i `environment.development.ts`)

Frontend Angular używa plików środowiskowych do konfiguracji. `environment.ts` jest przeznaczony dla produkcji, a `environment.development.ts` dla środowiska deweloperskiego.

**`webapp/src/environments/environment.ts` (Produkcja)**
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.yourdomain.com', // Adres URL API backendu produkcyjnego
  googleMapsApiKey: 'YOUR_PRODUCTION_GOOGLE_MAPS_API_KEY' // Klucz API Google Maps dla produkcji
};
```

**`webapp/src/environments/environment.development.ts` (Dewelopment)**
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080', // Adres URL API backendu lokalnego
  googleMapsApiKey: 'YOUR_DEVELOPMENT_GOOGLE_MAPS_API_KEY' // Klucz API Google Maps dla dewelopmentu
};
```
**Ważna uwaga dotycząca bezpieczeństwa:**
*   Zastąp `YOUR_PRODUCTION_GOOGLE_MAPS_API_KEY` i `YOUR_DEVELOPMENT_GOOGLE_MAPS_API_KEY` swoimi rzeczywistymi kluczami API Google Maps.
*   **Kluczowe jest ograniczenie kluczy API Google Maps**, aby zezwalały na żądania tylko z określonych domen (dla produkcji) lub `localhost` (dla dewelopmentu) i aby włączały tylko niezbędne API (np. Maps JavaScript API, Geocoding API). Zapobiega to nieautoryzowanemu użyciu i potencjalnym problemom z rozliczeniami.

## Struktura Projektu

```
.
├── docker/                 # Konfiguracja Docker Compose dla usług
│   └── docker-compose.yml
├── src/                    # Kod źródłowy Backendu (Spring Boot)
│   ├── main/
│   │   ├── java/           # Pliki źródłowe Java
│   │   └── resources/      # Zasoby aplikacji (właściwości, szablony)
│   └── test/               # Kod testowy Backendu
├── webapp/                 # Kod źródłowy Frontendu (Angular)
│   ├── src/                # Pliki aplikacji Angular
│   └── package.json        # Zależności i skrypty Frontendu
├── pom.xml                 # Konfiguracja budowania Maven dla backendu
└── README.md               # Ten plik
```
