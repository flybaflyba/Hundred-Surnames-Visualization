package edu.byuh.cis.hundredsurnamesvisualization;

public class Member {
    String simplified;
    String pinyin;
    Float size;
    Float x;
    Float y;
    String role;
    String link;


    public Member(String simplifiedP, String pinyinP, Float sizeP, Float xP, Float yP) {
        simplified = simplifiedP;
        pinyin = pinyinP;
        size = sizeP;
        x = xP;
        y = yP;
        role = "";
        link = "";
    }

    public void setRole(String r) {
        role = r;
    }

    public void setLink(String l) {
        link = l;
    }

    public void changeText(String s) {
        simplified = s;}


}
