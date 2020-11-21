package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataHolder {

    public ArrayList<Integer> allLargeImageIds;
    public ArrayList<Integer> allObjectInfoFileIds;
    public ArrayList<Member> memberObjects;
    public ArrayList<String> allSummaries = new ArrayList<>();
    public ArrayList<String> allKeys = new ArrayList<>();

    public DataHolder(Context context, float w) {

        allLargeImageIds = getAllLargeImagesIds();
        allObjectInfoFileIds = getAllInfoFilesIds();
        memberObjects = getMemberObjectsList(context, w);
        readInfoFile(context);


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
        ArrayList<Member> allObjectsList = new ArrayList<>();

        Bitmap empty;
        empty = loadAndScale(context.getResources(),R.drawable.no_image_replacement,w);
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

    public void readInfoFile(Context context) {
        try {
            InputStream allMemberInfoFile = context.getResources().openRawResource(R.raw.all_objects_summaries);
            if (allMemberInfoFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allMemberInfoFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    allSummaries.add(line+"\n");
                    allKeys.add(line+"\n");
                }
                allMemberInfoFile.close();
            }
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("TestFile", e.getMessage());
        }
    }

}
