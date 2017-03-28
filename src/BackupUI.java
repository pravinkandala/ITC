import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jets3t.service.*;
import org.jets3t.service.impl.rest.httpclient.GoogleStorageService;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.GSBucket;
import org.jets3t.service.model.GSObject;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.security.GSCredentials;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BackupUI.java
 *
 * Created on Oct 27, 2011, 10:03:18 PM
 */

/**
 *
 * @author 
 */
public class BackupUI extends javax.swing.JFrame {
        String BUCKET_NAME_G="";
        String Access_key_G="";
        String Secret_key_G="";
        GSCredentials credentials_G = new GSCredentials(Access_key_G, Secret_key_G);;
        GoogleStorageService service_G = new GoogleStorageService(credentials_G);
         String BUCKET_NAME_AWS="BUCKET_NAME";
        String Access_key_AWS=" ";
         String Secret_key_AWS="";
	AWSCredentials credentials = new AWSCredentials(Access_key_AWS, Secret_key_AWS);
	// To communicate with AMAZON S3
	RestS3Service s3service = new RestS3Service(credentials);
    /** Creates new form BackupUI */
    public BackupUI(String storage,String backup) throws ServiceException, IOException, NoSuchAlgorithmException, FileNotFoundException, PatchFailedException, ClassNotFoundException {
        initComponents();
        if(storage.contentEquals("GOOGLE STORAGE"))
        {
            GSBucket bucket=null;
            GSBucket [] gsbuckets=service_G.listAllBuckets();
            ArrayList al=new ArrayList();
            for(int i=0;i<gsbuckets.length;i++)
            {
                 al.add(gsbuckets[i].getName());
            }
            if(!al.contains(BUCKET_NAME_G))
                  bucket = service_G.createBucket(BUCKET_NAME_G);
            jLabel2.setText(storage);
            jTextArea1.setVisible(false);
            GSObject[] objects = service_G.listObjects(BUCKET_NAME_G);
            ArrayList gobj=new ArrayList();
            for(int i=0;i<objects.length;i++)
            {

                gobj.add(objects[i]);
            }
      
            int ret = jFileChooser1.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                 File[] files = jFileChooser1.getSelectedFiles();
                 jLabel1.setText("UPLOADING THE FILES INTO");
                 String text="";
                 for(int i=0;i<files.length;i++)
                 {
                    text=text+"\n"+files[i].getName();
                 }
                 if(backup.contentEquals("INCREMENTAL"))
                 {
                     GoogleIncremental gi=new GoogleIncremental(BUCKET_NAME_G,files,service_G,gobj);
                 }
                 else
                 {
                     ArrayList names =new ArrayList();
                     Iterator itr = gobj.iterator();
                     while (itr.hasNext()) {
                           GSObject element = (GSObject) itr.next();
                           names.add(element.getName());
                     }
      
                     for(int i=0;i<files.length;i++)
                     {
                     if(names.contains(files[i].getName()))
                     {
                         createFile_G(gobj,files[i]);
                     }
                     else
                     {
                         GSObject localObject = new GSObject(files[i]);
	                 localObject = service_G.putObject(BUCKET_NAME_G, localObject);
                     }

                     }
      
                 }
                 jTextArea1.setText(text);
                 jTextArea1.setVisible(true);
                 jLabel1.setText("UPLOADED THE FILES INTO");
            }
     }
     else
     {

          S3Bucket bucket=null;     
          S3Bucket [] sbuckets=s3service.listAllBuckets();
          ArrayList al=new ArrayList();
          for(int i=0;i<sbuckets.length;i++)
          {
            al.add(sbuckets[i].getName());
          }
          if(!al.contains(BUCKET_NAME_AWS))
               bucket = s3service.createBucket(BUCKET_NAME_AWS);
          jLabel2.setText(storage);
          jTextArea1.setVisible(false);
           S3Object[] objects = s3service.listObjects(BUCKET_NAME_AWS);
           ArrayList s3obj=new ArrayList();
            for(int i=0;i<objects.length;i++)
            {

                s3obj.add(objects[i]);
            }
            int ret = jFileChooser1.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                 File[] files = jFileChooser1.getSelectedFiles();
                 jLabel1.setText("UPLOADING THE FILES INTO");
                 String text="";
                 for(int i=0;i<files.length;i++)
                 {
                    text=text+"\n"+files[i].getName();
                 }
                 if(backup.contentEquals("INCREMENTAL"))
                 {
                     AmazonIncremental ai=new AmazonIncremental(BUCKET_NAME_AWS,files,s3service,s3obj);
                 }
                 else
                 {
                     ArrayList names =new ArrayList();
                     Iterator itr = s3obj.iterator();
                     while (itr.hasNext()) {
                           S3Object element = (S3Object) itr.next();
                           names.add(element.getName());
                     }

                     for(int i=0;i<files.length;i++)
                     {
                     if(names.contains(files[i].getName()))
                     {
                         createFile_AWS(s3obj,files[i]);
                     }
                     else
                     {
                         S3Object localObject = new S3Object(files[i]);
	                 localObject = s3service.putObject(BUCKET_NAME_AWS, localObject);
                     }

                     }

                 }
                 jTextArea1.setText(text);
                 jTextArea1.setVisible(true);
                 jLabel1.setText("UPLOADED THE FILES INTO");
            }

     }

  }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jFileChooser1 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jLabel1.setText("Choose the file you like to backup into ");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setForeground(new java.awt.Color(0, 153, 204));
        jLabel2.setText("jLabel2");
        jLabel2.setName("jLabel2"); // NOI18N

        jFileChooser1.setMultiSelectionEnabled(true);
        jFileChooser1.setName("jFileChooser1"); // NOI18N

        jButton1.setText("Close");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setBackground(new java.awt.Color(204, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Papyrus", 1, 13)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 51, 51));
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addContainerGap(548, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        //new CloudUI().setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    private void createVersion_G(File name,int version) throws NoSuchAlgorithmException, IOException, ServiceException {
    if(name.getName().contains("."))
    {
        String[] tag=name.getName().split("\\.");
        String versionName=tag[0]+"_v"+version+"."+tag[1];
        GSObject localObject = new GSObject(name);
        localObject.setName(versionName);
        localObject = service_G.putObject(BUCKET_NAME_G, localObject);
    }
    else
    {
        String versionName=name+"_v"+version;
        GSObject localObject = new GSObject(name.getAbsolutePath());
        localObject.setName(versionName);
	localObject = service_G.putObject(BUCKET_NAME_G, localObject);
    }


    }
     private void createVersion_AWS(File name,int version) throws NoSuchAlgorithmException, IOException, ServiceException {
    if(name.getName().contains("."))
    {
        String[] tag=name.getName().split("\\.");
        String versionName=tag[0]+"_v"+version+"."+tag[1];
        S3Object localObject = new S3Object(name);
        localObject.setName(versionName);
        localObject = s3service.putObject(BUCKET_NAME_AWS, localObject);
    }
    else
    {
        String versionName=name+"_v"+version;
        S3Object localObject = new S3Object(name.getAbsolutePath());
        localObject.setName(versionName);
	localObject = s3service.putObject(BUCKET_NAME_AWS, localObject);
    }


    }

    private int getLatestVersion_G(ArrayList matched) {
        int max=0;
        Iterator itr = matched.iterator();
        while (itr.hasNext()) {
           GSObject element = (GSObject) itr.next();
           if(element.getName().contains("."))
           {
              String versionTag=element.getName().split("\\.")[0].split("_")[1];
              int val= Integer.parseInt(versionTag.split("v")[1].toString());
              if(val>max)
                max=val;
           }
           else
           {
             String versionTag=element.getName().split("_")[1];
             int val= Integer.parseInt(versionTag.split("v")[1].toString());
             if(val>max)
               max=val;
           }
       }
        
       return max;
    }

    private int getLatestVersion_AWS(ArrayList matched) {
        int max=0;
        Iterator itr = matched.iterator();
        while (itr.hasNext()) {
           S3Object element = (S3Object) itr.next();
           if(element.getName().contains("."))
           {
              String versionTag=element.getName().split("\\.")[0].split("_")[1];
              int val= Integer.parseInt(versionTag.split("v")[1].toString());
              if(val>max)
                max=val;
           }
           else
           {
             String versionTag=element.getName().split("_")[1];
             int val= Integer.parseInt(versionTag.split("v")[1].toString());
             if(val>max)
               max=val;
           }
       }

       return max;
    }

    private String getFileName(String name) {
       if(name.contains("."))
       {
        return name.split("\\.")[0];
       }
       else
       {
           return name;
        }
    }


private void createFile_G(ArrayList al,File filename) throws NoSuchAlgorithmException, IOException, ServiceException
    {
               GSObject g;
                Iterator itr1 = al.iterator();
                 while (itr1.hasNext()) {
                     GSObject value =  (GSObject) itr1.next();
                     if(value.getName().contains(getFileName(filename.getName())+"_v") && value.getName().contains(getExtension(filename.getName())))
                     {
                   
                         if(filename.getName().contains("."))
                         {

                                 String[]  fileparts=filename.getName().split("\\.");
                                 ArrayList matched =new ArrayList();
                                 Iterator itr = al.iterator();
                                 while (itr.hasNext()) {
                                       GSObject element = (GSObject) itr.next();
                                       if(element.getName().contains(fileparts[0]+"_")&&element.getName().contains(fileparts[1]))
                                       {
                             
                                               matched.add(element);
                              
                                        }
                                 }

                                 int version=getLatestVersion_G(matched);
                    
                                 createVersion_G(filename,version+1);
                                 break;
                          }
                          else
                          {

                                ArrayList matched =new ArrayList();
                                Iterator itr = al.iterator();
                                while (itr.hasNext()) {
                                      GSObject element = (GSObject) itr.next();
                                      if(element.getName().contains(filename.getName()+"_v"))
                                      {
                                          matched.add(element);
                                      }
                                }

                                int version=getLatestVersion_G(matched);
                                createVersion_G(filename,version+1);
                                break;
                          }

                     }
                     else
                    {
                 
                      createVersion_G(filename,1);
                    }
                 }

    }

    private void createFile_AWS(ArrayList al,File filename) throws NoSuchAlgorithmException, IOException, ServiceException
    {
               S3Object g;
                Iterator itr1 = al.iterator();
                 while (itr1.hasNext()) {
                     S3Object value =  (S3Object) itr1.next();
                     if(value.getName().contains(getFileName(filename.getName())+"_v") && value.getName().contains(getExtension(filename.getName())))
                     {

                         if(filename.getName().contains("."))
                         {

                                 String[]  fileparts=filename.getName().split("\\.");
                                 ArrayList matched =new ArrayList();
                                 Iterator itr = al.iterator();
                                 while (itr.hasNext()) {
                                       S3Object element = (S3Object) itr.next();
                                       if(element.getName().contains(fileparts[0]+"_")&&element.getName().contains(fileparts[1]))
                                       {

                                               matched.add(element);

                                        }
                                 }

                                 int version=getLatestVersion_AWS(matched);

                                 createVersion_AWS(filename,version+1);
                                 break;
                          }
                          else
                          {

                                ArrayList matched =new ArrayList();
                                Iterator itr = al.iterator();
                                while (itr.hasNext()) {
                                      S3Object element = (S3Object) itr.next();
                                      if(element.getName().contains(filename.getName()+"_v"))
                                      {
                                          matched.add(element);
                                      }
                                }

                                int version=getLatestVersion_AWS(matched);
                                createVersion_AWS(filename,version+1);
                                break;
                          }

                     }
                     else
                    {

                      createVersion_AWS(filename,1);
                    }
                 }

    }

    private CharSequence getExtension(String name) {

        if(name.contains("."))
       {
        return name.split("\\.")[1];
       }
       else
       {
           return "";
        }




    }
}


