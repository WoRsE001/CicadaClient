package net.cicada.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.lwjgl.input.Mouse;

@Getter @Setter @UtilityClass
public class DeltaTracker implements Access {
    public float deltaScroll;

    public void startFrame() {
        deltaScroll = Mouse.getDWheel();
    }
}
