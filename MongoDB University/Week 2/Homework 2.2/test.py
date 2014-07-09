import json
import urllib2
import pymongo

# connect to mongo
connection = pymongo.Connection("mongodb://localhost", safe=True)

# get a handle to reddit database
db=connection.students
grades = db.grades

x = grades.find({'type':'homework'}).sort([('student_id', pymongo.ASCENDING), ('score', pymongo.ASCENDING)])
count = 0
previous = -1
for item in x:
	if item['student_id'] == previous:
		#print "Student_ID: " + str(item['student_id']) + "\tScore: " + str(item['score'])
		continue
	else:
		count = count + 1
		grades.remove(item)
		print "Student_ID: " + str(item['student_id']) + "\tScore: " + str(item['score'])
		previous = item['student_id']



print count

print x.count()

