# 🎓 Sistema de Controle de Monitoria - Frontend

Este projeto é o frontend de um sistema acadêmico para gerenciamento de monitorias, desenvolvido com Angular.

A aplicação permite gerenciar professores, alunos, cursos, disciplinas, matrizes curriculares, monitorias e relatórios acadêmicos, com autenticação via JWT e integração com backend em Spring Boot.

---

## 🚀 Tecnologias Utilizadas

* Angular (Standalone Components)
* TypeScript
* HTML5 + CSS3
* Angular Router
* HTTP Client (integração com API REST)
* JWT (autenticação)
* Git + GitHub

---

## 📌 Funcionalidades

### 🔐 Autenticação

* Login com JWT
* Proteção de rotas com Guard
* Interceptor para envio automático do token

### 👨‍🏫 Professores

* Cadastro de professores
* Validação de matrícula e telefone
* Associação com escola
* Cadastro de formação acadêmica (graduação, mestrado, doutorado, etc.)

### 🎓 Alunos

* Cadastro e listagem de alunos
* Associação com matrizes ofertadas

### 🏫 IES, Escolas e Cursos

* Cadastro de instituições de ensino
* Gerenciamento de escolas
* Cadastro de cursos vinculados

### 📚 Disciplinas e Matrizes Curriculares

* Cadastro de disciplinas
* Criação de matrizes curriculares
* Associação entre disciplinas e matrizes

### 📊 Monitorias

* Cadastro de monitorias
* Controle de status (ativa/inativa)
* Relacionamento com alunos e disciplinas

### 📈 Relatórios

* Geração de relatórios acadêmicos
* Visualização de dados de monitorias

### 📊 Dashboard

* Visão geral do sistema
* Indicadores principais

---

## 🖥️ Como Executar o Projeto

### 1️⃣ Clonar o repositório

```bash
git clone <url-do-repositorio>
```

---

### 2️⃣ Acessar a pasta

```bash
cd gestao-academica-fullstack/frontend
```

---

### 3️⃣ Instalar dependências

```bash
npm install
```

---

### 4️⃣ Executar o projeto

```bash
ng serve
```

---

### 5️⃣ Acessar no navegador

```
http://localhost:4200
```

---

## 🔗 Integração com Backend

Este frontend consome uma API REST desenvolvida em Spring Boot.

Certifique-se de que o backend esteja rodando em:

```
http://localhost:8080
```

---

## 📁 Estrutura do Projeto

```
src/
 ├── app/
 │   ├── core/        # Models, services globais, interceptors
 │   ├── shared/      # Componentes reutilizáveis
 │   ├── features/    # Módulos do sistema
 │   │   ├── auth
 │   │   ├── professores
 │   │   ├── alunos
 │   │   ├── cursos
 │   │   ├── escolas
 │   │   ├── ies
 │   │   ├── disciplinas
 │   │   ├── matrizes
 │   │   ├── monitorias
 │   │   └── relatorios
```



## 📌 Observações

* Projeto desenvolvido para fins acadêmicos
* Estrutura baseada em boas práticas de organização Angular
* Commits organizados por funcionalidades

