package edu.byuh.cis.hundredsurnamesvisualization;

public class Member {
    String simplified;
    String traditional;
    String pinyin;
    String pinyinCantonese;
    Float size;
    Float x;
    Float y;
    String role;
    String link;


    public Member(String simplifiedP, String pinyinP, String traditionalP, String pinyinCantoneseP, Float sizeP, Float xP, Float yP) {
        simplified = simplifiedP;
        traditional = traditionalP;
        pinyin = pinyinP;
        pinyinCantonese = pinyinCantoneseP;
        size = sizeP;
        x = xP;
        y = yP;
        role = "";
        link = "";
    }

    public void setLink(String l) {
        link = l;
    }


}
