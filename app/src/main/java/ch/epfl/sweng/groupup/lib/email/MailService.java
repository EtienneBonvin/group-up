package ch.epfl.sweng.groupup.lib.email;

import java.util.List;

/**
 * This is a basic API to provide email services that should allow to send emails
 * for different purposes everywhere in the app
 */
public interface MailService {
    void sendInvitationEmail(List<String> addresses);
    void sendInvitationEmail(String address);
}
