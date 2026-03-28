# FitBridge API - Guia de Requisições para Popular o Banco

> **Importante:** Execute as requisições na ordem abaixo (respeita as dependências entre tabelas).
>
> **Pré-requisito para busca sem acentos:** Execute no banco PostgreSQL:
> ```sql
> CREATE EXTENSION IF NOT EXISTS unaccent;
> ```

---

## 1. Cadastrar Instrutores

```
POST http://localhost:8080/api/instrutores/bulk
Content-Type: application/json
```
```json
[
  {
    "nome": "Carlos Silva",
    "sexo": "M",
    "idade": 35,
    "crefDiploma": "CREF 012345-G/SP",
    "email": "carlos@fitbridge.com",
    "senha": "123456"
  },
  {
    "nome": "Ana Oliveira",
    "sexo": "F",
    "idade": 28,
    "crefDiploma": "CREF 067890-G/PR",
    "email": "ana@fitbridge.com",
    "senha": "123456"
  }
]
```

---

## 2. Cadastrar Alunos

```
POST http://localhost:8080/api/alunos/bulk
Content-Type: application/json
```
```json
[
  {
    "nome": "João Lucas",
    "sexo": "M",
    "idade": 22,
    "altura": 1.78,
    "peso": 75.0,
    "objetivo": "Hipertrofia",
    "email": "joao@email.com",
    "senha": "123456"
  },
  {
    "nome": "Maria Fernanda",
    "sexo": "F",
    "idade": 25,
    "altura": 1.65,
    "peso": 60.0,
    "objetivo": "Emagrecimento",
    "email": "maria@email.com",
    "senha": "123456"
  }
]
```

---

## 3. Cadastrar Exercícios

> Faça um `POST` para cada exercício individualmente.

```
POST http://localhost:8080/api/exercicios
Content-Type: application/json
```

### Supino Reto
```json
{
  "nome": "Supino Reto",
  "descricao": "Deitar no banco e empurrar a barra para cima com os braços estendidos.",
  "musculoAlvo": "Peitoral",
  "musculosAuxiliares": "Tríceps, Deltóide Anterior",
  "diretorioImagem": ""
}
```

### Rosca Direta
```json
{
  "nome": "Rosca Direta",
  "descricao": "Em pé, flexionar os cotovelos trazendo a barra até os ombros.",
  "musculoAlvo": "Bíceps",
  "musculosAuxiliares": "Braquial, Braquiorradial",
  "diretorioImagem": ""
}
```

### Agachamento Livre
```json
{
  "nome": "Agachamento Livre",
  "descricao": "Com a barra apoiada nas costas, agachar até as coxas ficarem paralelas ao chão.",
  "musculoAlvo": "Quadríceps",
  "musculosAuxiliares": "Glúteo, Isquiotibiais",
  "diretorioImagem": ""
}
```

### Puxada Frontal
```json
{
  "nome": "Puxada Frontal",
  "descricao": "Sentado na máquina, puxar a barra até a altura do peito.",
  "musculoAlvo": "Costas",
  "musculosAuxiliares": "Bíceps, Romboides",
  "diretorioImagem": ""
}
```

### Elevação Lateral
```json
{
  "nome": "Elevação Lateral",
  "descricao": "Em pé, elevar os halteres lateralmente até a altura dos ombros.",
  "musculoAlvo": "Deltóide",
  "musculosAuxiliares": "Trapézio",
  "diretorioImagem": ""
}
```

### Leg Press
```json
{
  "nome": "Leg Press",
  "descricao": "Sentado na máquina, empurrar a plataforma com os pés.",
  "musculoAlvo": "Quadríceps",
  "musculosAuxiliares": "Glúteo, Isquiotibiais",
  "diretorioImagem": ""
}
```

---

## 4. Cadastrar Treinos (com exercícios vinculados)

> Os IDs abaixo assumem a ordem de criação dos passos anteriores:
> - **Instrutor 1** = Carlos Silva, **Instrutor 2** = Ana Oliveira
> - **Exercícios** IDs 1 a 6

### Treino A — Peito e Tríceps (Instrutor Carlos)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 1
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino A - Peito e Tríceps",
  "grupoMuscular": "Peitoral, Tríceps",
  "descricao": "Treino focado em empurrar. Ideal para segundas e quintas.",
  "instrutorId": 1,
  "itens": [
    { "exercicioId": 1, "series": 4, "repeticoes": 10 },
    { "exercicioId": 5, "series": 3, "repeticoes": 12 }
  ]
}
```

### Treino B — Costas e Bíceps (Instrutor Carlos)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 1
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino B - Costas e Bíceps",
  "grupoMuscular": "Costas, Bíceps",
  "descricao": "Treino focado em puxar. Ideal para terças e sextas.",
  "instrutorId": 1,
  "itens": [
    { "exercicioId": 4, "series": 4, "repeticoes": 10 },
    { "exercicioId": 2, "series": 3, "repeticoes": 12 }
  ]
}
```

### Treino C — Pernas e Glúteos (Instrutora Ana)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json

### Treinos adicionais para testes (inclui casos com acentos)

> Use estes treinos para verificar buscas case- e accent-insensitive.

