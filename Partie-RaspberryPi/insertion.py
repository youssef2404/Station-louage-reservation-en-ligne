# -*- coding: utf-8 -*-

#!/usr/bin/env python
import MySQLdb
#import socket
import drivers
from time import sleep
from datetime import datetime
import RPi.GPIO as GPIO
import mfrc522



GPIO.setmode(GPIO.BOARD) #Use Board numerotation mode
GPIO.setwarnings(False) #Disable warnings
# Une broche pour la sortie : la LED
GPIO.setup(40, GPIO.OUT)
# Une broche pour l'entree : Le poussoir
GPIO.setup(36, GPIO.IN)

#Use pin 12 for PWM signal
pwm_gpio = 12
frequence = 50
GPIO.setup(pwm_gpio, GPIO.OUT)
pwm = GPIO.PWM(pwm_gpio, frequence)

 
display = drivers.Lcd() 

#Set function to calculate percent from angle
def angle_to_percent (angle) :
    if angle > 180 or angle < 0 :
        return False

    start = 4
    end = 12.5
    ratio = (end - start)/180 #Calcul ratio from angle to percent

    angle_as_percent = angle * ratio

    return start + angle_as_percent

#Init at 0°
pwm.start(angle_to_percent(0))
sleep(1)
temp=0
idb=""
# Create an object of the class MFRC522
MIFAREReader = mfrc522.MFRC522()
db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
cursorSel=db.cursor()
selectAction=("SELECT nbLouages From station")
cursorSel.execute(selectAction)
resultSelect=cursorSel.fetchall()
for i in resultSelect:
    s=i[0]
cursorSel.close()
display.lcd_display_string(str(datetime.now().time().strftime("%H:%M")+" "+"Nabel"+" "+str(temp)), 1)
sleep(1)
display.lcd_display_string("voiture= "+str(s), 2)      
#managing error exception
try:
    while True:
        # Scan for cards    
        (status,TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)
 
        # If a card is found
        if status == MIFAREReader.MI_OK:
           # Get the UID of the card
           (status,uid) = MIFAREReader.MFRC522_Anticoll()
           uid2=str(uid)
           db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
           cursorSel=db.cursor()
           selectAction=("SELECT id FROM louages WHERE"+" RFID ="+"'"+uid2+"'")
           cursorSel.execute(selectAction)
           resultSelect=cursorSel.fetchall()
           for i in resultSelect:
               idb=i[0]
           cursorSel.close() 
           if (idb!=""): 
              idb=""
              print ("Card detected")
              db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
              cursorSel=db.cursor()
              selectAction=("SELECT nbLouages From station")
              cursorSel.execute(selectAction)
              resultSelect=cursorSel.fetchall()
              for i in resultSelect:
                  s=i[0]
              cursorSel.close()
              nb=int(s)
              nb = nb+1
              display.lcd_display_string("voiture= "+str(nb), 2)
              #Go at 90°
              pwm.ChangeDutyCycle(angle_to_percent(90))
              db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
              insertrec=db.cursor()
              numm=str(nb)
              sqlquery="UPDATE station SET nbLouages ="+numm
              insertrec.execute(sqlquery)
              db.commit()
              sleep(1)
              #finich at 90
              pwm.ChangeDutyCycle(angle_to_percent(0))
              sleep(1)
           else: 
              print("carte n'existe pas")
       
       
        
except KeyboardInterrupt:
    # If there is a KeyboardInterrupt (when you press ctrl+c), exit the program and cleanup
    print("Cleaning up!")
    display.lcd_clear()
