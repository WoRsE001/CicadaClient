package net.cicada.module.api;

import net.cicada.event.api.Event;
import net.cicada.event.api.EventCaller;
import net.cicada.event.api.EventListener;
import net.cicada.event.impl.KeyEvent;
import net.cicada.module.impl.combat.*;
import net.cicada.module.impl.connect.Blink;
import net.cicada.module.impl.connect.Ping;
import net.cicada.module.impl.connect.TimerRange;
import net.cicada.module.impl.exploit.*;
import net.cicada.module.impl.fun.Spin;
import net.cicada.module.impl.misc.*;
import net.cicada.module.impl.movement.*;
import net.cicada.module.impl.player.*;
import net.cicada.module.impl.render.*;
import net.cicada.module.impl.world.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements EventListener {
    public static final List<Module> MODULES = new ArrayList<>();

    // Combat
    public static AttackAura ATTACK_AURA = new AttackAura();
    public static AutoClicker AUTO_CLICKER = new AutoClicker();
    public static Critical CRITICAL = new Critical();
    public static KeepSprint KEEP_SPRINT = new KeepSprint();
    public static MoreKB MORE_KB = new MoreKB();
    public static TeleportAura TELEPORT_AURA = new TeleportAura();
    public static Velocity VELOCITY = new Velocity();
    // CONNECT
    public static Blink BLINK = new Blink();
    public static Ping PING = new Ping();
    public static TimerRange TIMER_RANGE = new TimerRange();
    // Movement
    public static AirStuck AIT_STUCK = new AirStuck();
    public static AutoSprint AUTO_SPRINT = new AutoSprint();
    public static Fly FLY = new Fly();
    public static HighJump HIGH_JUMP = new HighJump();
    public static LiquidWalk LIQUID_WALK = new LiquidWalk();
    public static LongJump LONG_JUMP = new LongJump();
    public static NoJumpDelay NO_JUMP_DELAY = new NoJumpDelay();
    public static NoSlow NO_SLOW = new NoSlow();
    public static Parkour PARKOUR = new Parkour();
    public static SnapTap SNAP_TAP = new SnapTap();
    public static Speed SPEED = new Speed();
    public static Spider SPIDER = new Spider();
    public static Strafe STRAFE = new Strafe();
    // Render
    public static Ambience AMBIENCE = new Ambience();
    public static Animation ANIMATION = new Animation();
    public static NoRender NO_RENDER = new NoRender();
    public static ClickGui CLICK_GUI = new ClickGui();
    public static CustomF5 CUSTOM_F5 = new CustomF5();
    public static ESP ESP = new ESP();
    public static FullBright FULL_BRIGHT = new FullBright();
    public static MurderHighlighter MURDER_HIGHLIGHTER = new MurderHighlighter();
    public static NoHurtCam NO_HURT_CAM = new NoHurtCam();
    public static PointerEsp POINTER_ESP = new PointerEsp();
    public static TargetESP TARGETESP = new TargetESP();
    public static WaterMark WATER_MARK = new WaterMark();
    // Player
    public static AutoPotion AUTO_POTION = new AutoPotion();
    public static AutoTool AUTO_TOOL = new AutoTool();
    public static ChestStealer CHEST_STEALER = new ChestStealer();
    public static InvManager INV_MANAGER = new InvManager();
    public static NoFall NO_FALL = new NoFall();
    public static Regen REGEN = new Regen();
    // World
    public static BridgeAssist BRIDGE_ASSIST = new BridgeAssist();
    public static FastBreak FAST_BREAK = new FastBreak();
    public static FastPlace FAST_PLACE = new FastPlace();
    public static Fucker FUCKER = new Fucker();
    public static Scaffold SCAFFOLD = new Scaffold();
    // Misc
    public static ClientSpoofer CLIENT_SPOOFER = new ClientSpoofer();
    public static Fixes FIXES = new Fixes();
    public static FlagDetector FLAG_DETECTOR = new FlagDetector();
    public static PacketLogger PACKET_LOGGER = new PacketLogger();
    public static Targets TARGETS = new Targets();
    // EXPLOIT
    public static Disabler DISABLER = new Disabler();
    public static FakeGameMode FAKE_GAME_MODE = new FakeGameMode();
    public static GodMode GOD_MODE = new GodMode();
    public static NoPitchLimit NO_PITCH_LIMIT = new NoPitchLimit();
    public static Phase PHASE = new Phase();
    public static SilentMurder SILENT_MURDER = new SilentMurder();
    // FUN
    public static Spin SPIN = new Spin();

    public ModuleManager() {
        for (Field field : getClass().getDeclaredFields()) {
            if (Module.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    MODULES.add((Module) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Module module : MODULES) {
            EventCaller.register(module);
        }
    }

    public static Module getModule(String name) {
        for (Module module : MODULES) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    @Override
    public void listen(Event event) {
        if (event instanceof KeyEvent e) {
            for (Module module : MODULES) {
                if (module.getKey() == e.getKeyCode()) {
                    module.toggle();
                }
            }
        }
    }

    @Override
    public boolean listen() {
        return true;
    }
}
