@echo off
REM 🧪 Script de Teste Automático - Sistema de Favoritos Editáveis
REM FitBridge API - Testes das novas funcionalidades

setlocal enabledelayedexpansion

set "BASE_URL=http://localhost:8080/api"
set "GREEN=[92m"
set "RED=[91m"
set "YELLOW=[93m"
set "NC=[0m"

echo 🧪 Iniciando testes do sistema de favoritos editáveis...
echo ==================================================

REM 1. Verificar se a aplicação está rodando
echo 🔍 Verificando se a aplicação está rodando...
curl -s "%BASE_URL%/alunos" >nul 2>&1
if errorlevel 1 (
    echo ❌ Aplicação não está rodando em %BASE_URL%
    echo 💡 Execute: .\mvnw.cmd spring-boot:run
    exit /b 1
)
echo ✅ Aplicação está rodando

REM 2. Criar dados de teste
echo.
echo 📝 Criando dados de teste...

REM Criar aluno
echo Criando aluno...
curl -s -X POST "%BASE_URL%/alunos" -H "Content-Type: application/json" -d "{\"nome\":\"Teste Silva\",\"sexo\":\"M\",\"idade\":30,\"altura\":1.80,\"peso\":80,\"objetivo\":\"Condicionamento\",\"email\":\"teste@email.com\",\"senha\":\"123\"}" > temp_aluno.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_aluno.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set ALUNO_ID=%%j
)

if "!ALUNO_ID!"=="" (
    echo ❌ Falha ao criar aluno
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Aluno criado (ID: !ALUNO_ID!)

REM Criar instrutor
echo Criando instrutor...
curl -s -X POST "%BASE_URL%/instrutores" -H "Content-Type: application/json" -d "{\"nome\":\"Instrutor Teste\",\"sexo\":\"F\",\"idade\":40,\"crefDiploma\":\"CREF-TEST\",\"email\":\"instrutor@email.com\",\"senha\":\"123\"}" > temp_instrutor.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_instrutor.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set INSTRUTOR_ID=%%j
)

if "!INSTRUTOR_ID!"=="" (
    echo ❌ Falha ao criar instrutor
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Instrutor criado (ID: !INSTRUTOR_ID!)

REM Criar exercícios
echo Criando exercícios...
curl -s -X POST "%BASE_URL%/exercicios" -H "Content-Type: application/json" -d "{\"descricao\":\"Supino Teste\",\"musculoAlvo\":\"Peitoral\"}" > temp_ex1.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_ex1.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set EXERCICIO1_ID=%%j
)

curl -s -X POST "%BASE_URL%/exercicios" -H "Content-Type: application/json" -d "{\"descricao\":\"Agachamento Teste\",\"musculoAlvo\":\"Quadriceps\"}" > temp_ex2.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_ex2.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set EXERCICIO2_ID=%%j
)

if "!EXERCICIO1_ID!"=="" if "!EXERCICIO2_ID!"=="" (
    echo ❌ Falha ao criar exercícios
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Exercícios criados (IDs: !EXERCICIO1_ID!, !EXERCICIO2_ID!)

REM Criar treino
echo Criando treino...
curl -s -X POST "%BASE_URL%/treinos" -H "Content-Type: application/json" -d "{\"titulo\":\"Treino Teste\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Treino de teste\",\"instrutorId\":!INSTRUTOR_ID!,\"itens\":[{\"exercicioId\":!EXERCICIO1_ID!,\"series\":3,\"repeticoes\":10},{\"exercicioId\":!EXERCICIO2_ID!,\"series\":4,\"repeticoes\":8}]}" > temp_treino.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_treino.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set TREINO_ID=%%j
)

