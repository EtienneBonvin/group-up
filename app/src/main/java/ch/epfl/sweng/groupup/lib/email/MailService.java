package ch.epfl.sweng.groupup.lib.email;

import java.util.List;

/**
 * This is a basic API to provide email services that should allow to send emails
 * for different purposes everywhere in the app
 */
public interface MailService {
    /**
     * Sends a predefined invitation email to every email address in the list
     * @param addresses the list of addresses to send the mail to
     */
    void sendInvitationEmail(List<String> addresses);

    /**
     * Sends a predefined invitation email to the given address
     * @param address the address to send the mail to
     */
    void sendInvitationEmail(String address);
}
