package gr.aueb.cf.eduapp.dto;

import java.util.Map;

public record ValidationResponseEntityDTO(String code, String message, Map<String, String> errors) {

}
