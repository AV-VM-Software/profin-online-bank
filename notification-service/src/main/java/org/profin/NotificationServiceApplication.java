package org.profin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.profin.dto.PaymentStatus;
import org.profin.dto.TransactionDTO;
import org.profin.dto.TransactionType;
import org.profin.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
@EnableEncryptableProperties
public class NotificationServiceApplication {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

//	@PostConstruct
//	public void testSmtpConnection() {
//		try {
//			MimeMessage mimeMessage = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//
//			helper.setTo("vozhov.artem1@gmail.com");
//			helper.setSubject("Test");
//			helper.setText("Test message");
//			helper.setFrom(new InternetAddress("vozhov.artem1@gmail.com", "Test Sender"));
//
//			mailSender.send(mimeMessage);
//			System.out.println("SMTP connection successful");
//		} catch (MessagingException | UnsupportedEncodingException | jakarta.mail.MessagingException e) {
//			System.err.println("SMTP connection failed: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
	@PostConstruct
	public void testSmtpConnection() {
		log.info("Starting test SMTP connection");
		try {
			TransactionDTO testTransaction = TransactionDTO.builder()
					.id(1L)
					.userId(1L)
					.recipientId(2L)
					.idSenderAccount(1L)
					.idRecipientAccount(2L)
					.transactionType(TransactionType.TRANSFER)
					.amount(BigDecimal.valueOf(100.0))
					.paymentStatus(PaymentStatus.COMPLETED)
					.build();

			emailService.sendTransactionReceipt("vozhov.artem1@gmail.com", testTransaction);
			log.info("Test email sent successfully");
		} catch (Exception e) {
			log.error("Failed to send test email", e);
		}
	}


}
