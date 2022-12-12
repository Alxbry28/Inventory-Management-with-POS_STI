package com.example.inventorymanagementsystem.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailerService {

    private Properties properties;
    private String senderEmail, receiverEmail;
    private String senderPassword;
    private Context context;

    public MailerService() {
        senderEmail = "thesis0812@gmail.com";
        senderPassword = "hqhhgweigykddwac";

        String host = "smtp.gmail.com";
        String port = "465";
        String ssl = "true";
        String auth = "true";

        properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", ssl);
        properties.put("mail.smtp.auth", auth);
    }

    public void sendMailTest() {
        receiverEmail = "testingsystem28@gmail.com";
        String subject = "Subject: Testing Java Mailer";
        String message = "Hello this is your first message.";

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.setSender(new InternetAddress(senderEmail));
            mimeMessage.setReplyTo(new InternetAddress[]{new InternetAddress(senderEmail)});
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getReceiverEmail()));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

	    /*
	     * Use the following approach instead of the above line if
	     * you want to control the MIME type of the attached file.
	     * Normally you should never need to do this.
	     *
	    FileDataSource fds = new FileDataSource(filename) {
		public String getContentType() {
		    return "application/octet-stream";
		}
	    };
	    mbp2.setDataHandler(new DataHandler(fds));
	    mbp2.setFileName(fds.getName());
	     */

            // create the second message part
//            MimeBodyPart mbpAttachments = new MimeBodyPart();

//            // attach the file to the message
//            mbpAttachments.attachFile(filename);

            // create the Multipart and add its parts to it
//            Multipart mp = new MimeMultipart();
//            mp.addBodyPart(mbpAttachments);
//
//            mimeMessage.setContent(mp);

            SendMail sendMail = new SendMail();
            sendMail.setContext(context);
            sendMail.execute(mimeMessage);

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }

    }

    public void sendSalesReport(HashMap<String, String> salesDetails) {
        String subject = "Subject: Sales Report";
        String message = "This is generated ";

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.setSender(new InternetAddress(senderEmail));
            mimeMessage.setReplyTo(new InternetAddress[]{new InternetAddress(senderEmail)});
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getReceiverEmail()));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            SendMail sendMail = new SendMail();
            sendMail.setContext(context);
            sendMail.execute(mimeMessage);

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }

    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        private Context context;

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Please Wait", "Sending Mail To Store Owner....", true);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException me) {
                me.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.d("com.example.inventorymanagementsystem.services.MailerService", "onPostExecute: " + s);
            if (s.equals("Success")) {
                Log.d("com.example.inventorymanagementsystem.services.MailerService", "onPostExecute: success" + s);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Success</font>"));
                builder.setMessage("Mail send successfully");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            } else {
                Log.d("com.example.inventorymanagementsystem.services.MailerService", "onPostExecute: failed" + s);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#F41E08'>Failed</font>"));
                builder.setMessage("Mail send failed");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    }

}
