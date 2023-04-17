package com.example.tripme;

public class Participant {
    String participantName;
    String participantRole;
    String participantPhone;

    public Participant(String name, String role, String phone) {
        this.participantName = name;
        this.participantRole = role;
        this.participantPhone = phone;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getParticipantRole() {
        return participantRole;
    }

    public String getParticipantPhone() {
        return participantPhone;
    }
}
