package edu.byuh.cis.hundredsurnamesvisualization;

import android.graphics.Bitmap;

public class Member {
    Bitmap image;
    Float size;
    Float x;
    Float y;
    String role;
    String link;

    public Member(Bitmap imageP, Float sizeP, Float xP, Float yP) {
        image = imageP;
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

    public void changeImage(Bitmap b) {
        image = b;
    }


}
