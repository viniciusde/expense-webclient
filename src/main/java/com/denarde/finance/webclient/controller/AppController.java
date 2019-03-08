package com.denarde.finance.webclient.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.denarde.finance.webclient.dto.ExpenseDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    	 client.post()
    		        .uri( "/expenses" )
    		        .body( BodyInserters.fromObject( expense ) )
    		        .exchange()
    		        .flatMap( clientResponse -> clientResponse.bodyToMono( ExpenseDTO.class ) );
    	
		return "redirect:/app";
	}
    

}
