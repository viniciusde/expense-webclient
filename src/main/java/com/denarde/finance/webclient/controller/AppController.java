package com.denarde.finance.webclient.controller;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.denarde.finance.webclient.dto.ExpenseDTO;

import reactor.core.publisher.Flux;

@Controller
public class AppController {
	
	WebClient client = WebClient.create("https://expense-client.herokuapp.com/client");
	
    @RequestMapping("/app")
    public String index(final Model model) {
    	
    	Flux<ExpenseDTO> expenseFlux = client.get()
  			  .uri("/expenses")
  			  .retrieve()
  			  .bodyToFlux(ExpenseDTO.class);
    	
    	 	IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(expenseFlux, 1);
        model.addAttribute("expenses", reactiveDataDrivenMode);

        return "index";
    }
    
    
    @RequestMapping("/app/novoGasto")
    public String newExpense(final Model model) {
        model.addAttribute("expense", new ExpenseDTO());

        return "novoGasto";
    }
    
    @PostMapping("/app/salvar")
	public String salvar(@Valid ExpenseDTO expense) {
//    	Mono<ExpenseDTO> expenseMono = client.post()
//                .uri("/expenses")
//                .body(BodyInserters.fromObject( expense ) )
//                .retrieve()
//                .bodyToMono(ExpenseDTO.class);
    	
    	RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ExpenseDTO> request = new HttpEntity<ExpenseDTO>(expense);
		restTemplate.postForObject("https://expense-client.herokuapp.com/client/expenses", request, ExpenseDTO.class);
    	
		return "redirect:/app";
	}
    

}
