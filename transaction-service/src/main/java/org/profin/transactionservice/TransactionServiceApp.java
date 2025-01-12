package org.profin.transactionservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class TransactionServiceApp
{
    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApp.class, args);
    }

}
