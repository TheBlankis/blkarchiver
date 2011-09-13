/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.Executable.Archiver;

import BLK.io.FileSystem.File;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author andresrg
 */
public class Scanner extends BLK.System.Threads.Thread
{
    private Folder folder;

    public Scanner(Folder folder)
    {
        super("Scanner: "+folder.getPath());
        this.folder = folder;
    }
    
    private void scan(Folder base)
    {
        for(Folder f : base.getFolders())
            this.scan(f);

        for(File f : base.getFiles())
            this.doElement(f);

        this.doElement(base);
    }

    private void doElement(File file)
    {
        //new Archiver(file).start();
        Archiver.archive(file);
    }

    private void doElement(Folder folder)
    {
        if(folder.delete())
            Main.updateIndex(folder);
    }

    @Override
    protected void doThread()
    {
        this.scan(this.folder);
    }
}
