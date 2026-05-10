package com.example.backendhealth.controllers;

import com.example.backendhealth.entities.ObjectifPersonnel;
import com.example.backendhealth.services.ObjectifService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/objectifs")
@CrossOrigin("*")
public class ObjectifController {

    private final ObjectifService service;

    public ObjectifController(ObjectifService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ObjectifPersonnel getObjectif(@PathVariable String userId) {
        return service.getByUserId(userId);
    }

    @PatchMapping("/{userId}/eau")
    public ObjectifPersonnel updateEau(@PathVariable String userId,
                                       @RequestBody Integer eau) {
        return service.updateEau(userId, eau);
    }

    @PatchMapping("/{userId}/sommeil")
    public ObjectifPersonnel updateSommeil(@PathVariable String userId,
                                           @RequestBody Integer sommeil) {
        return service.updateSommeil(userId, sommeil);
    }

    @PatchMapping("/{userId}/exercices")
    public ObjectifPersonnel updateExercices(@PathVariable String userId,
                                             @RequestBody Integer ex) {
        return service.updateExercices(userId, ex);
    }

    @PatchMapping("/{userId}/calories")
    public ObjectifPersonnel updateCalories(@PathVariable String userId,
                                            @RequestBody Integer cal) {
        return service.updateCalories(userId, cal);
    }

    @PatchMapping("/{userId}/proteines")
    public ObjectifPersonnel updateProteines(@PathVariable String userId,
                                             @RequestBody Integer prot) {
        return service.updateProteines(userId, prot);
    }
}