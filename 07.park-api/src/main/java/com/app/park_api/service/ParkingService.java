package com.app.park_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.Client;
import com.app.park_api.entity.ClientSpot;
import com.app.park_api.entity.Spot;
import com.app.park_api.entity.Spot.SpotStatus;
import com.app.park_api.util.ParkingUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingService {
    private final ClientSpotService clientSpotService;
    private final ClientService clientService;
    private final SpotService spotService;

    @Transactional
    public ClientSpot CheckIn(ClientSpot clientSpot) {
        Client client = clientService.findByCpf(clientSpot.getClient().getCpf());
        clientSpot.setClient(client);

        Spot spot = spotService.findFreeSpot();
        spot.setStatus(SpotStatus.OCCUPIED);
        clientSpot.setSpot(spot);

        clientSpot.setCheckIn(LocalDateTime.now());

        clientSpot.setReceipt(ParkingUtils.genReceipt());

        return clientSpotService.save(clientSpot);
    }

    @Transactional
    public ClientSpot CheckOut(String receipt) {
        ClientSpot clientSpot = clientSpotService.getByReceipt(receipt);

        clientSpot.setCheckOut(LocalDateTime.now());

        BigDecimal price = ParkingUtils.calculatePrice(clientSpot.getCheckIn(), clientSpot.getCheckOut());
        BigDecimal discount = ParkingUtils.calculateDiscount(price, clientSpot.getClient().getVisits());

        clientSpot.setPrice(price);
        clientSpot.setDiscount(discount);

        clientSpot.getClient().incrementVisits();

        clientSpot.getSpot().setStatus(SpotStatus.FREE);

        return clientSpotService.save(clientSpot);
    }

    
}

