package com.example.hw6.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class NoteController {

    NoteRepository repository;

    public NoteController(NoteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<Note>> getAllNotes() {
        List<Note> allNotes = repository.findAll();

        return ResponseEntity.ok(allNotes);
    }

    @PostMapping
    ResponseEntity<Note> createNoTe(@RequestBody Note note) {
        if (note.getTitle() == null || note.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        note.setCreatedAt(LocalDateTime.now());
        Note newNote = repository.save(note);

        return ResponseEntity.status(HttpStatus.CREATED).body(newNote);
    }

    @GetMapping("/{id}")
    ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        return repository.findById(id)
                .map(existingNote -> {
                    existingNote.setDescription(note.getDescription());
                    if(note.getTitle() != null){
                        existingNote.setTitle(note.getTitle());
                    }
                    return ResponseEntity.ok(repository.save(existingNote));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Note> deleteNote (@PathVariable Long id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return  ResponseEntity.noContent().build();
    }
}
