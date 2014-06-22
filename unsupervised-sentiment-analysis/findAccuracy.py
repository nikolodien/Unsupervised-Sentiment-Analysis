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
	if(splits[4]!=""):
		assigned = string.atoi(splits[2])
		if assigned == desired:
			correct = correct + 1
		count = count + 1

print correct,count,correct/(count*1.0)