
<template>
 <div class="container">
    <div>
      <img id="mainimg" src="~assets/camera.jpg" width=500 height=200/>

      <h1 class="title">Movie Search</h1>

      <div class="content">
        <input type="text" v-model="query"/>
        <p id="query-state"> Look up {{query}}</p>
        <button v-on:click="fetch">Look it up!</button>
            <div class = "list" v-for="movie in (movies ? movies : [])">
                <img v-bind:src="movie.poster_img_url" width="50" height="60"/>
                <h3>{{movie.title}}</h3>
                <p> {{movie.popularity_summary}}</p>
            </div>
        </div>

    </div>
  </div>
</template>

<script>
export default {
    data() {
        return {
            movies: [],
            query: null,
        }
    },

    methods: {
        async fetch() {
            if (this.query === null || this.query=== "")
                return;
            this.movies = await fetch("http://localhost:9000/search/" + this.query)
                        .then( (res) => res.json());
            console.log(this.movies);
        }
    }
}
</script>
