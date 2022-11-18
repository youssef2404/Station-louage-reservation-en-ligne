# -*- coding: utf-8 -*-

#!/usr/bin/env python
import socket
import drivers
from time import sleep
from datetime import datetime
import RPi.GPIO as GPIO
import MySQLdb


display = drivers.Lcd() 
HOST = '192.168.112.149' # Server IP or Hostname
PORT = 12345 # Pick an open Port 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')
nb=0
temp=0
t=0
c='-1'
#managing error exception
try:
	s.bind((HOST, PORT))
except socket.error:
	print('Bind failed ')

s.listen(5)
print('Socket awaiting messages')
(conn, addr) = s.accept()
print('Connected')
try:
    while True:
        data = conn.recv(1024)
        a = str(data)
        if "-1" in a:
           db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
           cursorSel=db.cursor()
           selectAction=("SELECT nbLouages From station")
           cursorSel.execute(selectAction)
           resultSelect=cursorSel.fetchall()
           for i in resultSelect:
                s=i[0]
           cursorSel.close()
           nb=int(s)
           nb=nb-1
           display.lcd_display_string("voiture= "+str(nb), 2)
           db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
           insertrec=db.cursor()
           numm=str(nb)
           sqlquery="UPDATE station SET nbLouages ="+numm
           insertrec.execute(sqlquery)
           db.commit()
           sleep(1)
        else :
           temp=data
           temp2=str(temp)
           result=temp2.index('b')
           print(result)
           youssef=temp2[2:4]
           print(youssef)
           display.lcd_display_string(str(datetime.now().time().strftime("%H:%M")+" "+"Nabel"+" "+str(youssef)), 1)
           sleep(1)
            
except KeyboardInterrupt:
    # If there is a KeyboardInterrupt (when you press ctrl+c), exit the program and cleanup
    print("Cleaning up!")
    display.lcd_clear()
