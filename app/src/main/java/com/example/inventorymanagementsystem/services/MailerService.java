package com.example.inventorymanagementsystem.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;

import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test2.xlsx");

            String filename = filePath.getAbsolutePath();
            DataSource source = new FileDataSource(filePath);

            mimeMessage.setSentDate(new Date());
            // create the second message part
            MimeBodyPart mbpAttachments = new MimeBodyPart();

            // attach the file to the message
            mbpAttachments.setDataHandler(new DataHandler(source));
            mbpAttachments.attachFile(filePath);

            // create the Multipart and add its parts to it
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mbpAttachments);

            mimeMessage.setContent(multipart);

            SendMail sendMail = new SendMail();
            sendMail.setContext(context);
            sendMail.execute(mimeMessage);

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sales_generated.xlsx");

            String filename = filePath.getAbsolutePath();
            DataSource source = new FileDataSource(filePath);

            mimeMessage.setSentDate(new Date());
            // create the second message part
            MimeBodyPart mbpAttachments = new MimeBodyPart();

            // attach the file to the message
            mbpAttachments.setDataHandler(new DataHandler(source));
            mbpAttachments.attachFile(filePath);

            // create the Multipart and add its parts to it
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mbpAttachments);

            mimeMessage.setContent(multipart);
            SendMail sendMail = new SendMail();
            sendMail.setContext(context);
            sendMail.execute(mimeMessage);

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            progressDialog = ProgressDialog.show(context, "Please Wait", "Sending Mail....", true);
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
