# Pedido Assíncrono

Este projeto demonstra uma arquitetura de **microserviços em Java** utilizando **Spring Boot** e **RabbitMQ** para processamento assíncrono de pedidos.  
A proposta é mostrar a comunicação entre serviços desacoplados por meio de **filas de mensagens**, aplicando conceitos de **Clean Architecture**.

Além disso, o projeto aborda a **automação do processo de entrega contínua (CD)** utilizando um **Dockerfile**, que empacota toda a aplicação em containers padronizados, simplificando a implantação e garantindo consistência entre diferentes ambientes (desenvolvimento, homologação e produção).

---

## 🏗 Arquitetura

- **RabbitMQ**: responsável pela fila de mensagens para comunicação assíncrona.
- **Microservice Pedidos**: envia os pedidos para a fila.
- **Microservice Processador de Pedidos**: consome e processa os pedidos recebidos.
- **Nginx**: atua como proxy reverso para orquestrar o tráfego.
- **Docker**: utilizado para empacotar e executar toda a aplicação de forma isolada.

---

## 📂 Estrutura do Projeto

```
pedido-assincrono/
│── docker/
│   ├── Dockerfile             # Imagem contendo Nginx, serviços e configuração
│   ├── nginx_custom.conf      # Configuração personalizada do Nginx
│
│── microservice-pedidos/      # Serviço de envio de pedidos
│   ├── src/...
│   ├── pom.xml
│
│── microservice-processador-pedidos/ # Serviço de processamento de pedidos
│   ├── src/...
│   ├── pom.xml
│
│── README.md
```

---

## ⚙️ Pré-requisitos

- [Java 17+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- Git instalado

---
## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **RabbitMQ**
- **Docker**
- **Nginx**
- **Maven**

---

## 📘 Como funciona

1. O serviço **Pedidos** envia mensagens para a fila do RabbitMQ.
2. O serviço **Processador** consome essas mensagens e executa o processamento.
3. O **Nginx** faz o balanceamento/reverso para gerenciar requisições externas.

---
## 🚀 Execução do Projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/EmmanoelMonteiro/pedido-assincrono.git
cd pedido-assincrono
```

---

### 2. Construir os microserviços (gera os `.jar`)

No diretório raiz do projeto (`pedido-assincrono/`), execute:

```bash
mvn clean package -DskipTests
```

Esse comando irá compilar e empacotar todos os subprojetos definidos no pom.xml principal, gerando os arquivos .jar dentro de:

* microservice-pedidos/target/pedidos.jar
* microservice-processador-pedidos/target/processador.jar

Esses artefatos são utilizados pelo **Dockerfile** para construir a imagem da aplicação, garantindo a automação do processo de **Continuous Delivery (CD)**.
Assim, a cada nova versão gerada, é possível empacotar automaticamente os serviços em containers padronizados, prontos para implantação em qualquer ambiente.

### 3. Criar a rede Docker

```bash
docker network create projeto-pedidos-net
```

---

### 4. Subir o container do RabbitMQ

```bash
docker run -d --hostname rabbitmq-host --name rabbitmq-server --network projeto-pedidos-net -p 5672:5672 -p 15672:15672 rabbitmq:3.13.7-management
```
* `-d`: Roda o contêiner em modo detached.
* `--network projeto-pedidos-net`: Conecta o contêiner à rede criada.
* `--name rabbitmq-server`: Define o nome do contêiner.
* `-p 5672:5672`: Mapeia a porta padrão do RabbitMQ para a máquina hospedeira.
* `-p 15672:15672`: Mapeia a porta da interface de gerenciamento do RabbitMQ.

Acesse o painel em: [http://localhost:15672](http://localhost:15672)  

Login padrão: **guest / guest**

---

### 5. Construir e rodar a imagem da aplicação

```bash
docker build -t app-pedidos-image ./docker
```

```bash
docker run -d   --name app-pedidos   --network projeto-pedidos-net   -p 80:80 -p 8080:8080 -p 8081:8081   app-pedidos-image
```

---

## 🔄 Execução Manual (sem Docker)

### Subir o RabbitMQ

```bash
docker run -d  --hostname rabbitmq-host --name rabbitmq-server --network projeto-pedidos-net -p 5672:5672 -p 15672:15672 rabbitmq:3.13.7-management
```
* `-d`: Roda o contêiner em modo detached.
* `--network projeto-pedidos-net`: Conecta o contêiner à rede criada.
* `--name rabbitmq-server`: Define o nome do contêiner.
* `-p 5672:5672`: Mapeia a porta padrão do RabbitMQ para a máquina hospedeira.
* `-p 15672:15672`: Mapeia a porta da interface de gerenciamento do RabbitMQ.
* 
### Rodar os microserviços via Maven

```bash
cd microservice-pedidos
mvn spring-boot:run
```

```bash
cd ../microservice-processador-pedidos
mvn spring-boot:run
```
---

## 📡 Endpoints

- **Microservice Pedidos**: [http://localhost:8080](http://localhost:8080)
- **Microservice Processador**: [http://localhost:8081](http://localhost:8081)
- **RabbitMQ Management**: [http://localhost:15672](http://localhost:15672)

---

## ✅ Testando o Sistema

Com o sistema rodando (em qualquer uma das opções), você pode testar o fluxo de processamento de pedidos.

Use uma ferramenta como `curl` (terminal) ou Postman/Insomnia para enviar uma requisição POST para o microserviço de pedidos.

**Para a Execução Manual:**
```bash
curl -X POST http://localhost:8080/api/pedidos \
-H "Content-Type: application/json" \
-d '{ "clienteId": "cliente-teste-123", "valorTotal": 99.99 }'
```

---

## ⚡ Automatização com Dockerfile (CD)

Este projeto já possui um **Dockerfile** preparado para automatizar a entrega contínua (**Continuous Delivery - CD**).  
Ele garante que todo o ambiente da aplicação seja empacotado em uma única imagem, incluindo:

- Instalação do **Java 17**
- Configuração do **Nginx** como proxy reverso
- Inclusão dos microserviços (`pedidos.jar` e `processador.jar`)
- Script de inicialização para rodar **os serviços + Nginx**

Isso permite que qualquer desenvolvedor ou servidor de integração contínua (**CI/CD pipelines**) possa **gerar a imagem e rodar a aplicação sem precisar de configurações adicionais no ambiente**.

### 📦 Construir a imagem

Dentro da pasta raiz do projeto `pedido-assincrono/`, execute:

```bash
docker build -t app-pedidos-image -f ./docker/Dockerfile .
```

Depois de criada a imagem, execute este comando no terminal para criar o container na rede correta de comunicação com o rabbitmq.
```bash
docker run -d --name app-pedidos --network projeto-pedidos-net -p 80:80 app-pedidos-image
```
---

## 📌 Próximos passos (evoluções sugeridas)

- Criar um `docker-compose.yml` para orquestrar todos os containers de forma automatizada.
- Adicionar testes de integração entre os microserviços.
- Implementar logs centralizados com ELK Stack.
- Configurar monitoramento (Prometheus + Grafana).

