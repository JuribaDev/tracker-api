#!/bin/bash

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

echo "Checking for required tools..."

if ! command_exists java; then
    echo "Java is not installed. Please install Java 17 or later."
    exit 1
fi

if ! command_exists mvn; then
    echo "Maven is not installed. Please install Maven."
    exit 1
fi

if ! command_exists openssl; then
    echo "OpenSSL is not installed. Please install OpenSSL."
    exit 1
fi

echo "All required tools are installed."

mkdir -p src/main/resources

echo "Generating RSA keys for JWT..."
openssl genrsa -out src/main/resources/app.key 2048
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub

echo "Creating application.properties..."
cat << EOF > src/main/resources/application.properties
# Server configuration
server.port=8080

# JWT configuration
jwt.public.key=classpath:app.pub
jwt.private.key=classpath:app.key

# Database configuration (update as needed)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging configuration
logging.level.org.springframework.security=DEBUG
EOF

echo "Application setup complete!"

echo "Updating .gitignore..."
if [ ! -f .gitignore ]; then
    touch .gitignore
fi

if ! grep -q "src/main/resources/app.key" .gitignore; then
    echo "src/main/resources/app.key" >> .gitignore
fi

if ! grep -q "src/main/resources/app.pub" .gitignore; then
    echo "src/main/resources/app.pub" >> .gitignore
fi

echo "Setup script completed successfully!"