package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // simpleMailMessage.setFrom("ravindernba2@gmail.com");
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        try {
            mailSender.send(simpleMailMessage);
            System.out.println("Mail successfully sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    // Specific method for sending alert notification
    public void sendAlertNotification(Alert alert,double currentPrice) {

        if (alert == null || alert.getUser() == null || alert.getUser().getEmail() == null) {
            System.err.println("Alert or User email is missing!");
            return;
        }

        String toEmail = alert.getUser().getEmail();
        String subject = "Crypto Alert Triggered for " + alert.getCoinSymbol();
        String body = "Hello " + alert.getUser().getUsername() + ",\n\n"
                + "📢 Your Crypto Price Alert has been triggered!\n\n"
                + "🔔 *Alert Details:*\n"
                + "• Coin: " + alert.getCoinName() + " (" + alert.getCoinSymbol() + ")\n"
                + "• Condition: Current Price " + alert.getOperator() + " " + alert.getThreshold() + "\n"
                + "• Threshold Set: " + alert.getThreshold() + "\n"
                + "• Current Price: " + currentPrice + "\n\n"
                + "Thank you for using *Crypto Wallet Tracker*! 🚀\n"
                + "Stay updated and informed.\n\n"
                + "--\n"
                + "Team Crypto Wallet";

        log.info("Sending email to " + toEmail);
        sendEmail(toEmail, subject, body);
    }

    public void sendAttachment(String username, byte[] data, String filename, String contentType) {
        User user = userRepository.findByEmail(username).orElseThrow();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Your Crypto Wallet Summary Report");
            helper.setText("Hello " + user.getUsername() + ",\n\nPlease find attached your crypto wallet summary report.\n\nBest regards,\nCrypto Tracker Team");

            helper.addAttachment(filename, new ByteArrayDataSource(data, contentType));

//            ByteArrayDataSource dataSource = new ByteArrayDataSource(data, contentType);
//            helper.addAttachment(filename, dataSource);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
