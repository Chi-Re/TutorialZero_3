package chire.val.tutorial.util;

import java.io.IOError;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ַ��������Ĺ����ࡣ
 * @author ����S
 */
public class CRString {
    /**�ָ���*/
    public final static String regexKey = "<~>";

    public final static String patternKey = "\\{\\{"+ regexKey + "\\}\\}";

    /**�����ͨ��<~>��������ҪѰ�ҵĹؼ���<br>���� as<~>as<br>����Ѱ��������ַ������� asddas �᷵��dd*/
    public static List<String> findAll(String pattern, String string){
        try {
            String[] lm = split(pattern);
            List<String> sr = new ArrayList<>();
            for (var m2 : string.split(lm[0])){
                if (m2.contains(harmless(lm[1]))){
                    for (int i = 0; i < m2.split(lm[1]).length; i+=2) {
                        sr.add(m2.split(lm[1])[i]);
                    }
                }
            }

            return sr;
        } catch (IOError e){
            throw new RuntimeException("δ֪�����޷�����findAll", e);
        }
    }

    public static List<String> findAll(String string){
        return findAll(patternKey, string);
    }
    
    public static String[] split(String regex){
        return regex.split(regexKey);
    }

    public static String harmless(String text){
        return text.replace("\\{", "{")
                .replace("\\}", "}")
                .replace("\\[", "[")
                .replace("\\]", "]");
    }

    public static boolean havaStr(String text, String hava, String partition, int location){
        String[] lst = text.split(partition);
        return lst[location].contains(hava);
    }

    public static boolean havaPathStr(String text, String hava, int location){
        String[] lst = text.split("/");
        return lst[location].contains(hava);
    }

    public static boolean havaPathStr(String text, String hava){
        String[] lst = text.split("/");
        return lst[lst.length - 1].contains(hava);
    }

    public static String onUFT8(String text){
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }
}
