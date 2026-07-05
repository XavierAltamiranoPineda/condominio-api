package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.response.DashboardResponse;
import com.condominio.condominio_api.dto.response.RecaudacionMensualDTO;
import com.condominio.condominio_api.repository.CuotaRepository;
import com.condominio.condominio_api.repository.PagoRepository;
import com.condominio.condominio_api.repository.TicketRepository;
import com.condominio.condominio_api.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TicketRepository ticketRepository;
    private final CuotaRepository cuotaRepository;
    private final PagoRepository pagoRepository;

    @Override
    public DashboardResponse getDashboardMetrics(Long condominioId) {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        long ticketsAbiertos = ticketRepository.countTicketsAbiertosByCondominioId(condominioId);
        long cuotasVencidas = cuotaRepository.countCuotasVencidasByCondominioId(condominioId);
        
        java.math.BigDecimal montoCuotasVencidas = cuotaRepository.sumSaldoVencidoTotalByCondominioId(condominioId);
        java.math.BigDecimal saldoPendienteTotal = cuotaRepository.sumSaldoPendienteTotalByCondominioId(condominioId);
        java.math.BigDecimal recaudacionMesActual = pagoRepository.sumRecaudacionMensual(condominioId, currentMonth, currentYear);
        
        List<RecaudacionMensualDTO> recaudacion6Meses = pagoRepository.getRecaudacionUltimos6Meses(condominioId)
                .stream()
                .map(proj -> RecaudacionMensualDTO.builder()
                        .mesAnio(String.format("%04d-%02d", proj.getAnio(), proj.getMes()))
                        .total(proj.getTotal())
                        .build())
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .ticketsAbiertos(ticketsAbiertos)
                .cuotasVencidas(cuotasVencidas)
                .montoCuotasVencidas(montoCuotasVencidas)
                .recaudacionMesActual(recaudacionMesActual)
                .saldoPendienteTotal(saldoPendienteTotal)
                .recaudacionUltimos6Meses(recaudacion6Meses)
                .build();
    }
}
