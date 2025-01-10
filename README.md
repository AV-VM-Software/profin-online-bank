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


   Dev Notes
===========================
## How to configure double push 2 orgins
```
git remote set-url --add --push origin git@github.com:AV-VM-Software/profin-online-bank.git
git remote set-url --add --push origin git@gitlab.fel.cvut.cz:manilvit/profin.git
```
```
git remote -v
```
Should return:
```
> origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (fetch)
origin  git@github.com:AV-VM-Software/profin-online-bank.git (push)
origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (push)
```

