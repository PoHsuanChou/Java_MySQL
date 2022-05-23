package edu.uob;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class DropCMD extends CheckParser{
    private String okOrError;
    public DropCMD(HashMap<String, ArrayList<Table>> databases, String line, String currentDatabase) throws IOException {
        super(line);
        Path thePath = Paths.get("");
        String directoryName = thePath.toAbsolutePath().normalize().toString();
        String Path = directoryName.replace("cw-db","testing");
//        String Path = "/Users/chous/Desktop/07 Briefing on DB assignment/resources/testing";
        String[] lineSplit = line.trim().split("\\s+");
        int choice = 0;
        for(int r = 0; r < lineSplit.length; r ++){
            if(lineSplit[r].toLowerCase().contains("database")){
                choice = 1;
                break;
            }
            if(lineSplit[r].toLowerCase().contains("table")){
                choice = 2;
                break;
            }
        }
        if(choice == 1){
            currentDatabase = super.getCreateOrDatabase();
            String directory = Path + File.separator + currentDatabase;
            Path path = Paths.get(directory);
            File Directory = new File(directory);
            if(Directory.exists()){
                Files.delete(path);
                databases.remove(currentDatabase);
                if(okOrError == null){
                    okOrError = "[OK] database has removed ";
                }

                return;
            }
            else{
                System.out.println("[ERROR] this directory does not exist");
                okOrError = "[ERROR] no current database";
                return;
            }
        }
        if(choice == 2){
            if(currentDatabase == null){
                System.out.println("[ERROR] no current database");
                okOrError = "[ERROR] no current database";
                return;
            }


            String DropTable = super.getTableName().get(0);
            String file = Path + File.separator + currentDatabase + File.separator + DropTable + ".tab";
            File FileWantToDrop = new File(file);
            if(FileWantToDrop.exists()){
                FileWantToDrop.delete();
                databases.get(currentDatabase).remove(DropTable);
                if(okOrError == null){
                    okOrError = "[OK] table has removed ";
                }

            }
            else{
                System.out.println("[ERROR] this table does not exist");
                okOrError = "[ERROR] this table does not exist";
            }
        }

    }

    public String getOkOrError() {
        return okOrError;
    }
}
