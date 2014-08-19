var express = require('express'),
	app = express(),
	cons = require('consolidate'),
	MongoClient = require('mongodb').MongoClient,
	Server = require('mongodb').Server;


app.engine('html', cons.swig);
app.set('view engine', 'html');
app.set('views', __dirname + "/views");

var mongoclient = new MongoClient(new Server('localhost', 27017, 
											 { 'native_parser' : true}));

var db = mongoclient.db('test');

// Tells the app how to handle the 
app.get('/', function (req, res) {
	db.collection('lmu').findOne({}, function (err, doc) {
		res.render('hello', doc);
	})
});

app.get('*', function (req, res) {
	res.send("404 - Page Not Found");
});

mongoclient.open(function (err, mongoclient) {
	app.listen(8080);
	console.log("Express server started on port 8080");
});
