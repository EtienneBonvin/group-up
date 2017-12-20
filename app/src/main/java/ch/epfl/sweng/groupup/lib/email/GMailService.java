package ch.epfl.sweng.groupup.lib.email;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
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


public class GMailService implements MailService {

    public static class GMailSender extends javax.mail.Authenticator {

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


        public class ByteArrayDataSource implements DataSource {

            private String CONTENT_TYPE = "application/octet-stream";
            private String TYPE = "text/plain";
            private byte[] data;
            private String type;


            public ByteArrayDataSource(byte[] data) {
                super();
                this.data = data;
                this.type = TYPE;
            }


            public String getContentType() {
                if (type == null) {
                    return CONTENT_TYPE;
                } else {
                    return type;
                }
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


            public void setType(String type) {
                this.type = type;
            }
        }


        private Context ctx;
        private String password;
        private Session session;
        private String user;


        public GMailSender(final Context ctx) {
            this.ctx = ctx;
            this.user = ctx.getString(R.string.sweng_email);
            this.password = ctx.getString(R.string.sweng_password);

            Properties props = new Properties();
            props.setProperty(ctx.getString(R.string.gmail_protocol_key), ctx.getString(R.string.gmail_protocol_value));
            String mailhost = ctx.getString(R.string.gmail_host);
            props.setProperty(ctx.getString(R.string.gmail_host_key), mailhost);
            props.put(ctx.getString(R.string.gmail_auth_key), ctx.getString(R.string.gmail_host_value));
            props.put(ctx.getString(R.string.gmail_port_key), ctx.getString(R.string.gmail_port_value));
            props.put(ctx.getString(R.string.gmail_socketFactory_key),
                      ctx.getString(R.string.gmail_socketFactory_value));
            props.put(ctx.getString(R.string.gmail_socketFactory_class_key),
                      ctx.getString(R.string.gmail_socketFactory_class_value));
            props.put(ctx.getString(R.string.gmail_socketFactory_fallback_key),
                      ctx.getString(R.string.gmail_socketFactory_fallback_value));
            props.setProperty(ctx.getString(R.string.gmail_quitwait_key), ctx.getString(R.string.gmail_quitwait_value));

            session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(ctx.getString(R.string.sweng_email),
                                                      ctx.getString(R.string.sweng_password));
                }
            });
        }


        public synchronized void sendMail(String recipients) throws Exception {
            try {
                MimeMessage message = new MimeMessage(session);
                DataHandler handler = new DataHandler(new ByteArrayDataSource(
                        ctx.getString(R.string.invitationMail_content)
                           .getBytes()));
                message.setSender(new InternetAddress(ctx.getString(R.string.invitationMail_senderAddress)));
                message.setSubject(ctx.getString(R.string.invitationMail_subject));
                message.setDataHandler(handler);
                if (recipients.indexOf(',') > 0) {
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                } else {
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
                }
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        static {
            Security.addProvider(new JSSEProvider());
        }
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class MailSender extends AsyncTask<String, Void, Void> {

        private final Context ctx;


        public MailSender(Context ctx) {
            this.ctx = ctx;
        }


        @Override
        protected Void doInBackground(String... receiverEmailAddress) {
            for (String recMail : receiverEmailAddress) {
                try {
                    GMailSender sender = new GMailSender(ctx);
                    sender.sendMail(recMail);
                } catch (Exception e) {
                    AndroidHelper.showToast(ctx, "Unable to send an email to " + recMail, Toast.LENGTH_SHORT);
                }
            }
            return null;
        }
    }


    private final MailSender s;


    public GMailService(Context ctx) {
        s = new MailSender(ctx);
    }


    /**
     * Sends a predefined invitation email to every email address in the list
     *
     * @param addresses the list of addresses to send the mail to
     */
    @Override
    public void sendInvitationEmail(List<String> addresses) {
        s.execute(addresses.toArray(new String[addresses.size()]));
    }
}
