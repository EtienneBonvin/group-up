package ch.epfl.sweng.groupup.lib.email;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.epfl.sweng.groupup.lib.AndroidHelper;

public class GMailService implements MailService {
    private final MailSender s;

    public GMailService(Context ctx){
        s = new MailSender(ctx);
    }

    /**
     * Sends a predefined invitation email to every email address in the list
     * @param addresses the list of addresses to send the mail to
     */
    @Override
    public void sendInvitationEmail(List<String> addresses) {
        s.execute(addresses.toArray(new String[addresses.size()]));

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class MailSender extends AsyncTask<String, Void, Void> {
        private final Context ctx;

        public MailSender(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected Void doInBackground(String... receiverEmailAddress) {
            for (String recMail : receiverEmailAddress) {
                try {
                    GMailSender sender = new GMailSender();
                    sender.sendMail(recMail);
                } catch (Exception e) {
                    AndroidHelper.showToast(ctx, "Unable to send an email to " + recMail, Toast.LENGTH_SHORT);
                }
            }
            return null;
        }
    }

    public static class GMailSender extends javax.mail.Authenticator {
        private String user;
        private String password;
        private Session session;

        static {
            Security.addProvider(new JSSEProvider());
        }

        public GMailSender() {
            this.user = "swenggroupup@gmail.com";
            this.password = "swengswengsweng";

            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            String mailhost = "smtp.gmail.com";
            props.setProperty("mail.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.quitwait", "false");

            session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("swenggroupup@gmail.com", "swengswengsweng");
                }
            });
        }

        public synchronized void sendMail(String recipients) throws Exception {
            try{
                MimeMessage message = new MimeMessage(session);
                DataHandler handler = new DataHandler(new ByteArrayDataSource("That app rocks!".getBytes()));
                message.setSender(new InternetAddress("GroupUp! <swenggroupup@gmail.com>"));
                message.setSubject("Hello ✌️");
                message.setDataHandler(handler);
                if (recipients.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                else
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
                Transport.send(message);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public class ByteArrayDataSource implements DataSource {
            private byte[] data;
            private String type;

            public ByteArrayDataSource(byte[] data) {
                super();
                this.data = data;
                this.type = "text/plain";
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContentType() {
                if (type == null)
                    return "application/octet-stream";
                else
                    return type;
            }

            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(data);
            }

            public String getName() {
                return "ByteArrayDataSource";
            }

            public OutputStream getOutputStream() throws IOException {
                throw new IOException("Not Supported");
            }
        }

        private static final class JSSEProvider extends Provider {
            public JSSEProvider() {
                super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
                AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
                    public Void run() {
                        put("SSLContext.TLS",
                                "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                        put("Alg.Alias.SSLContext.TLSv1", "TLS");
                        put("KeyManagerFactory.X509",
                                "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                        put("TrustManagerFactory.X509",
                                "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                        return null;
                    }
                });
            }
        }
    }
}
