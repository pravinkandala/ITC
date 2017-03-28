
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.multi.DownloadPackage;
import org.jets3t.service.multi.SimpleThreadedStorageService;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pravin
 */
class AmazonIncremental {

    AmazonIncremental(String bucket, File[] files, RestS3Service s3service, ArrayList s3obj) throws IOException, NoSuchAlgorithmException, S3ServiceException, ServiceException, FileNotFoundException, PatchFailedException, ClassNotFoundException {
       ArrayList matched =new ArrayList();
      Iterator itr = s3obj.iterator();
      while (itr.hasNext()) {
            S3Object element = (S3Object) itr.next();
            matched.add(element.getName());
         }
        for(int i=0;i<files.length;i++)
        {
            long lastmodtime=files[i].lastModified();
            if(matched.contains(files[i].getName()))
            {
               int index=matched.indexOf(getLatestVersion(files[i].getName(),matched));

               S3Object gso=(S3Object) s3obj.get(index);
               Date d =gso.getLastModifiedDate();
               long metatime=d.getTime();
               if(metatime<lastmodtime)
               {
                 String filename=getRestoreFile(bucket,files[i].getName(),matched,s3obj,s3service);

               DiffFiles diff=new DiffFiles(filename,files[i].getAbsolutePath(),"AMAZON STORAGE",matched);
               File f=new File(diff.name);
               S3Object localObject = new  S3Object(f);
	       localObject = s3service.putObject(bucket, localObject);



               }
               //else
                //   System.out.println("NO need to modify");


            }

        }
    }

     private String getRestoreFile(String bucket,String filename,ArrayList matched,ArrayList s3obj,RestS3Service s3service) throws ServiceException, FileNotFoundException, PatchFailedException, IOException, ClassNotFoundException
    {

                   ArrayList<S3Object> versions =new ArrayList<S3Object>();
                   Iterator itr = s3obj.iterator();
                   while (itr.hasNext()) {
                            S3Object element = (S3Object) itr.next();
                            if(element.getName().contains("diff_v") && element.getName().contains(filename))
                            versions.add(element);
                           }

                  int index=matched.indexOf(filename);
                  versions.add( (S3Object) s3obj.get(index));
                  DownloadPackage[] downloadPackages = new DownloadPackage[versions.size()];
                  SimpleThreadedStorageService thread_storage = new SimpleThreadedStorageService(s3service);
                  for(int i=0;i<versions.size();++i)
                  {
                      if(!versions.get(i).getName().contains("diff_"))
                      {
                          downloadPackages[i]=new DownloadPackage(versions.get(i),new File("copy_"+versions.get(i).getName()));
                      }
                      else
                          downloadPackages[i]=new DownloadPackage(versions.get(i),new File(versions.get(i).getName()));
                      }

                  thread_storage.downloadObjects(bucket, downloadPackages);
                  if(versions.size()==1)
                      return "copy_"+filename;
                  else
                  {
                      String originalfile="copy_"+filename;
                      int l=1;
                      while(l<=versions.size()-1)
                      {
                      Patch patch;
                      FileInputStream fis=new FileInputStream("diff_v"+l+"_"+filename+".txt");
                      ObjectInputStream ois=new ObjectInputStream(fis);
                      patch=(Patch)ois.readObject();
                      File f=new File("diff_v"+l+"_"+filename+".txt");
                      f.delete();
                      ois.close();
                       new MergeFiles(originalfile,patch);
                       l++;
                       fis.close();
                      }


                  }
        return "copy_"+filename;

    }


    private String getLatestVersion(String filename, ArrayList list) {
        int max=0;
        ArrayList matched=new ArrayList();
        Iterator itr1 = list.iterator();
                    while (itr1.hasNext()) {
                          String value =  (String) itr1.next();

               if(value.contains(filename) && value.contains("diff_v")&&value.contains(".txt"))
               {

                  matched.add(value);

               }

               }
        if(matched.size()==0)
            return filename;
        else
        {
         Iterator itr = matched.iterator();
         int value;
         while (itr.hasNext()) {
                          String diffitr =  (String) itr.next();
                          value=Integer.parseInt(diffitr.split("_v")[1].split("_")[0]);
                          if(value>max)
                              max=value;
            }
        }

       return "diff_v"+max+"_"+filename+".txt";
    }
  }

