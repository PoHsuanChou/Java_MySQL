package edu.uob;

import com.sun.jdi.Value;

import java.util.*;

public class SelectCMD extends CheckParser{
    private String okOrError;

    public SelectCMD(HashMap<String, ArrayList<Table>> databases, String line, String currentDatabase) {
        super(line);
        int index = 0;
        String TableName = super.getTableName().get(0);
        if(currentDatabase == null){
            System.out.println("[ERROR] no current database");
            okOrError = "[ERROR] no current database ";
            return;
        }
        if(!databases.containsKey(currentDatabase)){
            okOrError = "[ERROR] no current database ";
            return;
        }
        Boolean checkDuplicate = false;
        for(int i = 0; i < databases.get(currentDatabase).size(); i ++){
            if(databases.get(currentDatabase).get(i).getName().compareToIgnoreCase(TableName)==0){
                index = i;
                checkDuplicate = true;
            }
        }

        if(!checkDuplicate){
            System.out.println("[ERROR] no such table");
            okOrError = "[ERROR] no such table ";
            return;
        }
        boolean IsAll = false;
        Boolean checkAttributeDuplicate = false;
        String[] lineSplit = line.trim().split("\\s+");
        int choice = 0;
        for(int r = 0; r < lineSplit.length; r ++){
            if(lineSplit[r].toLowerCase().contains(")or(")){
                choice = 1;

            }
            if(lineSplit[r].toLowerCase().contains(")and(")){
                choice = 2;

            }
            if(lineSplit[r].toLowerCase().contains("where")){
                choice = 3;
            }
            if(lineSplit[r].toLowerCase().contains("*")){
                IsAll = true;
                checkAttributeDuplicate = true;
            }
        }
        int count = 0;
        for(int k = 0; k < super.getColName().size(); k ++){
            for(int j = 0; j < databases.get(currentDatabase).get(index).getTitle().size(); j ++){
                if(databases.get(currentDatabase).get(index).getTitle().get(j).toLowerCase().contains(super.getColName().get(k).toLowerCase())){
                    count++;
                }
            }
        }
        if(count == super.getColName().size()) {
            checkAttributeDuplicate = true;

        }
        if(!checkAttributeDuplicate){
            System.out.println("[ERROR] no such AttributeName");
            okOrError = "[ERROR] no such AttributeName ";
            return;
        }
        ArrayList<Integer> SelectedRows = new ArrayList<>();
        ArrayList<Integer> SelectedSecondRows = new ArrayList<>();
              if(choice == 0){
                  StoreData(databases,SelectedRows,currentDatabase,index,choice);
                  ChooseData(lineSplit,SelectedRows);
              }
              if(choice == 1){
                  StoreData(databases,SelectedRows,currentDatabase,index,choice);
                  StoreData(databases,SelectedSecondRows,currentDatabase,index,choice);
                  SelectedRows= ChooseData(SelectedRows,SelectedSecondRows,choice);
              }
              else if(choice == 2){
                  StoreData(databases,SelectedRows,currentDatabase,index,choice);
                  StoreData(databases,SelectedSecondRows,currentDatabase,index,choice);
                  SelectedRows= ChooseData(SelectedRows,SelectedSecondRows,choice);
              }
              else if(choice == 3){
                  StoreData(databases,SelectedRows,currentDatabase,index,choice);
                  SelectedRows= ChooseData(SelectedRows,SelectedSecondRows,choice);
              }
        ArrayList<Integer> col = new ArrayList<>();
        if(super.getColName().contains("*")){
            for(int i = 0; i < databases.get(currentDatabase).get(index).getTitle().size(); i ++){
                col.add(i);
            }
        }
        else{
            for(int i = 0; i < super.getColName().size(); i ++){
                col.add(databases.get(currentDatabase).get(index).getTitle().indexOf(super.getColName().get(i)));
            }
        }
        for(int i = 0; i < col.size(); i ++){
            if(col.get(i) == -1){
                System.out.println("[ERROR] AttributeName is different");
                okOrError = "[ERROR] AttributeName is different ";
                return;
            }
        }
        ArrayList<String> Correct = new ArrayList<>();
        if(IsAll){
            for(int i = 0; i < databases.get(currentDatabase).get(index).getTitle().size(); i ++){
                System.out.print(databases.get(currentDatabase).get(index).getTitle().get(i)+ "  ");
                Correct.add(databases.get(currentDatabase).get(index).getTitle().get(i)+ "  ");
            }
        }
        else{
            for(int i = 0; i < super.getColName().size(); i ++){
                Correct.add(super.getColName().get(i)+ "  ");
                System.out.print(super.getColName().get(i)+ "  ");
            }
        }

        System.out.println();
        for(int i = 0; i < SelectedRows.size(); i ++){
            for(int j = 0; j < col.size(); j ++){
                System.out.print(databases.get(currentDatabase).get(index).getData().get(SelectedRows.get(i)).get(col.get(j))+"  ");
                Correct.add(databases.get(currentDatabase).get(index).getData().get(SelectedRows.get(i)).get(col.get(j))+"  ");
            }
            System.out.println();
        }
        if(okOrError == null){
            okOrError = "[OK]" + Correct;
        }


    }

