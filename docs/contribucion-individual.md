# Contribución individual — P04

**Integrante:** Uriel Cruz Herrea
**Rol:** Líder técnico y desarrollador full-stack
**Fecha:** Octubre 2026

---

## 1. Decisiones técnicas tomadas

| # | Decisión | Alternativas consideradas | Justificación | Evidencia |
| :--- | :--- | :--- | :--- | :--- |
| 1 | Usar Spring Boot 4.1.1 con Java 17 | Spring Boot 2.7.18 (no disponible) | Spring Initializr ya no ofrece 2.7.x ni Java 11. Migración mínima: solo `javax.*` → `jakarta.*`. | `backend-citas/pom.xml` |
| 2 | Usar Angular 16 vía `npx` | Angular 18 global | El curso estandariza en 16. Se evita romper el proyecto personal del CV que usa 18. | `frontend-citas/package.json` |
| 3 | Separar `DTOs` de `Entities` JPA | Devolver `Cita` directamente en JSON | Evita exponer estructura interna de BD, previene ciclos infinitos en la serialización y campos sensibles (`passwordHash`). | `dto/CitaResponseDTO.java` |
| 4 | Rutas REST en plural (`/disponibilidades`) | Rutas en singular | Convención REST: los recursos representan colecciones. | `openapi.yaml` |
| 5 | Manejo de errores con `@RestControllerAdvice` | Try/catch en cada controller | Centralización: un solo lugar para mapear excepciones a códigos HTTP (404, 409). | `exception/ManejadorGlobalExcepciones.java` |
| 6 | Proxy Angular (`proxy.conf.json`) en lugar de CORS | Habilitar CORS abierto | Más limpio en desarrollo; el navegador ve todo como mismo origen. Se conserva `CorsConfig` como respaldo. | `frontend-citas/proxy.conf.json` |
| 7 | `@Transactional` en `CitaService.crear()` | Sin transacción | Atomicidad: si falla la bitácora o el `UPDATE` de disponibilidad, se revierte la cita. | `CitaService.java` |
| 8 | Filtro `fecha >= CURRENT_DATE` | Sin filtro temporal | Evita mostrar horarios del pasado; coherente con la regla de negocio. | `DisponibilidadRepository.java` |

## 2. Bitácora de ejecución

| Fecha | Acción | Comando / Archivo | Resultado | Interpretación |
| :--- | :--- | :--- | :--- | :--- |
| 2026-10-01 | Verificación de entorno | `java -version` con JDK 17 | Java 17 confirmado | Spring Boot 4.1.1 requiere Java 17+ |
| 2026-10-01 | Generación backend | Spring Initializr (Web, JPA, PostgreSQL, Validation) | `BUILD SUCCESS` | Esqueleto inicial |
| 2026-10-01 | Configuración BD | `application.properties` + scripts SQL de P02 | Backend conectado a `citas_academicas` | Persistencia verificada |
| 2026-10-01 | Endpoint health | `curl /api/health` | `200 OK` con `{"status":"UP"}` | Verificación positiva |
| 2026-10-01 | Ruta inexistente | `curl /api/noexiste` | `404 Not Found` con `ErrorDTO` | Contrato de errores uniforme |
| 2026-10-01 | Entidades JPA | 5 clases con `@Entity` + `jakarta.persistence.*` | Hibernate valida esquema | Modelo de datos íntegro |
| 2026-10-02 | Endpoint disponibilidades | `curl /api/disponibilidades` | `200 OK` con JSON | Punto de integración listo |
| 2026-10-02 | Generación frontend | `npx -p @angular/cli@16 ng new frontend-citas` | Proyecto Angular 16 creado | Versión del curso |
| 2026-10-02 | Servicio Angular | `DisponibilidadService.listar()` | Tabla renderizada en `/horarios` | Integración frontend-backend |
| 2026-10-02 | Endpoints de cita | POST/GET/DELETE en `CitaController` | 201/200/204 verificados | Flujo CRUD completo |
| 2026-10-02 | Pruebas negativas | Doble reserva + cancelar cita ya cancelada | 409 en ambos casos | Reglas de negocio aplicadas |
| 2026-10-02 | Componentes Angular | `SolicitarComponent` + `MisCitasComponent` | Flujo completo en UI | SPA funcional |
| 2026-10-03 | Pruebas end-to-end | 3 pruebas (crear, doble reserva, cancelar) | Todas exitosas | Sistema integrado |

