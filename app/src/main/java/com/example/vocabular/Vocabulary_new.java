package com.example.vocabular;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Vocabulary_new extends AppCompatActivity {
    List<String[]> vocab = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_new);
        File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
        if (file.exists()) {
            try {
                FileReader outputfile = new FileReader(file);
                CSVReader reader = new CSVReader(outputfile);
                vocab = reader.readAll();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            createListView();
        }

    }

    void createListView(){
        ListView lvMain = (ListView) findViewById(R.id.List1);
        int i = 0;
        String[] linkInnerH = new String[vocab.size()];
        for (String[] row : vocab) {
            linkInnerH[i] = row[0] + " - " + row[1] + " (" + row[2] + ")";
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, linkInnerH);
        lvMain.setAdapter(adapter);
        registerForContextMenu(lvMain);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        System.out.println(info.position);
        upVocab(info.position);
        createListView();
        return super.onContextItemSelected(item);
    }


    void upVocab(int index) {
        vocab.remove(index);
        File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
        if (file.exists()) {
            try {
                FileWriter outputfile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputfile);
                writer.writeAll(vocab);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