#### Treino D — Glúteos e Tríceps (Instrutora Ana)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 2
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino D - Glúteos e Tríceps",
  "grupoMuscular": "Glúteo, Tríceps",
  "descricao": "Foco em glúteos e tríceps, com variações de isolamento.",
  "instrutorId": 2,
  "itens": [
    { "exercicioId": 6, "series": 4, "repeticoes": 12 },
    { "exercicioId": 1, "series": 3, "repeticoes": 10 }
  ]
}
```

#### Treino E — Ombros e Bíceps (Instrutor Carlos)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 1
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino E - Ombros e Bíceps",
  "grupoMuscular": "Deltóide, Bíceps",
  "descricao": "Trabalho de ombro com ênfase em deltóide lateral e bíceps.",
  "instrutorId": 1,
  "itens": [
    { "exercicioId": 5, "series": 4, "repeticoes": 12 },
    { "exercicioId": 2, "series": 3, "repeticoes": 10 }
  ]
}
```

#### Treino F — Full Body (títulos com acentos)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 1
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino F - Peito, Glúteo e Bíceps",
  "grupoMuscular": "Peitoral, Glúteo, Bíceps",
  "descricao": "Sessão full body para consolidar força e resistência.",
  "instrutorId": 1,
  "itens": [
    { "exercicioId": 1, "series": 3, "repeticoes": 8 },
    { "exercicioId": 3, "series": 3, "repeticoes": 10 },
    { "exercicioId": 2, "series": 3, "repeticoes": 12 }
  ]
}
```

#### Treino G — Isolamento Tríceps (teste específico de busca)

```
POST http://localhost:8080/api/treinos
Content-Type: application/json
X-User-ID: 2
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino G - Isolamento Tríceps",
  "grupoMuscular": "Tríceps",
  "descricao": "Exercícios específicos para tríceps.",
  "instrutorId": 2,
  "itens": [
    { "exercicioId": 1, "series": 4, "repeticoes": 10 }
  ]
}
```

X-User-ID: 2
X-User-Type: INSTRUTOR
```
```json
{
  "titulo": "Treino C - Pernas e Glúteos",
  "grupoMuscular": "Quadríceps, Glúteo",
  "descricao": "Treino completo de membros inferiores.",
  "instrutorId": 2,
  "itens": [
    { "exercicioId": 3, "series": 4, "repeticoes": 8 },
    { "exercicioId": 6, "series": 4, "repeticoes": 12 }
  ]
}
```

---

## 5. Testar as 4 Novas Funções

### Função 1 — Favoritar um treino

```
POST http://localhost:8080/api/favoritos?alunoId=1&treinoId=1
X-User-ID: 1
X-User-Type: ALUNO
```

Favoritar mais de um:
```
POST http://localhost:8080/api/favoritos?alunoId=1&treinoId=2
X-User-ID: 1
X-User-Type: ALUNO
```

```
POST http://localhost:8080/api/favoritos?alunoId=2&treinoId=3
X-User-ID: 2
X-User-Type: ALUNO
```

---

### Função 2 — Listar todos os treinos favoritos de um aluno

```
GET http://localhost:8080/api/favoritos/aluno/1
X-User-ID: 1
X-User-Type: ALUNO
```

```
GET http://localhost:8080/api/favoritos/aluno/2
X-User-ID: 2
X-User-Type: ALUNO
```

---

### Função 3 — Listar todos os treinos criados por um instrutor

```
GET http://localhost:8080/api/treinos/instrutor/1
```
> Retorna os treinos A e B (criados pelo Carlos).

```
GET http://localhost:8080/api/treinos/instrutor/2
```
> Retorna o treino C (criado pela Ana).

---

### Função 4 — Pesquisar treinos por título ou músculo alvo

**Busca por título:**
```
GET http://localhost:8080/api/treinos/buscar?q=peito
GET http://localhost:8080/api/treinos/buscar?q=costas
GET http://localhost:8080/api/treinos/buscar?q=perna
```

**Busca case-insensitive (maiúsculas/minúsculas):**
```
GET http://localhost:8080/api/treinos/buscar?q=PEITO
GET http://localhost:8080/api/treinos/buscar?q=Biceps
```

**Busca accent-insensitive (com e sem acentos):**
```
GET http://localhost:8080/api/treinos/buscar?q=gluteo
GET http://localhost:8080/api/treinos/buscar?q=Glúteo
GET http://localhost:8080/api/treinos/buscar?q=triceps
GET http://localhost:8080/api/treinos/buscar?q=Tríceps
```

---

## Resumo dos Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/instrutores` | Cadastrar instrutor |
| `POST` | `/api/instrutores/bulk` | Cadastrar vários instrutores |
| `POST` | `/api/alunos` | Cadastrar aluno |
| `POST` | `/api/alunos/bulk` | Cadastrar vários alunos |
| `POST` | `/api/exercicios` | Cadastrar exercício |
| `POST` | `/api/treinos` | Cadastrar treino |
| `POST` | `/api/treinos/bulk` | Cadastrar vários treinos |
| `POST` | `/api/favoritos?alunoId=X&treinoId=Y` | Favoritar um treino |
| `GET`  | `/api/favoritos/aluno/{alunoId}` | Listar favoritos do aluno |
| `GET`  | `/api/treinos/instrutor/{instrutorId}` | Listar treinos do instrutor |
| `GET`  | `/api/treinos/buscar?q=termo` | Pesquisar treinos por título/músculo |
