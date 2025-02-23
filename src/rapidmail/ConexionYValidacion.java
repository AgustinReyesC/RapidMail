/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidmail;

import Frames.appCorreo;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author gutyc
 */

//Ahora si se supone que este comentario se tiene que guardar

public class ConexionYValidacion {
        
        private Properties props;
        private MimeMessage msg;
        private Transport mTransport;
        private Session sesion;
        
        //18 de febrero 2025: Cambié los métodos de static a no static para podes inicializar properties antes
        // y mantener la sesión abierta.
        
        public ConexionYValidacion(String remitente, String clave) {    
            props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            
            sesion = Session.getDefaultInstance(props);
            
            //Se conecta directamente, ya solo faltaría enviar los mensajes
            try {
                mTransport = sesion.getTransport("smtp");
                mTransport.connect(remitente, clave);
                
            } catch (MessagingException ex) {
                Logger.getLogger(ConexionYValidacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        public boolean seConecto() {
            return true;
        }
        

        //18 de febrero 2025: Ahora se crea el mensaje por separado, debido a que la conexion se da instantáneamente.
        public boolean construirMensaje(String remitente, ArrayList<String> destinatarios, String subject, String content) {
  
        msg = new MimeMessage(sesion);
        try {
            msg.setFrom(new InternetAddress(remitente)); 
            
            //18 de febrero 2025: ahora mi código no crea un nuevo mensaje por cada destinatario, sino que le manda 
            //a todos el mismo correo, lo que reduce mucho los tiempos
            for(String des : destinatarios){
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(des));
            }

            msg.setSubject(subject);
            msg.setContent(armarMensaje(content)); 
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
           
        return true;
    }
      
          
    //Método para generar estructura del archivo si es que tiene archivo o no 
    //18 de febrero 2025: Ahora tambien verifica que el mensaje esté encriptado con el método creado
    public Multipart armarMensaje(String content) throws MessagingException, IOException {
       
        ArrayList<File> arch = appCorreo.getArchivos();
        
        MimeBodyPart msj = new MimeBodyPart();
        msj.setText(content); 

        Multipart multi = new MimeMultipart();
        multi.addBodyPart(msj);

        if(!arch.isEmpty()) {
            for(File file: arch) {
              
                MimeBodyPart agregarArchivos = new MimeBodyPart();
                agregarArchivos.attachFile(file);
                
                multi.addBodyPart(agregarArchivos);
            } 
        }
        
       return multi;
    }
    
    
    
    
    
    //18 de febrero 2025: metodo exclusivo para enviar mensaje, para mantener la sesión abierta.    
    public boolean enviarMensaje() {
            try {           
                mTransport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO)); 
                
            } catch (MessagingException ex) {
                Logger.getLogger(ConexionYValidacion.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
        return true;
    }  
    
    
    
    //18 de Febrero 2025: Cree método cerrarConexion, importante para mejorar rapidez.
    public void cerrarConexion(Transport mTransport) {
        try {
            mTransport.close();
        } catch (MessagingException ex) {
            Logger.getLogger(ConexionYValidacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
