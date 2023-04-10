package me.dragonappear.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkStatisticsDto {
    private String date;
    private String count;
}
