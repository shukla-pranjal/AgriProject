# Agri-Project Master Developer Guide & Learning Resources

> This document is designed not just as project documentation, but as a learning resource for full-stack development with Spring Boot, React, and Microservices.



---

## 📚 Table of Contents
1. [Microservices Architecture Deep Dive](#1-microservices-architecture-deep-dive)
2. [Spring Boot Internals & Best Practices](#2-spring-boot-internals--best-practices)
3. [Data Persistence (JPA/Hibernate) Advanced](#3-data-persistence-jpahibernate-advanced)
4. [Security Implementation (JWT & Gateway)](#4-security-implementation-jwt--gateway)
5. [Infrastructure & DevOps (Docker/CI/CD)](#5-infrastructure--devops-dockercicd)
6. [Frontend Architecture (React)](#6-frontend-architecture-react)
7. [Machine Learning Integration](#7-machine-learning-integration)

---

## 1. Microservices Architecture Deep Dive

### Monolith vs. Microservices
- **Monolith**: Single deployable unit (JAR/WAR).
  - *Pros*: Simple to develop/test initially, ACID transactions easy.
  - *Cons*: Tight coupling, hard to scale specific parts, single point of failure (one memory leak crashes everything).
- **Microservices**: Loosely coupled services.
  - *Pros*: Independent scaling/deployment, fault isolation, technology agnostic (use Python for ML, Java for Backend).
  - *Cons*: Complexity (network latency, distributed transactions), operational overhead.

### Our Architecture Components
1.  **Service Discovery (Netflix Eureka)**:
    -   *Problem*: In cloud, IP addresses change dynamically. How does Service A find Service B?
    -   *Solution*: Services register with Eureka on startup ("I am USER-SERVICE at 10.0.0.1").
    -   *Client-Side Load Balancing*: Clients query Eureka to get available instances.

2.  **API Gateway (Spring Cloud Gateway)**:
    -   *Pattern*: **Gateway Routing Pattern**.
    -   *Role*: Reverse proxy. It hides the internal architecture from the client.
    -   *Features*:
        -   **Routing**: `/api/users/**` -> User Service.
        -   **Tech**: Built on **Spring WebFlux** (Reactive, Non-blocking I/O) using Project Reactor (Netty Server), widely preferred over blockage Zuul.

3.  **Cross-Cutting Concerns**:
    -   Things that affect all services: Logging, Metrics, Security.
    -   Handled at the Gateway or via AOP libraries included in services.

---

## 2. Spring Boot Internals & Best Practices

### Dependency Injection (DI) & IoC Container
-   **Inversion of Control (IoC)**: Instead of you creating objects (`new UserService()`), Spring does it.
-   **ApplicationContext**: The container that manages these objects (Beans).
-   **Bean Scopes**:
    -   `@Scope("singleton")`: (Default) One instance per container. Good for stateless services.
    -   `@Scope("prototype")`: New instance every request.
    -   `@Scope("request")`: One per HTTP request.

### Spring Bean Lifecycle
1.  **Instantiation**: Constructor called.
2.  **Populate Properties**: Dependencies injected (`@Autowired`).
3.  **Initialization**: `@PostConstruct` methods run.
4.  **Destruction**: `@PreDestroy` methods run before shutdown.

### AOP (Aspect Oriented Programming)
-   *Why?* To separate business logic from system services (Logging, Transaction management).
-   **Weaving**: Connecting aspects to your code. Spring uses **Runtime Weaving** (via Proxy objects).
-   *Example*: `@Transactional` creates a proxy around your method. It opens a transaction *before* your code runs and commits *after* it returns.

---

## 3. Data Persistence (JPA/Hibernate) Advanced

### Hibernate Entity States
1.  **Transient**: Just created (`new User()`), not in DB, no ID.
2.  **Persistent**: Associated with a Session/EntityManager. Changes are automatically saved (Dirty Checking).
3.  **Detached**: Session closed. Changes not saved unless re-attached (`merge()`).
4.  **Removed**: Scheduled for deletion.

### Fetch Types & Performance
-   **Lazy Loading (`FetchType.LAZY`)**:
    -   Default for `@OneToMany` / `@ManyToMany`.
    -   Data fetched only when accessed.
    -   *Risk*: **N+1 Problem** (1 query for parents, N queries for children).
    -   *Fix*: Use `JOIN FETCH` directly in JPQL queries.
-   **Eager Loading (`FetchType.EAGER`)**:
    -   Default for `@ManyToOne` / `@OneToOne`.
    -   Data fetched immediately.

### Caching Strategies
1.  **L1 Cache (Session)**: Enabled by default. Within distinct transaction.
2.  **L2 Cache (SessionFactory)**: Shared across sessions. Needs provider (EhCache, **Redis**, Caffeine).
    -   *Use Case*: Read-heavy, rarely-changing data (e.g., Categories, Cities).

---

## 4. Security Implementation (JWT & Gateway)

### Stateful vs. Stateless Auth
-   **Stateful (Session)**: Server keeps session ID in memory. Hard to scale (need sticky sessions or shared Redis session store).
-   **Stateless (JWT)**: Server signs a token. Client holds it. Any server can verify signature. easy horizontal scaling.

### JWT Structure (Header.Payload.Signature)
-   **Header**: Algo used (HS256).
-   **Payload (Claims)**: Data (User ID, Role, Expiry). **Open to read!** Do not put secrets here.
-   **Signature**: Hash of Header+Payload+SecretKey. Ensures integrity.

### Our Flow
1.  User posts Credentials to Auth Service.
2.  Auth Service validates & generates JWT.
3.  Client sends JWT in `Authorization: Bearer <token>` header.
4.  **Gateway** acts as the enforcement point:
    -   Intercepts request.
    -   Parses JWT.
    -   Validates Signature.
    -   Extracts Roles.
    -   Passes Identity to downstream services (via Header).

---

## 5. Infrastructure & DevOps (Docker/CI/CD)

### Docker Concepts
-   **Image vs Container**: Image is the recipe (Read-only class), Container is the instance (Runtime object).
-   **Layers**: Docker images are built in layers. Changing the last line of Dockerfile only rebuilds that layer.
    -   *Tip*: Copy `pom.xml` before source code. This caches the "Download Maven Dependencies" layer.

### CI/CD Pipeline Stages
1.  **Checkout**: Get code.
2.  **Build/Test**: Run unit tests (`mvn test`). Fail fast if bugs.
3.  **Package**: Create Docker image (`docker build`).
4.  **Push**: Upload to Registry (DockerHub).
5.  **Deploy**: (Future) SSH into server and `docker compose up -d`.

---

## 6. Frontend Architecture (React)

### React Hooks
-   `useState`: Local component state.
-   `useEffect`: Side effects (API calls, subscriptions). Dependency array `[]` controls when it runs.
-   `useContext`: Global state without Prop Drilling.

### Virtual DOM
-   React keeps a lightweight copy of DOM.
-   When state changes, it diffs new Virtual DOM with old one.
-   Only minimal real DOM updates are made. (Performance Booster).

---

## 7. Machine Learning Integration

### Workflow
-   **Training (Offline)**:
    1.  Data Collection (CSVs).
    2.  Preprocessing (Label Encoding, Normalization).
    3.  Model Training (`RandomForestClassifier`).
    4.  Serialization (`pickle.dump` -> `.pkl`).
-   **Inference (Online)**:
    1.  Flask App loads `.pkl` on startup.
    2.  Receives JSON input.
    3.  Calls `model.predict()`.
    4.  Returns prediction.

---

## 💡 Developer Cheat Sheet

### Git
-   `git stash`: Temporarily store changes.
-   `git rebase main`: Update your branch with main (cleaner history than merge).
-   `git cherry-pick <hash>`: Take one specific commit from another branch.

### Docker
-   `docker system prune -a`: Clean up all unused images/containers (Space saver!).
-   `docker logs --tail 100 -f <container_id>`: Live tail logs.
-   `docker exec -it <container_id> /bin/sh`: SSH into running container.
