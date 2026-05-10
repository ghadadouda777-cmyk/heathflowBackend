package com.example.backendhealth.services;
import com.example.backendhealth.entities.ObjectifPersonnel;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.ObjectifRepository;
import com.example.backendhealth.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ObjectifService {

    private final ObjectifRepository objectifRepository;
    private final UserRepository userRepository;

    public ObjectifService(ObjectifRepository objectifRepository,
                           UserRepository userRepository) {
        this.objectifRepository = objectifRepository;
        this.userRepository = userRepository;
    }

    public ObjectifPersonnel getByUserId(String userId) {
        return objectifRepository.findByUserId(userId)
                .orElseGet(() -> createObjectifDefaut(userId));
    }

    private ObjectifPersonnel createObjectifDefaut(String userId) {
        user u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
        ObjectifPersonnel obj = new ObjectifPersonnel();
        obj.setUser(u);
        obj.setObjectif_coupes_eau(8);
        obj.setObjectif_heures_sommeil(7);
        obj.setObjectif_exercices_semaine(4);
        obj.setObjectif_calories(2000);
        obj.setObjectif_proteines(150);
        return objectifRepository.save(obj);
    }

    public ObjectifPersonnel updateEau(String userId, Integer eau) {
        ObjectifPersonnel obj = getByUserId(userId);
        obj.setObjectif_coupes_eau(eau);
        return objectifRepository.save(obj);
    }

    public ObjectifPersonnel updateSommeil(String userId, Integer sommeil) {
        ObjectifPersonnel obj = getByUserId(userId);
        obj.setObjectif_heures_sommeil(sommeil);
        return objectifRepository.save(obj);
    }

    public ObjectifPersonnel updateExercices(String userId, Integer ex) {
        ObjectifPersonnel obj = getByUserId(userId);
        obj.setObjectif_exercices_semaine(ex);
        return objectifRepository.save(obj);
    }

    public ObjectifPersonnel updateCalories(String userId, Integer cal) {
        ObjectifPersonnel obj = getByUserId(userId);
        obj.setObjectif_calories(cal);
        return objectifRepository.save(obj);
    }

    public ObjectifPersonnel updateProteines(String userId, Integer prot) {
        ObjectifPersonnel obj = getByUserId(userId);
        obj.setObjectif_proteines(prot);
        return objectifRepository.save(obj);
    }
}