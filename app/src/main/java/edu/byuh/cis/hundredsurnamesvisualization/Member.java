package edu.byuh.cis.hundredsurnamesvisualization;

import android.graphics.Bitmap;

public class Member {
    String text;
    Float size;
    Float x;
    Float y;
    String role;
    String link;


    public Member(String textP, Float sizeP, Float xP, Float yP) {
        text = textP;
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

    public void changeText(String s) {text = s;}


}
