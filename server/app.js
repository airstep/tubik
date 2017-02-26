const YOUTUBE_CLIENT_ID = "210500066900-ufphglm9g4mre9b8aahfi68ikdi5s0h4.apps.googleusercontent.com";
const YOUTUBE_CLIENT_SECRET = "_MQh8yiJt6my3hFAAfnwyWNz";

var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');

// passport
var passport = require('passport');
var YoutubeV3Strategy = require('passport-youtube-v3').Strategy;

var youtubeUserProfile;

passport.use(new YoutubeV3Strategy({
    clientID: YOUTUBE_CLIENT_ID,
    clientSecret: YOUTUBE_CLIENT_SECRET,
    callbackURL: 'http://localhost:8080/youtube/callback',
    scope: ['https://www.googleapis.com/auth/youtube.readonly']
}, function (accessToken, refreshToken, profile, done) {
    profile.accessToken = accessToken;
    profile.refreshToken = refreshToken;
    youtubeUserProfile = profile;
    console.log('got authentication for', profile);
    done(profile);
    /*User.findOrCreate({ userId: profile.id }, function (err, user) {
      return done(err, user);
    });*/    
}))

var app = express();

// youtube

app.get('/youtube/authenticate', passport.authenticate('youtube'));
app.get('/youtube/callback', passport.authenticate('youtube'));

app.get('/youtube/getToken', (req, res) => {
  res.send(youtubeUserProfile);
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
