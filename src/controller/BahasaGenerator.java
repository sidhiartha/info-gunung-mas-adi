package controller;

import jakarta.servlet.ServletContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BahasaGenerator {

    public static final String FILENAME = "bahasa.0";

    public static void generateBaseBahasa(ServletContext context) {
        OntoBahasaReader bahasaReader = new OntoBahasaReader(context);
        MountaineeringReader mountReader = new MountaineeringReader(context);

        List<String> katas = bahasaReader.getAllKata();
        List<String> resultList = new ArrayList<String>();
        katas.addAll(mountReader.getAllKata());
        String[] katakata = null;
        boolean alreadyExist = false;

        Collections.sort(katas, comparator);
        try {
            FileWriter writer = new FileWriter(new File(FILENAME));

            for (String kata : katas) {
                if (CheckIfKataContainSpasi(kata)) {
                    katakata = ParseJadiKata(kata);

                    for (String item : katakata) {
                        if ((item.contains("-") == false)
//								&& (item.contains(" ") == false)
                                && item.trim().length() > 0) {
                            alreadyExist = false;
                            for (String myWord : resultList) {
                                if (myWord.equals(item)) {
                                    alreadyExist = true;
                                    break;
                                }
                            }
                            if (alreadyExist == false) {
                                resultList.add(item);
                            }
                        }
                    }
                } else {
                    if ((kata.contains("-") == false)
//							&& (kata.contains(" ") == false)
                            && kata.trim().length() > 0) {
                        alreadyExist = false;
                        for (String myWord : resultList) {
                            if (myWord.equals(kata)) {
                                alreadyExist = true;
                                break;
                            }
                        }
                        if (alreadyExist == false) {
                            resultList.add(kata);
                        }
                    }
                }
            }
            int indexKata = 0;

            Collections.sort(resultList);

            for (String resultKata : resultList) {
                if (resultKata.length() > 1
                        && indexKata < resultList.size() - 1) {
                    writer.append(resultKata + "\n");
                } else if (resultKata.length() > 1
                        && indexKata == resultList.size() - 1) {
                    writer.append(resultKata);
                }
                indexKata++;
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean CheckIfKataContainSpasi(String sourceKata) {
        boolean result = false;
        if (sourceKata.contains(" ")) {
            result = true;
        }
        return result;
    }

    public static String[] ParseJadiKata(String sourceKata) {
        String[] result = null;
        result = sourceKata.split(" ");
        return result;
    }

    static final Comparator<String> comparator = new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            // TODO Auto-generated method stub
            return o1.compareToIgnoreCase(o2);
        }
    };
}
