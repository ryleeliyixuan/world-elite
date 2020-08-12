package com.worldelite.job.constants;

/**
 * 文件类型<br><br>
 * <p><b>并不是所有文件格式的code都是正确的. 使用前请注意测试</b><br>code来源于网络</p>
 **/
public enum FileType {

    /**
     * JPEG(jpg)
     */
    JPG("ffd8ffe000104a464946", "jpg"),
    /**
     * PNG(png)
     */
    PNG("89504e470d0a1a0a0000", "png"),
    /**
     * GIF(gif)
     */
    GIF("47494638396126026f01", "gif"),
    /**
     * TIFF(tif)
     */
    TIF("49492a00227105008037", "tif"),
    /**
     * 16色位图(bmp)
     */
    BMP_16("424d228c010000000000", "bmp"),
    /**
     * 24位位图(bmp)
     */
    BMP_24("424d8240090000000000", "bmp"),
    /**
     * 256色位图(bmp)
     */
    BMP_256("424d8e1b030000000000", "bmp"),
    /**
     * CAD(dwg)
     */
    DWG("41433130313500000000", "dwg"),
    /**
     * HTML(html)
     */
    HTML("3c21444f435459504520", "html"),
    /**
     * HTM(htm)
     */
    HTM("3c21646f637479706520", "htm"),
    /**
     * 猎聘、智联简历。htm
     */
    HTM_2("3c68746d6c20786d6c6e", "htm"),
    /**
     * css
     */
    CSS("48544d4c207b0d0a0942", "css"),
    /**
     * js
     */
    JS("696b2e71623d696b2e71", "js"),
    /**
     * RichTextFormat(rtf)
     */
    RTF("7b5c727466315c616e73", "rtf"),
    /**
     * Photoshop(psd)
     */
    PSD("38425053000100000000", "psd"),
    /**
     * Email[OutlookExpress6](eml)
     */
    EML("46726f6d3a203d3f6762", "eml"),
    /**
     * MSExcel注意：word、msi和excel的文件头一样
     */
    DOC("d0cf11e0a1b11ae10000", "doc"),
    /**
     * docx文件
     */
    DOCX("504b0304140006000800", "docx"),
    /**
     * MSAccess(mdb)
     */
    MDB("5374616E64617264204A", "mdb"),
    PS("252150532D41646F6265", "ps"),
    /**
     * AdobeAcrobat(pdf)
     */
    PDF("255044462d312e", "pdf"),
    /**
     * rmvb/rm相同
     */
    RMVB("2e524d46000000120001", "rmvb"),
    /**
     * flv与f4v相同
     */
    FLV("464c5601050000000900", "flv"),
    MP4("00000020667479706d70", "mp4"),
    MP3("49443303000000002176", "mp3"),
    /**
     *
     */
    MPG("000001ba210001000180", "mpg"),
    /**
     * wmv与asf相同
     */
    WMV("3026b2758e66cf11a6d9", "wmv"),
    /**
     * Wave(wav)
     */
    WAV("52494646e27807005741", "wav"),
    AVI("52494646d07d60074156", "avi"),
    /**
     * MIDI(mid)
     */
    MID("4d546864000000060001", "mid"),
    ZIP("504b0304140000000800", "zip"),
    RAR("526172211a0700cf9073", "rar"),
    INI("235468697320636f6e66", "ini"),
    JAR("504b03040a0000000000", "jar"),
    /**
     * 可执行文件
     */
    EXE("4d5a9000030000000400", "exe"),
    /**
     * jsp文件
     */
    JSP("3c25402070616765206c", "jsp"),
    /**
     * MF文件
     */
    MF("4d616e69666573742d56", "mf"),
    /**
     * xml文件
     */
    XML("3c3f786d6c2076657273", "xml"),
    /**
     * sql文件
     */
    SQL("494e5345525420494e54", "sql"),
    /**
     * java文件
     */
    JAVA("7061636b616765207765", "java"),
    /**
     * bat文件
     */
    BAT("406563686f206f66660d", "bat"),
    /**
     * gz文件
     */
    GZ("1f8b0800000000000000", "gz"),
    /**
     * bat文件
     */
    PROPERTIES("6c6f67346a2e726f6f74", "properties"),
    /**
     * bat文件
     */
    CLASS("cafebabe0000002e0041", "class"),
    /**
     * bat文件
     */
    CHM("49545346030000006000", "chm"),
    /**
     * bat文件
     */
    MXP("04000000010000001300", "mxp"),
    TORRENT("6431303a637265617465", "torrent"),
    /**
     * 51job简历。mht
     */
    MHT("46726f6d3a3cd3c920cd", "mht"),
    /**
     * Quicktime(mov)
     */
    MOV("6D6F6F76", "mov"),
    /**
     * WordPerfect(wpd)
     */
    WPD("FF575043", "wpd"),
    /**
     * OutlookExpress(dbx)
     */
    DBX("CFAD12FEC5FD746F", "dbx"),
    /**
     * Outlook(pst)
     */
    PST("2142444E", "pst"),
    /**
     * Quicken(qdf)
     */
    QDF("AC9EBD8F", "qdf"),
    /**
     * WindowsPassword(pwl)
     */
    PWL("E3828596", "pwl"),
    /**
     * RealAudio(ram)
     */
    RAM("2E7261FD", "ram");

    public String code;
    public String name;

    FileType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
