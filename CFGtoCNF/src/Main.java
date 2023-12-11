import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static ArrayList<String>language=new ArrayList<>();//List to store our language
    private static int lineCount;  //linecount is to calculate our four step we have implemented
    private String epselonFound = "";   //to store non-terminals that have epseleon

    private static Map<String, List<String>> cfgMap = new LinkedHashMap<>(); //the map that store our productions
    public static void main(String[] args) {

        Main cfg=new Main();
        cfg.convertCFGtoCNF(); //calling our functions

    }

    public void convertCFGtoCNF() {

        fileRead();//Reading input from CFG.txt
        eliminateEpselon(); //Removing Epsilon
        eliminateUnitProduction();  //Unit production
        eliminateTerminals(); //Eliminating terminals
        breakStrings();//Braking long strings and defining new non-terminals
    }
    private static void fileRead(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("CFG.txt"));
            String line = reader.readLine();

            while (line != null) {
                if(line.contains("E")){
                    language.add(line.substring(2,line.length()));
                }
                else{
                    String[] parts=line.split("-");
                    String[]rhs=parts[1].split("\\|");
                    List<String> list2=new ArrayList<>();
                    for (int i = 0; i < rhs.length; i++) {
                        list2.add(rhs[i].trim());
                    }
                    cfgMap.put(parts[0].trim(), list2);
                    lineCount++;
                }
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminateEpselon() {

        System.out.println("\nEliminate €");

        for (int i = 0; i < lineCount; i++) {
            removeEpselon(); //Calling main functionto remove epsilon for every line
        }

        printMap();

    }
    private void removeEpselon() {

        Iterator itr = cfgMap.entrySet().iterator(); //iterator1 to find if any non-terminals have epselon
        Iterator itr2 = cfgMap.entrySet().iterator(); //iterator2 to travel for non-terminals that have epselon and size is grater than 1

        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            ArrayList<String> productionRow = (ArrayList<String>) entry.getValue();

            if (productionRow.contains("€")) {
                if (productionRow.size() > 1) {
                    productionRow.remove("€");//removing epselon
                    epselonFound = entry.getKey().toString(); //storing non-terminal that has epselon


                } else {

                    // remove if less than 1
                    epselonFound = entry.getKey().toString();
                    cfgMap.remove(epselonFound);
                }
            }
        }

        // find B and eliminate them
        while (itr2.hasNext()) {

            Map.Entry entry = (Map.Entry) itr2.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);

                for (int j = 0; j < temp.length(); j++) {
                    if (epselonFound.equals(Character.toString(productionList.get(i).charAt(j)))) {

                        if (temp.length() == 2) {

                            // remove specific character in string
                            temp = temp.replace(epselonFound, "");

                            if (!cfgMap.get(entry.getKey().toString()).contains(temp)) {
                                cfgMap.get(entry.getKey().toString()).add(temp);
                            }

                        } else if (temp.length()>2) {

                            String deletedTemp = new StringBuilder(temp).deleteCharAt(j).toString();

                            if (!cfgMap.get(entry.getKey().toString()).contains(deletedTemp)) {
                                cfgMap.get(entry.getKey().toString()).add(deletedTemp);
                            }

                        }  else {

                            if (!cfgMap.get(entry.getKey().toString()).contains("€")) {
                                cfgMap.get(entry.getKey().toString()).add("€");
                            }
                        }
                    }
                }
            }
        }
    }

    private void eliminateUnitProduction() {

        System.out.println("Eliminate unit production");

        for (int i = 0; i < lineCount; i++) {
            removeSingleVariable();
        }

        printMap();

    }

    private void removeSingleVariable() {
        //function to remove non-terminals those are in RHS in other non-terminals
        Iterator itr4 = cfgMap.entrySet().iterator();
        String key = null;


        while (itr4.hasNext()) {

            Map.Entry entry = (Map.Entry) itr4.next();
            Set set = cfgMap.keySet();
            ArrayList<String> keySet = new ArrayList<String>(set);
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);

                for (int j = 0; j < temp.length(); j++) {

                    for (int k = 0; k < keySet.size(); k++) {
                        if (keySet.get(k).equals(temp)) {

                            key = entry.getKey().toString();
                            List<String> productionValue = cfgMap.get(temp);
                            productionList.remove(temp);

                            for (int l = 0; l < productionValue.size(); l++) {
                                cfgMap.get(key).add(productionValue.get(l));
                            }
                        }
                    }
                }
            }
        }
    }

    private void breakStrings() {

        System.out.println("Break variable strings longer than 2\n");

        for (int i = 0; i < lineCount; i++) {
            removeThreeTerminal();
        }

        printMap();

    }
    private void removeThreeTerminal() {

        Iterator itr = cfgMap.entrySet().iterator();
        ArrayList<String> keyList = new ArrayList<>();
        Iterator itr2 = cfgMap.entrySet().iterator();

        // obtain key that use to eliminate two terminal and above
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            ArrayList<String> productionRow = (ArrayList<String>) entry.getValue();

            if (productionRow.size() < 2) {
                keyList.add(entry.getKey().toString());
            }
        }

        // find more than three terminal or combination of variable and terminal to eliminate them
        while (itr2.hasNext()) {

            Map.Entry entry = (Map.Entry) itr2.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            if (productionList.size() > 1) {
                for (int i = 0; i < productionList.size(); i++) {
                    String temp = productionList.get(i);

                    for (int j = 0; j < temp.length(); j++) {

                        if (temp.length() > 2) {
                            String stringToBeReplaced1 = temp.substring(j, temp.length());
                            String stringToBeReplaced2 = temp.substring(0, temp.length() - j);

                            for (String key : keyList) {

                                List<String> keyValues = new ArrayList<>();
                                keyValues = cfgMap.get(key);
                                String[] values = keyValues.toArray(new String[keyValues.size()]);
                                String value = values[0];

                                if (stringToBeReplaced1.equals(value)) {

                                    cfgMap.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(stringToBeReplaced1, key);

                                    if (!cfgMap.get(entry.getKey().toString()).contains(temp)) {
                                        cfgMap.get(entry.getKey().toString()).add(i, temp);
                                    }
                                } else if (stringToBeReplaced2.equals(value)) {

                                    cfgMap.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(stringToBeReplaced2, key);

                                    if (!cfgMap.get(entry.getKey().toString()).contains(temp)) {
                                        cfgMap.get(entry.getKey().toString()).add(i, temp);
                                    }
                                }
                            }
                        } else if (temp.length() == 2) {

                            for (String key : keyList) {

                                List<String> keyValues = new ArrayList<>();
                                keyValues = cfgMap.get(key);
                                String[] values = keyValues.toArray(new String[keyValues.size()]);
                                String value = values[0];


                                for (int pos = 0; pos < temp.length(); pos++) {
                                    String tempChar = Character.toString(temp.charAt(pos));


                                    if (value.equals(tempChar)) {

                                        cfgMap.get(entry.getKey().toString()).remove(temp);
                                        temp = temp.replace(tempChar, key);

                                        if (!cfgMap.get(entry.getKey().toString()).contains(temp)) {
                                            cfgMap.get(entry.getKey().toString()).add(i, temp);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            } else if (productionList.size() == 1) {

                for (int i = 0; i < productionList.size(); i++) {
                    String temp = productionList.get(i);

                    if (temp.length() == 2) {

                        for (String key : keyList) {

                            List<String> keyValues = new ArrayList<>();
                            keyValues = cfgMap.get(key);
                            String[] values = keyValues.toArray(new String[keyValues.size()]);
                            String value = values[0];


                            for (int pos = 0; pos < temp.length(); pos++) {
                                String tempChar = Character.toString(temp.charAt(pos));


                                if (value.equals(tempChar)) {

                                    cfgMap.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(tempChar, key);

                                    if (!cfgMap.get(entry.getKey().toString()).contains(temp)) {
                                        cfgMap.get(entry.getKey().toString()).add(i, temp);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private Boolean checkDuplicateInProductionList(Map<String, List<String>> map, String key) {

        Boolean notFound = true;

        Iterator itr = map.entrySet().iterator();
        outerloop://to exit at any point inside an inner loop

        while (itr.hasNext()) {

            Map.Entry entry = (Map.Entry) itr.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                if (productionList.size() < 2) {

                    if (productionList.get(i).equals(key)) {
                        notFound = false;
                        break outerloop;
                    } else {
                        notFound = true;
                    }
                }
            }
        }

        return notFound;
    }

    private void eliminateTerminals() {

        System.out.println("Eliminate terminals");

        Iterator itr5 = cfgMap.entrySet().iterator();
        String key = null;
        int asciiBegin = 71; //G

        Map<String, List<String>> tempList = new LinkedHashMap<>();

        while (itr5.hasNext()) {

            Map.Entry entry = (Map.Entry) itr5.next();
            Set set = cfgMap.keySet();

            ArrayList<String> keySet = new ArrayList<String>(set);
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();
            Boolean found1 = false;
            Boolean found2 = false;
            Boolean found = false;


            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);

                for (int j = 0; j < temp.length(); j++) {

                    if (temp.length() == 3) {

                        String newProduction = temp.substring(1, 3); // SA

                        if (checkDuplicateInProductionList(tempList, newProduction) && checkDuplicateInProductionList(cfgMap, newProduction)) {
                            found = true;
                        } else {
                            found = false;
                        }

                        if (found) {

                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction);
                            key = Character.toString((char) asciiBegin);

                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }

                    } else if (temp.length() == 2) { // if only two substring

                        for (int k = 0; k < keySet.size(); k++) {

                            if (!keySet.get(k).equals(Character.toString(productionList.get(i).charAt(j)))) { // if substring not equals to keySet
                                found = false;

                            } else {
                                found = true;
                                break;
                            }

                        }

                        if (!found) {
                            String newProduction = Character.toString(productionList.get(i).charAt(j));

                            if (checkDuplicateInProductionList(tempList, newProduction) && checkDuplicateInProductionList(cfgMap, newProduction)) {

                                ArrayList<String> newVariable = new ArrayList<>();
                                newVariable.add(newProduction);
                                key = Character.toString((char) asciiBegin);

                                tempList.put(key, newVariable);

                                asciiBegin++;

                            }
                        }
                    } else if (temp.length() == 4) {

                        String newProduction1 = temp.substring(0, 2); // SA
                        String newProduction2 = temp.substring(2, 4); // SA

                        if (checkDuplicateInProductionList(tempList, newProduction1) && checkDuplicateInProductionList(cfgMap, newProduction1)) {
                            found1 = true;
                        } else {
                            found1 = false;
                        }

                        if (checkDuplicateInProductionList(tempList, newProduction2) && checkDuplicateInProductionList(cfgMap, newProduction2)) {
                            found2 = true;
                        } else {
                            found2 = false;
                        }


                        if (found1) {

                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction1);
                            key = Character.toString((char) asciiBegin);

                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }

                        if (found2) {
                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction2);
                            key = Character.toString((char) asciiBegin);

                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }
                    }
                }
            }
        }
        cfgMap.putAll(tempList);
        printMap();
    }//eliminating terminals according to their size and define new non-terminals

    private void printMap() {

        Iterator print = cfgMap.entrySet().iterator();
        while (print.hasNext()) {
            Map.Entry pair = (Map.Entry) print.next();
            System.out.println(pair.getKey() + " - " + pair.getValue());
        }

        System.out.println(" ");
    }//toString method with iterator
}



