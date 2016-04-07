package com.julian.qplayer.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Julian on 2016/3/16.
 */
public class LrcParser {
    private LrcInfo lrcinfo = new LrcInfo();

    private long currentTime = 0;//存放临时时间
    private String currentContent = null;//存放临时歌词
//    private HashMap<Long, String> maps = new HashMap<>();//用户保存所有的歌词和时间点信息间的映射关系的Map
    private ArrayList<Lrc> lrcs = new ArrayList<>();



    /**
     * 根据文件路径，读取文件，返回一个输入流
     *
     * @param path
     *            路径
     * @return 输入流
     * @throws FileNotFoundException
     */
    private InputStream readLrcFile(String path) throws FileNotFoundException {
        File f = new File(path);
        InputStream ins = new FileInputStream(f);
        return ins;
    }

    public LrcInfo parser(String path) throws Exception {
        InputStream in = readLrcFile(path);
        lrcinfo = parser(in);
        return lrcinfo;

    }

    /**
     * 将输入流中的信息解析，返回一个LrcInfo对象
     *
     * @param inputStream
     *            输入流
     * @return 解析好的LrcInfo对象
     * @throws IOException
     */
    public LrcInfo parser(InputStream inputStream) throws IOException {
        // 三层包装
        InputStreamReader inr = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inr);
        // 一行一行的读，每读一行，解析一行
        String line = null;
        while ((line = reader.readLine()) != null) {
            parserLine(line);
        }
        // 全部解析完后，设置info
//        lrcinfo.setInfos(maps);
        Collections.sort(lrcs, new Comparator<Lrc>() {
            @Override
            public int compare(Lrc lhs, Lrc rhs) {
                return lhs.getTime() > rhs.getTime() ? 1 : -1;
            }
        });
//        for (Lrc lrc : lrcs){
//            Log.d("test",lrc.getTime() + "  " + lrc.getContent());
//        }
        lrcinfo.setLrcs(lrcs);
        return lrcinfo;
    }

    /**
     * 利用正则表达式解析每行具体语句
     * 并在解析完该语句后，将解析出来的信息设置在LrcInfo对象中
     *
     * @param str
     */
    private void parserLine(String str) {
        // 取得歌曲名信息
        if (str.startsWith("[ti:")) {
            String title = str.substring(4, str.length() - 1);
//            System.out.println("title--->" + title);
            lrcinfo.setTitle(title);

        }// 取得歌手信息
        else if (str.startsWith("[ar:")) {
            String singer = str.substring(4, str.length() - 1);
//            System.out.println("singer--->" + singer);
            lrcinfo.setSinger(singer);

        }// 取得专辑信息
        else if (str.startsWith("[al:")) {
            String album = str.substring(4, str.length() - 1);
//            System.out.println("album--->" + album);
            lrcinfo.setAlbum(album);

        }// 通过正则取得每句歌词信息
        else {
            // 设置正则规则
            String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
            // 编译
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);

            // 如果存在匹配项，则执行以下操作
            while (matcher.find()) {
                // 得到匹配的所有内容
                String msg = matcher.group();
                // 得到这个匹配项开始的索引
                int start = matcher.start();
                // 得到这个匹配项结束的索引
                int end = matcher.end();

                // 得到这个匹配项中的组数
                int groupCount = matcher.groupCount();
                // 得到每个组中内容
                for (int i = 0; i <= groupCount; i++) {
                    String timeStr = matcher.group(i);
                    if (i == 1) {
                        // 将第二组中的内容设置为当前的一个时间点
                        currentTime = strToLong(timeStr);
                    }
                }

                // 得到时间点后的内容
                String[] content = pattern.split(str);
                // 输出数组内容
                for (int i = 0; i < content.length; i++) {
                    if (i == content.length - 1) {
                        // 将内容设置为当前内容
                        currentContent = content[i];
                    }
                }
                // 设置时间点和内容的映射
                Lrc lrc = new Lrc(currentTime,currentContent);
                lrcs.add(lrc);
//                maps.put(currentTime, currentContent);
//                System.out.println("put---currentTime--->" + currentTime
//                        + "----currentContent---->" + currentContent);

            }
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     *
//     * @param group
     *            字符形式的时间点
     * @return Long形式的时间
     */
    private long strToLong(String timeStr) {
        // 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        // 1:使用：分割 2：使用.分割
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10;
    }


    public static void main(String[] args) {
        LrcParser lp = new LrcParser();
        try {
            lp.parser("G:\\WebRoot\\a1.lrc");
        } catch (Exception e) {
            System.out.println("parser erro");
            e.printStackTrace();
        }

    }
}
