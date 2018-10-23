package mx.com.libreria.mail.context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import mx.com.libreria.manager.Logs;

/**
 * Clase para enviar correos electronicos.
 */
public class MailClient {
    /**
     * Session de correo electrónico.
     */
    private Session session;
    /**
     * Accesor para la sessión de correo electrónico.
     * @return Session
     */
    public Session getSession() {
        return session;
    }
    /**
     * Mutator para la sesión de correo electrónico.
     * @param pSession   Sessión de correo electrónico
     */
    public void setSession(final Session pSession) {
        this.session = pSession;
    }

    /**
     * Metodo encargado de enviar correos electronicos.
     * @param  from                Remitente
     * @param  to                  Destinatario
     * @param  subject             Mensaje
     * @param  messageBody         Cuerpo del Mensaje
     * @param  attachments         Archivos adjuntos
     * @throws MessagingException  Excepcion
     */
    public void sendMail(final String from,
        final String to, final String subject, final String messageBody,
        final String[] attachments)
        throws MessagingException {
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        
        // Create a message part to represent the body text 
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageBody, "text/html");        
        
        //use a MimeMultipart as we need to handle the file attachments 
        Multipart multipart = new MimeMultipart();

        //add the message body to the mime message 
        multipart.addBodyPart(messageBodyPart);

        // add any file attachments to the message 
        addAtachments(attachments, multipart);

        // Put all message parts in the message 
        message.setContent(multipart);

        // Send the message 
        Transport.send(message);
    }

    /**
     * Metodo para agregar archivos adjuntos.
     * @param  attachments         Archivos
     * @param  multipart           Objeto para guardar archivos
     * @throws MessagingException  Excepciones
     */
    protected void addAtachments(final String[] attachments,
        final Multipart multipart) throws MessagingException {
    
        if (attachments == null || attachments.length <= 0) {
            return;
        }

        for (int i = 0; i <= (attachments.length - 1); i++) {
            String filename = attachments[i];

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            //use a JAF FileDataSource as it does MIME type detection 
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));

            //assume that the filename you want to send is the same as the 
            //actual file name - could alter this to remove the file path 
            attachmentBodyPart.setFileName(filename);

            //add the attachment 
            multipart.addBodyPart(attachmentBodyPart);
        }
    }
        
    /**
     * Metodo para realizar pruebas a la clase.
     * @param args   Argumentos de consola
     */
    public static void main(final String[] args) {
        try {
            MailClient client = new MailClient();
            String from = "a00743525@itesm.mx";
            String to = "mauricio.gomez@hsbc.com.mx";
            String subject = "Test";
            String message = "Testing";
            String[] filenames = {"c://hsbc.gif"};

            client.sendMail(from, 
                            to, subject, message, filenames);
        } catch (Exception e) {
            Logs.error(MailClient.class, e.toString());
        }
    }
}
