import json
import urllib2
import pymongo

# connect to mongo
connection = pymongo.Connection("mongodb://localhost", safe=True)

# get a handle to reddit database
db=connection.reddit
stories = db.stories

# get the reddit home page
reddit_page = urllib2.urlopen("http://www.reddit.com/r/technology/.json")

# parse the json into python objects
parsed = json.loads(reddit_page.read())

# iterate through every news item on the page
for item in parsed['data']['children']:
	stories.insert(item['data'])