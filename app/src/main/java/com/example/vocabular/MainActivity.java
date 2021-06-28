package com.example.vocabular;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.view.KeyEvent.KEYCODE_ENTER;


public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnFetch = (Button) findViewById(R.id.downloadBtn);
        Button save = (Button) findViewById(R.id.saveButton);
        EditText Text1 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText Text2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        Text1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    translate("eng");
                    handled = true;
                }
                return handled;
            }
        });
        Text2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    translate("rus");
                    handled = true;
                }
                return handled;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();
            }
        });
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate("button");
            }
        });
    }


    void saveWord(){
        EditText Text1 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText Text2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        String word1 = Text1.getText().toString();
        String word2 = Text2.getText().toString();
        if ((word1.equals("")) || (word2.equals(""))) return;
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Text1, InputMethodManager.SHOW_IMPLICIT);
        Text1.setText("");
        Text2.setText("");
        File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
        if (!file.exists()) {
            try {
                FileWriter outputfile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputfile);
                String[] data1 = {word1, word2, "0"};
                writer.writeNext(data1);
                writer.close();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сохранено", Toast.LENGTH_SHORT);
                toast.show();
            } catch (IOException e) {
                // TODO автоматически сгенерированный блок catch
                e.printStackTrace();
            }
        } else {
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(file, true));
                String[] record = {word1, word2, "0"};
                writer.writeNext(record);
                writer.close();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сохранено", Toast.LENGTH_SHORT);
                toast.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void translate(String but){
        EditText Text1 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText Text2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        hideKeyboard(MainActivity.this);
        String word1 = Text1.getText().toString();
        String word2 = Text2.getText().toString();
        String word_url;
        String filter;
        boolean L;
        if (but.equals("button")) {
            if (!word1.equals("")) {
                word_url = "https://www.multitran.com/m.exe?l1=1&l2=2&s=" + word1;
                L = true;
                filter = "a[href$=1]";
            } else {
                word_url = "https://www.multitran.com/m.exe?l1=2&l2=1&s=" + word2;
                L = false;
                filter = "a[href$=2]";
            }
        }
        else if (but.equals("eng")) {
                word_url = "https://www.multitran.com/m.exe?l1=1&l2=2&s=" + word1;
                L = true;
                filter = "a[href$=1]";
        }
        else {
                word_url = "https://www.multitran.com/m.exe?l1=2&l2=1&s=" + word2;
                L = false;
                filter = "a[href$=2]";
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    String content = getContent(word_url);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Document doc = Jsoup.parse(content);
                            Elements link = doc.select("[class^=trans]");

                            List<String> listA = new ArrayList<String>();

                            int i = 0;
                            for (Element links : link.select(filter)) {
                                listA.add(links.html());
                                if (i > 20) break;
                                i++;
                            }
                            String[] linkInnerH = listA.toArray(new String[0]);
                            ListView lvMain = (ListView) findViewById(R.id.List);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, linkInnerH);
                            lvMain.setAdapter(adapter);
                            lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    String selectedFromList = (String) (lvMain.getItemAtPosition(position));
                                    if (L)
                                        Text2.setText(selectedFromList);
                                    else
                                        Text1.setText(selectedFromList);
                                }
                            });
                        }
                    });
                } catch (IOException ex) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Учить");
        menu.add("Мой словарь");
        menu.add("Инструкция");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Учить") {
            Intent intent = new Intent(this, Learn.class);
            startActivity(intent);
        } else if (item.getTitle() == "Мой словарь") {
            Intent intent = new Intent(this, Vocabulary_new.class);
            startActivity(intent);
        } else if (item.getTitle() == "Инструкция") {
            Intent intent = new Intent(this, Instruction.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}