package com.cscorner.quote;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuotesApi {

    @GET("?method=getQuote&format=json&lang=en") // Forismatic API endpoint
    Call<Quote> getRandomQuote();

}
