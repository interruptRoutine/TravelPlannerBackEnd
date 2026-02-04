# TravelPlanner API Documentation

## Panoramica

Questa documentazione descrive tutti gli endpoint REST disponibili nell'applicazione TravelPlanner.

**Base URL:** `http://localhost:8080`

---

## Controllers

### 1. HomeController

**Base Path:** `/`

| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| GET | `/` | Pagina di benvenuto con informazioni sul progetto |

#### GET /

**Descrizione:** Restituisce un messaggio di benvenuto con informazioni sul progetto.

**Risposta:**
```
|--------------------------------------------------------------------| 
| Questo è un progetto di fine academy di Capgemini: Travel Planner. |
|                  Per le funzioni base consultare:                  |
|                localhost:8080/swagger-ui/index.html                |
|--------------------------------------------------------------------|
```

---

### 2. UserController

**Base Path:** `/api/users`

| Metodo | Endpoint | Descrizione | Autenticazione |
|--------|----------|-------------|----------------|
| POST | `/register` | Registra un nuovo utente | No |
| POST | `/login` | Autentica un utente | No |
| DELETE | `/delete` | Elimina l'utente autenticato | Sì (Token) |
| PUT | `/modify` | Modifica i dati dell'utente autenticato | Sì (Token) |

---

#### POST /api/users/register

**Descrizione:** Registra un nuovo utente nel sistema.

**Request Body:** `UserRegisterDto`

```json
{
    "email": "string",
    "password": "string",
    "name": "string",
    "location": "string",
    "dob": "YYYY-MM-DD"
}
```

**Validazione Password:**
- Minimo 8 caratteri
- Almeno una lettera maiuscola
- Almeno una lettera minuscola
- Almeno un numero
- Almeno un carattere speciale (`@$!%*?&-`)

**Risposta Successo (200 OK):** `User`

```json
{
    "id": 1,
    "email": "user@example.com",
    "name": "Nome Utente",
    "location": "Roma",
    "dob": "1990-01-01",
    "token": "uuid-token-generato",
    "role": "USER",
    "distanceUnit": "KILOMETERS",
    "temperatureUnit": "CELSIUS"
}
```

**Errori Possibili:**
- `InvalidCredentialsException`: "Email già in uso. Fai il login o usa un'altra email."
- `InvalidCredentialsException`: "Password non valida. La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale."

---

#### POST /api/users/login

**Descrizione:** Autentica un utente esistente e restituisce un token.

**Request Body:** `UserLoginDto`

```json
{
    "email": "string",
    "password": "string"
}
```

**Risposta Successo (200 OK):**

```json
{
    "token": "uuid-token-utente"
}
```

**Cookie Impostato:**
- Nome: `token`
- Valore: Token dell'utente
- Max Age: 3600 secondi (1 ora)
- Path: `/`

**Errori Possibili:**
- `InvalidCredentialsException`: "Utente non trovato."
- `InvalidCredentialsException`: "Password inserita non valida."

---

#### DELETE /api/users/delete

**Descrizione:** Elimina l'utente attualmente autenticato.

**Autenticazione:** Richiesta (Token nel cookie o header)

**Risposta Successo (200 OK):** Nessun contenuto

**Errori Possibili:**
- `InvalidCredentialsException`: "Token non valido."

---

#### PUT /api/users/modify

**Descrizione:** Modifica i dati dell'utente autenticato.

**Autenticazione:** Richiesta (Token nel cookie o header)

**Request Body:** `UserModifyDto`

```json
{
    "email": "string (opzionale)",
    "name": "string (opzionale)",
    "password": "string (opzionale)",
    "location": "string (opzionale)",
    "dateOfBirth": "YYYY-MM-DD (opzionale)",
    "temperatureUnit": "CELSIUS | FAHRENHEIT (opzionale)",
    "distanceUnit": "KILOMETERS | MILES (opzionale)"
}
```

**Nota:** Solo i campi forniti e non vuoti verranno aggiornati.

**Risposta Successo (200 OK):** `User` (utente aggiornato)

**Errori Possibili:**
- `InvalidCredentialsException`: "Token non valido."

---

## DTOs (Data Transfer Objects)

### UserRegisterDto

| Campo | Tipo | Descrizione |
|-------|------|-------------|
| email | String | Email dell'utente |
| password | String | Password (deve rispettare i criteri di validazione) |
| name | String | Nome dell'utente |
| location | String | Località dell'utente |
| dob | LocalDate | Data di nascita |

### UserLoginDto

| Campo | Tipo | Descrizione |
|-------|------|-------------|
| email | String | Email dell'utente |
| password | String | Password dell'utente |

### UserModifyDto

| Campo | Tipo | Descrizione |
|-------|------|-------------|
| email | String | Nuova email (opzionale) |
| name | String | Nuovo nome (opzionale) |
| password | String | Nuova password (opzionale) |
| location | String | Nuova località (opzionale) |
| dateOfBirth | LocalDate | Nuova data di nascita (opzionale) |
| temperatureUnit | TemperatureUnit | Unità di temperatura (opzionale) |
| distanceUnit | DistanceUnit | Unità di distanza (opzionale) |

---

## Enums

### Roles
- `USER`
- (altri ruoli definiti nel sistema)

### TemperatureUnit
- `CELSIUS`
- `FAHRENHEIT`

### DistanceUnit
- `KILOMETERS`
- `MILES`

---

## Autenticazione

L'applicazione utilizza un sistema di autenticazione basato su token. Dopo il login, il token viene:
1. Restituito nel body della risposta
2. Impostato come cookie HTTP

Per le richieste autenticate, il token deve essere fornito tramite il cookie `token`.

---

## Swagger UI

Per una documentazione interattiva e per testare gli endpoint, visita:

**URL:** `http://localhost:8080/swagger-ui/index.html`
