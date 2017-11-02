package ch.epfl.sweng.groupup.lib.email;

import java.util.List;

public class GMailService implements MailService {

    private MailSender s = new MailSender();

    /**
     * Sends a predefined invitation email to every email address in the list
     * @param addresses the list of addresses to send the mail to
     */
    @Override
    public void sendInvitationEmail(List<String> addresses) {
        s.execute(addresses.toArray(new String[addresses.size()]));

    }

    /**
     * Sends a predefined invitation email to the given address
     * @param address the address to send the mail to
     */
    @Override
    public void sendInvitationEmail(String address) {
        s.execute(address);
    }
}
