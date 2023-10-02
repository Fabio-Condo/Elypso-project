package org.elypso.exception.domain;

public class CommunicationSessionAlreadyReservedException extends Exception{
    CommunicationSessionAlreadyReservedException(String message){
        super(message);
    }
}
