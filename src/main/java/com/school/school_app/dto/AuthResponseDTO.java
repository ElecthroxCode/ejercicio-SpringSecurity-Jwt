package com.school.school_app.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwtToken", "status"})//establezco el orden
public record AuthResponseDTO(String username, String message, String jwtToken, Boolean status) {

}
