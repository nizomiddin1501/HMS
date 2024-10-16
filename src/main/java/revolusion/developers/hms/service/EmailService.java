package revolusion.developers.hms.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
