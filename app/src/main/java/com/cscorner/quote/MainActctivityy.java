package com.cscorner.quote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActctivityy extends Activity {

    private TextView quoteTextView;
    private ImageButton shareButton, saveFavoriteButton, viewFavoritesButton;
   ImageButton nextButton, previousButton;
    private String currentQuote = "";
    private List<String> quoteList = new ArrayList<>(); // Store fetched quotes

   //  private ImageButton  nextButton, previousButton;
    private int currentQuoteIndex = -1; // Index to track the current quote


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actctivityy);

        quoteTextView = findViewById(R.id.quoteTextView);
        shareButton = findViewById(R.id.shareButton);
        saveFavoriteButton = findViewById(R.id.saveFavoriteButton);

        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);


        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActctivityy.this, FavouriteQuoteActivity.class);
            startActivity(intent);
        });

        fetchRandomQuote();

        shareButton.setOnClickListener(v -> shareQuote());

        saveFavoriteButton.setOnClickListener(v -> saveToFavorites());

        nextButton.setOnClickListener(v -> showNextQuote());

        previousButton.setOnClickListener(v -> showPreviousQuote());
    }

    private void fetchRandomQuote() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forismatic.com/api/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuotesApi quotesApi = retrofit.create(QuotesApi.class);
        Call<Quote> call = quotesApi.getRandomQuote();
        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                if (response.isSuccessful()) {
                    Quote quoteResponse = response.body();
                    if (quoteResponse != null) {
                        String quoteText = quoteResponse.getQuoteText() + "\n\n- " + quoteResponse.getQuoteAuthor();
                        updateQuoteList(quoteText);
                    } else {
                        Log.e("MainActivity", "Quote response is null");
                    }
                } else {
                    Log.e("MainActivity", "Response error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                Log.e("MainActivity", "Error fetching quote: " + t.getMessage());
            }
        });
    }

    private void updateQuoteList(String newQuote) {
        if (!quoteList.contains(newQuote)) {
            quoteList.add(newQuote);
            currentQuoteIndex = quoteList.size() - 1;
            currentQuote = newQuote;
            quoteTextView.setText(newQuote);
            Log.d("MainActivity", "New quote added: " + newQuote);
        }
    }
    private void showNextQuote() {
        if (currentQuoteIndex < quoteList.size() - 1) {
            currentQuoteIndex++;
            currentQuote = quoteList.get(currentQuoteIndex);
            quoteTextView.setText(currentQuote);
        } else {
            fetchRandomQuote(); // Fetch new quote if we reach the end of the list
        }
    }

    private void showPreviousQuote() {
        if (currentQuoteIndex > 0) {
            currentQuoteIndex--;
            currentQuote = quoteList.get(currentQuoteIndex);
            quoteTextView.setText(currentQuote);
        } else {
            Toast.makeText(this, "No previous quotes!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQuote() {
        String quote = quoteTextView.getText().toString();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, quote);
        startActivity(Intent.createChooser(shareIntent, "Share Quote via"));
    }

    private void saveToFavorites() {
        // Save to SharedPreferences logic remains the same
        Log.d("QuoteApp", "Attempting to save to favorites");

        SharedPreferences prefs = getSharedPreferences("QuoteAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get current saved favorites as a Set
        Set<String> favoriteQuotes = prefs.getStringSet("favoriteQuotes", new HashSet<>());

        // Create a new HashSet to avoid modifying the original reference
        Set<String> updatedFavoriteQuotes = new HashSet<>(favoriteQuotes);

        // Check if currentQuote is not null or empty
        if (currentQuote != null && !currentQuote.isEmpty()) {
            Log.d("QuoteApp", "Current quote: " + currentQuote);

            // Append the current quote if it's not already in favorites
            if (!updatedFavoriteQuotes.contains(currentQuote)) {
                updatedFavoriteQuotes.add(currentQuote);
                editor.putStringSet("favoriteQuotes", updatedFavoriteQuotes);
                editor.apply();
                Toast.makeText(this, "Quote saved to favorites!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quote is already in favorites!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("QuoteApp", "Current quote is invalid!");
            Toast.makeText(this, "Current quote is not valid!", Toast.LENGTH_SHORT).show();
        }
    }
}

//        fetchRandomQuote();
//        startQuoteAutoUpdate();
//
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareQuote();
//            }
//        });
//
//        saveFavoriteButton.setOnClickListener(v -> saveToFavorites());
//
//    }
//
//    private void shareQuote() {
//        String quote = quoteTextView.getText().toString();
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, quote);
//        startActivity(Intent.createChooser(shareIntent, "Share Quote via"));
//}
//
//     private void fetchRandomQuote() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.forismatic.com/api/1.0/") // Forismatic API base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        QuotesApi quotesApi = retrofit.create(QuotesApi.class);
//        Call<Quote> call = quotesApi.getRandomQuote();
//        call.enqueue(new Callback<Quote>() {
//            @Override
//            public void onResponse(Call<Quote> call, Response<Quote> response) {
//                if (response.isSuccessful()) {
//                    Quote quoteResponse = response.body();
//                    if (quoteResponse != null) {
//                        // Combine quote text and author, and update the TextView
//                        String quoteText = quoteResponse.getQuoteText() + "\n\n- " + quoteResponse.getQuoteAuthor();
//                        quoteTextView.setText(quoteText);
//
//                        // Update currentQuote immediately after fetching
//                        currentQuote = quoteText;
//                        Log.d("MainActivity", "Quote fetched and set to currentQuote: " + currentQuote);
//                    } else {
//                        Log.e("MainActivity", "Quote response is null");
//                    }
//                } else {
//                    Log.e("MainActivity", "Response error: " + response.code() + " - " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Quote> call, Throwable t) {
//                Log.e("MainActivity", "Error fetching quote: " + t.getMessage());
//            }
//        });
//    }
//
////
//private void saveToFavorites() {
//    Log.d("QuoteApp", "Attempting to save to favorites");
//
//    SharedPreferences prefs = getSharedPreferences("QuoteAppPrefs", MODE_PRIVATE);
//    SharedPreferences.Editor editor = prefs.edit();
//
//    // Get current saved favorites as a Set
//    Set<String> favoriteQuotes = prefs.getStringSet("favoriteQuotes", new HashSet<>());
//
//    // Create a new HashSet to avoid modifying the original reference
//    Set<String> updatedFavoriteQuotes = new HashSet<>(favoriteQuotes);
//
//    // Check if currentQuote is not null or empty
//    if (currentQuote != null && !currentQuote.isEmpty()) {
//        Log.d("QuoteApp", "Current quote: " + currentQuote);
//
//        // Append the current quote if it's not already in favorites
//        if (!updatedFavoriteQuotes.contains(currentQuote)) {
//            updatedFavoriteQuotes.add(currentQuote);
//            editor.putStringSet("favoriteQuotes", updatedFavoriteQuotes);
//            editor.apply();
//            Toast.makeText(this, "Quote saved to favorites!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Quote is already in favorites!", Toast.LENGTH_SHORT).show();
//        }
//    } else {
//        Log.e("QuoteApp", "Current quote is invalid!");
//        Toast.makeText(this, "Current quote is not valid!", Toast.LENGTH_SHORT).show();
//}
//}
//        private void startQuoteAutoUpdate() {
//            final Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    fetchRandomQuote(); // Call the method to fetch quote
//                    handler.postDelayed(this, 3000); // Fetch quote every 60 seconds
//                }
//            };
//            handler.postDelayed(runnable, 1000); // Start the runnable
//        }
//
//    }
//
//
