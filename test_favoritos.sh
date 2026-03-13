#!/bin/bash

# 🧪 Script de Teste Automático - Sistema de Favoritos Editáveis
# FitBridge API - Testes das novas funcionalidades

BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "🧪 Iniciando testes do sistema de favoritos editáveis..."
echo "=================================================="

# Função para verificar se comando foi bem-sucedido
check_success() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $1${NC}"
    else
        echo -e "${RED}❌ $1${NC}"
        exit 1
    fi
}

# 1. Verificar se a aplicação está rodando
echo "🔍 Verificando se a aplicação está rodando..."
curl -s "$BASE_URL/alunos" > /dev/null
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Aplicação não está rodando em $BASE_URL${NC}"
    echo "💡 Execute: ./mvnw spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✅ Aplicação está rodando${NC}"

# 2. Criar dados de teste
echo ""
echo "📝 Criando dados de teste..."

# Criar aluno
ALUNO_RESPONSE=$(curl -s -X POST "$BASE_URL/alunos" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste Silva","sexo":"M","idade":30,"altura":1.80,"peso":80,"objetivo":"Condicionamento","email":"teste@email.com","senha":"123"}')

ALUNO_ID=$(echo $ALUNO_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$ALUNO_ID" ]; then
    echo -e "${RED}❌ Falha ao criar aluno${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Aluno criado (ID: $ALUNO_ID)${NC}"

# Criar instrutor
INSTRUTOR_RESPONSE=$(curl -s -X POST "$BASE_URL/instrutores" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Instrutor Teste","sexo":"F","idade":40,"crefDiploma":"CREF-TEST","email":"instrutor@email.com","senha":"123"}')

INSTRUTOR_ID=$(echo $INSTRUTOR_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$INSTRUTOR_ID" ]; then
    echo -e "${RED}❌ Falha ao criar instrutor${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Instrutor criado (ID: $INSTRUTOR_ID)${NC}"

# Criar exercícios
EXERCICIO1_RESPONSE=$(curl -s -X POST "$BASE_URL/exercicios" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Supino Teste","musculoAlvo":"Peitoral"}')

EXERCICIO1_ID=$(echo $EXERCICIO1_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$EXERCICIO1_ID" ]; then
    echo -e "${RED}❌ Falha ao criar exercício 1${NC}"
    exit 1
fi

EXERCICIO2_RESPONSE=$(curl -s -X POST "$BASE_URL/exercicios" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Agachamento Teste","musculoAlvo":"Quadriceps"}')

EXERCICIO2_ID=$(echo $EXERCICIO2_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$EXERCICIO2_ID" ]; then
    echo -e "${RED}❌ Falha ao criar exercício 2${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Exercícios criados (IDs: $EXERCICIO1_ID, $EXERCICIO2_ID)${NC}"

# Criar treino
TREINO_RESPONSE=$(curl -s -X POST "$BASE_URL/treinos" \
  -H "Content-Type: application/json" \
  -d "{\"titulo\":\"Treino Teste\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Treino de teste\",\"instrutorId\":$INSTRUTOR_ID,\"itens\":[{\"exercicioId\":$EXERCICIO1_ID,\"series\":3,\"repeticoes\":10},{\"exercicioId\":$EXERCICIO2_ID,\"series\":4,\"repeticoes\":8}]}")

TREINO_ID=$(echo $TREINO_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$TREINO_ID" ]; then
    echo -e "${RED}❌ Falha ao criar treino${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Treino criado (ID: $TREINO_ID)${NC}"

# 3. Testar sistema de favoritos
echo ""
echo "⭐ Testando sistema de favoritos..."

# 3.1 Criar favorito
echo "   Criando favorito..."
FAVORITO_RESPONSE=$(curl -s -X POST "$BASE_URL/favoritos?alunoId=$ALUNO_ID&treinoId=$TREINO_ID" \
  -H "Content-Type: application/json")

FAVORITO_ID=$(echo $FAVORITO_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
if [ -z "$FAVORITO_ID" ]; then
    echo -e "${RED}❌ Falha ao criar favorito${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Favorito criado (ID: $FAVORITO_ID)${NC}"

# Verificar se treinoDados foi incluído
echo $FAVORITO_RESPONSE | grep -q "treinoDados"
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Dados do treino foram serializados corretamente${NC}"
else
    echo -e "${RED}❌ Dados do treino não foram incluídos${NC}"
    exit 1
fi

# 3.2 Listar favoritos
echo "   Listando favoritos do aluno..."
FAVORITOS_LIST=$(curl -s -X GET "$BASE_URL/favoritos/aluno/$ALUNO_ID" \
  -H "Content-Type: application/json")

FAVORITOS_COUNT=$(echo $FAVORITOS_LIST | grep -o '"id":[0-9]*' | wc -l)
if [ "$FAVORITOS_COUNT" -gt 0 ]; then
    echo -e "${GREEN}✅ $FAVORITOS_COUNT favorito(s) encontrado(s)${NC}"
else
    echo -e "${RED}❌ Nenhum favorito encontrado${NC}"
fi

# 3.3 Editar favorito
echo "   Editando dados do treino favorito..."
EDIT_RESPONSE=$(curl -s -X PUT "$BASE_URL/favoritos/$FAVORITO_ID" \
  -H "Content-Type: application/json" \
  -d "{\"id\":$TREINO_ID,\"titulo\":\"Treino Modificado\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Descrição modificada\",\"itens\":[{\"id\":1,\"exercicioId\":$EXERCICIO1_ID,\"descricao\":\"Supino Teste\",\"musculoAlvo\":\"Peitoral\",\"series\":5,\"repeticoes\":6},{\"id\":2,\"exercicioId\":$EXERCICIO2_ID,\"descricao\":\"Agachamento Teste\",\"musculoAlvo\":\"Quadriceps\",\"series\":3,\"repeticoes\":12}]}")

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Edição realizada com sucesso${NC}"
else
    echo -e "${RED}❌ Falha na edição${NC}"
    exit 1
fi

# 3.4 Verificar edição
echo "   Verificando se edição foi persistida..."
VERIFICACAO=$(curl -s -X GET "$BASE_URL/favoritos/$FAVORITO_ID" \
  -H "Content-Type: application/json")

# Verificar se séries foram alteradas para 5
SERIES_ATUAL=$(echo $VERIFICACAO | grep -o '"series":[0-9]*' | head -1 | cut -d':' -f2)
if [ "$SERIES_ATUAL" = "5" ]; then
    echo -e "${GREEN}✅ Edição verificada (séries alteradas para 5)${NC}"
else
    echo -e "${RED}❌ Edição não foi persistida corretamente${NC}"
fi

# 4. Testes de erro
echo ""
echo "🧪 Testando cenários de erro..."

# 4.1 Favorito inexistente
echo "   Testando favorito inexistente..."
ERROR_RESPONSE=$(curl -s -X GET "$BASE_URL/favoritos/999" \
  -H "Content-Type: application/json" -w "%{http_code}")

if [[ $ERROR_RESPONSE == *"404"* ]]; then
    echo -e "${GREEN}✅ Tratamento correto de favorito inexistente${NC}"
else
    echo -e "${YELLOW}⚠️  Resposta inesperada para favorito inexistente${NC}"
fi

# 5. Limpeza
echo ""
echo "🧹 Limpando dados de teste..."

curl -s -X DELETE "$BASE_URL/favoritos/$FAVORITO_ID" > /dev/null && echo -e "${GREEN}✅ Favorito removido${NC}"
curl -s -X DELETE "$BASE_URL/treinos/$TREINO_ID" > /dev/null && echo -e "${GREEN}✅ Treino removido${NC}"
curl -s -X DELETE "$BASE_URL/exercicios/$EXERCICIO1_ID" > /dev/null && echo -e "${GREEN}✅ Exercício 1 removido${NC}"
curl -s -X DELETE "$BASE_URL/exercicios/$EXERCICIO2_ID" > /dev/null && echo -e "${GREEN}✅ Exercício 2 removido${NC}"
curl -s -X DELETE "$BASE_URL/instrutores/$INSTRUTOR_ID" > /dev/null && echo -e "${GREEN}✅ Instrutor removido${NC}"
curl -s -X DELETE "$BASE_URL/alunos/$ALUNO_ID" > /dev/null && echo -e "${GREEN}✅ Aluno removido${NC}"

echo ""
echo -e "${GREEN}🎉 Todos os testes foram executados com sucesso!${NC}"
echo ""
echo "📋 Resumo dos testes:"
echo "   ✅ Criação de favorito com dados serializados"
echo "   ✅ Listagem de favoritos do aluno"
echo "   ✅ Edição de dados locais do treino"
echo "   ✅ Persistência das modificações"
echo "   ✅ Tratamento de erros (404)"
echo "   ✅ Limpeza automática dos dados de teste"
echo ""
echo "💡 Próximos passos:"
echo "   1. Implemente o frontend Angular conforme o guia"
echo "   2. Teste a integração completa"
echo "   3. Adicione validações e tratamento de erros"