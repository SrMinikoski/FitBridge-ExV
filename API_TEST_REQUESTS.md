# 🧪 Testes Manuais da API - Sistema de Favoritos Editáveis

## 📋 Pré-requisitos para Testes

Antes de executar os testes, certifique-se de que:

1. **Aplicação está rodando** na porta padrão (8080)
2. **Banco de dados PostgreSQL** está configurado e acessível
3. **Dados de teste existem**:
   - Pelo menos 1 Aluno (id: 1)
   - Pelo menos 1 Treino com exercícios (id: 1)
   - Pelo menos 1 Instrutor (id: 1)

---

## 🔄 Fluxo Completo de Teste

### **Passo 1: Verificar Dados Existentes**

```bash
# Verificar alunos existentes
curl -X GET "http://localhost:8080/api/alunos" \
  -H "Content-Type: application/json"

# Verificar treinos existentes
curl -X GET "http://localhost:8080/api/treinos" \
  -H "Content-Type: application/json"

# Verificar exercícios existentes
curl -X GET "http://localhost:8080/api/exercicios" \
  -H "Content-Type: application/json"
```

---

### **Passo 2: Criar Dados de Teste (se necessário)**

```bash
# Criar um aluno de teste
curl -X POST "http://localhost:8080/api/alunos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "sexo": "M",
    "idade": 25,
    "altura": 1.75,
    "peso": 70.5,
    "objetivo": "Ganho de massa muscular",
    "email": "joao.silva@email.com",
    "senha": "123456"
  }'

# Criar um instrutor de teste
curl -X POST "http://localhost:8080/api/instrutores" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "sexo": "F",
    "idade": 35,
    "crefDiploma": "CREF-12345",
    "email": "maria.santos@email.com",
    "senha": "123456"
  }'

# Criar exercícios de teste
curl -X POST "http://localhost:8080/api/exercicios" \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Supino Reto com Barra",
    "musculoAlvo": "Peitoral"
  }'

curl -X POST "http://localhost:8080/api/exercicios" \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Agachamento Livre",
    "musculoAlvo": "Quadríceps"
  }'

# Criar um treino de teste (substitua INSTRUTOR_ID pelo ID real)
curl -X POST "http://localhost:8080/api/treinos" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Treino Superior Completo",
    "grupoMuscular": "Peito e Pernas",
    "descricao": "Treino focado no desenvolvimento do peitoral e quadríceps",
    "instrutorId": 1,
    "itens": [
      {
        "exercicioId": 1,
        "series": 4,
        "repeticoes": 8
      },
      {
        "exercicioId": 2,
        "series": 3,
        "repeticoes": 10
      }
    ]
  }'
```

---

### **Passo 3: Testar Sistema de Favoritos**

#### **3.1 Criar um Favorito (Salvar Treino)**

```bash
# Criar favorito - substitua ALUNO_ID e TREINO_ID pelos IDs reais
curl -X POST "http://localhost:8080/api/favoritos?alunoId=1&treinoId=1" \
  -H "Content-Type: application/json"

# Resposta esperada:
# {
#   "id": 1,
#   "alunoId": 1,
#   "dataInclusao": "2026-03-12T21:00:00",
#   "treinoDados": {
#     "id": 1,
#     "titulo": "Treino Superior Completo",
#     "grupoMuscular": "Peito e Pernas",
#     "descricao": "Treino focado no desenvolvimento do peitoral e quadríceps",
#     "itens": [
#       {
#         "id": 1,
#         "exercicioId": 1,
#         "descricao": "Supino Reto com Barra",
#         "musculoAlvo": "Peitoral",
#         "series": 4,
#         "repeticoes": 8
#       },
#       {
#         "id": 2,
#         "exercicioId": 2,
#         "descricao": "Agachamento Livre",
#         "musculoAlvo": "Quadríceps",
#         "series": 3,
#         "repeticoes": 10
#       }
#     ]
#   }
# }
```

#### **3.2 Listar Favoritos de um Aluno**

```bash
# Listar todos os favoritos do aluno 1
curl -X GET "http://localhost:8080/api/favoritos/aluno/1" \
  -H "Content-Type: application/json"

# Resposta esperada: Array de FavoritoDTO
```

#### **3.3 Obter um Favorito Específico**

