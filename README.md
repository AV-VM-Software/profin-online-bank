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