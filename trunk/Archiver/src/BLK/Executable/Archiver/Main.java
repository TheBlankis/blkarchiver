/*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 */

package BLK.Executable.Archiver;

import BLK.Storage.IZone;
import BLK.Storage.TreeFactory;
import BLK.io.FileSystem.File;
import BLK.io.FileSystem.FileSystem;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
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

    static void updateIndex(FileSystem base) 
    {
        TreeFactory.sendMessage(getIndexerZone(),base.getPath());


        System.out.println();
        System.out.println("=========================================================================");
        System.out.println(base.getPath());
        System.out.println("=========================================================================");
        System.out.println();
    }

    static Folder getBase() {
        return Main.data;
    }

    public static Folder getBaseArchive()
    {
        Folder f = Main.data;

        f=new Folder(f,"Standard");
        f=new Folder(f,"Storage");
        f=new Folder(f,"Archive");

        return f;
    }
    public static IZone getIndexerZone()
    {
        IZone z = TreeFactory.getTree(Main.data);

        z=z.getSubZone("Standard");
        z=z.getSubZone("Storage");
        z=z.getSubZone("Indexer");
        
        return z;

    }

}
