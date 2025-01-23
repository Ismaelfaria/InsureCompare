package api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.dto.QuoteDTO;
import application.service.QuoteService;
import domain.entity.Quote;

@RestController
@RequestMapping("/Quote")
public class QuoteController {

	@Autowired
	private QuoteService quoteService;

	@PostMapping
	public ResponseEntity<QuoteDTO> saveQuote(@RequestBody QuoteDTO quoteDTO) {
		try {
			Quote savedQuote = quoteService.saveQuote(quoteDTO);
			QuoteDTO savedQuoteDTO = quoteService.findByIdQuotes(savedQuote.getId()).orElse(null);
			return new ResponseEntity<>(savedQuoteDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/quotes/{id}")
	public ResponseEntity<QuoteDTO> updateQuote(@PathVariable Long id, @RequestBody Map<String, Object> updateRequest) {
		try {
			QuoteDTO updatedQuote = quoteService.updateQuote(id, updateRequest);
			return ResponseEntity.ok(updatedQuote);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/quotes/delete/{id}")
	public ResponseEntity<Void> deleteQuoteById(@PathVariable Long id) {
		try {
			quoteService.deleteQuoteById(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/quotes")
	public ResponseEntity<List<QuoteDTO>> findAllQuotes() {
		try {
			List<QuoteDTO> quotes = quoteService.findAllQuotes();
			return ResponseEntity.ok(quotes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/quotes/{id}")
	public ResponseEntity<QuoteDTO> findQuoteById(@PathVariable Long id) {
		try {
			Optional<QuoteDTO> quotesDTO = quoteService.findByIdQuotes(id);
			return quotesDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
