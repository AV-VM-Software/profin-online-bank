Get Started
===========================

###Configure Apache Kafka
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

### Run Spring Boot Services
make sure you have maven installed and kafka broker is running
    
* ```cd transaction-service/``` ```mvn spring-boot:run```
* ```cd account-management-service/``` ```mvn spring-boot:run```
- ```cd notification-service/```
- ```mvn spring-boot:run -Dspring-boot.run.arguments="--jasypt.encryptor.password=your_encryption_key```
- : This sets the Jasypt encryption key as a command-line argument for the application.


## Configure Smtp 
### Note : For testing purposes for now it is using my ecnrypted app password, skip this steps if you don't want to use your own email

To enable email notifications using your Google account, follow these steps:

### Step 1: Obtain a Google App Password

1. **Enable 2-Step Verification**:
    - Go to your Google Account settings.
    - Navigate to "Security".
    - Under "Signing in to Google", enable 2-Step Verification if it's not already enabled.

2. **Generate an App Password**:
    - After enabling 2-Step Verification, go back to the "Security" section.
    - Under "Signing in to Google", click on "App passwords".
    - Select "Mail" as the app and "Other" as the device, then enter a name (e.g., "MyApp SMTP").
    - Click "Generate" to get your app password. Make sure to copy it, as you'll need it for the next steps.

### Step 2: Encrypt your App Password
Use Jasypt to encrypt your app password:
```bash
 java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.3/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="your_app_password" password=your_encryption_key algorithm=PBEWithMD5AndDES
```

- Replace `your_app_password` with the app password you generated from Google.
- Replace `your_encryption_key` with a secure key that you'll use to encrypt and decrypt the password.
- When you start the notification service, pass the encryption key as a command-line argument.

you will get something like this:
``` 
----ENVIRONMENT-----------------

Runtime: Red Hat, Inc. OpenJDK 64-Bit Server VM 21.0.5+11 



----ARGUMENTS-------------------

input: aomz hrfw yxqp jozw
password: your_encryption_key
algorithm: PBEWithMD5AndDES



----OUTPUT----------------------

2fiWLcpEJU2YvBFBtSvEd/guPlDdVu87ETLZqpkZI64=

```

### Step 3: Configure the Notification Service
- Open the `application.properties` file in the `notification-service/src/main/resources` directory.
- Set the `spring.mail.username` property to your Gmail address.
- Set the `spring.mail.password` property to the encrypted app password you generated.


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
```
docker start broker 
```

## Look for messages in topic
```docker
# Просмотр последних N сообщений
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic transactions.pending \
    --from-beginning

```

## Delete topic
```docker
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic transactions.pending
```
```docker
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic transactions.pending --partitions 3 --replication-factor 1
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
should return:
```
> origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (fetch)
origin  git@github.com:AV-VM-Software/profin-online-bank.git (push)
origin  git@gitlab.fel.cvut.cz:manilvit/profin.git (push)
```


