package chire.val.tutorial.io;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static chire.val.tutorial.util.CRString.havaPathStr;
import static chire.val.tutorial.util.CRString.onUFT8;

/**
 * 非常奇特的文件操作类(迫真)，从功能上来讲是可以的，不过优化嘛...
 * @author 炽热S
 */
public class CRFile {
    private final File file;
    private final FileType type;
    private final URL fileURL;
    private final String path;

    public CRFile(String path, FileType type){
        try {
            this.type = type;
            if (type == FileType.local){
                this.path = path;
                this.file = new File(path);
                this.fileURL = this.file.toURI().toURL();
            } else if (type == FileType.internal){
                //为了防止某个大聪明写出错误的内部路径而设计的
                if (path.charAt(0) == '/' || path.startsWith("./")){
                    throw new RuntimeException("文件内部路径错误");
                }
                this.path = path;
                this.fileURL = this.getLocalStream(path);
                this.file = new File(this.fileURL.getFile());
            } else {
                throw new RuntimeException("错误，类型未知");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("创建CRFile时出错", e);
        }
    }

    public CRFile(File file, FileType type){
        try {
            this.file = file;
            this.type = type;
            this.path = file.getPath();
            this.fileURL = file.toURI().toURL();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public CRFile(URL fileURL, FileType type){
        try {
            this.fileURL = fileURL;
            this.file = new File(fileURL.getFile());
            this.type = type;
            this.path = fileURL.getPath();
        } catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    public CRFile(URL fileURL){
        this(fileURL, FileType.local);
    }

    public CRFile(String path){
        this(path, FileType.local);
    }

    public CRFile(File file){
        this(file, FileType.local);
    }

    public File getFile(){
        return this.file;
    }
    public URL getFileURL(){
        return this.fileURL;
    }
    public FileType getType(){
        return this.type;
    }

    public URL getLocalStream(String path){
        //TODO 我为什么要为了一个很小很小的概率而这样...
        if (path.contains("!/") && path.contains("file:/")){
            if (path.split("!/")[0].contains("file:/")){
                path = path.replaceFirst(path.split("!/")[0], "").replaceFirst("!/", "");
            }
        }
        path = onUFT8(path);
        return Objects.requireNonNull(CRFile.class.getClassLoader().getResource(path));
    }

    public URL getLocalStream(){
        return this.getLocalStream(this.path);
    }


    /**打印时返回路径*/
    @Override
    public String toString() {
        return onUFT8(this.file.getPath().replace('\\', '/'));
    }

    public CRFile copy(){
        return new CRFile(this.file, this.type);
    }

    /**注意:内部文件的fileURL无法使用child*/
    public CRFile child(String path){
        //if (this.type == FileType.internal) throw new RuntimeException("内部不支持");
        if (this.type == FileType.local) {
            CRFile cFile = new CRFile(this.file.getPath() + "/" + path);
            //如果文件夹不存在就创建
            if (this.file.isDirectory()) cFile.mkdir();
            return cFile;
        } else if (this.type == FileType.internal) {
            return new CRFile(this.path+"/"+path, FileType.internal);
        } else {
            throw new RuntimeException("文件类型未知");
        }
    }

    public void mkdir(){
        if  (!file.exists() && file.isDirectory()) {
            if (file.mkdirs()){
                //log.info(this.file + " 文件创建");
            }
        }
    }

    /**
     * @param create 当为true如果文件不存在就创建文件(也会返回true)
     */
    public boolean exists(boolean create){
        boolean m = file.exists();
        if (create){
            m = file.mkdir();
        }
        return m;
    }

    public boolean exists(){
        return this.exists(false);
    }

//    public String[] getFiles(String ext){
//        if (this.type == FileType.local){
//            if (!this.file.isDirectory()) throw new CrengineRuntimeException("非文件夹无法获取子文件");
//            return this.file.list(new FileExtFilter(ext));
//        } else if (this.type == FileType.internal){
//            return null;
//        } else {
//            return null;
//        }
//    }

//    public List<CRFile> getFilePaths(String ext){
//        String[] files = getFiles(ext);
//        List<CRFile> FPList = new ArrayList<>(files.length);
//        for (var f : files){
//            CRFile FPl = this.child(f);
//            if (!FPl.file.isDirectory()) FPList.add(FPl);
//        }
//        return FPList;
//    }

    public List<CRFile> getExtFiles(String ext){
        if (this.type == FileType.local){
            if (!this.file.isDirectory()) throw new RuntimeException("非文件夹无法获取子文件");
            String[] lists = this.file.list(new FileExtFilter(ext));
            if (lists == null) lists = new String[]{};
            List<CRFile> FPList = new ArrayList<>(lists.length);
            for (var f : lists){
                CRFile FPl = this.child(f);
                if (!FPl.file.isDirectory()) FPList.add(FPl);
            }
            return FPList;
        } else if (this.type == FileType.internal){
            try {
                //获取文件夹基础路径
                URL url = CRFile.class.getClassLoader().getResource(this.path);
                String jarPath = url.toString().substring(0, url.toString().indexOf("!/") + 2);

                //修改
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();

                //储存
                List<CRFile> listF = new ArrayList<>();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(this.path) && !entry.isDirectory()) {
                        //TODO 注意:此处很小可能有误判(谁闲的没事创建一个叫:as.png.jpg)
                        if (name.contains(ext) && havaPathStr(name, ext)) {
                            listF.add(new CRFile(CRFile.class.getClassLoader().getResource(name), FileType.internal));
                        }
                    }
                }
                return listF;
            } catch (IOException e){
                throw new RuntimeException("加载内部文件夹列表失败", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    public List<CRFile> getFiles(){
        return this.getExtFiles(".");
    }

    /**返回将带文件类型后缀(XX.png)*/
    public String getFileName(){
        String[] ls = this.path.split("/");
        return URLDecoder.decode(ls[ls.length-1], StandardCharsets.UTF_8);
    }

    /**返回将不带文件类型后缀(XX)*/
    public String getName(){
        return this.getFileName().split("\\.")[0];
    }

    public String getDataType(){
        String[] lst = this.path.split("\\.");
        return lst[lst.length - 1];
    }

    public void writeString(String content, boolean line){
        this.writeString(content, true, line);
    }

    public void writeString(String content, boolean append, boolean line) {
        // 判断是否是文件夹,不是文件则抛出异常
        if(!this.file.isDirectory() && this.type != FileType.internal) {
            try {
                if(!this.file.exists()) {
                    boolean nl = this.file.createNewFile();
                }

                FileOutputStream fileOut = new FileOutputStream(this.file, append);
                OutputStreamWriter writer = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
                writer.write(content);
                if (line) writer.write("\n");
                writer.close();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("文件夹和内部文件无法写入！");
        }
    }

    public String read(String encoding){
        try {
            if(this.file.isFile() && this.file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(this.file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lt;
                StringBuilder rline = new StringBuilder();
                while((lt = bufferedReader.readLine()) != null){
                    rline.append(lt);
                }
                read.close();
                return rline.toString();
            }else{
                throw new RuntimeException("找不到指定的文件");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取文件内容出错", e);
        }
    }

    public String readStr(){
        return this.read("utf-8");
    }

    public void deleteFile() {
        if (this.file.exists()) {
            if (this.file.isDirectory()){
                throw new RuntimeException("无法删除文件夹");
            } else if (this.type == FileType.internal){
                throw new RuntimeException("内部文件无法删除");
            }

            boolean deleted = this.file.delete();
            if (!deleted) {
                throw new RuntimeException("无法删除文件: " + this.file.getPath());
            }
        }
    }
}
