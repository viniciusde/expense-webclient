package com.denarde.finance.webclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.denarde.finance.webclient.dto.ExpenseDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/client")
public class ExpenseContoller {
	
	WebClient client = WebClient.create("https://apiexpense.herokuapp.com/api");
	
	
    @GetMapping("/expenses/{id}")
    private Mono<ExpenseDTO> getExpense(@PathVariable String id) {
    	Mono<ExpenseDTO> expenseMono = client.get()
    			  .uri("/expenses/{id}", id)
    			  .retrieve()
    			  .bodyToMono(ExpenseDTO.class);
    	
    	return expenseMono;
    }

    @GetMapping("/expenses")
    private Flux<ExpenseDTO> getExpenses() {
    	Flux<ExpenseDTO> expenseFlux = client.get()
    			  .uri("/expenses")
    			  .retrieve()
    			  .bodyToFlux(ExpenseDTO.class);
    	
        return expenseFlux;
    }

    @PostMapping("/expenses")
    private Mono<ExpenseDTO> save(@RequestBody ExpenseDTO expense) {
    	Mono<ExpenseDTO> expenseMono = client.post()
                .uri("/expenses")
                .body(BodyInserters.fromObject( expense ) )
                .retrieve()
                .bodyToMono(ExpenseDTO.class);
    	
        return expenseMono;
    }
}
