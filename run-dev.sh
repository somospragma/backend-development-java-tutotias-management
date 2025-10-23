#!/bin/bash

echo "🚀 Iniciando Sistema de Tutorías en modo desarrollo..."

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose -f docker-compose.dev.yml down

# Construir y ejecutar los servicios
echo "🔨 Construyendo y ejecutando servicios..."
docker-compose -f docker-compose.dev.yml up --build -d

# Mostrar logs
echo "📋 Mostrando logs..."
docker-compose -f docker-compose.dev.yml logs -f