FROM openjdk:21-jdk-slim

LABEL maintainer="Emmanoel Monteiro <emmanoeljr@gmail.com>"
LABEL version="1.0"
LABEL description="Container de simulação de processamento de mensageria com RabbitMQ e microservices em Java."
LABEL created_by="Emmanoel Monteiro"

# Instalar dependências e Java 17
RUN apt-get update \
    && apt-get install -y nginx \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Remover a configuração padrão do Nginx que pode estar em sites-enabled, se presente.
RUN rm -f /etc/nginx/sites-enabled/default

# Criar diretório de aplicações
RUN mkdir -p /app

# Copiar o arquivo de configuração Nginx personalizado para a pasta de configurações do Nginx.
# O Nginx inclui automaticamente arquivos .conf dessa pasta.
COPY nginx_custom.conf /etc/nginx/sites-enabled/default.conf

# Copiar arquivos JAR para o container
COPY microservice-pedidos-1.0.0-SNAPSHOT.jar /app/pedidos.jar
COPY microservice-processador-pedidos-1.0.0-SNAPSHOT.jar /app/processador.jar

# Expor portas
EXPOSE 80 8080 8081

# Script de inicialização para rodar os serviços e o Nginx
RUN echo '#!/bin/bash\n\
java -jar /app/pedidos.jar &\n\
java -jar /app/processador.jar &\n\
nginx -g "daemon off;"\n' > /start.sh && chmod +x /start.sh

# Usar o script como comando padrão
CMD ["/start.sh"]

# Depois de criada a imagem, execute este comando no terminal para criar o container na rede correta de comunicação com o rabbitmq.
# docker run -d --name app-pedidos-teste --network projeto-pedidos-net -p 80:80 -p 8080:8080 -p 8081:8081 app-pedidos-teste:1.0
