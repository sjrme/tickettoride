var express = require("express");
var url = require("url");
var request = require("request");
app = express();


app.get("/search/:query", function (req, res) {

	var user_query = req.params.query;
        var new_query = user_query.replace("+", " ");
	options = {
		protocol: "https",
		host: "api.themoviedb.org",
		pathname: "/3/search/movie",
		query: {
			api_key: "e427336c939e1ad6d4982b9d8e54d921",
			query: new_query,
			page: 1,
			include_adult: false
		}
	}

	var fullUrl = url.format(options);

	console.log(fullUrl);
	request(fullUrl, function (error, movieresponse, moviebody) {
		bodyObj = JSON.parse(moviebody);
		movies = bodyObj.results;

		resArr = [];
		count = movies.length;
		if (movies.length > 10) {
			count = 10;
		}

		for ( i = 0;  i < count; i++) {
			console.log(movies[i]);
			var poster_img = "https://image.tmdb.org/t/p/original"
							+ movies[i].poster_path;
			var summary = movies[i].vote_average
							+ "/10 with " + movies[i].vote_count + " votes";

			moveObj = {
				movie_id: movies[i].id,
				title: movies[i].title,
				poster_img_url: poster_img,
				popularity_summary: summary
			}

			resArr.push(moveObj);

		}

		res.set("access-control-allow-origin", "*");
		res.send(resArr);


	});

});




app.listen(9000);
