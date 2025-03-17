# Desafio de Integração com Banco de Dados

## 📌 Descrição
Este projeto é uma API desenvolvida em **Java com Spring Boot** que gerencia pedidos e produtos, garantindo a consistência dos dados e a disponibilidade de estoque. Caso algum produto do pedido não tenha estoque disponível, o pedido será automaticamente cancelado e o usuário será notificado.

## 🚀 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **H2 Database (para testes)**
- **Maven**
- **JUnit & Mockito**
- **Git**

## 📂 Estrutura do Projeto
```
/src
 ├── main/java/com/desafio_api/app
 │   ├── controller/    # Controladores REST
 │   ├── domain/        # Modelos de Entidade
 │   ├── repository/    # Interfaces JPA
 │   ├── service/       # Lógica de Negócio
 │   ├── AppApplication.java  # Classe principal
 ├── test/java/com/desafio_api/app
 │   ├── service/       # Testes unitários
```

## 🛠 Configuração e Execução

### 1️⃣ **Configuração do Banco de Dados**
No arquivo `application.properties`, configure a conexão com o MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```
Caso queira usar o banco H2 para testes:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

### 2️⃣ **Rodando a Aplicação**
```sh
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

## 📌 Funcionalidades Implementadas

### 📦 **Gerenciamento de Pedidos e Estoque**
- Criar um pedido.
- Verificar estoque automaticamente.
- Cancelar o pedido caso não haja estoque disponível.
- Notificar o usuário sobre o cancelamento.
- Processar pagamento e atualizar estoque.

### 🔍 **Endpoints Disponíveis**
| Método | Endpoint                | Descrição                      |
|--------|-------------------------|--------------------------------|
| `POST` | `/products`             | Criar um novo produto          |
| `POST` | `/orders`               | Criar um novo produto          |
| `POST` | `/orders/1/pay`         | Realizar pagamento             |
| `GET`  | `/orders`               | Listar pedidos do usuário      |

## 🧪 Testes
Para rodar os testes unitários e de integração:
```sh
mvn test
```

## 📩 Contato
Caso tenha dúvidas, entre em contato via [seu email ou GitHub].

