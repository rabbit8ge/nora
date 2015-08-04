package com.he.app.nora.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.Context;
import android.os.Environment;

public class FileUtility {

	private String SDPath;

	
	public FileUtility() {
		
		SDPath =Environment.getExternalStorageDirectory()+"/";
	}


	public String getSDPath() {
		return SDPath;
	}

	//创建文件
	public File creatSDFile(String filename) throws IOException{
		File file=new File(SDPath+filename);
		file.createNewFile();
		return file;
	}
	//创建目录
	public File creatSDDir(String dirname) throws IOException{
		File dir=new File(SDPath+dirname);
		dir.mkdir();
		return dir;
	}
	
	//判断文件是否存在
	public boolean isFileExist(String filename){
		File file=new File(SDPath+filename);
		return file.exists();
	}
	
	public File Write2SDFromInput(String path,String filename,InputStream input){
		File file=null;
		OutputStream output=null;
		try{
			creatSDDir(path);
			file=creatSDFile(path+filename);
			output=new FileOutputStream(file);
			byte buffer[]=new byte[4*1024];
			while(input.read(buffer)!=-1){
				output.write(buffer);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

    public OutputStream Write2InnerFromInput(Context context, String subpath, String filename, InputStream input) {
        // Write to disk.
        //OutputStream writer = null;
        OutputStream out = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            //writer = new FileOutputStream(out);
            byte buffer[] = new byte[4 * 1024];
            while (input.read(buffer) != -1) {
                out.write(buffer);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {

            }
        }
        return out;
    }
}








