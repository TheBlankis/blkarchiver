/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.Executable.Archiver;

import BLK.io.FileSystem.File;
import BLK.io.FileSystem.FileSystem;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author andresrg
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static Folder data=null;
    public static void main(String[] args) 
    {
        System.out.println("=========================================================================");
        System.out.println("============================== BLKArchiver ==============================");
        System.out.println("=========================================================================");

        if(args.length<2)
            help();

        

        for(String s : args)
            if(data==null)
                data=new Folder(s);
            else
            {
                FileSystem fs = FileSystem.getObject(s);
                if(fs instanceof Folder)
                    new Scanner((Folder)fs).start();
                else if(fs instanceof File)
                    new Archiver((File)fs).start();
            }
    }

    private static void help()
    {
        System.out.println("Please enter DataFolder, ScanFolder/FileToArchive, [ScanFolder/FileToArchive], [ScanFolder/FileToArchive], ...");
        
        System.exit(1);
    }

    static void updateIndex(FileSystem base) {
        System.out.println();
        System.out.println("=========================================================================");
        System.out.println(base.getPath());
        System.out.println("=========================================================================");
        System.out.println();
    }

    static Folder getBase() {
        return Main.data;
    }

}