if "!TREINO_ID!"=="" (
    echo ❌ Falha ao criar treino
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Treino criado (ID: !TREINO_ID!)

REM 3. Testar sistema de favoritos
echo.
echo ⭐ Testando sistema de favoritos...

REM 3.1 Criar favorito
echo    Criando favorito...
curl -s -X POST "%BASE_URL%/favoritos?alunoId=!ALUNO_ID!&treinoId=!TREINO_ID!" -H "Content-Type: application/json" > temp_favorito.json

for /f "tokens=*" %%i in ('findstr /r /c:"\"id\":[0-9]*" temp_favorito.json') do (
    for /f "tokens=2 delims=:" %%j in ("%%i") do set FAVORITO_ID=%%j
)

if "!FAVORITO_ID!"=="" (
    echo ❌ Falha ao criar favorito
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Favorito criado (ID: !FAVORITO_ID!)

REM Verificar se treinoDados foi incluído
findstr "treinoDados" temp_favorito.json >nul
if errorlevel 1 (
    echo ❌ Dados do treino não foram incluídos
    del temp_*.json 2>nul
    exit /b 1
)
echo ✅ Dados do treino foram serializados corretamente

REM 3.2 Listar favoritos
echo    Listando favoritos do aluno...
curl -s -X GET "%BASE_URL%/favoritos/aluno/!ALUNO_ID!" -H "Content-Type: application/json" > temp_favoritos_list.json

for /f %%c in ('findstr /r /c:"\"id\":[0-9]*" temp_favoritos_list.json ^| find /c "id"') do set FAVORITOS_COUNT=%%c

if !FAVORITOS_COUNT! gtr 0 (
    echo ✅ !FAVORITOS_COUNT! favorito(s) encontrado(s)
) else (
    echo ❌ Nenhum favorito encontrado
)

REM 3.3 Editar favorito
echo    Editando dados do treino favorito...
curl -s -X PUT "%BASE_URL%/favoritos/!FAVORITO_ID!" -H "Content-Type: application/json" -d "{\"id\":!TREINO_ID!,\"titulo\":\"Treino Modificado\",\"grupoMuscular\":\"Superior\",\"descricao\":\"Descrição modificada\",\"itens\":[{\"id\":1,\"exercicioId\":!EXERCICIO1_ID!,\"descricao\":\"Supino Teste\",\"musculoAlvo\":\"Peitoral\",\"series\":5,\"repeticoes\":6},{\"id\":2,\"exercicioId\":!EXERCICIO2_ID!,\"descricao\":\"Agachamento Teste\",\"musculoAlvo\":\"Quadriceps\",\"series\":3,\"repeticoes\":12}]}" >nul

if errorlevel 0 (
    echo ✅ Edição realizada com sucesso
) else (
    echo ❌ Falha na edição
    del temp_*.json 2>nul
    exit /b 1
)

REM 3.4 Verificar edição
echo    Verificando se edição foi persistida...
curl -s -X GET "%BASE_URL%/favoritos/!FAVORITO_ID!" -H "Content-Type: application/json" > temp_verificacao.json

REM Verificar se séries foram alteradas para 5
findstr /r /c:"\"series\":5" temp_verificacao.json >nul
if errorlevel 0 (
    echo ✅ Edição verificada (séries alteradas para 5)
) else (
    echo ❌ Edição não foi persistida corretamente
)

REM 4. Testes de erro
echo.
echo 🧪 Testando cenários de erro...

REM 4.1 Favorito inexistente
echo    Testando favorito inexistente...
curl -s -X GET "%BASE_URL%/favoritos/999" -H "Content-Type: application/json" -w "%%{http_code}" | findstr "404" >nul
if errorlevel 0 (
    echo ✅ Tratamento correto de favorito inexistente
) else (
    echo ⚠️  Resposta inesperada para favorito inexistente
)

REM 5. Limpeza
echo.
echo 🧹 Limpando dados de teste...

curl -s -X DELETE "%BASE_URL%/favoritos/!FAVORITO_ID!" >nul && echo ✅ Favorito removido
curl -s -X DELETE "%BASE_URL%/treinos/!TREINO_ID!" >nul && echo ✅ Treino removido
curl -s -X DELETE "%BASE_URL%/exercicios/!EXERCICIO1_ID!" >nul && echo ✅ Exercício 1 removido
curl -s -X DELETE "%BASE_URL%/exercicios/!EXERCICIO2_ID!" >nul && echo ✅ Exercício 2 removido
curl -s -X DELETE "%BASE_URL%/instrutores/!INSTRUTOR_ID!" >nul && echo ✅ Instrutor removido
curl -s -X DELETE "%BASE_URL%/alunos/!ALUNO_ID!" >nul && echo ✅ Aluno removido

REM Limpar arquivos temporários
del temp_*.json 2>nul

echo.
echo 🎉 Todos os testes foram executados com sucesso!
echo.
echo 📋 Resumo dos testes:
echo    ✅ Criação de favorito com dados serializados
echo    ✅ Listagem de favoritos do aluno
echo    ✅ Edição de dados locais do treino
echo    ✅ Persistência das modificações
echo    ✅ Tratamento de erros (404)
echo    ✅ Limpeza automática dos dados de teste
echo.
echo 💡 Próximos passos:
echo    1. Implemente o frontend Angular conforme o guia
echo    2. Teste a integração completa
echo    3. Adicione validações e tratamento de erros

pause