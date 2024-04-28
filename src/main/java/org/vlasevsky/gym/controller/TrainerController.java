package org.vlasevsky.gym.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.mapper.mapstruct.TrainerMapper;
import org.vlasevsky.gym.service.TrainerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@AllArgsConstructor
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<CredentialsDto> registerTrainee(@RequestBody TrainerRegistrationDto registrationDto) {
        CredentialsDto credentials = trainerService.register(registrationDto);
        return new ResponseEntity<>(credentials, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileReadDto> getTrainerProfile(@RequestParam String username) {
        TrainerProfileReadDto profile = trainerService.findTrainerByUsername(username);
        if (profile != null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<TrainerProfileReadDto> updateTrainerProfile(@RequestBody TrainerCreateDto dto) {
        TrainerProfileReadDto updatedProfile = trainerService.update(dto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> changeTrainerActiveStatus(@RequestParam String username, @RequestParam boolean isActive) {
        trainerService.changeActiveStatus(username, isActive);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingReadDto>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String traineeName) {
        List<TrainingReadDto> trainings = trainerService.getTrainerTrainings(username, from, to, traineeName);
        return ResponseEntity.ok(trainings);
    }


}
