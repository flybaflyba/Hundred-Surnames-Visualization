package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;
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
    public ArrayList<String> surnameCharactersSimplified;
    public ArrayList<String> surnameCharactersTraditional;
    public ArrayList<String> surnamePinyinsSimplified;
    public ArrayList<String> surnamePinyinsCantonese;
    public ArrayList<String> famousPeopleList;


    public DataHolder(Context context) {

        allLargeImageIds = getAllLargeImagesIds();
        allObjectInfoFileIds = getAllInfoFilesIds();
        surnameCharactersSimplified = readFile(context, R.raw.surname_characters_simplified);
        surnameCharactersTraditional = readFile(context, R.raw.surname_characters_traditional);
        surnamePinyinsSimplified = readFile(context, R.raw.surname_pinyins_simplified);
        surnamePinyinsCantonese = readFile(context, R.raw.surname_pinyins_cantonese);

        famousPeopleList=readFile(context,R.raw.famous_people);
        //System.out.println(famousPeopleList);


        memberObjects = getMemberObjectsList(surnameCharactersSimplified, surnamePinyinsSimplified);


    }

    private ArrayList<Integer> getAllLargeImagesIds() {
        ArrayList<Integer> allLargeImagesIds = new ArrayList<>();
        for (int i = 0; i <= 99; i ++) {
            allLargeImagesIds.add(R.drawable.no_image_replacement);
        }
        return allLargeImagesIds;
    }

    private ArrayList<Integer> getAllInfoFilesIds() {

        ArrayList<Integer> allInfoFilesIds = new ArrayList<>();
        for (int i = 0; i <= 99; i ++) {
            allInfoFilesIds.add(R.raw.sample_info);
        }
        return allInfoFilesIds;
    }

    private ArrayList<Member> getMemberObjectsList(ArrayList<String> surnameCharactersSimplified, ArrayList<String> surnamesPinyinSimplified) {
        ArrayList<Member> allObjectsList = new ArrayList<>();

//        for (String s:surnameCharactersSimplified) {
//            allObjectsList.add(new Member(s,0f, 0f, 0f));
//        }

        for (int i=0; i<surnameCharactersSimplified.size(); i++) {
            allObjectsList.add(new Member(surnameCharactersSimplified.get(i), surnamesPinyinSimplified.get(i), surnameCharactersTraditional.get(i), surnamePinyinsCantonese.get(i),0f, 0f, 0f));
        }

        return allObjectsList;
    }



    public ArrayList<String> readFile(Context context, int fileId) {

        ArrayList<String> allSummaries = new ArrayList<>();

        try {
            InputStream allMemberInfoFile = context.getResources().openRawResource(fileId);
            if (allMemberInfoFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allMemberInfoFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    allSummaries.add(line+"\n");
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

        return allSummaries;
    }

}
