/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.Executable.Archiver;

import BLK.System.Logger;
import BLK.System.Utils.Hash.MD5;
import BLK.io.FileSystem.File;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author andresrg
 */
class Archiver extends BLK.System.Threads.Thread
{
    private static String getInfo(File f)
    {
        String info=new String();

        info+="\n";

        info+="Name: "+f.getName()+"\n";
        info+="Path: "+f.getParent().getPath()+"\n";
        info+="Full: "+f.getPath()+"\n";
        info+="MD5: "+f.getMd5()+"\n";
        info+="SHA1: "+f.getSha1()+"\n";
        info+="CRC32: "+f.getCrc32()+"\n";
        info+="UID: "+f.getUid()+"\n";
        info+="Size: "+f.getSize().toString()+"\n";
        info+="Type: "+f.getType()+"\n";

        info+="\n";

        return info;
    }
    private static Folder getFolder(File raw)
    {
        Long b = raw.getSize();

        Long k =new Long("0");
        while(b>=1024)
        {
            k=k+1;
            b=b-1024;
        }

        Long m =new Long("0");
        while(k>=1024)
        {
            m=m+1;
            k=k-1024;
        }

        Long g =new Long("0");
        while(m>=1024)
        {
            g=g+1;
            m=m-1024;
        }

        Folder f = Main.getBase();
        f=new Folder(f,b.toString());
        f=new Folder(f,k.toString());
        f=new Folder(f,m.toString());
        f=new Folder(f,g.toString());
        f=new Folder(f, MD5.calc(raw.getUid()));

        return f;
    }

    public static void archive(File file)
    {
        if(file.getSize()==0)
        {
            file.delete();
            return;
        }
        if(file.getExtension().equalsIgnoreCase("blkd"))
            return;

        String UID=file.getUid();
        Folder dest=getFolder(file);

        Info data_ori=new Info(file);

        File bin=new File(dest, UID+".blkb");
        Info data = new Info(bin);

        if(bin.exists() && !bin.equals(file))
        {
            Logger.getLogger().error("UID ERROR: "+bin.getPath());
            archive(bin);
        }
        else
        {
            if(data.setData(getInfo(file)+data_ori.getData()))
            {
                if(bin.exists())
                    if(file.delete())
                        Main.updateIndex(file);
                else
                    if(file.move(bin))
                    {
                        Main.updateIndex(bin);
                        Main.updateIndex(file);
                    }
                
                if(bin.exists())
                    if(data_ori.delete())
                        Main.updateIndex(data_ori.getFile());
                else
                    if(data.delete())
                        Main.updateIndex(data.getFile());
            }
        }

        System.gc();


        

    }


    
    private final File file;

    public Archiver(File file) 
    {
        super("Archiver: "+file.getPath());
        this.file = file;
    }

    @Override
    protected void doThread()
    {
        Archiver.archive(this.file);
    }


}
