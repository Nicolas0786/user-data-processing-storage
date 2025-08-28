# User Data Processing Storage

Sistema para processamento e armazenamento de dados de usuários a partir de arquivos **CSV**, **JSON** ou **XML**.  
Os dados são armazenados em banco SQL na tabela `users`, incluindo a origem do arquivo (`source`).

---

## Funcionalidades
- Upload de arquivos **CSV**, **JSON** ou **XML** contendo `name` e `email`.
- Validação de formato e dados (nome obrigatório, e-mail válido).
- Persistência em banco de dados SQL (MySQL).
- Consulta de usuários em múltiplos formatos:
  - **JSON** (default)  
  - **XML**  
  - **CSV**  
- Tratamento de erros padronizado.
- Documentação interativa da API com **Swagger UI**.
- Deploy via **Docker Compose**.

---

## Tecnologias
- Java 17+
- Spring Boot 3 (Web, Data JPA, Validation)
- Hibernate / JPA
- MySQL 8
- Apache Commons CSV
- Jackson (JSON/XML)
- Swagger / OpenAPI
- Docker & Docker Compose

---

## Como rodar

### 1. Pré-requisitos
- Docker + Docker Compose instalados

### 2. Subir aplicação e banco
Na raiz do projeto, rode:
docker compose up -d --build ou docker compose up --build

### 3. Swagger UI
Acessa a documentação da API e seguir as orientações:
http://localhost:8080/swagger-ui/index.html

### 4. Como testar a Api Pelo Swagger
### UPLOAD
- No Swagger, abra o endpoint POST /user-file/upload.
- Clique em Try it out.
- Em type, digite: csv, json ou xml.
- Em file, clique em Choose File e selecione um arquivo de exemplo (ex.: users.csv).
- Clique em Execute.

### LISTAR USUARIOS
- No Swagger, abra o endpoint GET /user-file.
- Clique em Try it out.
- No campo format, você pode deixar vazio (retorna JSON por padrão) ou informar xml ou csv.
- Clique em Execute.
- A lista de usuários cadastrados será exibida no corpo da resposta.

---

## Exemplo de arquivo

### 1. CSV

name,email
John Doe,john.doe@example.com
Jane Smith,jane.smith@example.com

### 2. JSON

[
  { "name": "John Doe", "email": "john.doe@example.com" },
  { "name": "Jane Smith", "email": "jane.smith@example.com" }
]

### 3. XML

<users>
  <user>
    <name>John Doe</name>
    <email>john.doe@example.com</email>
  </user>
  <user>
    <name>Jane Smith</name>
    <email>jane.smith@example.com</email>
  </user>
</users>









