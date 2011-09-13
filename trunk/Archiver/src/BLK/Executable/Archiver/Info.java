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
import BLK.io.FileSystem.FileSystem;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Info
{
    private File info;

    public Info(File info)
    {
        this.info = new File(info.getParent(), info.getNameWithOutExtension()+".blkd");
    }


    public String getData()
    {
        if(this.info.exists())
            return Info.processInfo(this.info.getString("",false));
        else
            return new String();
                    
    }

    public Boolean setData(String newData)
    {
        String data=new String();

        if(this.info.exists())
            data=this.info.getString("",false);


        data="\n"+newData+"\n"+data+"\n";

        if(!this.info.setString(processInfo(data)))
            return false;

        Main.updateIndex(this.info);
        
        return true;
    }

    private static String processInfo(String info)
    {
        String tmp = info;
        tmp=tmp.replaceAll("(\r\n|\r|\n|\n\r)", "\n");

        ArrayList<String> heads = new ArrayList<String>();
        for(String row : tmp.split("\n"))
        {
            Boolean ok=false;
            String[] row_p = row.split(":",2);
            if(row_p.length>1)
            {
                ok=true;
                String head=row_p[0].trim();
                for(String head_ex : heads)
                    if(head_ex.equalsIgnoreCase(head))
                    {
                        ok=false;
                        break;
                    }


                if(ok)
                    heads.add(head);
            }


        }


        for(String head : heads)
        {
            String new_head=head;

            if(head.equalsIgnoreCase("fname"))
                new_head="Name";
            else if (head.equalsIgnoreCase("fpath"))
                new_head="Path";

            tmp=tmp.replaceAll(head, "\n"+new_head);
        }

        ArrayList<String> lines= new ArrayList<String>();
        for(String row : tmp.split("\n"))
        {
            String r=row.trim();
            if(!r.isEmpty())
            {
                String[] row_p = row.split(":",2);
                if(row_p.length>1 && !row_p[1].trim().equalsIgnoreCase("null")  && !row_p[1].trim().endsWith(".blkd") && !row_p[1].trim().endsWith(".blkb"))
                {
                    String row_final=row_p[0].trim().toUpperCase()+": "+row_p[1].trim().toLowerCase();

                    Boolean ok=true;
                    for(String line2 : lines)
                    {
                        if(line2.equalsIgnoreCase(row_final))
                        {
                            ok=false;
                            break;
                        }
                    }

                    if(ok)
                        lines.add(row_final);
                }
            }
        }

        tmp=new String();

        for(String line : lines)
            tmp+=line+"\n";


        return tmp;
    }

    public Boolean delete()
    {
        return this.info.delete();
    }

    public FileSystem getFile()
    {
        return this.info;
    }

}
