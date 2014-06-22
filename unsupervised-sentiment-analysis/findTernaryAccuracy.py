#!/usr/bin/python

import string
import sys

pos="pos"
neg="neg"
POS=0
NEG=1
OBJ=2

correct = 0
count = 0

first=True

for line in open(sys.argv[1],'r'):
	if(first):
		first = False
		continue
	desired = POS
	splits = line.split(" ")
	filename = splits[1].split("/")
	filename = filename[len(filename)-1]
	if filename.find(pos) == 0:
		desired = POS
	else:
		desired = NEG
	if(not len(splits)>4):
		continue
	assigned = string.atoi(splits[2])
	if(assigned!=OBJ):
		assigned = string.atoi(splits[2])
	elif(splits[4]!=""):
		assigned = string.atoi(splits[4])
	else:
		assigned=OBJ
	if assigned == desired:
		correct = correct + 1
	count = count + 1
	print "filename:"+filename+",assigned:"+str(assigned)+",splits[4]="+splits[4]+",splits[2]="+splits[2]

print correct,count,correct/(count*1.0)