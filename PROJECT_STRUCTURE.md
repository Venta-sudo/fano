# PROJECT_STRUCTURE.md - Architecture et Hiérarchie du Projet

## 1. Structure Générale du Projet

```
projet-paiement/
├── microservices/              # Tous les services Spring Boot
│   ├── auth-service/
│   ├── order-service/
│   ├── invoice-service/
│   └── notification-service/
├── docker-compose.yaml         # Orchestration de tous les services
├── STACK.md                    # Choix technologiques
├── TODO.md                     # Livrables et objectifs
├── ROADMAP.md                  # Planning du projet
├── .gitignore                  # Ignorer les fichiers Maven/IDE
├── .gitattributes              # Normalisation des fins de ligne
└── setup.sh                    # Script d'initialisation
```

---

## 2. Structure d'un Microservice (Exemple: auth-service)

```
auth-service/
├── pom.xml                     # Dépendances Maven (Spring Boot, Kafka, PostgreSQL, etc.)
├── mvnw / mvnw.cmd             # Maven wrapper (permet ./mvnw au lieu de mvn)
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── AuthServiceApplication.java     # Point d'entrée Spring Boot
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java         # Configuration Spring Security
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java         # Endpoints REST (/api/auth/login, /api/auth/register)
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java           # DTO pour requête login
│   │   │   │   └── RegisterRequest.java        # DTO pour requête register
│   │   │   ├── model/
│   │   │   │   └── User.java                   # Entité JPA (table users)
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java         # Interface Spring Data JPA pour BD
│   │   │   └── service/
│   │   │       └── AuthService.java            # Logique métier (authentification, tokens, etc.)
│   │   └── resources/
│   │       ├── application.properties           # Config Spring (port, BD, Kafka)
│   │       ├── static/                          # Fichiers statiques (CSS, JS)
│   │       └── templates/                       # Templates HTML
│   └── test/
│       └── java/...                             # Tests unitaires
├── target/                      # Build Maven (ignoré par .gitignore)
├── HELP.md                      # Auto-généré par Spring Initializer
└── Dockerfile                   # À créer pour containerisation
```

---

## 3. Hiérarchie des Couches dans un Microservice

Chaque microservice suit une architecture en couches :

### 3.1 Couche Présentation (Controller)
**Fichier** : `AuthController.java`
- Reçoit les requêtes HTTP
- Valide les paramètres d'entrée
- Appelle la couche Service
- Retourne les réponses JSON

**Exemple** :
```
POST /api/auth/login → AuthController → appelle AuthService
```

### 3.2 Couche Service (Business Logic)
**Fichier** : `AuthService.java`
- Contient la logique métier
- Gère l'authentification, les tokens JWT
- Appelle le Repository pour accéder à la BD
- Publie les événements Kafka si nécessaire

**Exemple** :
```
Vérifier email/mot de passe → générer JWT → retourner token
```

### 3.3 Couche Données (Repository)
**Fichier** : `UserRepository.java`
- Interface Spring Data JPA
- Requêtes à PostgreSQL
- Pas de logique métier ici

**Exemple** :
```
findByEmail(String email) → requête SELECT * FROM users WHERE email = ?
```

### 3.4 Couche Modèle (Entity)
**Fichier** : `User.java`
- Représente la table PostgreSQL
- Annotations JPA (@Entity, @Table, @Column)
- Getters/Setters

### 3.5 DTOs (Data Transfer Objects)
**Fichiers** : `LoginRequest.java`, `RegisterRequest.java`
- Ne contiennent PAS les données sensibles (pas de mots de passe en réponse)
- Utilisés pour les requêtes/réponses HTTP
- Séparation entre modèle interne et API publique

### 3.6 Configuration
**Fichier** : `SecurityConfig.java`
- Configuration Spring Security
- Règles d'authentification
- CORS si nécessaire

---

## 4. Flux de Données (Exemple: Login)

