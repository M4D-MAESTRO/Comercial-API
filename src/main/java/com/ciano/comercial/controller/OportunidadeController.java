package com.ciano.comercial.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ciano.comercial.model.Oportunidade;
import com.ciano.comercial.repository.OportunidadeRepository;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {

	//Repositorio injetado
	@Autowired
	private OportunidadeRepository oportunidadeRepository;
	
	//Create
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Oportunidade insert(@Valid @RequestBody Oportunidade o) {
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository
				.findByDescricaoAndNomeProspecto(o.getDescricao(), o.getNomeProspecto());

		if (oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Já existe uma oportunidade para esse prospector com a mesma descrição.");
		}

		return oportunidadeRepository.save(o);
	}

	//Read
	@GetMapping
	public List<Oportunidade> getAll() {
		return oportunidadeRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);
		if (oportunidade.isPresent()) {
			return ResponseEntity.ok(oportunidade.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	//Update
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Oportunidade o){
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(o.getId());
		oportunidadeRepository.save(o);
		if(oportunidade.isPresent()) {
			return ResponseEntity.ok(o);
		}
		return ResponseEntity.notFound().build();
	}

	

	//Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Oportunidade> o = oportunidadeRepository.findById(id);

		if (!o.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há uma oportunidade com este ID");
		}

		oportunidadeRepository.delete(o.get());
		return ResponseEntity.noContent().build();

	}

}
