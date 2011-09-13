/*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 */

package BLK.Executable.Archiver;

import BLK.io.FileSystem.File;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
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
