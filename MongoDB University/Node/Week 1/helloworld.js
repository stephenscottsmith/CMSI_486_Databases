var http = require('http'),
	express = require('express'),
	cons = requires('consolidate'),
	mongodb = require('mongodb');

var server = http.createServer(function (request, response) {
	response.writeHead(200, {"Content-Type":"text/plain"});
	response.end("Hello, world!");
});

server.listen(8000);
console.log("Server running at http://localhost:8000");
//console.log("Hello, world!");