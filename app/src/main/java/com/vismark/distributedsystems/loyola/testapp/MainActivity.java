package com.vismark.distributedsystems.loyola.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderAllQuestions();

        Button firstButton = (Button)findViewById(R.id.submitButton);


        //Setting onClick behaviour
        firstButton.setOnClickListener(new buttonOneClicked());
    }

    //OnClick button handler classes
    private class buttonOneClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            submitAnswer();
        }
    }

    private void renderAllQuestions() {
        String questionsJson = fetchAllQuestions();
    }

    private String fetchAllQuestions() {
        Log.i("INFO","Attempting to fetch all Questions from the instructor server!");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5001/")
                .addConverterFactory(GsonConverterFactory.create());


        Retrofit retrofit = builder.build();

        TestClient client = retrofit.create(TestClient.class);
        Call<Questions> call = client.fetchAllQuestions();

        call.enqueue(new Callback<Questions>() {

            @Override
            public void onResponse(Call<Questions> call, Response<Questions> response) {
                Toast.makeText(getApplicationContext(),
                        "Call made successfully!",
                        Toast.LENGTH_LONG)
                        .show();
                Log.i("INFO","Call was made successfully!");

                Log.i("INFO", response.body().toString());
            }

            @Override
            public void onFailure(Call<Questions> call, Throwable t) {
                Log.i("ERROR","Something blew up!");
                t.printStackTrace();
                Log.i("INFO", call.request().toString());
            }
        });

        return "All good";

    }




    //Ancillary methods
    private void submitAnswer() {
        Toast.makeText(getApplicationContext(),
                "Answer has been submitted.",
                Toast.LENGTH_LONG)
                .show();

        sendNetworkRequest("This is an example payload");
    }

    private void sendNetworkRequest(String data) {

        Log.i("INFO","Attempting to make REST call now!");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        TestClient client = retrofit.create(TestClient.class);
        Call<String> call = client.updateAnswer1Count(data);

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(),
                        "Call made successfully!",
                        Toast.LENGTH_LONG)
                        .show();
                Log.i("INFO","Call waas made successfully!");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("ERROR","Something blew up!");
                t.printStackTrace();
                Log.i("INFO", call.request().toString());
            }
        });
    }

}
