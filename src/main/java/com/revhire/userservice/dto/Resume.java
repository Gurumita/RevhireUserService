package com.revhire.userservice.dto;

import com.revhire.userservice.models.*;
import lombok.Data;

import java.util.List;

@Data
public class Resume {

    private User user;
    private List<Skills> skills;
    private List<Education> education;
    private List<Experience> experience;
    private List<Language> languages;
    private Summary summary;
}
