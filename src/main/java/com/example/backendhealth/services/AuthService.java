package com.example.backendhealth.services;

import com.example.backendhealth.dto.AbonnementDTO;
import com.example.backendhealth.dto.LoginDTO;
import com.example.backendhealth.dto.RegisterDTO;
import com.example.backendhealth.entities.Abonnement;
import com.example.backendhealth.entities.Bloomer;
import com.example.backendhealth.entities.Nutritionist;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.AbonnementRepository;
import com.example.backendhealth.repositories.BloomerRepository;
import com.example.backendhealth.repositories.NutritionistRepository;
import com.example.backendhealth.repositories.UserRepository;
import com.example.backendhealth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backendhealth.repositories.CoachRepository;
import com.example.backendhealth.entities.Coach;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AbonnementRepository abonnementRepository;

  @Autowired
  private NutritionistRepository nutritionistRepository;

  @Autowired
  private BloomerRepository bloomerRepository;

  @Autowired
  private CoachRepository coachRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // ─── REGISTER SIMPLE ──────────────────────────────────────────────────────
  @Transactional
  public Map<String, String> register(RegisterDTO dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new RuntimeException("Email déjà utilisé !");
    }

    user newUser;

    // ✅ FIX : On NE fait plus userRepository.save(newUser) séparément.
    // Avec JOINED inheritance, sauvegarder Nutritionist/Bloomer insère
    // automatiquement dans "users" ET dans la table fille.

    if ("NUTRITIONIST".equals(dto.getRole())) {
      Nutritionist nutri = new Nutritionist();
      nutri.setNom(dto.getNom());
      nutri.setPrenom(dto.getPrenom());
      nutri.setEmail(dto.getEmail());
      nutri.setTel(dto.getTel());
      nutri.setPwd(passwordEncoder.encode(dto.getPwd()));
      nutri.setRole("NUTRITIONIST");
      nutri.setActive(true);
      nutri.setSpecialite(dto.getSpecialite());
      nutri.setLocalisation(dto.getLocalisation());
      nutritionistRepository.save(nutri);
      newUser = nutri;

    } else if ("COACH".equals(dto.getRole())) {
      Coach coach = new Coach();
      coach.setNom(dto.getNom());
      coach.setPrenom(dto.getPrenom());
      coach.setEmail(dto.getEmail());
      coach.setTel(dto.getTel());
      coach.setPwd(passwordEncoder.encode(dto.getPwd()));
      coach.setRole("COACH");
      coach.setActive(true);
      coach.setCoachSpecialite(dto.getCoachSpecialite());
      coach.setCertifications(dto.getCertifications());
      coachRepository.save(coach);
      newUser = coach;

    } else {
      Bloomer bloomer = new Bloomer();
      bloomer.setNom(dto.getNom());
      bloomer.setPrenom(dto.getPrenom());
      bloomer.setEmail(dto.getEmail());
      bloomer.setTel(dto.getTel());
      bloomer.setPwd(passwordEncoder.encode(dto.getPwd()));
      bloomer.setRole("BLOOMER");
      bloomer.setActive(true);
      bloomer.setAge(dto.getAge());
      bloomer.setHeight(dto.getHeight());
      bloomer.setWeight(dto.getWeight());
      bloomer.setGoal(dto.getGoal());
      bloomer.setLifestyleLevel(dto.getLifestyleLevel());
      bloomerRepository.save(bloomer);
      newUser = bloomer;
    }

    String token = jwtUtil.generateToken(newUser.getEmail(), newUser.getRole());

    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    response.put("email", newUser.getEmail());
    response.put("role", newUser.getRole());
    response.put("userId", String.valueOf(newUser.getId()));
    response.put("id", String.valueOf(newUser.getId()));
    response.put("nom", newUser.getNom());
    response.put("prenom", newUser.getPrenom());
    response.put("typeAbonnement",
            dto.getTypeAbonnement() != null ? dto.getTypeAbonnement() : "MOIS_1");
    response.put("message", "Inscription réussie !");
    return response;
  }

  // ─── REGISTER + PAIEMENT EN UNE SEULE TRANSACTION ────────────────────────
  @Transactional
  public Map<String, String> registerWithPayment(RegisterDTO registerDTO,
                                                 AbonnementDTO.PaymentRequest paymentRequest) {

    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new RuntimeException("Email déjà utilisé !");
    }

    user newUser;

    // ✅ FIX : même logique, plus de double save

    if ("NUTRITIONIST".equals(registerDTO.getRole())) {
      Nutritionist nutri = new Nutritionist();
      nutri.setNom(registerDTO.getNom());
      nutri.setPrenom(registerDTO.getPrenom());
      nutri.setEmail(registerDTO.getEmail());
      nutri.setTel(registerDTO.getTel());
      nutri.setPwd(passwordEncoder.encode(registerDTO.getPwd()));
      nutri.setRole("NUTRITIONIST");
      nutri.setActive(true);
      nutri.setSpecialite(registerDTO.getSpecialite());
      nutri.setLocalisation(registerDTO.getLocalisation());
      nutritionistRepository.save(nutri);
      newUser = nutri;

    } else if ("COACH".equals(registerDTO.getRole())) {
      Coach coach = new Coach();
      coach.setNom(registerDTO.getNom());
      coach.setPrenom(registerDTO.getPrenom());
      coach.setEmail(registerDTO.getEmail());
      coach.setTel(registerDTO.getTel());
      coach.setPwd(passwordEncoder.encode(registerDTO.getPwd()));
      coach.setRole("COACH");
      coach.setActive(true);
      coach.setCoachSpecialite(registerDTO.getCoachSpecialite());
      coach.setCertifications(registerDTO.getCertifications());
      coachRepository.save(coach);
      newUser = coach;

    } else {
      Bloomer bloomer = new Bloomer();
      bloomer.setNom(registerDTO.getNom());
      bloomer.setPrenom(registerDTO.getPrenom());
      bloomer.setEmail(registerDTO.getEmail());
      bloomer.setTel(registerDTO.getTel());
      bloomer.setPwd(passwordEncoder.encode(registerDTO.getPwd()));
      bloomer.setRole("BLOOMER");
      bloomer.setActive(true);
      bloomer.setAge(registerDTO.getAge());
      bloomer.setHeight(registerDTO.getHeight());
      bloomer.setWeight(registerDTO.getWeight());
      bloomer.setGoal(registerDTO.getGoal());
      bloomer.setLifestyleLevel(registerDTO.getLifestyleLevel());
      bloomerRepository.save(bloomer);
      newUser = bloomer;
    }

    // Créer l'abonnement
    Abonnement.TypeAbonnement type =
            Abonnement.TypeAbonnement.valueOf(paymentRequest.getTypeAbonnement());

    Abonnement abonnement = new Abonnement();
    abonnement.setType_abonnement(type);
    abonnement.setMontant(type.prix);
    abonnement.setDateDebut(LocalDate.now());
    abonnement.setDateFin(LocalDate.now().plusMonths(type.dureeEnMois));
    abonnement.setStatut(Abonnement.StatutAbonnement.ACTIF);
    abonnement.setStripeSessionId(UUID.randomUUID().toString());
    abonnement.setUserEntity(newUser);
    abonnementRepository.save(abonnement);

    String token = jwtUtil.generateToken(newUser.getEmail(), newUser.getRole());

    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    response.put("email", newUser.getEmail());
    response.put("role", newUser.getRole());
    response.put("userId", newUser.getId());
    response.put("id", String.valueOf(newUser.getId()));
    response.put("nom", newUser.getNom());
    response.put("prenom", newUser.getPrenom());
    response.put("message", "Inscription et paiement réussis !");
    return response;
  }

  // ─── LOGIN ────────────────────────────────────────────────────────────────
  public Map<String, String> login(LoginDTO dto) {
    user u = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("Email introuvable !"));

    if (!passwordEncoder.matches(dto.getPwd(), u.getPwd())) {
      throw new RuntimeException("Mot de passe incorrect !");
    }
    if (!u.isActive()) {
      throw new RuntimeException("Compte désactivé !");
    }

    String token = jwtUtil.generateToken(u.getEmail(), u.getRole());

    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    response.put("role", u.getRole());
    response.put("nom", u.getNom());
    response.put("prenom", u.getPrenom());
    response.put("userId", u.getId());
    response.put("email", u.getEmail());
    response.put("id", String.valueOf(u.getId()));
    response.put("message", "Connexion réussie !");
    return response;
  }
}
