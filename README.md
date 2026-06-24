# dot nanny — Backend

Spring Boot REST API for **dot nanny**, a childcare-agency platform that matches
vetted, background-checked **nannies** with **guardians** and their **wards**
(children). This backend mirrors the domain of the dot nanny frontend
(compliance/vetting, ward care & medical profiles, incidents & safeguarding,
ratio rules, bookings) and persists to **MongoDB**.

## Tech stack

- **Java 21** (toolchain)
- **Spring Boot 4** — Web MVC, Validation, Actuator
- **Spring Data MongoDB**
- **Gradle** (wrapper included)
- **Lombok**

## Getting started

### Prerequisites
- JDK 21
- MongoDB running locally (`mongodb://localhost:27017`) or a connection string

Start MongoDB quickly with Docker:

```bash
docker run -d --name dotnanny-mongo -p 27017:27017 mongo:7
```

### Run

```bash
./gradlew bootRun          # macOS/Linux
gradlew.bat bootRun        # Windows
```

The API starts on `http://localhost:8080`. On first run, demo data is seeded
automatically (see **Demo data** below). If MongoDB isn't running, the app still
starts and logs a warning — seeding is skipped.

### Build & test

```bash
./gradlew build            # compiles + runs unit tests (no MongoDB required)
```

The unit tests cover the ported compliance/ratio logic and do **not** need a
database.

## Configuration

Override via environment variables (defaults in `src/main/resources/application.yml`):

| Variable        | Default                                                        | Purpose                          |
| --------------- | ------------------------------------------------------------- | -------------------------------- |
| `MONGODB_URI`   | `mongodb://localhost:27017/dotnanny`                          | MongoDB connection string        |
| `PORT`          | `8080`                                                        | HTTP port                        |
| `CORS_ORIGINS`  | `http://localhost:5173,http://localhost:4173,http://localhost:3000` | Allowed frontend origins (CORS)  |

## API overview

Base path: `/api`

### Auth & users
| Method | Path             | Description                          |
| ------ | ---------------- | ------------------------------------ |
| POST   | `/auth/register` | Register a guardian or nanny         |
| POST   | `/auth/login`    | Login (returns the user + role)      |
| GET    | `/users`         | List users                           |

### Wards (children) — care & medical profiles
| Method | Path                     | Description                         |
| ------ | ------------------------ | ----------------------------------- |
| GET    | `/wards?guardianId=`     | List wards (optionally by guardian) |
| GET    | `/wards/{id}`            | Get a ward profile                  |
| GET    | `/wards/by-name/{name}`  | Look up a ward by name              |
| POST   | `/wards`                 | Create / update (upsert) a profile  |
| DELETE | `/wards/{id}`            | Delete (erasure)                    |

### Compliance & vetting
| Method | Path                                                  | Description                              |
| ------ | ----------------------------------------------------- | ---------------------------------------- |
| GET    | `/compliance/providers`                               | All providers + computed summaries       |
| GET    | `/compliance/health`                                  | Org-wide compliance rollup (KPI card)    |
| GET    | `/compliance/{providerId}`                            | A provider's record + summary            |
| GET    | `/compliance/{providerId}/approvable`                 | Whether the provider can be approved     |
| PUT    | `/compliance/{providerId}/jurisdiction`               | Set the provider's jurisdiction          |
| POST   | `/compliance/{providerId}/documents`                  | Upload / update a document               |
| DELETE | `/compliance/{providerId}/documents/{itemKey}`        | Remove a document                        |
| POST   | `/compliance/{providerId}/documents/{itemKey}/verify` | Verify / unverify (`?verified=true`)     |

### Incidents & safeguarding
| Method | Path                        | Description                          |
| ------ | --------------------------- | ------------------------------------ |
| GET    | `/incidents?nannyId=`       | List incidents (all or by nanny)     |
| POST   | `/incidents`                | File an incident report              |
| PUT    | `/incidents/{id}/status`    | Mark under review / closed           |
| PUT    | `/incidents/{id}/escalate`  | Escalate to the safeguarding authority |

### Ratios, bookings & reference data
| Method | Path               | Description                                       |
| ------ | ------------------ | ------------------------------------------------- |
| POST   | `/ratio/evaluate`  | Evaluate children-in-care vs jurisdiction limits  |
| GET    | `/bookings`        | List bookings (`?guardianId=` / `?nannyId=`)      |
| POST   | `/bookings`        | Create a booking                                  |
| GET    | `/jurisdictions`   | Jurisdiction rule sets (UK / Ontario / California) |
| GET    | `/consents`        | Standard childcare consent definitions            |
| GET    | `/age-groups`      | Age-group options                                 |
| GET    | `/health`          | Service health                                    |

Actuator health is also at `/actuator/health`.

## Demo data

Seeded on first run when collections are empty:

| Role     | Email                 | Password   |
| -------- | --------------------- | ---------- |
| Guardian | `ward@dot-nanny.com`  | `ward123`  |
| Nanny    | `nanny@dot-nanny.com` | `nanny123` |
| Admin    | `admin@dot-nanny.com` | `admin123` |

Plus seeded ward care profiles, provider compliance records across all three
jurisdictions (one approvable, one with outstanding checks), and three example
incident reports (including an escalated safeguarding case).

## Project structure

```
src/main/java/com/dotnanny
  common/      Enums (Role, Jurisdiction, DocStatus, Incident*…) + NotFoundException
  catalog/     Static jurisdiction rule sets + consent catalog (ported from the frontend)
  model/       MongoDB documents (User, WardProfile, ProviderCompliance, Incident, Booking)
  repository/  Spring Data Mongo repositories
  dto/         Request/response records
  service/     Business logic (compliance summarize, ratio evaluate, incidents…)
  web/         REST controllers + global exception handler
  config/      CORS + demo data seeder
```

## Notes

- **Auth is demo-grade** (plain-text passwords, no JWT/session). For production,
  add Spring Security with hashed credentials and token-based auth before
  exposing any real data.
- Children's medical data, background-check results and safeguarding incidents are
  sensitive — add encryption at rest, access control and audit logging before
  going live.
