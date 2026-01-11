package github.nitespring.reindeer.client.render.entity;

import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class CustomDataTickets  {

    public static final DataTicket<Integer> COLOUR = DataTicket.create("tick", Integer.class);
    public static final DataTicket<Integer> LIGHT_STATE = DataTicket.create("light_state", Integer.class);
    public static final DataTicket<Boolean> HAS_LIGHTS = DataTicket.create("has_lights", Boolean.class);
    public static final DataTicket<Boolean> IS_RUDOLPH = DataTicket.create("is_rudolph", Boolean.class);
    public static final DataTicket<Boolean> IS_BABY = DataTicket.create("is_baby", Boolean.class);
    public static final DataTicket<Boolean> HAS_SADDLE = DataTicket.create("has_saddle", Boolean.class);

}
