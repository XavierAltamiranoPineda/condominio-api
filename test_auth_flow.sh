#!/bin/bash
# Script para probar el flujo completo de autenticación

BASE_URL="http://localhost:8080/api/v1"
USERNAME="admin"
PASSWORD="Admin@123"

echo "=========================================="
echo "🧪 INICIANDO PRUEBA DE AUTENTICACIÓN"
echo "=========================================="

echo -e "\n1️⃣ POST /auth/login"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\", \"password\":\"$PASSWORD\"}")

# Extraer tokens usando grep/sed (asumiendo jq no está instalado, o usándolo si lo está)
ACCESS_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
REFRESH_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"refreshToken":"[^"]*' | cut -d'"' -f4)

if [ -z "$ACCESS_TOKEN" ]; then
    echo "❌ Falló el login:"
    echo "$LOGIN_RESPONSE"
    exit 1
fi
echo "✅ Login exitoso. JWT obtenido."

echo -e "\n2️⃣ GET /usuarios protegido (con JWT válido)"
curl -s -o /dev/null -w "%{http_code}\n" -X GET "$BASE_URL/usuarios" \
  -H "Authorization: Bearer $ACCESS_TOKEN" | grep -q "200"
if [ $? -eq 0 ]; then
    echo "✅ 200 OK - Acceso permitido"
else
    echo "❌ Falló el acceso protegido"
fi

echo -e "\n3️⃣ GET /roles protegido (sin JWT para forzar 401/403)"
curl -s -o /dev/null -w "%{http_code}\n" -X GET "$BASE_URL/roles" | egrep -q "401|403"
if [ $? -eq 0 ]; then
    echo "✅ 401/403 - Acceso denegado (como se esperaba)"
else
    echo "❌ El endpoint permitió el acceso sin token"
fi

echo -e "\n4️⃣ POST /auth/refresh"
REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/refresh" \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\":\"$REFRESH_TOKEN\"}")

NEW_ACCESS_TOKEN=$(echo $REFRESH_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
if [ -n "$NEW_ACCESS_TOKEN" ]; then
    echo "✅ Refresh exitoso. Nuevo JWT obtenido."
else
    echo "❌ Falló el refresh token"
fi

echo -e "\n5️⃣ POST /auth/logout"
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE_URL/auth/logout" \
  -H "X-Refresh-Token: $REFRESH_TOKEN" | grep -q "200\|204"
if [ $? -eq 0 ]; then
    echo "✅ Logout exitoso"
else
    echo "❌ Falló el logout"
fi

echo -e "\n6️⃣ GET /usuarios protegido (con token inválido / inventado)"
curl -s -o /dev/null -w "%{http_code}\n" -X GET "$BASE_URL/usuarios" \
  -H "Authorization: Bearer invalid.token.here" | egrep -q "401|403"
if [ $? -eq 0 ]; then
    echo "✅ 401/403 - Token inválido rechazado"
else
    echo "❌ Falló la validación del token inválido"
fi

echo "=========================================="
echo "🎉 PRUEBA COMPLETADA"
echo "=========================================="
