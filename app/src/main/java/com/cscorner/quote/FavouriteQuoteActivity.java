package com.cscorner.quote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouriteQuoteActivity extends Activity {

    private ListView favoritesListView;
    // private ArrayAdapter<String> adapter;
    private List<String> favoriteQuotesList;
    private FavoriteQuoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_quote);

        favoritesListView = findViewById(R.id.favoritesListView);
        favoriteQuotesList = new ArrayList<>();

        //*************************Load favorite quotes from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("QuoteAppPrefs", MODE_PRIVATE);
        Set<String> savedQuotes = prefs.getStringSet("favoriteQuotes", new HashSet<>());

        if (!savedQuotes.isEmpty()) {
            favoriteQuotesList.addAll(savedQuotes);
        }

        // Use custom adapter for favorite quotes
        adapter = new FavoriteQuoteAdapter(this, favoriteQuotesList);
        favoritesListView.setAdapter(adapter);

        // Set up the click listener for list items
        favoritesListView.setOnItemClickListener((parent, view, position, id) -> {
            String quoteToDelete = favoriteQuotesList.get(position);
            showConfirmationDialog(quoteToDelete, position);
        });
    }

    // *************remove confirmation
    private void showConfirmationDialog(String quoteToDelete, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Quote")
                .setMessage("Are you sure you want to delete this quote from favourite quote?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeFromFavorites(quoteToDelete);
                    favoriteQuotesList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(FavouriteQuoteActivity.this, "Quote removed from favorites!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Method to remove the quote from SharedPreferences
    private void removeFromFavorites(String quoteToRemove) {
        SharedPreferences prefs = getSharedPreferences("QuoteAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> favoriteQuotes = prefs.getStringSet("favoriteQuotes", new HashSet<>());

        if (favoriteQuotes != null) {
            Set<String> updatedFavorites = new HashSet<>(favoriteQuotes);
            updatedFavorites.remove(quoteToRemove);
            editor.putStringSet("favoriteQuotes", updatedFavorites);
            editor.apply();
        }
    }
}