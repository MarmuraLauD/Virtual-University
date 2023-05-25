package com.bettervns.teachersservice.requests;

public record TeacherRequest(
     String name,
     String surname,
     String father,
     String email,
     String chair_id
) {
}