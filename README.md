# Sistema de Processamento de Pedidos Assíncrono

Este projeto demonstra uma arquitetura de microserviços em Java utilizando Spring Boot e RabbitMQ para processamento assíncrono de pedidos. Ele é ideal para quem busca entender a integração entre serviços desacoplados por meio de filas de mensagens.

---

## Destaques do Projeto

* **Configuração Maven Multi-Módulo**: O projeto é estruturado como um projeto pai Maven com dois submódulos, facilitando a gestão de dependências e a compilação de múltiplos microserviços em um único repositório.
* **Integração RabbitMQ**: Mostra como implementar o envio de mensagens (publicação) de pedidos em uma fila e o consumo dessas mensagens por um serviço processador, garantindo um fluxo de trabalho assíncrono e resiliente.
* **Serialização/Desserialização JSON**: Utiliza as bibliotecas **Jackson** e **Lombok** para converter objetos Java em JSON para envio via RabbitMQ e vice-versa, essencial para a comunicação entre os serviços.
* **Resolução de Ciclos de Dependência no Spring**: Aborda a identificação e correção de ciclos de dependência entre *beans* Spring, um problema comum em aplicações complexas, garantindo a inicialização correta do contexto da aplicação.
* **Ajustes do Lombok**: Detalha a importância e o uso correto das anotações do Lombok (`@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) para gerar automaticamente código boilerplate, crucial para a serialização e desserialização de objetos pelo Jackson.

---

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado em sua máquina:

* **Java Development Kit (JDK) 21 LTS**
* **Apache Maven**
* **Docker Desktop**: Necessário para rodar o RabbitMQ em um container.

---

## Configuração do RabbitMQ com Docker Desktop

O projeto foi desenvolvido para se integrar com o RabbitMQ 3.13.7 rodando localmente. Se você ainda não tem o RabbitMQ em um container, siga os passos abaixo para iniciá-lo:

1.  **Abra o Docker Desktop.**
2.  **Execute o seguinte comando no seu terminal** para baixar e iniciar o container do RabbitMQ com a interface de gerenciamento:

    ```bash
    docker run -d --hostname my-rabbit --name rabbitmq-server -p 5672:5672 -p 15672:15672 rabbitmq:3.13.7-management
    ```

    * `-d`: Roda o container em modo *detached* (em segundo plano).
    * `--hostname my-rabbit`: Define o hostname do container.
    * `--name rabbitmq-server`: Atribui um nome ao container para fácil referência.
    * `-p 5672:5672`: Mapeia a porta padrão AMQP (Advanced Message Queuing Protocol) do RabbitMQ.
    * `-p 15672:15672`: Mapeia a porta da interface de gerenciamento web do RabbitMQ.

3.  **Verifique se o RabbitMQ está rodando** acessando a interface de gerenciamento no seu navegador: `http://localhost:15672/`. As credenciais padrão são `guest` para usuário e senha.

---

## Estrutura do Projeto

pedido-assincrono/
├── pom.xml
├── microservice-pedidos/
│   ├── src/main/java/com/exemplo/pedidos/
│   │   ├── ... (código do microserviço de pedidos)
│   └── src/main/resources/application.properties
└── microservice-processador-pedidos/
├── src/main/java/com/exemplo/processador/
│   ├── ... (código do microserviço processador)
└── src/main/resources/application.properties

---

## Como Executar o Projeto

Siga estes passos para baixar, compilar e executar os microserviços:

1.  **Clone o Repositório:**

    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    cd seu-repositorio
    ```

    *(Lembre-se de substituir `seu-usuario/seu-repositorio.git` pelo caminho real do seu projeto no GitHub)*

2.  **Compile o Projeto Pai:**
    No diretório raiz do projeto (`pedido-assincrono/`), execute o seguinte comando Maven para compilar todos os módulos e instalar as dependências:

    ```bash
    mvn clean install
    ```

3.  **Execute o Microserviço de Pedidos (`microservice-pedidos`):**
    Navegue até a pasta do `microservice-pedidos` e inicie-o:

    ```bash
    cd microservice-pedidos
    mvn spring-boot:run
    ```

    Este serviço iniciará na porta padrão `8080`.

4.  **Execute o Microserviço Processador de Pedidos (`microservice-processador-pedidos`):**
    Abra um **novo terminal**, navegue até a pasta do `microservice-processador-pedidos` e inicie-o:

    ```bash
    cd ../microservice-processador-pedidos
    mvn spring-boot:run
    ```

    Este serviço iniciará na porta padrão `8081` e começará a escutar mensagens na fila do RabbitMQ.

---

## Testando o Sistema

Com ambos os microserviços rodando e o RabbitMQ ativo, você pode criar um pedido enviando uma requisição POST para o `microservice-pedidos`.

Use uma ferramenta como `curl` (terminal) ou Postman/Insomnia para enviar a requisição:

```bash
curl -X POST http://localhost:8080/api/pedidos \
-H "Content-Type: application/json" \
-d '{
    "clienteId": "cliente-teste-123",
    "valorTotal": 99.99
}'