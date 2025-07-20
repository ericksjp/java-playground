package com.playground.mscreditAnalizer.domain;

import java.util.List;

import com.playground.mscreditAnalizer.web.dto.ClientCardDto;
import com.playground.mscreditAnalizer.web.dto.ClientDataDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientStatus {
    private ClientDataDto clientData;
    private List<ClientCardDto> clientCards;
}
