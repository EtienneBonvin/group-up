package ch.epfl.sweng.groupup.lib.email;

import java.util.List;

/**
 * This class has been created for testing purpose and is used to mock
 * a GMailService. Note that it actually does NOT send any emails!
 */
public class MockGmailService implements MailService {
    @Override
    public void sendInvitationEmail(List<String> addresses) {
        for (String address: addresses) {
            sendInvitationEmail(address);
        }
    }

    @Override
    public void sendInvitationEmail(String address) {
        System.out.println("Sent invitation email to " + address);
    }
}
