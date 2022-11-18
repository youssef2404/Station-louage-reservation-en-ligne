from time import strftime
from twilio.rest import Client
from datetime import datetime
import MySQLdb
Client=Client("ACf944cac731fdaf1085c3a711469e9291","2e5f423853baeb27a6fa47bb8f047d0f")
db=MySQLdb.connect("192.168.112.232","youssef","","beb_alioua")
while True:
    heure_from_base=0
    minute_from_base=0
    periode_from_base=""
    date=str(datetime.now())
    dateTest="'"+date[8:10]+"-"+date[5:7]+"-"+date[0:4]+"'"
    time=strftime("%I:%M %p")
    heure=time[0:2]
    minute=time[3:5]
    periode=str(time[6:8])
    heure_int=int(heure)
    minute_int=int(minute)
    cursorSelectHeure_fin_journee=db.cursor()
    selectHeureFinJournee=("SELECT Heure_fin_journee FROM `station`")
    cursorSelectHeure_fin_journee.execute(selectHeureFinJournee)
    resultSelectHeureFinJournee=cursorSelectHeure_fin_journee.fetchall()
    for a in resultSelectHeureFinJournee:
        Heurefinjournee=a[0]
        heure_from_base=int(Heurefinjournee[0:2])
        minute_from_base=int(Heurefinjournee[3:5])
        periode_from_base=str(Heurefinjournee[6:8])
    cursorSelectHeure_fin_journee.close()
    soustraction_heure=heure_from_base-heure_int
    soustraction_minute=minute_from_base-minute_int
    print(str(soustraction_heure))
    print(str(soustraction_minute))
    print(periode_from_base)
    print(periode)
    if(soustraction_heure == 1 and soustraction_minute == 0 and periode_from_base == periode):
        ##db=MySQLdb.connect("192.168.1.4","youssef","","beb_alioua")
        cursorSelectTel=db.cursor()
        selectTel=("SELECT Num_Tel FROM `tickets` WHERE status = false AND Sms = false AND plateforme = 'Mobile' AND dateReservation = "+dateTest)
        cursorSelectTel.execute(selectTel)
        resultSelectTel=cursorSelectTel.fetchall()
        for i in resultSelectTel:
            NumTelFromBase=i[0]
            NumTelFromBase=str(NumTelFromBase)
            cursorSelectdestination=db.cursor()
            selectdestination=("SELECT station_arrive FROM `tickets` WHERE Num_Tel = "+NumTelFromBase)
            cursorSelectdestination.execute(selectdestination)
            resultSelectdestination=cursorSelectdestination.fetchall()
            for j in resultSelectdestination:
                destinationfrombase=str(j[0])
                destinationtest="'"+str(destinationfrombase)+"'"
                cursorSelectPlaceDispo=db.cursor()
                selectPlaceDispo=("SELECT nbPlace_possible FROM `louages` WHERE zone= "+destinationtest)
                cursorSelectPlaceDispo.execute(selectPlaceDispo)
                resultSelectPlacedispo=cursorSelectPlaceDispo.fetchall()
                for k in resultSelectPlacedispo:
                    nb_place_possible=int(k[0])
                    if(nb_place_possible > 0):
                        Client.messages.create(to="+216"+str(NumTelFromBase),from_="+13166130334",body="votre ticket vers "+destinationfrombase+" va expiree dans une heure il vous reste que "+str(nb_place_possible)+"places")
                        cursorupdateticket=db.cursor()
                        requete="UPDATE `tickets` SET Sms = true WHERE Num_Tel ="+str(NumTelFromBase)
                        requete1=requete+" AND station_arrive =" +destinationtest
                        requete2=requete1+" AND dateReservation = "+dateTest
                        updateticket=requete2
                        cursorupdateticket.execute(updateticket)
                        db.commit()
                        cursorupdateticket.close()
                    else:
                        Client.messages.create(to="+216"+str(NumTelFromBase),from_="+13166130334",body="pas de place disponible vers "+str(destinationfrombase)+" votre ticket est expirée")
                        cursorupdateticket=db.cursor()  
                        requete="UPDATE `tickets` SET Sms = true WHERE Num_Tel ="+str(NumTelFromBase)
                        requete1=requete+" AND station_arrive =" +destinationtest
                        requete2=requete1+" AND dateReservation = "+dateTest
                        updateticket=requete2
                        cursorupdateticket.execute(updateticket)
                        db.commit()
                        cursorupdateticket.close()
                cursorSelectPlaceDispo.close()
            cursorSelectdestination.close()
        cursorSelectTel.close()
    elif(soustraction_heure == 0 and soustraction_minute == 0 and periode_from_base == periode):
        cursorupdateticketpoursupprimer=db.cursor() 
        updateticketpoursupprimer="UPDATE `tickets` SET status = true WHERE Sms = true AND plateforme = 'Mobile'"
        cursorupdateticketpoursupprimer.execute(updateticketpoursupprimer)
        db.commit()
        cursorupdateticketpoursupprimer.close()
        cursorupdateticketpoursupprimer_web=db.cursor() 
        updateticketpoursupprimer_web=("UPDATE `tickets` SET status = true WHERE plateforme = 'web' AND dateReservation = "+dateTest)
        cursorupdateticketpoursupprimer_web.execute(updateticketpoursupprimer_web)
        db.commit()
        cursorupdateticketpoursupprimer_web.close()
        cursordeleteticket=db.cursor() 
        deleteticket=("DELETE FROM `tickets` WHERE status = true")
        cursordeleteticket.execute(deleteticket)
        db.commit()
        cursordeleteticket.close()
        
    else:
        print("pas encore")
       
