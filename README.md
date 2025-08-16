# Sistema de Processamento de Pedidos Assíncrono

Este projeto demonstra uma arquitetura de microserviços em Java utilizando Spring Boot e RabbitMQ para processamento assíncrono de pedidos. Ele é ideal para quem busca entender a integração entre serviços desacoplados por meio de filas de mensagens, com uma estrutura que segue os princípios da Arquitetura Limpa (Clean Architecture).

### Destaques do Projeto

* **Configuração Maven Multi-Módulo**: O projeto é estruturado como um projeto pai Maven com dois submódulos, facilitando a gestão de dependências e a compilação de múltiplos microserviços em um único repositório.
* **Integração RabbitMQ**: Mostra como implementar o envio de mensagens (publicação) de pedidos em uma fila e o consumo dessas mensagens por um serviço processador, garantindo um fluxo de trabalho assíncrono e resiliente.
* **Serialização/Desserialização JSON**: Utiliza as bibliotecas `Jackson` e `Lombok` para converter objetos Java em JSON para envio via RabbitMQ e vice-versa, essencial para a comunicação entre os serviços. A linha `mapper.registerModule(new JavaTimeModule());` é utilizada para habilitar o Jackson a serializar classes de data e hora do Java 8+.
* **Padrão de Arquitetura Limpa (Clean Architecture)**: A estrutura de diretórios e a organização do código nos microserviços refletem os princípios da Arquitetura Limpa, separando as preocupações em camadas (Domínio, Aplicação, Infraestrutura) para promover código testável, manutenível e independente de frameworks externos.

### Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado em sua máquina:
* **Java Development Kit (JDK) 21 LTS**
* **Apache Maven**
* **Docker Desktop**: Necessário para rodar os containers de RabbitMQ e da aplicação.
* **WSL (Windows Subsystem for Linux)** ou **Git Bash**: Para executar o script de deploy automatizado.

---

## 🚀 Como Executar o Projeto

Você tem duas opções para executar o projeto: **manualmente** (para desenvolvimento) ou de forma **automatizada** (para simular um ambiente de CD).

### Opção 1: Execução Manual (Local)

Esta opção é ideal para desenvolvimento e testes rápidos.

1.  **Configure o RabbitMQ com Docker Desktop:**
    Inicie o container do RabbitMQ na sua máquina, que será acessível pelo seu projeto local.

    ```bash
    docker run -d --hostname my-rabbit --name rabbitmq-server -p 5672:5672 -p 15672:15672 rabbitmq:3.13.7-management
    ```

    Verifique se o RabbitMQ está rodando acessando `http://localhost:15672/`.

2.  **Clone e Compile o Repositório:**
    ```bash
    git clone [https://github.com/EmmanoelMonteiro/pedido-assincrono.git](https://github.com/EmmanoelMonteiro/pedido-assincrono.git)
    cd pedido-assincrono
    mvn clean install 
    ```

3.  **Execute os Microserviços:**
    * **Microserviço de Pedidos:**
        ```bash
        cd microservice-pedidos
        mvn spring-boot:run
        ```
    * **Microserviço Processador de Pedidos:**
      Abra um **novo terminal**, navegue até a pasta `microservice-processador-pedidos` e inicie-o:
        ```bash
        cd ../microservice-processador-pedidos
        mvn spring-boot:run
        ```

### Opção 2: Execução Automatizada com Docker (CD)

Esta opção utiliza um pipeline de deploy para construir e executar os microserviços em um ambiente isolado de containers.

1.  **Crie a Rede Docker:**
    Para que os containers de aplicação e de mensagens se comuniquem, eles devem estar na mesma rede Docker. Crie uma rede personalizada:
    ```bash
    docker network create projeto-pedidos-net
    ```

2.  **Inicie o RabbitMQ na Nova Rede:**
    Execute o comando para iniciar o RabbitMQ. **É crucial que ele seja criado na mesma rede `projeto-pedidos-net`** para permitir a comunicação com os microserviços. A imagem a ser utilizada é `rabbitmq:3.13.7-management` e o nome do container deve ser `rabbitmq-server`.
    ```bash
    docker run -d --network projeto-pedidos-net --hostname my-rabbit --name rabbitmq-server -p 5672:5672 -p 15672:15672 rabbitmq:3.13.7-management
    ```

3.  **Acesse o Terminal do WSL ou Git Bash:**
    Abra um terminal com ambiente `bash` e navegue até a raiz do projeto.

4.  **Execute o Script de Deploy:**
    O script `deploy-pipeline.sh` automatiza a compilação, cópia dos JARs e a execução dos microserviços dentro de um container Docker.

    ```bash
    chmod +x deploy-pipeline.sh
    ./deploy-pipeline.sh
    ```
    O script irá criar um container de aplicação e um container Nginx para gerenciar o acesso aos serviços.

---

## ✅ Testando o Sistema

Com o sistema rodando (em qualquer uma das opções), você pode testar o fluxo de processamento de pedidos.

Use uma ferramenta como `curl` (terminal) ou Postman/Insomnia para enviar uma requisição POST para o microserviço de pedidos.

**Para a Execução Manual:**
```bash
curl -X POST http://localhost:8080/api/pedidos \
-H "Content-Type: application/json" \
-d '{ "clienteId": "cliente-teste-123", "valorTotal": 99.99 }'