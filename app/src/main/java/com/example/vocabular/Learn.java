package com.example.vocabular;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Learn extends AppCompatActivity {

    int[] marks = new int[20];
    List<String[]> vocab = new LinkedList<>();
    List<String[]> sortedBuf = new LinkedList<>();
    int state = 0;
    boolean down = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Button forwardBtn = (Button) findViewById(R.id.forward);
        Button backwardBtn = (Button) findViewById(R.id.backward);
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardBtnClick();
            }
        });
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backwardBtnClick();
            }
        });
        createNewTable();
    }

    //Создание новой таблицы для заучивания
    void createNewTable() {
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
                reader.close();
                vocab = allRows;
                Collections.shuffle(vocab);
                List<String[]> sortedWords = sort(allRows);
                sortedBuf = sortedWords;
//                for (String[] row : allRows) {
//                    System.out.println(Arrays.toString(row));
//                }
//                TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
                generateTable(sortedWords, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Read CSV line by line and use the string array as you want
        }
    }


    //создать таблицу из данного массива
    void generateTable(List<String[]> sortedWords, boolean rotate) {
        int k, b, j, c;
        if (!rotate) {
            k = 0;
            b = 1;
        } else {
            k = 1;
            b = -1;
        }
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
        table.removeAllViews();
        int i = 0;
        for (String[] row : sortedWords) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            j = k;
            c = 0;
            tableRow.setPadding(5, 5, 5, 5);
            while ((j >= 0) && (j <= 1)) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(25);
                textView.setBackgroundResource(R.drawable.cell_shape);
                textView.setTextColor(Color.parseColor("#000000"));
                String parsedWord = parseWord(row[j]);
                textView.setText(parsedWord);
                textView.setPadding(5, 5, 5, 5);
                tableRow.addView(textView, c);
                j = j + b;
                c++;
            }
            table.addView(tableRow, i);
            i++;
        }
    }

    //ограничение на длину строки
    String parseWord(String word) {
        String ret = "";
        if (word.length() > 15) {
            ret = word.replace(' ', '\n');
            return ret;
        } else {
            return word;
        }
    }

    //выбрать самые неизученные 20 слов
    List<String[]> sort(List<String[]> arr) {
        List<String[]> ret = new LinkedList<>();
        for (int i = 0; i < 11; i++) {
            if (ret.size() < 20) {
                for (String[] row : arr) {
                    if (!row[2].equals("Ln")) {
//                        System.out.println(row[2]);
                        int val = Integer.parseInt(row[2]);
//                        System.out.println(val);
                        if (Integer.parseInt(row[2]) == i) {
                            if (ret.size() < 20) {
                                ret.add(row);
//                                System.out.println(ret.size() - 1);
                                marks[ret.size() - 1] = arr.indexOf(row);
                            }
                        }
                    }
                }
            }
//            System.out.print(marks);
        }
        return ret;
    }

    //обновить данные об изучении слов
    List<String[]> upgradeWords(List<String[]> arr) {
//        String[] s= [];
        for (int i = 0; i < 20; i++) {
            String[] s = arr.get(marks[i]);
//            System.out.println(s[0]);
//            System.out.println(s[1]);
//            System.out.println(s[2]);
            if (!s[2].equals("Ln")) {
//                System.out.println(s[2]);
                int val = Integer.parseInt(s[2]);
                if (val < 10) {
                    val = val + 1;
                    s[2] = Integer.toString(val);
                }
                arr.set(marks[i], s);
            }
        }
        return arr;
    }

    //нажатие на кнопку вперед
    void forwardBtnClick() {
        ScrollView SC = (ScrollView) findViewById(R.id.scrollW);
        if (state == 0) {
            if (!down) {
                ObjectAnimator.ofInt(SC, "scrollY", 1000).setDuration(1200).start();
                down = true;
                state++;
            }
            else {
                paintColomn("white");
                if(down) {
                    SC.fullScroll(View.FOCUS_UP);
                    down = false;
                }
                state+=2;
            }
        } else if (state == 1) {
            paintColomn("white");
            if (down) {
                SC.fullScroll(View.FOCUS_UP);
                down = false;
            }
            state++;
        } else if (state < 22) {
            paintWord(state - 2);
            state++;
            if (state == 13) {
                if (!down) {
                    ObjectAnimator.ofInt(SC, "scrollY", 1000).setDuration(1200).start();
                    down = true;
                }
            }
        } else if (state == 22) {
            paintColomn("black");
            generateTable(sortedBuf, true);
            if (down) {
                SC.fullScroll(View.FOCUS_UP);
                down = false;
            }
            state++;
        } else if (state == 23) {
            if (!down) {
                ObjectAnimator.ofInt(SC, "scrollY", 1000).setDuration(1200).start();
                down = true;
                state++;
            }
            else
            {
                paintColomn("white");
                if (down) {
                    SC.fullScroll(View.FOCUS_UP);
                    down = false;
                }
                state+=2;
            }
        } else if (state == 24) {
            paintColomn("white");
            if (down) {
                SC.fullScroll(View.FOCUS_UP);
                down = false;
            }
            state++;
        } else if (state < 45) {
            paintWord(state - 25);
            state++;
            if (state == 36) {
                if (!down) {
                    ObjectAnimator.ofInt(SC, "scrollY", 1000).setDuration(1200).start();
                    down = true;
                }
            }
        } else if (state == 45) {
            upLearning();
            createNewTable();
            if (down) {
                SC.fullScroll(View.FOCUS_UP);
                down = false;
            }
            state = 0;
        }
    }

    //нажатие на кнопку ошибки
    void backwardBtnClick() {
//        ScrollView SC = (ScrollView) findViewById(R.id.scrollW);
//        SC.fullScroll(View.FOCUS_UP);
        if ((state > 1) && (state < 23)) {
            state = 0;
            paintColomn("black");
        } else if ((state > 23) && (state < 46)) {
            state = 23;
            paintColomn("black");
        }
    }

    //отобразить слово в строке
    void paintWord(int num) {
        TableLayout table1 = (TableLayout) findViewById(R.id.tableLayout1);
        View view = table1.getChildAt(num);
        if (view instanceof TableRow) {
            TableRow row1 = (TableRow) view;
            View view1 = row1.getChildAt(1);
            if (view1 instanceof TextView) {
                TextView prison = (TextView) view1;
                prison.setTextColor(Color.parseColor("#000000"));
                row1.removeViewAt(1);
                row1.addView(prison, 1);
            }
            table1.removeViewAt(num);
            table1.addView(row1, num);
        }
    }

    //Скрыть/отобразить столбец
    void paintColomn(String col) {
        if (col.equals("black")) col = "#000000";
        else if (col.equals("white")) col = "#FFFFFF";
        TableLayout table1 = (TableLayout) findViewById(R.id.tableLayout1);
        for (int i = 0; i < 20; i++) {
            View view = table1.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row1 = (TableRow) view;
                View view1 = row1.getChildAt(1);
                if (view1 instanceof TextView) {
                    TextView prison = (TextView) view1;
                    prison.setTextColor(Color.parseColor(col));
                    row1.removeViewAt(1);
                    row1.addView(prison, 1);
                }
                table1.removeViewAt(i);
                table1.addView(row1, i);
            }
        }
    }

    //записать данные об изучении в файл
    void upLearning() {
        List<String[]> newVocab = new LinkedList<>();
        newVocab = upgradeWords(vocab);
        File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
        if (file.exists()) {
            try {
                FileWriter outputfile = new FileWriter(file);
                // создаем объект файлового объекта CSVWriter в качестве параметра
                CSVWriter writer = new CSVWriter(outputfile);
                // добавляем заголовок в csv
//            String[] header = {"Eng", "Ru", "Learn"};
                writer.writeAll(newVocab);
//
//            CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
//            //Read all rows at once
//                        List<String[]> allRows = reader.readAll();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}