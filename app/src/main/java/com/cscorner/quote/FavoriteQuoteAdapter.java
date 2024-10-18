package com.cscorner.quote;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteQuoteAdapter extends ArrayAdapter<String> {

    private List<String> favoriteQuotes;
    private Context context;

    public FavoriteQuoteAdapter(Context context, List<String> quotes) {
        super(context, R.layout.list_item_with_delete, quotes);
        this.context = context;
        this.favoriteQuotes = quotes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_with_delete, parent, false);
        }

        // Get quote and bind to TextView
        String quote = favoriteQuotes.get(position);
        TextView quoteTextView = convertView.findViewById(R.id.quoteTextView);
        quoteTextView.setText(quote);

        return convertView;
    }

    public List<String> getFavoriteQuotes() {
        return favoriteQuotes;
}


}