## 3. Dificultades y soluciones

| Problema | Causa | Solución |
| :--- | :--- | :--- |
| Spring Initializr ya no ofrece 2.7.18 ni Java 11 | Actualización de la plataforma | Adoptar Spring Boot 4.1.1 + Java 17 y migrar `javax.*` → `jakarta.*` |
| Angular CLI global era 18 | El proyecto del CV usa 18 | Usar `npx -p @angular/cli@16` para el proyecto del curso |
| Proxy no funcionaba al inicio | `proxy.conf.json` no estaba referenciado en `angular.json` | Añadir `"proxyConfig": "proxy.conf.json"` en `serve.options` |
| BD tenía datos viejos de P02 | Registros con fechas pasadas | Crear script de reseteo `03_reset_disponibilidad.sql` con `CURRENT_DATE + INTERVAL` |
| Error de serialización con `@ManyToOne` LAZY | `LazyInitializationException` fuera de la transacción | Añadir `@Transactional(readOnly = true)` en los métodos de servicio |

## 4. Mi contribución específica

- **Arquitectura**: diseño completo de capas + diagrama de componentes (Mermaid).
- **Backend**: 5 entidades JPA, 5 repositorios, 3 servicios, 4 controllers, 2 excepciones, 1 manejador global, 1 configuración CORS.
- **Frontend**: 3 componentes, 3 servicios HTTP, 3 modelos TypeScript, routing, navegación.
- **Contrato**: `openapi.yaml` con 6 endpoints documentados.
- **Base de datos**: reutilización del esquema de P02 + script de reseteo `03_reset_disponibilidad.sql`.
- **Documentación**: README reproducible, registro individual, esta bitácora.

## 5. Contribución de mis compañeros

- **Erick Romero García**: revisó el modelo de datos y sugirió que las restricciones `CHECK` y `UNIQUE` de PostgreSQL se reflejaran en las anotaciones JPA (`@Column(unique = true)`).
- **Josue Sánchez Valente**: revisó el catálogo de endpoints y sugirió la separación DTO/Entity para no exponer `passwordHash` en las respuestas.

## 6. Cumplimiento de criterios de aceptación

| CA | Descripción | Estado |
| :--- | :--- | :---: |
| CA01 | Flujo completo desde base sintética con cambio persistido |  VERIFICADO |
| CA02 | Caso inválido rechazado con mensaje comprensible | VERIFICADO |
| CA03 | API devuelve JSON coherente con 2xx y 4xx | VERIFICADO |
| CA04 | Evento IoT simulado (Web 4.0) | ⏳ PENDIENTE (P07) |
| CA05 | Otra persona puede preparar, ejecutar, probar y limpiar mediante README | VERIFICADO |
| CA06 | Autoría, contribución individual, limitaciones y cambios autorizados |  VERIFICADO |

## 7. Evidencia de contribución

- **Commits del repositorio**: https://github.com/UrielCruzHerrera/citas-academicas
- **Capturas**: `docs/capturas/` (diagrama, backend 200/404/201/409, frontend funcional)
- **Archivos clave**: `pom.xml`, `application.properties`, `CitaService.java`, `openapi.yaml`.

## 8. Limitaciones declaradas

- **Autenticación simulada**: el `idEstudiante` se fija en `1` como valor de demostración. En Web 2.0 se abordará con sesión JSF, y en Web 4.0 con JWT.
- **Confirmación por parte del asesor**: pendiente para P05.
- **Evento IoT**: pendiente para P07 (Web 4.0).
- **Datos reales**: no se usan. Todo es sintético.