/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frames;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author AJMM
 */
public class enviadosFrame extends javax.swing.JFrame {

    /**
     * Creates new form enviadosFrame
     */
    Message[] mensajes;
    ArrayList<JTextArea> areasDeMensajes;
    public enviadosFrame(Message[] mensajes) throws IOException {
        initComponents();
        this.mensajes =  mensajes;
        areasDeMensajes = new ArrayList<>();
        
        panelEnviados.setLayout(new BoxLayout(panelEnviados, BoxLayout.Y_AXIS));//para que se expandan las textAreas
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //código conseguido de https://es.stackoverflow.com/questions/59000/como-leer-los-correos-enviados-desde-gmail-utilizando-javamail?utm_source=chatgpt.com
        try{
         for (Message m : mensajes) {

             //creo el area
            JTextArea currArea = new JTextArea(1,60);
            
            //añado pequeña descripción del mensaje
            currArea.setVisible(true);
            //destinatario
            ArrayList<String> destinatariosParsed = new ArrayList();
            for(String destinatario : InternetAddress.toString(m.getRecipients(Message.RecipientType.TO)).split(",")){
                destinatariosParsed.add(destinatario);
            }
            if (destinatariosParsed.size() > 1){
                currArea.setText("Para: " + destinatariosParsed.get(0) + " y " + (destinatariosParsed.size() - 1) + " más");
            }else{
                currArea.setText("Para: " + destinatariosParsed.get(0));
            }
                
            //asunto
             System.out.println("string length: " + currArea.getText().length());
            while(currArea.getText().length() < 33){//para que esté alineado
                currArea.append(" ");
            }
            currArea.append("\t" + m.getSubject());
            
            //fecha
            while(currArea.getText().length() < 66){//para que esté alineado
                currArea.append(" ");
            }
            //codigo para formatear fechas de: https://docs.oracle.com/javase/tutorial/i18n/format/simpleDateFormat.html
            Date fecha = m.getSentDate();
            SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'del' yyyy 'a las' hh:mm a", new Locale("es", "ES"));
            String fechaEnEspañol = formato.format(fecha);
            currArea.append("\t" + fechaEnEspañol);
            
            //le añado un listener para que cree el frame específico de cada mensaje
            //código específico de como usar un mouse listener sacado de: https://www.youtube.com/watch?v=jptf1Wd_omw
            currArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //creo el otro frame y le doy la info de los mensajes
                    Message currMensaje = mensajes[areasDeMensajes.indexOf(currArea)];
                    String[] destinatarios = null;
                    try {
                        destinatarios = InternetAddress.toString(currMensaje.getAllRecipients()).split(",");
                    } catch (MessagingException ex) {
                        Logger.getLogger(enviadosFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        //codigo para checar si es texto sacado de: https://old.chuidiang.org/java/herramientas/javamail/leer-correo-javamail.php
                        unCorreoEnviadoFrame frameDeCurrCorreo = new unCorreoEnviadoFrame(currMensaje.getSubject(), destinatarios,
                                currMensaje.getContent(), currMensaje.isMimeType("text/*"));
                        frameDeCurrCorreo.setVisible(true);
                    } catch (MessagingException ex) {
                        Logger.getLogger(enviadosFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(enviadosFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            currArea.setEditable(false);
            
            //añado el panel
            panelEnviados.add(currArea);
            areasDeMensajes.add(currArea);
            }
        }catch(MessagingException ex){
           
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPaneEnviados = new javax.swing.JScrollPane();
        panelEnviados = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout panelEnviadosLayout = new javax.swing.GroupLayout(panelEnviados);
        panelEnviados.setLayout(panelEnviadosLayout);
        panelEnviadosLayout.setHorizontalGroup(
            panelEnviadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 824, Short.MAX_VALUE)
        );
        panelEnviadosLayout.setVerticalGroup(
            panelEnviadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        scrollPaneEnviados.setViewportView(panelEnviados);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneEnviados, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneEnviados, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelEnviados;
    private javax.swing.JScrollPane scrollPaneEnviados;
    // End of variables declaration//GEN-END:variables
}

//                System.out.println("Bcc User Name: "+InternetAddress.toString(m.getRecipients(Message.RecipientType.BCC)));
//                System.out.println("SENT DATE: "+m.getSentDate());
//                System.out.println("SUBJECT: "+m.getSubject());
//System.out.println("Content: "+bp.getContent());
//System.out.println("Content:" + content);
                //creo el texArea de current message y lo añado
 //System.out.println("TextArea " + (areasDeMensajes.indexOf(currArea) + 1) + " clicked!");