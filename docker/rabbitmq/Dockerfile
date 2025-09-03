FROM rabbitmq:3-management

RUN apt-get update && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

COPY rabbitmq-init /etc/rabbitmq/rabbitmq-init.sh
RUN chmod +x /etc/rabbitmq/rabbitmq-init.sh
