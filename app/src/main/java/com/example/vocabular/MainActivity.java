package com.example.vocabular;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView contentView = (TextView) findViewById(R.id.content);
//        WebView webView = (WebView) findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
        Button btnFetch = (Button) findViewById(R.id.downloadBtn);
        Button save = (Button) findViewById(R.id.saveButton);
        EditText Text1 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText Text2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Text1, InputMethodManager.SHOW_IMPLICIT);
//                isStoragePermissionGranted();
                String word1 = Text1.getText().toString();
                String word2 = Text2.getText().toString();
                Text1.setText("");
                Text2.setText("");
//                File sdPath = Environment.getExternalStorageDirectory();
//                sdPath = new File(sdPath.getAbsolutePath() + "/" + "Vocabular");
//                if (!sdPath.mkdirs()) Log.d(TAG, "НЕ СОЗДАНО");
                File file = new File(getExternalFilesDir(null), "Мой словарь.csv");
//                File file = new File(sdPath, "data.csv");
//                File file = new File(csv);
                if (!file.exists()) {
                    try {
                        // создаем объект FileWriter с файлом в качестве параметра
                        FileWriter outputfile = new FileWriter(file);
                        // создаем объект файлового объекта CSVWriter в качестве параметра
                        CSVWriter writer = new CSVWriter(outputfile);
                        // добавляем заголовок в csv
                        String[] header = {"Eng", "Ru", "Ln"};
                        writer.writeNext(header);
                        // добавить данные в csv
                        String[] data1 = {word1, word2, "0"};
                        writer.writeNext(data1);
                        // закрываем соединение с писателем
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
        });
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
                String word1 = Text1.getText().toString();
                String word2 = Text2.getText().toString();
                String word_url;
                boolean L;
                if (!word1.equals("")){
                    word_url = "https://www.multitran.com/m.exe?l1=1&l2=2&s=" + word1;
                    L=true;
                }
                else{
                    word_url = "https://www.multitran.com/m.exe?l1=2&l2=1&s=" + word2;
                    L=false;
                }
//                contentView.setText("Загрузка...");
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String content = getContent(word_url);
//                            webView.post(new Runnable() {
//                                public void run() {
//                                    webView.loadDataWithBaseURL("https://www.multitran.com/m.exe?l1=1&l2=2&s=cat",content, "text/html", "UTF-8", "https://www.multitran.com/m.exe?l1=1&l2=2&s=cat");
//                                    Toast.makeText(getApplicationContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                            runOnUiThread(new Runnable() {
                                public void run() {
//                                    contentView.setText(content);
                                    Document doc = Jsoup.parse(content);
//                                    Elements link = doc.select("[class^=trans]");
                                    Elements link = doc.select("[class^=trans]");

//                                    contentView.setText(doc.html());
//                                    contentView.setText(doc.title());
//                                    String name = metaElement.attr("name");a[href*=/search/]
//                                    String linkInnerH = link.attr("s");
//                                    String[] linkInnerH = {};
                                    List<String> listA = new ArrayList<String>();

                                    int i = 0;
                                    for (Element links : link.select("a[href$=1]")) {
//                                        linkInnerH = linkInnerH + links.text()+"\n";
                                        listA.add(links.html());
//                                        linkInnerH = linkInnerH + links.html()+"\n";
                                        // get the value from href attribute
//                                        System.out.println("\nLink : " + links.attr("href"));
//                                        System.out.println("Text : " + links.text());
                                        if (i > 20) break;
                                        i++;
                                    }
                                    String[] linkInnerH = listA.toArray(new String[0]);
//                                    String[] linkInnerH = (String[]) listA.toArray();
//                                    String[] linkInnerH = {"fsdf", "sfsdfs"};
//                                    String linkInnerH = link.html();
                                    ListView lvMain = (ListView) findViewById(R.id.List);

                                    // создаем адаптер
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, linkInnerH);

                                    // присваиваем адаптер списку
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
//                                    contentView.setText(linkInnerH);

//                                    Log.d(TAG, content);
                                }
                            });
                        } catch (IOException ex) {
                            runOnUiThread(new Runnable() {
                                public void run() {
//                                    contentView.setText("Ошибка: " + ex.getMessage());
                                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Учить") {
            Intent intent = new Intent(this, Learn.class);
            startActivity(intent);
        } else if (item.getTitle() == "Мой словарь") {
            Intent intent = new Intent(this, Vocabulary.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}