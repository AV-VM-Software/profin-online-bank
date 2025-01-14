Configure Apache Kafka
===========================
- start docker deamon
- ```docker-compose up -d```
- go to container term ```docker exec -it broker bash```


### Create topics 

- ```/opt/kafka/bin/kafka-topics.sh --create --topic transactions.pending --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1```
- ```/opt/kafka/bin/kafka-topics.sh --create --topic transactions.processed --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1```
- ```/opt/kafka/bin/kafka-topics.sh --create --topic transactions.notifications --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1```

### Validate created topics
```/opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092```

Should return:
```
transactions.notifications
transactions.pending
transactions.processed
```

 Used Patterns
===========================
- Saga orchestration
- Reactive programming
- Event driven architecture
- Microservice architecture
- Factory method
- Singleton 
- Observer Listener Publisher?
- Functional programming
- Builder
- Strategy


   Dev Notes
===========================
Не создавай новых
```
docker start broker 
docker start schema-registry
```

## look for messages in topic


```docker
# Просмотр последних N сообщений
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic transactions.pending \
    --from-beginning

```

удалить всё сообщение в топике
```docker
# Установить retention.ms в 1 секунду
/opt/kafka/bin/kafka-configs.sh --bootstrap-server localhost:9092 \
    --entity-type topics \
    --entity-name transactions.pending \
    --alter --add-config retention.ms=1

# Подождать несколько секунд

# Вернуть значение retention в -1 (никогда не удалять)
/opt/kafka/bin/kafka-configs.sh --bootstrap-server localhost:9092 \
    --entity-type topics \
    --entity-name transactions.pending \
    --alter --add-config retention.ms=-1

или 
# Удаляем топик
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic transactions.pending

# Создаем топик заново
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic transactions.pending --partitions 3 --replication-factor 1

или admin api?
```



## How to configure double push 2 orgins
```bash
git remote set-url --add --push origin git@github.com:AV-VM-Software/profin-online-bank.git
git remote set-url --add --push origin git@gitlab.fel.cvut.cz:manilvit/profin.git
```
```
git remote -v
```bash
Should return:
```
> origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (fetch)
origin  git@github.com:AV-VM-Software/profin-online-bank.git (push)
origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (push)
```

Eureka Server НЕ обязателен при использовании Kafka
Основные причины 1:

Kafka сам по себе обеспечивает балансировку нагрузки через партиции
Сервисы взаимодействуют через топики, а не через прямые HTTP вызовы
Kafka имеет собственный механизм обнаружения брокеров и потребителей
Когда нужен Eureka Server
При использовании REST взаимодействий между сервисами 1:
Для service discovery
Для load balancing через Ribbon
При использовании API Gateway (например, Zuul)
При гибридной архитектуре:
Часть сервисов общается через HTTP
Часть через Kafka



Transaction processing
===========================
- В итоге тразакция сохранятеся в баз
