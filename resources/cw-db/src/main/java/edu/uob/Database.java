package edu.uob;

import java.util.ArrayList;
import java.util.Locale;

public class Database {
    private String name;
    ArrayList<Table> table = new ArrayList<>();

    public Database(String name) {
        this.name = name;
    }

}
