const YOUTUBE_CLIENT_ID = "210500066900-ufphglm9g4mre9b8aahfi68ikdi5s0h4.apps.googleusercontent.com";
const YOUTUBE_CLIENT_SECRET = "_MQh8yiJt6my3hFAAfnwyWNz";

var app = require('express');
var passport = require('passport');
var YoutubeV3Strategy = require('passport-youtube-v3').Strategy;

// Make sure the client ID/secret is setup for the following urls:
// Domain: http://localhost:8080
// Callback url: http://localhost:8080/callback

passport.use(new YoutubeV3Strategy({
    clientID: YOUTUBE_CLIENT_ID,
    clientSecret: YOUTUBE_CLIENT_SECRET,
    callbackURL: 'http://localhost:8080/callback',
    scope: ['https://www.googleapis.com/auth/youtube.readonly']
}, function (accessToken, refreshToken, profile, done) {
    console.log('got authentication for', profile);
    done(profile);
}))

app.get('/authenticate', passport.authenticate('youtube'));
app.get('/callback', passport.authenticate('youtube'));

app.listen(8080);

console.log('Now open http://localhost:8080/authenticate');