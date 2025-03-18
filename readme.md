# Desafio API - Sistema de Pedidos

## 📌 Descrição
Este projeto é uma API para gerenciamento de pedidos e estoque de produtos. O sistema permite a criação de usuários, autenticação, manipulação de produtos e relatórios gerenciais.

## 🛠 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT para autenticação**
- **JPA / Hibernate**
- **MySQL**

## 🚀 Como Executar o Projeto

### 1⃣ **Configurar o Banco de Dados**
Certifique-se de que o MySQL está rodando e que as configurações de conexão no `application.properties` estão corretas:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
```

### 2⃣ **Rodar a Aplicação**
No terminal, execute:
```sh
mvn spring-boot:run
```
A API estará rodando em `http://localhost:8080`

### 3⃣ **Importar a Collection do Insomnia**
A collection do Insomnia está disponível no repositório para facilitar os testes dos endpoints.

## 🔒 Autenticação e Uso do Token

1. **Cadastrar um usuário** (`ADMIN` ou `USER`):
   ```sh
   POST - http://localhost:8080/auth/register
   ```
   Enviar um JSON no corpo da requisição:
   ```json
   {
     "username": "admin",
     "password": "123456",
     "role": "ADMIN"
   }
   ```

2. **Realizar login para obter o token**:
   ```sh
   POST - http://localhost:8080/auth/login
   ```
   Corpo da requisição:
   ```json
   {
     "username": "admin",
     "password": "123456"
   }
   ```
   Resposta:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1..."
   }
   ```
   Copie o token retornado e inclua no **Header Authorization** das requisições seguintes:
   ```
   Authorization: Bearer SEU_TOKEN_AQUI
   ```

## 📌 Endpoints Disponíveis

### 🛍️ Produtos
- Criar um produto (**ADMIN**):
  ```sh
  POST - http://localhost:8080/products
  ```
- Atualizar um produto (**ADMIN**):
  ```sh
  PUT - http://localhost:8080/products/{id}
  ```
- Deletar um produto (**ADMIN**):
  ```sh
  DELETE - http://localhost:8080/products/{id}
  ```
- Listar todos os produtos:
  ```sh
  GET - http://localhost:8080/products
  ```
- Buscar um produto por ID:
  ```sh
  GET - http://localhost:8080/products/{id}
  ```

### 📊 Relatórios
- Usuários que mais compraram:
  ```sh
  GET - http://localhost:8080/reports/top-users
  ```
- Ticket médio das compras:
  ```sh
  GET - http://localhost:8080/reports/average-ticket
  ```
- Faturamento mensal:
  ```sh
  GET - http://localhost:8080/reports/monthly-revenue
  ```

### 🛠️ Pedidos
- Criar um novo pedido (**USER**):
  ```sh
  POST - http://localhost:8080/orders
  ```
- Realizar pagamento de um pedido (**USER**):
  ```sh
  POST - http://localhost:8080/orders/{id}/pay
  ```
- Listar pedidos do usuário (**USER**):
  ```sh
  GET - http://localhost:8080/orders
  ```

## 📝 Observação
- O repositório contém a **collection do Insomnia** para facilitar os testes.
- Para realizar qualquer operação protegida, é necessário autenticar-se e incluir o **token JWT** no cabeçalho.

---

