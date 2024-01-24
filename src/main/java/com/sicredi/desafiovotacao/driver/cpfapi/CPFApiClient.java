package com.sicredi.desafiovotacao.driver.cpfapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CPFClient", url = "${cpf-api.url}")
public interface CPFApiClient {

    @GetMapping(value = "/validar")
    boolean validarCPF(@RequestParam(value = "cpf") String cpf);
}
