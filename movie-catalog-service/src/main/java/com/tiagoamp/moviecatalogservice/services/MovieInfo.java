package com.tiagoamp.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tiagoamp.moviecatalogservice.model.CatalogItem;
import com.tiagoamp.moviecatalogservice.model.Movie;
import com.tiagoamp.moviecatalogservice.model.Rating;

@Service
public class MovieInfo {
	
	@Autowired
	private RestTemplate restTemplate;

	
	@HystrixCommand(fallbackMethod="getFallbackCatalogItem")
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		/*Movie movie = webClientBuilder.build()
			.get()
			.uri("http://localhost:8089/movies/" + rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class)
			.block();*/
		return new CatalogItem(movie.getName(), "Desc", rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
	
}
