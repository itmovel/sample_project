package com.copel.sample.application.port.out;

import java.util.List;
import com.copel.sample.domain.Uc;

public interface UcRepository {
    
    List<Uc> buscarPorDocumento(String documento);

}