```
1. Client envoie POST /api/auth/login avec LoginRequest
                            ↓
2. AuthController reçoit la requête
   - Valide le DTO
                            ↓
3. AuthController appelle AuthService.login(loginRequest)
                            ↓
4. AuthService :
   - Appelle UserRepository.findByEmail(email)
   - PostgreSQL retourne l'utilisateur
   - Vérifie le mot de passe (BCrypt)
   - Génère un JWT
   - Publie l'événement "user-logged-in" dans Kafka (optionnel)
                            ↓
5. AuthService retourne le token JWT
                            ↓
6. AuthController retourne la réponse JSON au client
```

---

## 5. Application.properties (Configuration)

Pour chaque service, configuré comme suit :

**auth-service/src/main/resources/application.properties**
```
# Port HTTP
server.port=8001

# PostgreSQL
spring.datasource.url=jdbc:postgresql://postgres:5432/auth_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update

# Kafka
spring.kafka.bootstrap-servers=kafka:9092
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

# Actuator (metrics pour Prometheus)
management.endpoints.web.exposure.include=health,metrics,prometheus
```

**Chaque service aura son propre port** :
- Auth-service : 8001
- Order-service : 8002
- Invoice-service : 8003
- Notification-service : 8004

---

## 6. Communication Inter-Services

### 6.1 Communication Synchrone (REST)
Un service appelle directement un autre via HTTP

**Exemple** : Order-service veut vérifier l'utilisateur
```
Order-service → HTTP GET /api/auth/verify/{userId} → Auth-service
```

### 6.2 Communication Asynchrone (Kafka)
Un service publie un événement, les autres écoutent

**Exemple** : Auth-service publie "user-registered"
```
Auth-service → Kafka topic "user-events" → 
  Order-service (listener) reçoit l'événement
  Notification-service (listener) reçoit l'événement
```

---

## 7. Docker et Conteneurisation

### 7.1 Dockerfile (à créer pour chaque service)
Chaque microservice a son Dockerfile pour être containerisé

### 7.2 docker-compose.yaml (À la racine)
Orchestre tous les services :
- 4 microservices Spring Boot
- 1 PostgreSQL
- 1 Kafka + Zookeeper
- 1 NGINX (load balancer)
- 1 Prometheus
- 1 Grafana

**Lancer tout** :
```bash
docker-compose up
```

---

## 8. Règles de Nommage et Conventions

| Élément | Convention | Exemple |
|---------|-----------|---------|
| Package | `com.example.demo` + domaine | `com.example.demo.auth` |
| Controller | `NomController` | `AuthController.java` |
| Service | `NomService` | `AuthService.java` |
| Repository | `NomRepository` | `UserRepository.java` |
| Entity | `Nom` (singulier) | `User.java` |
| DTO | `NomRequest` / `NomResponse` | `LoginRequest.java` |
| Configuration | `NomConfig` | `SecurityConfig.java` |
| Kafka Topic | lowercase-kebab-case | `user-events`, `order-created` |
| Port | Séquentiel | 8001, 8002, 8003, 8004 |

---

## 9. Flux de Développement (Pour chaque Service)

1. **Créer l'Entity** (modèle BD)
2. **Créer le Repository** (accès aux données)
3. **Créer les DTOs** (requêtes/réponses)
4. **Créer le Service** (logique métier + Kafka)
5. **Créer le Controller** (endpoints REST)
6. **Créer la Configuration** (Spring Security, etc.)
7. **Tester localement** : `./mvnw spring-boot:run`
8. **Containeriser** : créer le Dockerfile

---

## 10. Git et Versioning

- **Chaque microservice** est un dossier indépendant avec ses sources
- **.gitignore unifié** à la racine ignore tous les `/target/`, `/.idea/`, etc.
- **Push complet** : tout le projet va sur GitHub

**Structure GitHub** :
```
github.com/username/projet-paiement/
├── microservices/
│   ├── auth-service/
│   ├── order-service/
│   ├── invoice-service/
│   └── notification-service/
├── docker-compose.yaml
├── .gitignore
└── README.md
```

---

## Résumé

- **Projet = 1 docker-compose.yaml + 4 microservices indépendants**
- **Chaque microservice = architecture en 6 couches** (Controller → Service → Repository → Entity)
- **Communication** : REST (sync) ou Kafka (async)
- **Config centralisée** : application.properties de chaque service
- **Déploiement** : tout containerisé et orchestré