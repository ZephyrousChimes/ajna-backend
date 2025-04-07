package org.dexteradei.whitegold.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OHLC {
    double open;
    double high;
    double low;
    double close;
}
