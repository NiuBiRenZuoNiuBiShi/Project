package com.setravel.swifttravel.entities.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class BuyTicketsOutput {
    private String message;
    private List<String> seatId;
    private String carriageId;
    private String trainNumber;
}
