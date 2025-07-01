# LiterAlura Challenge-Java

Este projeto é um aplicativo de console desenvolvido em Java com Spring Boot, como parte de um Alura Challenge de programação. O objetivo é criar um catálogo de livros interativo que consome a API pública [Gutendex](https://gutendex.com/) para buscar informações sobre livros e autores, e persistir esses dados em um banco de dados PostgreSQL local.

## ✨ Funcionalidades

O menu da aplicação oferece as seguintes opções:

1.  **Buscar livro por título:** Busca na API Gutendex e salva o primeiro resultado encontrado no banco de dados local para consultas futuras.
2.  **Listar livros registrados:** Mostra todos os livros que foram salvos no banco de dados.
3.  **Listar autores registrados:** Mostra todos os autores dos livros que foram salvos.
4.  **Listar autores vivos em um determinado ano:** Filtra e exibe autores que estavam vivos no ano especificado.
5.  **Listar livros em um determinado idioma:** Mostra uma lista de livros registrados, filtrados por um idioma específico (ex: 'pt', 'en', 'es', 'fr').

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Data JPA** com **Hibernate** para a persistência de dados.
* **PostgreSQL** como banco de dados relacional.
* **Maven** para gerenciamento de dependências.
* **Jackson** para o parse de dados JSON da API.

## 🚀 Como Executar o Projeto

Siga os passos abaixo para executar o projeto em sua máquina local.

### Pré-requisitos

* **Java 17** ou superior instalado.
* **Maven** instalado.
* **PostgreSQL** instalado e em execução.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/literatura.git](https://github.com/seu-usuario/literatura.git)
    cd literatura
    ```
    *(Lembre-se de substituir `seu-usuario` pelo seu nome de usuário no GitHub)*

2.  **Crie o banco de dados:**
    Abra seu cliente PostgreSQL (como `psql` ou DBeaver) e execute o seguinte comando para criar o banco de dados:
    ```sql
    CREATE DATABASE literatura_db;
    ```

3.  **Configure a conexão:**
    Abra o arquivo `src/main/resources/application.properties` e altere as seguintes propriedades com as suas credenciais do PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/literatura_db
    spring.datasource.username=seu-usuario-do-postgres
    spring.datasource.password=sua-senha-do-postgres
    ```

4.  **Execute a aplicação:**
    Você pode executar a aplicação de duas maneiras:
    
    * **Via terminal Maven:**
        ```bash
        ./mvnw spring-boot:run
        ```
    * **Via sua IDE (IntelliJ IDEA, Eclipse):**
        Abra o projeto e execute a classe principal `LiteraturaApplication.java`.
