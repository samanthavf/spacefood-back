package com.samantha.spacefood_back.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.samantha.spacefood_back.entities.Cart;
import com.samantha.spacefood_back.entities.Dish;
import com.samantha.spacefood_back.exception.CartNotFoundException;
import com.samantha.spacefood_back.repositories.CartRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {
	private final CartRepo cartRepo;
	
	@Transactional
	public Cart cart(Long id){
	  Optional<Cart> carrinho = cartRepo.findById(id);
	  if (carrinho.isPresent()) {
		  return carrinho.get();
	}
	  else {
		  throw new CartNotFoundException("Carrinho não encontrado.");
	}
	}
	
	public Cart createCart() {
	    Cart carrinho = new Cart();
	    carrinho.setPratoSelecionado(new ArrayList<>());
	    carrinho.setValorTotal(0.0);
	    return cartRepo.save(carrinho);
	}

	
	public Cart addDish(Long cartId, Dish prato) {
		Optional<Cart> findCart = cartRepo.findById(cartId);
		
		Cart carrinho = findCart.get();
		carrinho.getPratoSelecionado().add(prato); 
		
		double precoFinal = carrinho.getValorTotal()+prato.getPreco();
		carrinho.setValorTotal(precoFinal);
		
		return cartRepo.save(carrinho);
	}
	
	
	public Cart removeDish(Long cartId, Dish prato) throws Exception {
		Optional<Cart> findCart = cartRepo.findById(cartId);
		
		if (findCart.isPresent()) {
			
			Cart carrinho = findCart.get();
			
			if (carrinho.getPratoSelecionado().contains(prato)) {
				
				double precoFinal = carrinho.getValorTotal()-prato.getPreco();
				carrinho.setValorTotal(Math.max(precoFinal, 0));
				carrinho.getPratoSelecionado().remove(prato);
				
				return cartRepo.save(carrinho);		
			}else {
	            throw new Exception("Prato não encontrado no carrinho.");
	        }	
		}else {
			throw new CartNotFoundException("Carrinho indisponível.");
	}
}
}