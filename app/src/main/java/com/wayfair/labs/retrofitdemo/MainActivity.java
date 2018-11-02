package com.wayfair.labs.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private GitHubService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Optional
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder().create();
        RxJava2CallAdapterFactory rx2Adapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rx2Adapter)
                .build();

        service = retrofit.create(GitHubService.class);

        ((EditText) findViewById(R.id.query)).setOnEditorActionListener((textView, i, keyEvent) -> {
            search(textView.getEditableText().toString());
            return true;
        });

        findViewById(R.id.searchButton).setOnClickListener(v -> {
            search(((EditText) findViewById(R.id.query)).getEditableText().toString());
        });
    }

    private void search(String terms) {
        Disposable disposable = service.searchRepos(terms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> ((TextView) findViewById(R.id.results)).setText(getString(R.string.total, response.total)),
                        throwable -> ((TextView) findViewById(R.id.results)).setText(throwable.getLocalizedMessage())
                );
    }
}
