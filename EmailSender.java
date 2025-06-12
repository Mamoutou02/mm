import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;

public class EmailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String EMAIL = "";  // Votre email Gmail
    private static final String PASSWORD = "";  // Votre mot de passe d'application Gmail
    private final Session session;

    public EmailSender() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", HOST);

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            
            // Créer le contenu du message
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);
            
            // Créer le multipart pour combiner les parties
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            // Définir le contenu du message
            message.setContent(multipart);
            
            Transport.send(message);
            System.out.println("✓ Email envoyé à : " + to);
        } catch (MessagingException e) {
            System.err.println("✗ Échec de l'envoi à " + to + " : " + e.getMessage());
        }
    }
} 