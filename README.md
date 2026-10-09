# P04 — Arquitectura Web 3.0 (PR02 Sistema de Citas Académicas)

**Materia:** Desarrollo de Sistemas Web  
**Proyecto:** PR02 — Sistema de citas académicas  
**Etapa:** Web 3.0 (Angular + Spring Boot + PostgreSQL)  
**Equipo:** Uriel Cruz Herrera, Erick Romero García, Josue Sánchez Valente
**Facilitador:** Dr. Gabriel Rodríguez Vásquez  
**Fecha:** Octubre 2026

---

## Descripción

Arquitectura desacoplada cliente-servidor que separa:
- **Frontend** (Angular 16): SPA que consume la API REST y renderiza la interfaz.
- **Backend** (Spring Boot 4.1.1 + Java 17): API REST que aplica reglas de negocio y persiste en PostgreSQL.
- **Base de datos** (PostgreSQL 16): esquema con 5 entidades e integridad referencial.

## Estructura del repositorio

```
P04/
├── backend-citas/              ← Spring Boot + Maven
│   ├── pom.xml
│   └── src/main/java/mx/uv/dsw/citas/
│       ├── BackendCitasApplication.java
│       ├── controller/         ← @RestController
│       ├── service/            ← @Service
│       ├── repository/         ← @Repository (Spring Data JPA)
│       ├── entity/             ← @Entity (JPA)
│       ├── dto/                ← DTOs de entrada/salida
│       ├── exception/          ← Manejo global de errores
│       └── config/             ← CORS y otras configuraciones
├── frontend-citas/             ← Angular 16
│   ├── package.json
│   ├── proxy.conf.json
│   └── src/app/
│       ├── components/         ← Horarios, Solicitar, MisCitas
│       ├── services/           ← Servicios HTTP
│       └── models/             ← Interfaces TypeScript
├── sql/
│   ├── 01_schema.sql
│   ├── 02_datos_prueba.sql
│   └── 03_reset_disponibilidad.sql
└── docs/
    ├── arquitectura.md
    ├── openapi.yaml
    ├── contribucion-individual.md
    └── capturas/
```

## Requisitos

- Java 17
- Maven 3.9+
- Node.js 18+ (recomendado 20)
- Angular CLI 16 (se instala vía `npx`)
- PostgreSQL 16 corriendo en `localhost:5432`

## Instalación

### 1. Base de datos

```bash
psql -U postgres -c "CREATE DATABASE citas_academicas;"
psql -U postgres -d citas_academicas -f sql/01_schema.sql
psql -U postgres -d citas_academicas -f sql/02_datos_prueba.sql
```

### 2. Variables de entorno (backend)

```bash
export CITAS_DB_URL="jdbc:postgresql://localhost:5432/citas_academicas"
export CITAS_DB_USER="postgres"
export CITAS_DB_PASS="postgres"
```

### 3. Backend

```bash
cd backend-citas
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

Backend en `http://localhost:8080`.

### 4. Frontend

```bash
cd frontend-citas
npm install
npm start
```

Frontend en `http://localhost:4200`.

## Endpoints principales

| Método | Ruta | Descripción | Código esperado |
| :--- | :--- | :--- | :--- |
| GET | `/api/health` | Health check | 200 |
| GET | `/api/disponibilidades` | Listar horarios disponibles | 200 |
| GET | `/api/motivos` | Listar motivos activos | 200 |
| GET | `/api/citas?idEstudiante={id}` | Listar citas del estudiante | 200 |
| GET | `/api/citas/{id}` | Obtener una cita | 200 / 404 |
| POST | `/api/citas` | Crear cita | 201 / 400 / 409 |
| DELETE | `/api/citas/{id}?idUsuario={id}` | Cancelar cita | 204 / 404 / 409 |

Contrato completo en `docs/openapi.yaml`.

## Flujo principal

```
/horarios → lista bloques DISPONIBLES
    │
    ▼
/solicitar → POST /api/citas
    │
    ▼
Validación backend: ¿disponibilidad DISPONIBLE?
    │
   ┌┴┐
   │ │
  Sí No → 409 Conflict
   │
   ▼
Cita creada (201) + bitácora + bloque a OCUPADO
    │
    ▼
/mis-citas → lista citas + botón cancelar
    │
    ▼
DELETE /api/citas/{id} → 204 → bloque liberado
```

## Pruebas documentadas

| # | Tipo | Descripción | Resultado |
| :--- | :--- | :--- | :--- |
| 1 | Positiva | `GET /api/health` | 200 con `status: UP` |
| 2 | Negativa | `GET /api/noexiste` | 404 con `ErrorDTO` |
| 3 | Positiva | `POST /api/citas` con disponibilidad libre | 201 con cita creada |
| 4 | Negativa | `POST /api/citas` con disponibilidad ocupada | 409 con mensaje claro |
| 5 | Positiva | `DELETE /api/citas/{id}` | 204 sin body |
| 6 | Negativa | `DELETE` cita ya cancelada | 409 |
| 7 | E2E positiva | Crear cita desde Angular | UI + BD consistentes |
| 8 | E2E negativa | Doble reserva forzada con DevTools | Error visible en UI |

Capturas en `docs/capturas/`.

## Reseteo de datos para pruebas

Si necesitas reiniciar la disponibilidad para volver a probar:

```bash
psql -U postgres -d citas_academicas -f sql/03_reset_disponibilidad.sql
```

## Limitaciones declaradas

- **Autenticación simulada**: `idEstudiante = 1` como valor demo.
- **IoT / Web 4.0**: pendiente para P07.
- **Confirmación por asesor**: pendiente para P05 (Web 2.0 → JSF).
- **Datos sintéticos**: no se usan datos reales.

## Autoría

| Integrante | Contribución principal |
| :--- | :--- |
| Uriel Cruz Herrera | Arquitectura, backend completo, frontend completo, documentación |
| [Erick Romero García] | Revisión de modelo de datos |
| [Josue Sánchez Valente] | Revisión del contrato API |

Detalle en `docs/contribucion-individual.md`.