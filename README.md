# Informations
Les étapes à faire :
1. git clone <url-projet>
2. Ensuite utilisez ces commandes :
cd microservices/auth-service
./mvnw clean package -DskipTests
cd ../../microservices/order-service
./mvnw clean package -DskipTests
cd ../../microservices/invoice-service
./mvnw clean package -DskipTests
cd ../../microservices/notification-service
./mvnw clean package -DskipTests
cd ../../

si ça ne marche pas avec ./mvnw, utilisez mvn seulement ex. : mvn clean package -DskipTests

3. Lancez Docker Compose :
à l'emplacement où se trouve `docker-compose.yaml`, veuillez exécuter cette commande :
docker-compose up --build

4. Vérification : Vérififiez que tout fonctionne bien
- Prometheus : http://localhost:9090/
- Graphana : http://localhost:3000/
ajoutez Prometheus comme source de données
http://prometheus:9090

curl -X POST http://localhost/api/orders \
-H "Content-Type: application/json" \
-d '{
    "userId": 123,
    "productDescription": "Un produit fantastique",
    "quantity": 1,
    "totalPrice": 99.99
}'