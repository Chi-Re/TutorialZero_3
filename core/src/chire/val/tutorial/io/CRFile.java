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
 * �ǳ����ص��ļ�������(����)���ӹ����������ǿ��Եģ������Ż���...
 * @author ����S
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
                //Ϊ�˷�ֹĳ�������д��������ڲ�·������Ƶ�
                if (path.charAt(0) == '/' || path.startsWith("./")){
                    throw new RuntimeException("�ļ��ڲ�·������");
                }
                this.path = path;
                this.fileURL = this.getLocalStream(path);
                this.file = new File(this.fileURL.getFile());
            } else {
                throw new RuntimeException("��������δ֪");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("����CRFileʱ����", e);
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
        //TODO ��ΪʲôҪΪ��һ����С��С�ĸ��ʶ�����...
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


    /**��ӡʱ����·��*/
    @Override
    public String toString() {
        return onUFT8(this.file.getPath().replace('\\', '/'));
    }

    public CRFile copy(){
        return new CRFile(this.file, this.type);
    }

    /**ע��:�ڲ��ļ���fileURL�޷�ʹ��child*/
    public CRFile child(String path){
        //if (this.type == FileType.internal) throw new RuntimeException("�ڲ���֧��");
        if (this.type == FileType.local) {
            CRFile cFile = new CRFile(this.file.getPath() + "/" + path);
            //����ļ��в����ھʹ���
            if (this.file.isDirectory()) cFile.mkdir();
            return cFile;
        } else if (this.type == FileType.internal) {
            return new CRFile(this.path+"/"+path, FileType.internal);
        } else {
            throw new RuntimeException("�ļ�����δ֪");
        }
    }

    public void mkdir(){
        if  (!file.exists() && file.isDirectory()) {
            if (file.mkdirs()){
                //log.info(this.file + " �ļ�����");
            }
        }
    }

    /**
     * @param create ��Ϊtrue����ļ������ھʹ����ļ�(Ҳ�᷵��true)
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
//            if (!this.file.isDirectory()) throw new CrengineRuntimeException("���ļ����޷���ȡ���ļ�");
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
            if (!this.file.isDirectory()) throw new RuntimeException("���ļ����޷���ȡ���ļ�");
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
                //��ȡ�ļ��л���·��
                URL url = CRFile.class.getClassLoader().getResource(this.path);
                String jarPath = url.toString().substring(0, url.toString().indexOf("!/") + 2);

                //�޸�
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();

                //����
                List<CRFile> listF = new ArrayList<>();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(this.path) && !entry.isDirectory()) {
                        //TODO ע��:�˴���С����������(˭�е�û�´���һ����:as.png.jpg)
                        if (name.contains(ext) && havaPathStr(name, ext)) {
                            listF.add(new CRFile(CRFile.class.getClassLoader().getResource(name), FileType.internal));
                        }
                    }
                }
                return listF;
            } catch (IOException e){
                throw new RuntimeException("�����ڲ��ļ����б�ʧ��", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    public List<CRFile> getFiles(){
        return this.getExtFiles(".");
    }

    /**���ؽ����ļ����ͺ�׺(XX.png)*/
    public String getFileName(){
        String[] ls = this.path.split("/");
        return URLDecoder.decode(ls[ls.length-1], StandardCharsets.UTF_8);
    }

    /**���ؽ������ļ����ͺ�׺(XX)*/
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
        // �ж��Ƿ����ļ���,�����ļ����׳��쳣
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
            throw new RuntimeException("�ļ��к��ڲ��ļ��޷�д�룡");
        }
    }

    public String read(String encoding){
        try {
            if(this.file.isFile() && this.file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(this.file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lt;
                StringBuilder rline = new StringBuilder();
                while((lt = bufferedReader.readLine()) != null){
                    rline.append(lt);
                }
                read.close();
                return rline.toString();
            }else{
                throw new RuntimeException("�Ҳ���ָ�����ļ�");
            }
        } catch (Exception e) {
            throw new RuntimeException("��ȡ�ļ����ݳ���", e);
        }
    }

    public String readStr(){
        return this.read("utf-8");
    }

    public void deleteFile() {
        if (this.file.exists()) {
            if (this.file.isDirectory()){
                throw new RuntimeException("�޷�ɾ���ļ���");
            } else if (this.type == FileType.internal){
                throw new RuntimeException("�ڲ��ļ��޷�ɾ��");
            }

            boolean deleted = this.file.delete();
            if (!deleted) {
                throw new RuntimeException("�޷�ɾ���ļ�: " + this.file.getPath());
            }
        }
    }
}