    public void ChooseData(String[] lineSplit,ArrayList<Integer> SelectedRows) {
            ArrayList<Integer> newArray = new ArrayList<>();
            for (int element : SelectedRows) {
                if (!newArray.contains(element)) {
                    newArray.add(element);
                }
            }
            SelectedRows = newArray;
    }
    public ArrayList<Integer> ChooseData(ArrayList<Integer>SelectedRows,ArrayList<Integer>SelectedSecondRows, int choice){
        if(choice ==1){
            TreeSet<Integer> set = new TreeSet<>();
            for (int num:SelectedRows) {
                set.add(num);
            }
            for (int num: SelectedSecondRows) {
                set.add(num);
            }
            ArrayList<Integer> convert = new ArrayList<>(set);
            return convert;
        }
        if(choice == 2){
            ArrayList<Integer> newArray = new ArrayList<>();
            for(int i = 0; i < SelectedRows.size(); i++)
            {
                for(int j = 0; j < SelectedSecondRows.size(); j++)
                {
                    if(SelectedRows.get(i) == SelectedSecondRows.get(j))
                    {
                        newArray.add(SelectedRows.get(i));
                    }
                }
            }
            return newArray;
        }

        return SelectedRows;
    }




public void StoreData(HashMap<String, ArrayList<Table>> databases,ArrayList<Integer> SelectedRows,String currentDatabase, int index,int choice)
{
    if(choice != 1 && choice != 2 && choice != 3){
        for(int i = 0; i < databases.get(currentDatabase).get(index).getData().size(); i ++){
            SelectedRows.add(i);
        }
        return;
    }
    ArrayList<String> ColName = new ArrayList<>();
    ArrayList<String> Operator = new ArrayList<>();
    ArrayList<String> Value = new ArrayList<>();
    ColName.add(super.getConditionList().get(0));
    super.getConditionList().remove(0);
    Operator.add(super.getConditionList().get(0));
    super.getConditionList().remove(0);
    Value.add(super.getConditionList().get(0));
    super.getConditionList().remove(0);
    int targetCol = 0;
    for(int t = 0; t < databases.get(currentDatabase).get(index).getTitle().size(); t ++){
        if(databases.get(currentDatabase).get(index).getTitle().get(t).toLowerCase().contains(ColName.get(0).toLowerCase())){
            targetCol = t;
        }
    }
    boolean CheckDigit = true;
    boolean CheckDigit1 = true;
    for(int x = 0; x < Value.get(0).length(); x ++){
        if(!Character.isDigit(Value.get(0).charAt(x))){
            CheckDigit = false;
        }
    }
    for(int y = 0; y < databases.get(currentDatabase).get(index).getData().size(); y ++){
        String word = databases.get(currentDatabase).get(index).getData().get(y).get(targetCol);
        for(int z = 0; z < word.length();z++){
            if(!Character.isDigit(word.charAt(z))){
                CheckDigit1 = false;
            }
        }
    }

    if(CheckDigit&& CheckDigit1 ){
        ConditionHandleNum(databases,Operator,Value,SelectedRows,currentDatabase,index,targetCol,0);
    }

    else if(Operator.get(0).contains("==")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(Value.get(0))){
                    SelectedRows.add(b);
                }
            }

    }
    else if(Operator.get(0).contains("!=")){
            if(Isletter(Value.get(0)) && Isletter(ColName.get(0)) || IsNum(ColName.get(0)) && IsNum(Value.get(0))){
                for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                    if(!databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(Value.get(0))){
                        SelectedRows.add(b);
                    }
                }
            }
            else{
                System.out.println("Error");
                okOrError = "[ERROR] only number can be compared ";
                return;
            }

    }
    else if(Operator.get(0).toLowerCase().contains("like")){
        String RemoveSingleQuote = Value.get(0).replace("'","");
        char[] ToChar = RemoveSingleQuote.toCharArray();
        boolean CheckPercentage = false;
        ArrayList<Integer> percentageIndex = new ArrayList<>();
        for(int c = 0; c < RemoveSingleQuote.length(); c ++){
            if(ToChar[c] == '%'){
                percentageIndex.add(c);
            }
        }
        ConditionHandleLike(databases,currentDatabase,index,targetCol,percentageIndex,RemoveSingleQuote,SelectedRows);
    }
    else{
        System.out.println("[ERROR] only number can be compared");
        okOrError = "[ERROR] only number can be compared ";
        return;
    }
    return;
}

    public void ConditionHandleNum(HashMap<String, ArrayList<Table>> databases,ArrayList<String> Operator,ArrayList<String> Value,ArrayList<Integer> SelectedRows, String currentDatabase, int index, int targetCol,int a){
        if(Operator.get(a).contains(">=")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(Integer.parseInt(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol)) >= Integer.parseInt(Value.get(a))){
                    SelectedRows.add(b);
                }
            }
        }
        else if(Operator.get(a).contains("<=")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(Integer.parseInt(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol)) <= Integer.parseInt(Value.get(a))){
                    SelectedRows.add(b);
                }
            }
        }
        else if(Operator.get(a).contains(">")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(Integer.parseInt(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol)) > Integer.parseInt(Value.get(a))){
                    SelectedRows.add(b);
                }
            }
        }
        else if(Operator.get(a).contains("<")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(Integer.parseInt(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol)) < Integer.parseInt(Value.get(a))){
                    SelectedRows.add(b);
                }
            }
        }
        else if(Operator.get(0).contains("==")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(Value.get(0))){
                    SelectedRows.add(b);
                }
            }
        }
        else if(Operator.get(0).contains("!=")){
            for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
                if(!databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(Value.get(0))){
                    SelectedRows.add(b);
                }
            }
        }

        return;
    }
    public void ConditionHandleLike (HashMap<String, ArrayList<Table>> databases,String currentDatabase, int index, int targetCol, ArrayList<Integer>  percentageIndex, String RemoveSingleQuote,ArrayList<Integer> SelectedRows ){
        for(int b = 0; b < databases.get(currentDatabase).get(index).getData().size(); b ++){
            if(percentageIndex.size() > 1){
                String replace = RemoveSingleQuote.replace("%"," ");
                String[] split = replace.split(" ");
                String target = split[1];
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(target)){
                    SelectedRows.add(b);
                }
            }
            String RemovePercentage =  RemoveSingleQuote.replace("%","");
            if(percentageIndex.get(0) == 0){
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).endsWith(RemovePercentage)){
                    SelectedRows.add(b);
                }

            }
            else if(percentageIndex.get(0) == RemoveSingleQuote.length() - 1){
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).startsWith(RemovePercentage)){
                    SelectedRows.add(b);
                }

            }
            else{
                String replace = RemoveSingleQuote.replace("%"," ");
                String[] split = replace.split(" ");
                String target1 = split[0];
                String target2 = split[1];
                if(databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(target1) && databases.get(currentDatabase).get(index).getData().get(b).get(targetCol).contains(target2)){
                    SelectedRows.add(b);
                }
            }
        }
    }



    public boolean IsNum(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public boolean Isletter(String str){
        char [] chars = str.toCharArray();
        if(CharLiteral(chars)){
            return true;
        }
        else{
            return false;
        }
    }

    public String getOkOrError() {
        return okOrError;
    }
}


