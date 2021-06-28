package com.example.vocabular;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Vocabulary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

//        int BOOKSHELF_ROWS = 20;
//        int BOOKSHELF_COLUMNS = 3;

//Рабочий вариант
//        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
//        for (int i = 0; i < BOOKSHELF_ROWS; i++) {
//            TableRow tableRow = new TableRow(this);
//            tableRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {
//                TextView textView = new TextView(this);
//                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                textView.setTextSize(40);
//                textView.setBackgroundResource(R.drawable.cell_shape);
//                textView.setTextColor(Color.parseColor("#000000"));
//                textView.setText("Word");
//                tableRow.addView(textView, j);
//            }
//            table.addView(tableRow, i);
//        }

//Второй рабочий вариант закомментировать всё в ресурсах
//        LinearLayout table = new LinearLayout(this);
//        table.setOrientation(LinearLayout.HORIZONTAL);
//
//        for (int i = 0; i < BOOKSHELF_COLUMNS; i++) {
//            LinearLayout column = new LinearLayout(this);
//            column.setOrientation(LinearLayout.VERTICAL);
//
//            TextView header = new TextView(this);
//            header.setText("SS");
//
//            column.addView(header);
//
//            for (int j = 0; j < BOOKSHELF_ROWS; j++) {
//                TextView cell = new TextView(this);
//                cell.setText("AAA");
//
//                column.addView(cell);
//            }
//
//            table.addView(column);
//        }
//        setContentView(table);


//Просто список из массива
//        ListView listView = findViewById(R.id.listView);
//
//// определяем строковый массив
//        final String[] catNames = new String[] {
//                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
//                "Китти", "Масяня", "Симба"
//        };
//
//// используем адаптер данных
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, catNames);
//
//        listView.setAdapter(adapter);
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
        if (file.exists()) {
            try {
                FileReader outputfile = new FileReader(file);
                // создаем объект файлового объекта CSVWriter в качестве параметра
                CSVReader reader = new CSVReader(outputfile);
                // добавляем заголовок в csv
//            String[] header = {"Eng", "Ru", "Learn"};
//            writer.writeNext(header);
//
//            CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
//            //Read all rows at once
                List<String[]> allRows = reader.readAll();
//                for (String[] row : allRows) {
//                    System.out.println(Arrays.toString(row));
//                }

                int i = 0;
                for (String[] row : allRows) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    tableRow.setPadding(5,5,5,5);
                    for (int j = 0; j < 3; j++) {
                        TextView textView = new TextView(this);
                        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        textView.setTextSize(25);
                        textView.setBackgroundResource(R.drawable.cell_shape);
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setText(row[j]);
                        textView.setPadding(3,3,3,3);
                        tableRow.addView(textView, j);
                    }
                    table.addView(tableRow, i);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Read CSV line by line and use the string array as you want

        }
    }
}