package com.sicredi.desafiovotacao.driver.cpfapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CPFApiService {

    private CPFApiClient CPFApiClient;

    @Autowired
    public CPFApiService(CPFApiClient CPFApiClient) {
        this.CPFApiClient = CPFApiClient;
    }

    public boolean verificaCPFValido(String cpf) {
        return this.CPFApiClient.validarCPF(cpf);
    }
}
