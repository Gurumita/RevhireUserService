package com.revhire.userservice.Services;

import com.revhire.userservice.repository.SkillsRepository;
import com.revhire.userservice.exceptions.NotFoundException;
import com.revhire.userservice.models.Skills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {
    @Autowired
    SkillsRepository skillsRepository;

    public Skills createSkill(Skills skill) {
        return skillsRepository.save(skill);
    }

    public List<Skills> getAllSkills(){
        return skillsRepository.findAll();
    }



}

