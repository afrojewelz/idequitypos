package tk.lemetweaku.idequitypos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.lemetweaku.idequitypos.common.Actions;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Positions {

    // delayTransacList
    CopyOnWriteArrayList<Order> delayOrderList = new CopyOnWriteArrayList<Order>();
    // stock position(-1 means cancel)
    @Builder.Default
    private int nstate = 0;
    // initial position
    @Builder.Default
    private int openPositions = 0;
    // current position
    @Builder.Default
    private int currPositions = 0;

    public void addToOrderList(Order order) {

        delayOrderList.add(order);
        Collections.sort(delayOrderList);
    }

    public void setPositions(Order order) {
        // if cancel
        if (nstate == -1) {
            return;
        }
        if (order.getCommand().equals(Actions.INSERT)) {
            setCurrPositions(currPositions + order.getQuantity());
        }
        if (order.getCommand().equals(Actions.UPDATE)) {
            setCurrPositions(order.getQuantity());
        }
        if (order.getCommand().equals(Actions.CANCEL)) {
            setNstate(-1);
            setCurrPositions(openPositions);
        }
    }
}