```bash
# Obter favorito com ID 1
curl -X GET "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json"

# Resposta esperada: Um FavoritoDTO completo
```

#### **3.4 Editar Dados Locais do Treino Favorito**

```bash
# Atualizar dados do treino favorito (modificar séries e repetições)
curl -X PUT "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "titulo": "Treino Superior Completo - MODIFICADO",
    "grupoMuscular": "Peito e Pernas",
    "descricao": "Treino personalizado com ajustes nas séries",
    "itens": [
      {
        "id": 1,
        "exercicioId": 1,
        "descricao": "Supino Reto com Barra",
        "musculoAlvo": "Peitoral",
        "series": 5,
        "repeticoes": 6
      },
      {
        "id": 2,
        "exercicioId": 2,
        "descricao": "Agachamento Livre",
        "musculoAlvo": "Quadríceps",
        "series": 4,
        "repeticoes": 8
      }
    ]
  }'

# Resposta esperada: FavoritoDTO atualizado com os novos dados
```

#### **3.5 Verificar se as Alterações Foram Salvas**

```bash
# Verificar se as alterações foram persistidas
curl -X GET "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json"

# Deve retornar os dados modificados (séries: 5→6, repetições: 4→8)
```

#### **3.6 Remover Favorito**

```bash
# Remover favorito
curl -X DELETE "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json"

# Status esperado: 204 No Content
```

---

## 🧪 Testes de Cenários Especiais

### **Cenário 1: Tentar Favoritar Treino Inexistente**

```bash
# Tentar favoritar treino que não existe
curl -X POST "http://localhost:8080/api/favoritos?alunoId=1&treinoId=999" \
  -H "Content-Type: application/json"

# Resposta esperada: 404 Not Found
```

### **Cenário 2: Tentar Editar Favorito Inexistente**

```bash
# Tentar editar favorito que não existe
curl -X PUT "http://localhost:8080/api/favoritos/999" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "titulo": "Treino Teste",
    "grupoMuscular": "Teste",
    "descricao": "Teste",
    "itens": []
  }'

# Resposta esperada: 404 Not Found
```

### **Cenário 3: Verificar Integridade dos Dados**

```bash
# Criar favorito
curl -X POST "http://localhost:8080/api/favoritos?alunoId=1&treinoId=1" \
  -H "Content-Type: application/json"

# Modificar apenas um exercício
curl -X PUT "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "titulo": "Treino Superior Completo",
    "grupoMuscular": "Peito e Pernas",
    "descricao": "Treino focado no desenvolvimento do peitoral e quadríceps",
    "itens": [
      {
        "id": 1,
        "exercicioId": 1,
        "descricao": "Supino Reto com Barra",
        "musculoAlvo": "Peitoral",
        "series": 6,
        "repeticoes": 4
      },
      {
        "id": 2,
        "exercicioId": 2,
        "descricao": "Agachamento Livre",
        "musculoAlvo": "Quadríceps",
        "series": 3,
        "repeticoes": 10
      }
    ]
  }'

# Verificar se apenas o primeiro exercício foi modificado
curl -X GET "http://localhost:8080/api/favoritos/1" \
  -H "Content-Type: application/json"
```

---

## 📊 Scripts de Teste Automatizados

### **Script Bash Completo (teste_favoritos.sh)**

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "🧪 Iniciando testes do sistema de favoritos..."

