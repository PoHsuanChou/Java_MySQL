package edu.uob;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class CheckParser {
    private int countWord;
    private String theLine[];
    private static int count;
    private boolean TrueOrFalse;
    ArrayList<String> parserArray= new ArrayList<>();
    private List<String> ColName = new ArrayList<>();
    private List<String>TableName = new ArrayList<>();
    private ArrayList<String>Data = new ArrayList<>();
    private String DatabaseName;
    private String CommandType;
    private String AlterationType;
    private List<String> ConditionList = new ArrayList<>();
    private String CreateOrDatabase;

    public void setCommandType(String commandType) {
        CommandType = commandType;
    }

    public boolean isTrueOrFalse() {
        return TrueOrFalse;
    }

    public List<String> getColName() {
        return ColName;
    }

    public List<String> getTableName() {
        return TableName;
    }

    public ArrayList<String> getData() {
        return Data;
    }

    public String getDatabaseName() {
        return DatabaseName;
    }

    public String getCommandType() {
        return CommandType;
    }

    public String getAlterationType() {
        return AlterationType;
    }

    public List<String> getConditionList() {
        return ConditionList;
    }

    public String getCreateOrDatabase() {
        return CreateOrDatabase;
    }

    public CheckParser(String line) {
        count = 0;
        line = line.replace(";"," ;");
        line = line.replace(","," , ");///
        theLine = line.trim().split("\\s+");
        this.countWord = theLine.length;

        for(int i = 0; i < countWord; i ++){
            parserArray.add(theLine[i]);
        }
        this.TrueOrFalse = Command(parserArray);
    }

    public boolean Command(ArrayList<String> parserArray){
        if(BoolCommandType(parserArray)){
            if(CommandType(parserArray)){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(parserArray.get(count).contains(";")){
                    return true;
                }

                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    public boolean BoolCommandType(ArrayList<String> parserArray){
        if(parserArray.get(count).compareToIgnoreCase("use")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("create")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Drop")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Alter")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Insert")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Select")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Update")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Delete")== 0){return true;}
        if(parserArray.get(count).compareToIgnoreCase("Join")== 0){return true;}
        else {
            return false;
        }
    }

    public boolean CommandType(ArrayList<String> parserArray){
        if(parserArray.get(count).compareToIgnoreCase("use") == 0){
            this.CommandType = parserArray.get(count);
            if(parserArray.size()!= 3){
                return false;
            }
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(PlainText(parserArray)){
                this.DatabaseName = parserArray.get(count);
                return true;
            }
            else{
                return false;
            }
        }
        else if(parserArray.get(count).compareToIgnoreCase("create") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).compareToIgnoreCase("table") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(count + 2 == parserArray.size()){
                    if(PlainText(parserArray)){
                        this.TableName.add(parserArray.get(count));
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else if(PlainText(parserArray)){
                    this.TableName.add(parserArray.get(count));
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(parserArray.get(count).compareToIgnoreCase("(") == 0){
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(AttributeList(parserArray)){
                            if(!CheckBoundary(parserArray)){
                                return false;
                            }
                            count++;
                            if(parserArray.get(count).contains(")")){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }

                    else{
                        return false;
                    }

                }
                else{
                    return false;
                }
            }
            else if(parserArray.get(count).compareToIgnoreCase("database") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(PlainText(parserArray)){
                    this.CreateOrDatabase = parserArray.get(count);
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(parserArray.get(count).compareToIgnoreCase("drop") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).compareToIgnoreCase("table") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(PlainText(parserArray)){
                    this.TableName.add(parserArray.get(count));
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(parserArray.get(count).compareToIgnoreCase("database") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(PlainText(parserArray)){
                    this.CreateOrDatabase = parserArray.get(count);
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }

        }
        else if(parserArray.get(count).compareToIgnoreCase("alter") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).compareToIgnoreCase("table") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(PlainText(parserArray)){
                    TableName.add(parserArray.get(count));//// add table to data
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(AlterationType(parserArray)){
                        this.AlterationType = parserArray.get(count); // add alterType
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(PlainText(parserArray)){
                            this.ColName.add(parserArray.get(count)); //add colname
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(parserArray.get(count).compareToIgnoreCase("Insert") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).compareToIgnoreCase("into") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(PlainText(parserArray)){
                    TableName.add(parserArray.get(count)); //add table name
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(parserArray.get(count).compareToIgnoreCase("values(") == 0){
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(ValueList(parserArray)){
                            if(!CheckBoundary(parserArray)){
                                return false;
                            }
                            count++;
                            if(parserArray.get(count).compareToIgnoreCase(")") == 0){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(parserArray.get(count).compareToIgnoreCase("Select") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(WildAttribList(parserArray)){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(parserArray.get(count).compareToIgnoreCase("FROM") == 0){
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(PlainText(parserArray)){
                        this.TableName.add(parserArray.get(count)); //add table name
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                            count++;
                            if(parserArray.get(count).compareToIgnoreCase("where") == 0){
                                if(!CheckBoundary(parserArray)){
                                    return false;
                                }
                                count++;
                                if(Condition(parserArray)){
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                            else{
                                if(count == parserArray.size() - 1){
                                    count--;
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(parserArray.get(count).compareToIgnoreCase("Update") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(PlainText(parserArray)){
                this.TableName.add(parserArray.get(count)); // add table name
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(parserArray.get(count).compareToIgnoreCase("set") == 0){
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(NameValueList(parserArray)){
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(parserArray.get(count).compareToIgnoreCase("where") == 0){
                            if(!CheckBoundary(parserArray)){
                                return false;
                            }
                            count++;
                            if(Condition(parserArray)){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else {
                return false;
            }

        }
        else if(parserArray.get(count).compareToIgnoreCase("delete") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
             count++;
             if(parserArray.get(count).compareToIgnoreCase("from") == 0){
                 if(!CheckBoundary(parserArray)){
                     return false;
                 }
                 count++;
                 if(PlainText(parserArray)){
                     this.TableName.add(parserArray.get(count));
                     if(!CheckBoundary(parserArray)){
                         return false;
                     }
                     count++;
                     if(parserArray.get(count).compareToIgnoreCase("where") == 0){
                         if(!CheckBoundary(parserArray)){
                             return false;
                         }
                         count++;
                         if(Condition(parserArray)){
                             return true;
                         }
                         else{
                             return false;
                         }
                     }
                     else{
                         return false;
                     }
                 }
                 else{
                     return false;
                 }
             }
             else{
                 return false;
             }
        }
        else if(parserArray.get(count).compareToIgnoreCase("join") == 0){
            this.CommandType = parserArray.get(count);
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(PlainText(parserArray)){
                this.TableName.add(parserArray.get(count));
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(parserArray.get(count).compareToIgnoreCase("and") == 0){
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(PlainText(parserArray)){
                        this.TableName.add(parserArray.get(count));
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(parserArray.get(count).compareToIgnoreCase("on") == 0){
                            if(!CheckBoundary(parserArray)){
                                return false;
                            }
                            count++;
                            if(PlainText(parserArray)){
                                this.ColName.add(parserArray.get(count));
                                if(!CheckBoundary(parserArray)){
                                    return false;
                                }
                                count++;
                                if(parserArray.get(count).compareToIgnoreCase("and") == 0){
                                    if(!CheckBoundary(parserArray)){
                                        return false;
                                    }
                                    count++;
                                    if(PlainText(parserArray)){
                                        this.ColName.add(parserArray.get(count));
                                        return true;
                                    }
                                    else{
                                        return false;
                                    }
                                }
                                else{
                                    return false;
                                }

                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean Structure(ArrayList<String> parserArray){
        if(parserArray.get(count).compareToIgnoreCase("database") == 0){
            return true;
        }
        else if(parserArray.get(count).compareToIgnoreCase("table") == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean PlainText(ArrayList<String> parserArray){
        String s = parserArray.get(count);
        if(s == null){
            return false;
        }
        for(int i = 0; i < s.length(); i ++){
            if(!Character.isLetterOrDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public boolean AlterationType(ArrayList<String> parserArray){
        if(parserArray.get(count).compareToIgnoreCase("add") == 0 ||parserArray.get(count).compareToIgnoreCase("drop") == 0){
            return true;
        }
        else{
            return false;
        }
    }
//    public boolean ValueList(ArrayList<String> parserArray, int count){}
    public boolean BooleanLiteral(ArrayList<String> parserArray){
        if(parserArray.get(count).contains("TRUE") || parserArray.get(count).contains("FALSE")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean StringLiteralArray(ArrayList<String> parserArray){
        char [] chars = parserArray.get(count).toCharArray();
//        String s = parserArray.get(count);
        if(CharLiteral(chars)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean StringLiteral(String s){
        char [] chars = s.toCharArray();
        if(CharLiteral(chars)){
            return true;
        }
        else{
            return false;
        }
    }





    public boolean CharLiteral(char[] charArray){
        int check = 0;
        for(int i = 0; i < charArray.length;i++){
            if(IsLetter(charArray[i]) || IsSymbol(charArray[i]) || charArray[i] == ' '){
                check++;
            }
        }
        if(check == charArray.length){
            return true;
        }
        return false;
    }
    public boolean IsLetter(char c){
        if(!(Character.isLetter(c))){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean IsSymbol(char c){
        char[] symbol = {'!' , '#' , '$' , '%' , '&' , '(' , ')' , '*' , '+' , ',' , '-' , '.' , '/' , ':' , ';' , '>' , '=' , '<' , '?' , '@' , '[' , '\\' , ']' , '^' , '_' , '`' , '{' ,'}' , '~'};
        for(int i = 0; i < symbol.length; i ++){
            if(symbol[i] == c){
                return true;
            }
        }
        return false;
    }
    public boolean DigitSequence(ArrayList<String> parserArray){
        String s = parserArray.get(count);
        if(s == null){
            return false;
        }
        for(int i = 0; i < s.length(); i ++){
            if(!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public boolean FloatLiteral(ArrayList<String> parserArray){
        String s = parserArray.get(count);
        if(s == null){
            return false;
        }
        try {
            double n = Double.parseDouble(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public boolean IntegerLiteral(ArrayList<String> parserArray){
        String s = parserArray.get(count);
        if (s== null) {
            return false;
        }
        try {
            int d = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean Value(ArrayList<String> parserArray){
        char firstChar =parserArray.get(count).charAt(0);
        int last = parserArray.get(count).length() - 1;
        char lastChar = parserArray.get(count).charAt(last);
        if(firstChar == '\'' && lastChar == '\''){
            String s = parserArray.get(count).replace("'", "");
            if(StringLiteral(s)){
                return true;
            }
            else{
                return false;
            }
        }
        else if(BooleanLiteral(parserArray)){
            return true;
        }
        else if(FloatLiteral(parserArray)){
            return true;
        }
        else if(IntegerLiteral(parserArray)){
            return true;
        }
        else if(parserArray.get(count).contains("NULL")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean ValueList(ArrayList<String> parserArray){
        if(Value(parserArray)){
            Data.add(parserArray.get(count)); // add data
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).compareToIgnoreCase(",") == 0){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(ValueList(parserArray)){
                    return true;
                }
                else{
                    return false;
                }
            }
                count--;
                return true;
        }
        return false;
    }

    public boolean Operator(ArrayList<String> parserArray){
        String[] c = {"==",">","<",">=","<=","!=" };
        for(int i = 0; i < c.length; i ++){
            if(parserArray.get(count).contains(c[i])){
                return true;
            }
        }
        if(parserArray.get(count).compareToIgnoreCase("like") == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean AttributeList(ArrayList<String> parserArray){
        if(PlainText(parserArray)){
            this.ColName.add(parserArray.get(count)); // add col name
            if(!CheckBoundary(parserArray)){
                return false;
            }
                count++;
                if(parserArray.get(count).compareToIgnoreCase(",") == 0){
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(AttributeList(parserArray)){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    count--;
                    return true;
                }
        }
        return false;
    }

    public boolean WildAttribList(ArrayList<String> parserArray){
        if(AttributeList(parserArray)){
            return true;
        }
        else if(parserArray.get(count).contains("*")){
            this.ColName.add(parserArray.get(count)); // add col name
            return true;
        }
        else{
            return false;
        }
    }

    public boolean Condition(ArrayList<String> parserArray){
        if(parserArray.get(count).contains("(")){
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(Condition(parserArray)){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(count >= parserArray.size()-1){
                    count--;
                    return true;
                }
                if(parserArray.get(count).compareToIgnoreCase(")AND(") == 0 || parserArray.get(count).compareToIgnoreCase(")OR(") == 0){
                    if(!CheckBoundary(parserArray)){
                        return false;
                    }
                    count++;
                    if(Condition(parserArray)){
                        if(!CheckBoundary(parserArray)){
                            return false;
                        }
                        count++;
                        if(parserArray.get(count).contains(")")){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(PlainText(parserArray)){
            this.ConditionList.add(parserArray.get(count).replace("'",""));
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(Operator(parserArray)){
                this.ConditionList.add(parserArray.get(count).replace("'",""));
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(Value(parserArray)){
                    this.ConditionList.add(parserArray.get(count).replace("'",""));
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean NameValueList(ArrayList<String> parserArray){
        if(NameValuePair(parserArray)){
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).contains(",")){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(NameValueList(parserArray)){
                    return true;
                }
                else{
                    return false;
                }
            }

            else{
                if(parserArray.get(count).compareToIgnoreCase("where") == 0){
                    count--;
                    return true;
                }
                else if(NameValuePair(parserArray)){
                    count--;
                    return true;
                }
                else{
                    return false;
                }

            }

        }
        else{
            return false;
        }
    }
    public boolean NameValuePair(ArrayList<String> parserArray){
        if(PlainText(parserArray)){
            this.ColName.add(parserArray.get(count)); // add col name
            if(!CheckBoundary(parserArray)){
                return false;
            }
            count++;
            if(parserArray.get(count).contains("=")){
                if(!CheckBoundary(parserArray)){
                    return false;
                }
                count++;
                if(Value(parserArray)){
                    this.Data.add(parserArray.get(count));
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean CheckBoundary(ArrayList<String> parserArray){
        if(count>=parserArray.size() - 1){
            return false;
        }
        return true;
    }
    }




