package com.psych.game;

import com.psych.game.model.*;
import com.psych.game.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dev-test")
public class HelloWorld {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameModeRepository gameModeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    RoundRepository roundRepository;
    @Autowired
    EllenAnswerRepository ellenAnswerRepository;

    @Autowired
    ContentWriterRepository contentWriterRepository;

@GetMapping("/")
    public String hello()
{
    return "Hello,world";
}
    @GetMapping("/populate")
    public String populateDB()
    {
    for (Player player : playerRepository.findAll()) {
        player.getGames().clear();
        playerRepository.save(player);
    }
    gameRepository.deleteAll();
    playerRepository.deleteAll();
    questionRepository.deleteAll();
    gameModeRepository.deleteAll();

    Player luffy = new Player.Builder()
            .alias("Monkey D. Luffy")
            .email("luffy@psych.com")
            .saltedHashedPassword("strawhat")
            .build();
    playerRepository.save(luffy);
    Player robin = new Player.Builder()
            .alias("Nico Robin")
            .email("robin@psych.com")
            .saltedHashedPassword("poneglyph")
            .build();
    playerRepository.save(robin);



    return "populated";


   }

  @GetMapping("/question")
   public List<Question> getAllQuestion()
   {
    return questionRepository.findAll();
   }

    @GetMapping("/question/{id}")
    public Optional<Question> getQuestionById(@PathVariable(name = "id") Long id) {
        return questionRepository.findById(id);
    }


    @GetMapping("/player")
    public List<Player> getAllPlayers()
    {
        return playerRepository.findAll();
    }

    @GetMapping("/player/{id}")
    public Optional<Player> getPlayerById(@PathVariable(name = "id") Long id) {
        return playerRepository.findById(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/game/{id}")
    public Optional<Game> getGameById(@PathVariable(name = "id") Long id) {
        return gameRepository.findById(id);
    }

    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("/admin/{id}")
    public Optional<Admin> getAdminById(@PathVariable(name = "id") Long id) {
        return adminRepository.findById(id);
    }

    @GetMapping("/rounds")
    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }

    @GetMapping("/round/{id}")
    public Optional<Round> getRoundById(@PathVariable(name = "id") Long id) {
        return roundRepository.findById(id);
    }

    @GetMapping("/contentWriters")
    public List<ContentWriter> getAllContentWriters() {
        return contentWriterRepository.findAll();
    }

    @GetMapping("/contentWriter/{id}")
    public Optional<ContentWriter> getContentWriterById(@PathVariable(name = "id") Long id) {
        return contentWriterRepository.findById(id);
    }

    @GetMapping("/ellenAnswers")
    public List<EllenAnswer> getAllEllenAnswers() {
        return ellenAnswerRepository.findAll();
    }

    @GetMapping("/ellenAnswer/{id}")
    public Optional<EllenAnswer> getEllenAnswerById(@PathVariable(name = "id") Long id) {
        return ellenAnswerRepository.findById(id);
    }

}
