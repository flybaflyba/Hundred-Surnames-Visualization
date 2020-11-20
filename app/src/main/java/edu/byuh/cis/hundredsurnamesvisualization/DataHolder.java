package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DataHolder {

    public ArrayList<Integer> allLargeImageIds;
    public ArrayList<Integer> allObjectInfoFileIds;
    public ArrayList<Member> memberObjects;

    public DataHolder(Context context, float w) {

        allLargeImageIds = getAllLargeImagesIds();
        allObjectInfoFileIds = getAllInfoFilesIds();
        memberObjects = getMemberObjectsList(context, w);


    }

    private ArrayList<Integer> getAllLargeImagesIds() {
        ArrayList<Integer> allLargeImagesIds = new ArrayList<>();
        for (int i = 0; i <= 99; i ++) {
            allLargeImagesIds.add(R.drawable.no_image_replacement);
            // repeat this step to add all large images' id to a list
        }
        return allLargeImagesIds;
    }

    private ArrayList<Integer> getAllInfoFilesIds() {

        ArrayList<Integer> allInfoFilesIds = new ArrayList<>();
        for (int i = 0; i <= 99; i ++) {
            allInfoFilesIds.add(R.raw.sample_info);
            // repeat this step to add all large images' id to a list
        }
        return allInfoFilesIds;
    }

    private ArrayList<Member> getMemberObjectsList(Context context, float w) {

        Bitmap empty;
        empty = loadAndScale(context.getResources(),R.drawable.no_image_replacement,w);

        ArrayList<Member> allObjectsList = new ArrayList<>();

        for (int i = 0; i <= 99; i ++) {
            allObjectsList.add(new Member(empty, 0f, 0f, 0f));
            // repeat this step to create objects with all resized bitmaps
        }

        return allObjectsList;
    }
    private Bitmap loadAndScale(Resources res, int id, float newWidth) {
        Bitmap original = BitmapFactory.decodeResource(res, id);
        float aspectRatio = (float)original.getHeight()/(float)original.getWidth();
        float newHeight = newWidth * aspectRatio;
        return Bitmap.createScaledBitmap(original, (int)newWidth, (int)newHeight, true);
    }

}
