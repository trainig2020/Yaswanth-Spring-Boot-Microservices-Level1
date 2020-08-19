package com.yaswanth.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.yaswanth.model.CatlogItem;
import com.yaswanth.model.Movie;
import com.yaswanth.model.Rating;
import com.yaswanth.model.UserRating;


@RestController
@RequestMapping("/catlog")
public class MovieCatlogController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatlogItem> getCatlog(@PathVariable("userId") String userId) {

		//List<Rating> ratings = Arrays.asList(new Rating("1234", 4), new Rating("5678", 4)); 
		
		UserRating userRating = restTemplate.getForObject("http://Rating-Data-Service/ratingsdata/user/" + userId, UserRating.class);

		return userRating.getRatings().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://Movie-Info-Service/movies/" + rating.getMovieId(), Movie.class);
			return new CatlogItem(movie.getName(), "Desc", rating.getRating());
		}).collect(Collectors.toList());
	}
}





/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/