# 1. Criar dados de teste
echo "📝 Criando dados de teste..."
ALUNO_ID=$(curl -s -X POST "$BASE_URL/alunos" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste Silva","sexo":"M","idade":30,"altura":1.80,"peso":80,"objetivo":"Condicionamento","email":"teste@email.com","senha":"123"}' | jq -r '.id')

INSTRUTOR_ID=$(curl -s -X POST "$BASE_URL/instrutores" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Instrutor Teste","sexo":"F","idade":40,"crefDiploma":"CREF-TEST","email":"instrutor@email.com","senha":"123"}' | jq -r '.id')

EXERCICIO1_ID=$(curl -s -X POST "$BASE_URL/exercicios" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Supino Teste","musculoAlvo":"Peitoral"}' | jq -r '.id')

EXERCICIO2_ID=$(curl -s -X POST "$BASE_URL/exercicios" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Agachamento Teste","musculoAlvo":"Quadriceps"}' | jq -r '.id')

TREINO_ID=$(curl -s -X POST "$BASE_URL/treinos" \
  -H "Content-Type: application/json" \
  -d "{\"titulo\":\"Treino Teste\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Treino de teste\",\"instrutorId\":$INSTRUTOR_ID,\"itens\":[{\"exercicioId\":$EXERCICIO1_ID,\"series\":3,\"repeticoes\":10},{\"exercicioId\":$EXERCICIO2_ID,\"series\":4,\"repeticoes\":8}]}" | jq -r '.id')

echo "✅ Dados criados - Aluno: $ALUNO_ID, Treino: $TREINO_ID"

# 2. Testar criação de favorito
echo "⭐ Testando criação de favorito..."
FAVORITO_ID=$(curl -s -X POST "$BASE_URL/favoritos?alunoId=$ALUNO_ID&treinoId=$TREINO_ID" \
  -H "Content-Type: application/json" | jq -r '.id')

if [ "$FAVORITO_ID" != "null" ]; then
  echo "✅ Favorito criado com ID: $FAVORITO_ID"
else
  echo "❌ Falha ao criar favorito"
  exit 1
fi

# 3. Testar listagem de favoritos
echo "📋 Testando listagem de favoritos..."
FAVORITOS_COUNT=$(curl -s -X GET "$BASE_URL/favoritos/aluno/$ALUNO_ID" \
  -H "Content-Type: application/json" | jq length)

if [ "$FAVORITOS_COUNT" -gt 0 ]; then
  echo "✅ $FAVORITOS_COUNT favorito(s) encontrado(s)"
else
  echo "❌ Nenhum favorito encontrado"
fi

# 4. Testar edição de favorito
echo "✏️ Testando edição de favorito..."
curl -s -X PUT "$BASE_URL/favoritos/$FAVORITO_ID" \
  -H "Content-Type: application/json" \
  -d "{\"id\":$TREINO_ID,\"titulo\":\"Treino Modificado\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Descrição modificada\",\"itens\":[{\"id\":1,\"exercicioId\":$EXERCICIO1_ID,\"descricao\":\"Supino Teste\",\"musculoAlvo\":\"Peitoral\",\"series\":5,\"repeticoes\":6},{\"id\":2,\"exercicioId\":$EXERCICIO2_ID,\"descricao\":\"Agachamento Teste\",\"musculoAlvo\":\"Quadriceps\",\"series\":3,\"repeticoes\":12}]}" > /dev/null

echo "✅ Edição realizada"

# 5. Verificar edição
echo "🔍 Verificando edição..."
SERIES_MODIFICADAS=$(curl -s -X GET "$BASE_URL/favoritos/$FAVORITO_ID" \
  -H "Content-Type: application/json" | jq '.treinoDados.itens[0].series')

if [ "$SERIES_MODIFICADAS" = "5" ]; then
  echo "✅ Edição verificada com sucesso (séries: 5)"
else
  echo "❌ Edição não foi aplicada corretamente"
fi

# 6. Limpar dados de teste
echo "🧹 Limpando dados de teste..."
curl -s -X DELETE "$BASE_URL/favoritos/$FAVORITO_ID" > /dev/null
curl -s -X DELETE "$BASE_URL/treinos/$TREINO_ID" > /dev/null
curl -s -X DELETE "$BASE_URL/exercicios/$EXERCICIO1_ID" > /dev/null
curl -s -X DELETE "$BASE_URL/exercicios/$EXERCICIO2_ID" > /dev/null
curl -s -X DELETE "$BASE_URL/instrutores/$INSTRUTOR_ID" > /dev/null
curl -s -X DELETE "$BASE_URL/alunos/$ALUNO_ID" > /dev/null

echo "✅ Testes concluídos!"
```

### **Arquivo .http para VS Code (REST Client)**

```http
### Criar Aluno de Teste
POST http://localhost:8080/api/alunos
Content-Type: application/json

{
  "nome": "João Silva",
  "sexo": "M",
  "idade": 25,
  "altura": 1.75,
  "peso": 70.5,
  "objetivo": "Ganho de massa muscular",
  "email": "joao.silva@email.com",
  "senha": "123456"
}

### Criar Instrutor de Teste
POST http://localhost:8080/api/instrutores
Content-Type: application/json

{
  "nome": "Maria Santos",
  "sexo": "F",
  "idade": 35,
  "crefDiploma": "CREF-12345",
  "email": "maria.santos@email.com",
  "senha": "123456"
}

### Criar Exercícios de Teste
POST http://localhost:8080/api/exercicios
Content-Type: application/json

{
  "descricao": "Supino Reto com Barra",
  "musculoAlvo": "Peitoral"
}

###
POST http://localhost:8080/api/exercicios
Content-Type: application/json

{
  "descricao": "Agachamento Livre",
  "musculoAlvo": "Quadríceps"
}

### Criar Treino de Teste
POST http://localhost:8080/api/treinos
Content-Type: application/json

{
  "titulo": "Treino Superior Completo",
  "grupoMuscular": "Peito e Pernas",
  "descricao": "Treino focado no desenvolvimento do peitoral e quadríceps",
  "instrutorId": 1,
  "itens": [
    {
      "exercicioId": 1,
      "series": 4,
      "repeticoes": 8
    },
    {
      "exercicioId": 2,
      "series": 3,
      "repeticoes": 10
    }
  ]
}

### ⭐ CRIAR FAVORITO (Teste Principal)
POST http://localhost:8080/api/favoritos?alunoId=1&treinoId=1
Content-Type: application/json

### LISTAR FAVORITOS DO ALUNO
GET http://localhost:8080/api/favoritos/aluno/1
Content-Type: application/json

### OBTER FAVORITO ESPECÍFICO
GET http://localhost:8080/api/favoritos/1
Content-Type: application/json

### ✏️ EDITAR DADOS DO TREINO FAVORITO
PUT http://localhost:8080/api/favoritos/1
Content-Type: application/json

{
  "id": 1,
  "titulo": "Treino Superior Completo - MODIFICADO",
  "grupoMuscular": "Peito e Pernas",
  "descricao": "Treino personalizado com ajustes nas séries",
  "itens": [
    {
      "id": 1,
      "exercicioId": 1,
      "descricao": "Supino Reto com Barra",
      "musculoAlvo": "Peitoral",
      "series": 5,
      "repeticoes": 6
    },
    {
      "id": 2,
      "exercicioId": 2,
      "descricao": "Agachamento Livre",
      "musculoAlvo": "Quadríceps",
      "series": 4,
      "repeticoes": 8
    }
  ]
}

### VERIFICAR EDIÇÃO
GET http://localhost:8080/api/favoritos/1
Content-Type: application/json

### REMOVER FAVORITO
DELETE http://localhost:8080/api/favoritos/1
Content-Type: application/json

### 🧪 TESTES DE ERRO

### Tentar favoritar treino inexistente
POST http://localhost:8080/api/favoritos?alunoId=1&treinoId=999
Content-Type: application/json

### Tentar editar favorito inexistente
PUT http://localhost:8080/api/favoritos/999
Content-Type: application/json

{
  "id": 1,
  "titulo": "Treino Teste",
  "grupoMuscular": "Teste",
  "descricao": "Teste",
  "itens": []
}
```

---

## 📝 Instruções de Uso

1. **Inicie a aplicação Spring Boot**
2. **Execute os comandos curl** na ordem apresentada
3. **Verifique as respostas** - elas devem conter os dados completos dos treinos
4. **Teste as modificações** - altere séries/repetições e veja se persistem
5. **Para limpeza**: execute os comandos de DELETE ao final

**Ferramentas recomendadas:**
- **curl** (linha de comando)
- **Postman** (interface gráfica)
- **VS Code REST Client** (extensão .http)
- **jq** (para formatar JSON no terminal)

---

## 🎯 Casos de Teste Esperados

| Cenário | Requisição | Status Esperado | Verificação |
|---------|------------|-----------------|-------------|
| Criar favorito válido | POST /favoritos | 200 OK | Retorna FavoritoDTO com treinoDados |
| Listar favoritos | GET /favoritos/aluno/1 | 200 OK | Array de FavoritoDTO |
| Editar favorito | PUT /favoritos/1 | 200 OK | Dados modificados persistem |
| Favorito inexistente | GET /favoritos/999 | 404 | Not Found |
| Deletar favorito | DELETE /favoritos/1 | 204 | No Content |

**Sucesso!** 🎉 Agora você pode testar todas as funcionalidades implementadas!