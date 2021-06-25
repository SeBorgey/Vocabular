package com.example.vocabular;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView contentView = (TextView) findViewById(R.id.content);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        Button btnFetch = (Button)findViewById(R.id.downloadBtn);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText("Загрузка...");
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            String content = getContent("https://www.multitran.com/m.exe?l1=1&l2=2&s=cat");
                            webView.post(new Runnable() {
                                public void run() {
                                    webView.loadDataWithBaseURL("https://www.multitran.com/m.exe?l1=1&l2=2&s=hi",content, "text/html", "UTF-8", "https://www.multitran.com/m.exe?l1=1&l2=2&s=cat");
                                    Toast.makeText(getApplicationContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
                                }
                            });
                            contentView.post(new Runnable() {
                                public void run() {
//                                    contentView.setText(content);
                                    Document doc = Jsoup.parse(content);
//                                    Elements link = doc.select("[class^=trans]");
                                    Elements link = doc.select("[class^=trans]");

//                                    contentView.setText(doc.html());
//                                    contentView.setText(doc.title());
//                                    String name = metaElement.attr("name");a[href*=/search/]
//                                    String linkInnerH = link.attr("s");
                                    String linkInnerH = "";
                                    int i=0;
                                    for (Element links : link.select("a[href$=1]")) {
//                                        linkInnerH = linkInnerH + links.text()+"\n";
                                        linkInnerH = linkInnerH + links.html()+"\n";
                                        // get the value from href attribute
//                                        System.out.println("\nLink : " + links.attr("href"));
//                                        System.out.println("Text : " + links.text());
                                        if (i>5) break;
                                        i++;
                                    }
//                                    String linkInnerH = link.html();
                                    contentView.setText(linkInnerH);

//                                    Log.d(TAG, content);
                                }
                            });
                        }
                        catch (IOException ex){
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText("Ошибка: " + ex.getMessage());
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
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
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
}