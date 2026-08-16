package euclid.messages;

import euclid.game.Player;
import euclid.messages.headers.IncomingEvents;
import euclid.messages.headers.OutgoingEvents;
import euclid.messages.incoming.*;
import euclid.network.streams.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler {

    private static final Logger log = LogManager.getLogger(MessageHandler.class);
    private static final MessageHandler INSTANCE = new MessageHandler();

    private final Map<String, IMessageEvent> events = new HashMap<>();
    private final Map<String, String> composers = new HashMap<>();

    public static MessageHandler getInstance() {
        return INSTANCE;
    }

    public void load() {
        resolveMessages();
    }

    public void resolveMessages() {
        registerEvent(IncomingEvents.VERSIONCHECK, new VERSIONCHECK());
        registerEvent(IncomingEvents.FINDUSER, new FINDUSER());
        registerEvent(IncomingEvents.APPROVENAME, new APPROVENAME());
        registerEvent(IncomingEvents.REGISTER, new REGISTER());
        registerEvent(IncomingEvents.UPDATE, new UPDATE());
        registerEvent(IncomingEvents.LOGIN, new LOGIN());
        registerEvent(IncomingEvents.INFORETRIEVE, new INFORETRIEVE());
        registerEvent(IncomingEvents.INITUNITLISTENER, new INITUNITLISTENER());
        registerEvent(IncomingEvents.Move, new Move());
        registerEvent(IncomingEvents.GOAWAY, new GOAWAY());
        registerEvent(IncomingEvents.CLIENTIP, new CLIENTIP());
        registerEvent(IncomingEvents.CREATEFLAT, new CREATEFLAT());
        registerEvent(IncomingEvents.TRYFLAT, new TRYFLAT());
        registerEvent(IncomingEvents.GOTOFLAT, new GOTOFLAT());
        registerEvent(IncomingEvents.SEARCHBUSYFLATS, new SEARCHBUSYFLATS());
        registerEvent(IncomingEvents.SETFLATINFO, new SETFLATINFO());
        registerEvent(IncomingEvents.GETFLATINFO, new GETFLATINFO());
        registerEvent(IncomingEvents.UPDATEFLAT, new UPDATEFLAT());
        registerEvent(IncomingEvents.GETORDERINFO, new GETORDERINFO());
        registerEvent(IncomingEvents.GETCREDITS, new GETCREDITS());
        registerEvent(IncomingEvents.CHAT, new CHAT());
        registerEvent(IncomingEvents.SHOUT, new SHOUT());
        registerEvent(IncomingEvents.WHISPER, new WHISPER());
        registerEvent(IncomingEvents.DELETEFLAT, new DELETEFLAT());
        registerEvent(IncomingEvents.GETSTRIP, new GETSTRIP());
        registerEvent(IncomingEvents.PLACESTUFFFROMSTRIP, new PLACESTUFFFROMSTRIP());
        registerEvent(IncomingEvents.PLACEITEMFROMSTRIP, new PLACEITEMFROMSTRIP());
        registerEvent(IncomingEvents.MOVESTUFF, new MOVESTUFF());
        registerEvent(IncomingEvents.ADDSTRIPITEM, new ADDSTRIPITEM());
        registerEvent(IncomingEvents.PURCHASE, new PURCHASE());
        registerEvent(IncomingEvents.FLATPROPERTYBYITEM, new FLATPROPERTYBYITEM());

        registerComposer("HELLO", OutgoingEvents.HELLO);
        registerComposer("SECRET_KEY", OutgoingEvents.SECRET_KEY);
        registerComposer("ENCRYPTION_ON", OutgoingEvents.ENCRYPTION_ON);
        registerComposer("ENCRYPTION_OFF", OutgoingEvents.ENCRYPTION_OFF);
        registerComposer("NAME_APPROVED", OutgoingEvents.NAME_APPROVED);
        registerComposer("NAME_UNACCEPTABLE", OutgoingEvents.NAME_UNACCEPTABLE);
        registerComposer("SYSTEMBROADCAST", OutgoingEvents.SYSTEMBROADCAST);
        registerComposer("USEROBJECT", OutgoingEvents.USEROBJECT);
        registerComposer("ALLUNITS", OutgoingEvents.ALLUNITS);
        registerComposer("OBJECTS", OutgoingEvents.OBJECTS);
        registerComposer("USERS", OutgoingEvents.USERS);
        registerComposer("STATUS", OutgoingEvents.STATUS);
        registerComposer("HEIGHTMAP", OutgoingEvents.HEIGHTMAP);
        registerComposer("LOGOUT", OutgoingEvents.LOGOUT);
        registerComposer("FLATCREATED", OutgoingEvents.FLATCREATED);
        registerComposer("FLAT_LETIN", OutgoingEvents.FLAT_LETIN);
        registerComposer("BUSY_FLAT_RESULTS", OutgoingEvents.BUSY_FLAT_RESULTS);
        registerComposer("FLATINFO", OutgoingEvents.FLATINFO);
        registerComposer("ORDERINFO", OutgoingEvents.ORDERINFO);
        registerComposer("WALLETBALANCE", OutgoingEvents.WALLETBALANCE);
        registerComposer("CHAT", OutgoingEvents.CHAT);
        registerComposer("SHOUT", OutgoingEvents.SHOUT);
        registerComposer("WHISPER", OutgoingEvents.WHISPER);
        registerComposer("ACTIVE_OBJECTS", OutgoingEvents.ACTIVE_OBJECTS);
        registerComposer("ITEMS", OutgoingEvents.ITEMS);
        registerComposer("YOUAREOWNER", OutgoingEvents.YOUAREOWNER);
        registerComposer("YOUARECONTROLLER", OutgoingEvents.YOUARECONTROLLER);
        registerComposer("STRIPINFO", OutgoingEvents.STRIPINFO);
        registerComposer("ACTIVEOBJECT_ADD", OutgoingEvents.ACTIVEOBJECT_ADD);
        registerComposer("ACTIVEOBJECT_UPDATE", OutgoingEvents.ACTIVEOBJECT_UPDATE);
        registerComposer("ADDITEM", OutgoingEvents.ADDITEM);
        registerComposer("ACTIVEOBJECT_REMOVE", OutgoingEvents.ACTIVEOBJECT_REMOVE);
        registerComposer("REMOVEITEM", OutgoingEvents.REMOVEITEM);
        registerComposer("ADDSTRIPITEM", OutgoingEvents.ADDSTRIPITEM);
        registerComposer("FLATPROPERTY", OutgoingEvents.FLATPROPERTY);
    }

    public void registerEvent(String header, IMessageEvent event) {
        events.put(header, event);
    }

    public void registerComposer(String className, String header) {
        composers.put(className, header);
    }

    public String getComposerId(IMessageComposer composer) {
        return composers.get(composer.getClass().getSimpleName());
    }

    public IMessageEvent getEvent(String header) {
        return events.get(header);
    }

    public String getComposerByHeader(String header) {
        for (Map.Entry<String, String> entry : composers.entrySet()) {
            if (entry.getValue().equals(header)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void handleMessage(Player player, Request request) {
        try {
            IMessageEvent messageEvent = events.get(request.getHeader());

            if (messageEvent != null) {
                if (messageEvent.authenticationRequired() && !player.isAuthenticated()) {
                    player.getLog().debug("User attempted request without authentication: [ {} ] / {}", request.getHeader(), request.getMessageBody());
                    return;
                }

                player.getLog().debug("RECEIVED {}: [ {} ] / {}", messageEvent.getClass().getSimpleName(), request.getHeader(), request.getMessageBody());
                messageEvent.handle(player, request);
            } else {
                player.getLog().debug("Unknown: [ {} ] / {}", request.getHeader(), request.getMessageBody());
            }
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
        }
    }
}
