/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidmail;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.swing.JOptionPane;

/**
 *
 * @author gutyc
 */
public class FuncionesLogin {
    public static boolean validarUsuario(String remitente, String clave) {
        
        //Generar una conexión rápida
        //Método que surge de la clase ConexionYValidación, donde sólo es genera la conexión, pero no el envío del mensaje.
        
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
            Session sesion = Session.getDefaultInstance(props);
             
        try {
            Transport mTransport = sesion.getTransport("smtp");
            mTransport.connect(remitente, clave);
            mTransport.close();
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Algo salió mal! Compruebe correo o contraseña");
            return false;
        }
        
        return true;
    }
    
    
    
    public static boolean validarCorreo(String correo) {
        
        if(!correo.contains("@") || !correo.contains(".")){
           JOptionPane.showMessageDialog(null, "Formato de correo incorrecto");
            return false;
        }
        return true;
    }
}
